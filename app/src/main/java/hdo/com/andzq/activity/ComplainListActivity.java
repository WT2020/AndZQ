package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.ComplainListAdapter;
import hdo.com.andzq.bean.ComplainResultBean;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 报修列表显示界面
 * author 陈锐
 * version 1.0
 * Created 2017/4/6.
 * modified on 2017/10/17 by 张建银 我的投诉排序处理
 */
public class ComplainListActivity extends BaseActivity {
    private EmptyRecyclerView mRecyclerView;
    private Context mContext;
    private AutoSwipeRefreshLayout mRefreshLayout;
    private ArrayList<ComplainResultBean.MyComplainBean> list = new ArrayList<>();
    private ComplainListAdapter adapter;//适配器
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_list);
        mContext = this;
        initView();
        initToolbar();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();//设置页面自动刷新
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        View id_empty_view = findViewById(R.id.rl_empty);
        mRecyclerView.setEmptyView(id_empty_view);

        mRecyclerView.setAdapter(adapter = new ComplainListAdapter(mContext, list,
                onRecyclerItemClick));
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PostRequest request = NetWorkUtils.getInstance().getMyComplain(mContext);
                request.execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(ExamineUtils.isTokenErr(s)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ComplainListActivity.this);
                            SpUtils.putString(ComplainListActivity.this, mContext.getString(R.string.preference_token), "");
                            dialog.setTitle("提示")
                                    .setMessage("登录已经失效、即将重新登录")
                                    .setCancelable(false);
                            runOnUiThread(dialog::show);
                            handler.postDelayed(() -> {
                                startActivity(new Intent(ComplainListActivity.this, LoginActivity.class));
                                ComplainListActivity.this.finish();
                            }, 2000);
                        }
                        JSONObject js;
                        try {
                            js = new JSONObject(s);
                            String state = js.getString("RESP_STATE");//判断状态是否为成功 成功再解析
                            if (state.equals("STATE_SUCCESS")) {
                                Gson gson = new Gson();
                                ComplainResultBean b = gson.fromJson(s, ComplainResultBean.class);
                                list.clear();
                                for (ComplainResultBean.MyComplainBean bean:b.getMyComplain()){
                                    if(bean.getPState().equals("0")){
                                        list.add(bean);
                                    }
                                }
                                for (ComplainResultBean.MyComplainBean bean:b.getMyComplain()){
                                    if(bean.getPState().equals("1")){
                                        list.add(bean);
                                    }
                                }
                                for (ComplainResultBean.MyComplainBean bean:b.getMyComplain()){
                                    if(bean.getPState().equals("2")){
                                        list.add(bean);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                runOnUiThread(()->mRefreshLayout.setRefreshing(false));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        runOnUiThread(()->{
                            mRefreshLayout.setRefreshing(false);
                            SnackbarUtils.networkErr(ComplainListActivity.this,mRecyclerView);
                        });
                    }
                });

            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.rv_content);
        mRefreshLayout = (AutoSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    /**
     * recyclerView的点击监听
     */
    private ComplainListAdapter.OnRecyclerItemClick onRecyclerItemClick =
            new ComplainListAdapter.OnRecyclerItemClick() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = getIntent();
                    intent.setClass(mContext, ComplainResultActivity.class);
                    intent.putExtra("MyComplainBean", list.get(position));
                    startActivity(intent);
                }
            };

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("我的投诉");
        save.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 返回按钮按下
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
