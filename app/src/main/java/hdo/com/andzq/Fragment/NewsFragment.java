package hdo.com.andzq.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hdo.com.andzq.R;
import hdo.com.andzq.activity.NewsContentActivity;
import hdo.com.andzq.adapter.NewsAdapter;
import hdo.com.andzq.base.BaseFragment;

import hdo.com.andzq.bean.NewsBean;
import hdo.com.andzq.bean.NewsBodyBean;
import hdo.com.andzq.decoration.DividerItemDecoration;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 动态Fragment
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 * modify 添加了新闻页面 by 邓杰 on 2017/4/1
 */

public class NewsFragment extends BaseFragment {
    private EmptyRecyclerView mRvNews;
    private AutoSwipeRefreshLayout mRefreshLayout;
    ArrayList<NewsBodyBean> newsBeenArray = new ArrayList<>();

    NewsAdapter adapter;// recyclerView 适配器
    private SQLiteDatabase database;
    private int startPos=1;
    private Handler handler = new Handler();
    private boolean isNewsLoading = false;

    /**
     * 初始化布局
     */
    @Override
    public View initView(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_news, viewGroup,
                false);
        mRvNews = (EmptyRecyclerView) view.findViewById(R.id.rv_news);
        mRefreshLayout = (AutoSwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //初始化数据库
        database = Connector.getDatabase();
        View view = getView().findViewById(R.id.rl_empty);
        mRvNews.setEmptyView(view);
        //设置垂直滑动
        mRvNews.setLayoutManager(new LinearLayoutManager(mActivity));
        //添加装饰器
        mRvNews.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        //适配器
        mRvNews.setAdapter(adapter = new NewsAdapter(mActivity, newsBeenArray, onRecyclerItemClick));
        mRvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast= false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&!isNewsLoading){
                    int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
                    int childCount = manager.getItemCount();
                    if(lastVisiblePosition >= childCount - 1&&isSlidingToLast){
                        isNewsLoading = true;//是否已经在加载新闻数据
                        View v = recyclerView.getChildAt(manager.getChildCount()-1);

                        ContentLoadingProgressBar pbLoadMore = (ContentLoadingProgressBar) v.findViewById(R.id.pb_load_more);
                        ImageView ivInfo = (ImageView) v.findViewById(R.id.iv_info);
                        TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
                        handler.post(()->{

                            pbLoadMore.setVisibility(View.VISIBLE);
                            ivInfo.setVisibility(View.GONE);
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg.setText("加载更多...");
                        });
                        startPos = manager.getItemCount();
                        loadNetNews(startPos, startPos+10,false);//加载更多的逻辑
                        LogUtils.e("startPos",startPos+"");
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });
        //刷新时间大于一个小时 从网络获取数据 否则从数据库取数据
        if (TimeUtils.compareHour(System.currentTimeMillis(),
                SpUtils.getLong(mActivity, getString(R.string.preference_news_refresh), 0)) > 1) {
            mRefreshLayout.autoRefresh();//自动刷新
        } else {
            mActivity.runOnUiThread(()-> {
                List<NewsBodyBean> all = DataSupport.findAll(NewsBodyBean.class);//读取数据库
                newsBeenArray.clear();
                newsBeenArray.addAll(all);
                adapter.notifyDataSetChanged();
                handler.postDelayed(()->{if (mRvNews.getChildCount()<10) {//如果收到的数据小于10
                    //这里 -1 拿到RecyclerView 脚布局
                    View v = mRvNews.getChildAt(mRvNews.getChildCount() - 1);
                    LogUtils.e("mRvNews.getAdapter().getItemCount()", mRvNews.getAdapter().getItemCount() + "");
                    ContentLoadingProgressBar pbLoadMore = (ContentLoadingProgressBar) v.findViewById(R.id.pb_load_more);
                    ImageView ivInfo = (ImageView) v.findViewById(R.id.iv_info);
                    TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
                    if (pbLoadMore != null) {
                        tvMsg.setVisibility(View.VISIBLE);
                        pbLoadMore.setVisibility(View.INVISIBLE);
                        ivInfo.setVisibility(View.VISIBLE);
                        tvMsg.setText("没有更多啦！");
                    }
                }},100);
            });
        }
        //设置刷新进度监听
        mRefreshLayout.setOnRefreshListener(()->loadNetNews(1,10,true));
    }
    /**
     * 加载网络新闻
     */
    private void loadNetNews(int start,int end,boolean clearing) {
        Map<String, String> map = new HashMap<>();
        map.put("start", start+"");
        map.put("end", end+"");
        NetWorkUtils.getInstance().loadNews(mActivity, map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                LogUtils.e("新闻",s);
                if (s.contains(Constant.STATE_SUCCESS)) {
                    Gson gson = new Gson();
                    NewsBean newsBean = gson.fromJson(s, NewsBean.class);
                    //保存请求网络的时间
                    SpUtils.putLong(mActivity, getString(R.string.preference_news_refresh), System.currentTimeMillis());
                    List<NewsBodyBean> body = newsBean.getBody();

                    LogUtils.e("body size",body.size()+"");
                    if (body.size()<10){//如果收到的数据小于10
                        //这里 -1 拿到RecyclerView 脚布局
                        View v = mRvNews.getChildAt(mRvNews.getChildCount()-1);
                        LogUtils.e("mRvNews.getAdapter().getItemCount()",mRvNews.getAdapter().getItemCount()+"");
                        ContentLoadingProgressBar pbLoadMore = (ContentLoadingProgressBar) v.findViewById(R.id.pb_load_more);
                        ImageView ivInfo = (ImageView) v.findViewById(R.id.iv_info);
                        TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
                        if(pbLoadMore!=null) {
                            handler.postDelayed(()->{
                                isNewsLoading = false;
                                pbLoadMore.setVisibility(View.GONE);
                                ivInfo.setVisibility(View.VISIBLE);
                                tvMsg.setVisibility(View.VISIBLE);
                                tvMsg.setText("没有更多啦！");
                            },500);
                        }
                    }
                    if (clearing){
                        newsBeenArray.clear();
                        DataSupport.deleteAll(NewsBodyBean.class);//先清空数据库
                    }
                    DataSupport.saveAll(body);//将新闻内容缓存到本地
                    newsBeenArray.addAll(body);//将新闻加到List中
                    mActivity.runOnUiThread(adapter::notifyDataSetChanged);
                } else {
                    SnackbarUtils.networkErr(mActivity, mRefreshLayout);
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                mRefreshLayout.setRefreshing(false);
                SnackbarUtils.networkErr(mActivity, mRefreshLayout);
                super.onError(call, response, e);
            }
        });
    }

    //点击监听
    private NewsAdapter.OnRecyclerItemClick onRecyclerItemClick = (v, position) -> {
        Intent intent = new Intent(mActivity, NewsContentActivity.class);
        intent.putExtra(NewsContentActivity.NEWS_NAME, newsBeenArray.get(position).getTitle());
        intent.putExtra(NewsContentActivity.NEWS_CONTENT, newsBeenArray.get(position)
                .getContents());
        intent.putExtra(NewsContentActivity.NEWS_ICON, newsBeenArray.get(position).getImage());
        startActivity(intent);
    };
}
