package hdo.com.andzq.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hdo.com.andzq.R;
import hdo.com.andzq.activity.ComplainActivity;
import hdo.com.andzq.activity.LoginActivity;
import hdo.com.andzq.activity.MyRepairsListActivity;
import hdo.com.andzq.activity.NewCustomerActivity;
import hdo.com.andzq.activity.NewsContentActivity;
import hdo.com.andzq.activity.RepairsActivity;
import hdo.com.andzq.activity.RepairsListActivity;
import hdo.com.andzq.adapter.NewsAdapter;
import hdo.com.andzq.base.BaseFragment;
import hdo.com.andzq.bean.NewsBean;
import hdo.com.andzq.bean.NewsBodyBean;
import hdo.com.andzq.bean.SupportStaffBean;
import hdo.com.andzq.decoration.DividerItemDecoration;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import hdo.com.andzq.view.RoundRectImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 首页Fragment
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 * modify 添加了跳转投诉页面 by 邓杰 on 2017/3/30
 * modify 添加GridView by 陈锐 on 2017/3/30
 * modify by 张建银 on 2017/8/29 修改客服团队头像和其他信息从服务器获取
 * modified by 张建银 on 2017/10/20 修复头像不能显示的bug
 * modified by 张建银 on 2017/10/28 修复账户无权限操作时界面无反应问题
 */

public class HomeFragment extends BaseFragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;
    /**
     * 顶部功能区按钮
     */
    MyAdapter adapter;
    SupportStaffBean supportStaffBean;
    private GridView mGvFunction;
    private int[] mDrawableIds;
    private String[] mTitileStrs;
    private EmptyRecyclerView mRvNews;//新闻

    private List<SupportStaffBean.BodyBean> staffList = new ArrayList<>();
    //角色
    private int role;
    private static final int ENGINEER = 3;//维修工程师
    private int phonePosisiton;//选择用户需要拨打的电话号码

    ArrayList<NewsBodyBean> newsBeenArray = new ArrayList<>();//新闻数据
    private AutoSwipeRefreshLayout swipeRefreshLayout;//home界面刷新控件
    private NewsAdapter newsAdapter;
    private int startPos = 1;
    private Handler handler = new Handler();
    private boolean isNewsLoading = false;

    /**
     * 初始化布局
     *
     * @return view对象
     */
    @Override
    public View initView(ViewGroup vg) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home, vg, false);
        swipeRefreshLayout = (AutoSwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mGvFunction = (GridView) view.findViewById(R.id.gv_function);
        mRvNews = (EmptyRecyclerView) view.findViewById(R.id.rv_content);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //判断角色
        role = Integer.parseInt(SpUtils.getString(mActivity, mActivity.getString(R.string
                .preference_role_id), "2"));
        LogUtils.e("role:", role + "");

//        if (role != 3) {
            //访问网络获取 客户信息
            Call call = NetWorkUtils.getInstance().getSupportStaff(mActivity);//PostRequest request
            new Thread(() -> {
                try {
                    Response response = call.execute();
                    String body = response.body().string();
                    Log.e("body11", body);
                    if (body.contains(Constant.TOKEN_ERR)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        SpUtils.putString(mActivity, mActivity.getString(R.string.preference_token), "");
                        dialog
                                .setTitle("提示")
                                .setMessage("登录已经失效、即将重新登录")
                                .setCancelable(false);
                        getActivity().runOnUiThread(dialog::show);
                        handler.postDelayed(() -> {
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            mActivity.finish();
                        }, 2000);
                    } else {
                        Gson gson = new Gson();
//                    SpUtils.putString(mActivity, mActivity.getString(R.string
//                            .preference_support_staff), body);
                        SupportStaffBean staffBean = gson.fromJson(body, SupportStaffBean.class);
                        staffList.clear();
                        staffList.addAll(staffBean.getBody());
                        mActivity.runOnUiThread(adapter::notifyDataSetChanged);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
//        }

        //设置垂直滑动
        mRvNews.setLayoutManager(new LinearLayoutManager(mActivity));
        View id_empty_view = getView().findViewById(R.id.rl_empty);
        mRvNews.setEmptyView(id_empty_view);
        mRvNews.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mRvNews.setAdapter(newsAdapter = new NewsAdapter(mActivity, newsBeenArray, onRecyclerItemClick));
        mRvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isNewsLoading) {
                    int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
                    int childCount = manager.getItemCount();
                    if (lastVisiblePosition >= childCount - 1 && isSlidingToLast) {
                        isNewsLoading = true;//是否已经在加载新闻数据
                        View v = recyclerView.getChildAt(manager.getChildCount() - 1);

                        ContentLoadingProgressBar pbLoadMore = (ContentLoadingProgressBar) v.findViewById(R.id.pb_load_more);
                        ImageView ivInfo = (ImageView) v.findViewById(R.id.iv_info);
                        TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);

                        pbLoadMore.setVisibility(View.VISIBLE);
                        ivInfo.setVisibility(View.INVISIBLE);
                        tvMsg.setVisibility(View.VISIBLE);
                        tvMsg.setText("加载更多...");
                        startPos = manager.getItemCount();
                        loadNetNews(startPos, startPos + 10, false);//加载更多的逻辑
                        LogUtils.e("startPos", startPos + "");
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
        //根据不同的角色展示不同的图标
        switch (role) {
            case 1://普通用户
                mTitileStrs = new String[]{"报修申请", "投诉建议", "新建进度", "我的维修单"};
                mDrawableIds = new int[]{R.mipmap.home_btn_repair,
                        R.mipmap.home_btn_complaint,
                        R.mipmap.home_btn_query,
                        R.mipmap.home_btn_my_repairs
//                        R.mipmap.home_btn_limits, 权限管理
                };
                break;
            case ENGINEER://维修人员 隐藏客服人员
                mTitileStrs = new String[]{"我的维修单", "新建进度"};
                mDrawableIds = new int[]{R.mipmap.home_btn_my_repairs, R.mipmap.home_btn_query,};
                break;
            case 6://超管
                mTitileStrs = new String[]{"报修申请", "投诉建议", "我的报修单", "我的维修单"};
                mDrawableIds = new int[]{R.mipmap.home_btn_repair,
                        R.mipmap.home_btn_complaint,
                        R.mipmap.home_btn_query,
                        R.mipmap.home_btn_my_repairs
                };
                break;
            case 4://客户经理
                mTitileStrs = new String[]{"报修申请", "新建客户", "新建进度", "我的维修单"};
                mDrawableIds = new int[]{R.mipmap.home_btn_repair,
                        R.mipmap.home_btn_manager,
                        R.mipmap.home_btn_query,
                        R.mipmap.home_btn_my_repairs
                };
                break;
            case 2://所有
            default:
//                mTitileStrs = new String[]{"报修申请", "投诉建议", "新建进度", "权限管理", "客服经理", "我的维修单"};
//                mDrawableIds = new int[]{R.mipmap.home_btn_repair,
//                        R.mipmap.home_btn_complaint,
//                        R.mipmap.home_btn_query,
//                        R.mipmap.home_btn_limits,
//                        R.mipmap.home_btn_manager,
//                        R.mipmap.home_btn_my_repairs};
                mTitileStrs = new String[]{"报修申请", "投诉建议", "新建进度", "我的维修单"};
                mDrawableIds = new int[]{R.mipmap.home_btn_repair,
                        R.mipmap.home_btn_complaint,
                        R.mipmap.home_btn_query,
                        R.mipmap.home_btn_my_repairs};
                break;
        }

        adapter = new MyAdapter();
        mGvFunction.setAdapter(adapter);
        mGvFunction.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("ADMINID:", "" + role);
            //维修人员
            if (role == ENGINEER) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        showStaffInfoDialog(position);
                        break;
                    case 4:
                        toActivity(mActivity, MyRepairsListActivity.class);
                        break;
                    case 5:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        //toActivity(mActivity, NewListActivity.class);
                        break;
                }
            } else if (role == 1) {//普通用户（政企客户）
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        showStaffInfoDialog(position);
                        break;
                    case 4:
                        toActivity(mActivity, RepairsActivity.class);
                        break;
                    case 5:
                        toActivity(mActivity, ComplainActivity.class);
                        break;
                    case 6:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        //toActivity(mActivity, NewListActivity.class);
                        break;
                    case 7:
                        toActivity(mActivity, RepairsListActivity.class);
                        break;
                    default:
                        break;
                }
            } else if (role == 6) {//超管
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        showStaffInfoDialog(position);
                        break;
                    case 4:
                        toActivity(mActivity, RepairsActivity.class);
                        break;
                    case 5:
                        toActivity(mActivity, ComplainActivity.class);
                        break;
                    case 6:
                        toActivity(mActivity, RepairsListActivity.class);
                        break;
                    case 7:
                        toActivity(mActivity, MyRepairsListActivity.class);
                        break;
                    default:
                        break;
                }
            } else if (role == 4) {//客户经理
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        showStaffInfoDialog(position);
                        break;
                    case 4:
                        toActivity(mActivity, RepairsActivity.class);
                        break;
                    case 5:
                       // toActivity(mActivity, AddCustomerActivity.class);
                        toActivity(mActivity, NewCustomerActivity.class);
                        break;
                    case 6:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        break;
                    case 7:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        break;
                }
            }
            else {//所有
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        showStaffInfoDialog(position);
                        break;
                    case 4:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        //toActivity(mActivity, RepairsActivity.class);
                        break;
                    case 5:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        //toActivity(mActivity, ComplainActivity.class);
                        break;
                    case 6:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        //toActivity(mActivity, NewListActivity.class);
                        break;
                    case 7:
                        ToastUtils.makeText(getActivity(), "您的账户没有该权限！");
                        //toActivity(mActivity, MyRepairsListActivity.class);
//                        toActivity(mActivity, PermissionManageActivity.class);
                        break;
//                    case 8:
//                        toActivity(mActivity, StaffInfoActivity.class);
//                        break;
//                    case 9:
//                        toActivity(mActivity, MyRepairsListActivity.class);
//                        break;
                    default:
                        break;
                }
            }
        });
        //刷新时间大于一个小时 从网络获取数据 否则从数据库取数据
        if (TimeUtils.compareHour(System.currentTimeMillis(),
                SpUtils.getLong(mActivity, getString(R.string.preference_news_refresh), 0)) > 1) {
            swipeRefreshLayout.autoRefresh();//自动刷新
        } else {
            mActivity.runOnUiThread(() -> {
                List<NewsBodyBean> all = DataSupport.findAll(NewsBodyBean.class);//读取数据库
                newsBeenArray.clear();
                newsBeenArray.addAll(all);
                newsAdapter.notifyDataSetChanged();
                handler.postDelayed(() -> {
                    if (mRvNews.getChildCount() < 10) {//如果收到的数据小于10
                        //这里 -1 拿到RecyclerView 脚布局
                        View v = mRvNews.getChildAt(mRvNews.getChildCount() - 1);
                        LogUtils.e("mRvNews.getAdapter().getItemCount()", mRvNews.getAdapter().getItemCount() + "");
                        ContentLoadingProgressBar pbLoadMore = (ContentLoadingProgressBar) v.findViewById(R.id.pb_load_more);
                        ImageView ivInfo = (ImageView) v.findViewById(R.id.iv_info);
                        TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
                        if (pbLoadMore != null) {
                            pbLoadMore.setVisibility(View.INVISIBLE);
                            ivInfo.setVisibility(View.VISIBLE);
                            tvMsg.setText("没有更多啦！");
                        }
                    }
                }, 1000);

            });
        }
        swipeRefreshLayout.setOnRefreshListener(() -> loadNetNews(1, 10, true));
    }

    /**
     * 加载网络新闻
     */
    private void loadNetNews(int start, int end, boolean clearing) {
        Map<String, String> map = new HashMap<>();
        map.put("start", start + "");
        map.put("end", end + "");
        NetWorkUtils.getInstance().loadNews(mActivity, map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                LogUtils.e("新闻", s);
                if (s.contains(Constant.STATE_SUCCESS)) {
                    Gson gson = new Gson();
                    NewsBean newsBean = gson.fromJson(s, NewsBean.class);
                    //保存请求网络的时间
                    SpUtils.putLong(mActivity, getString(R.string.preference_news_refresh), System.currentTimeMillis());
                    List<NewsBodyBean> body = newsBean.getBody();

                    LogUtils.e("body size", body.size() + "");
                    if (body.size() < 10) {//如果收到的数据小于10
                        //这里 -1 拿到RecyclerView 脚布局
                        View v = mRvNews.getChildAt(mRvNews.getChildCount() - 1);
                        LogUtils.e("mRvNews.getAdapter().getItemCount()", mRvNews.getAdapter().getItemCount() + "");
                        ContentLoadingProgressBar pbLoadMore = (ContentLoadingProgressBar) v.findViewById(R.id.pb_load_more);
                        ImageView ivInfo = (ImageView) v.findViewById(R.id.iv_info);
                        TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
                        if (pbLoadMore != null) {
                            handler.postDelayed(() -> {
                                isNewsLoading = false;
                                pbLoadMore.setVisibility(View.INVISIBLE);
                                ivInfo.setVisibility(View.VISIBLE);
                                tvMsg.setVisibility(View.VISIBLE);
                                tvMsg.setText("没有更多啦！");
                            }, 500);
                        }
                    }
                    if (clearing) {
                        newsBeenArray.clear();
                        DataSupport.deleteAll(NewsBodyBean.class);//先清空数据库
                    }
                    DataSupport.saveAll(body);//将新闻内容缓存到本地
                    newsBeenArray.addAll(body);//将新闻加到List中
                    mActivity.runOnUiThread(newsAdapter::notifyDataSetChanged);
                } else {
                    SnackbarUtils.networkErr(mActivity, swipeRefreshLayout);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                SnackbarUtils.networkErr(mActivity, swipeRefreshLayout);
                super.onError(call, response, e);
            }
        });
    }

    /**
     * 展示员工信息的dialog
     */
    private void showStaffInfoDialog(final int position) {
        phonePosisiton = position;
        if (staffList.size() <= 0) return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View view = View.inflate(mActivity, R.layout.dialog_staffinfo, null);
        RoundRectImageView ivStaffPhoto = (RoundRectImageView) view.findViewById(R
                .id.iv_staff_photo);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvPosition = (TextView) view.findViewById(R.id.tv_position);
        TextView tvTel = (TextView) view.findViewById(R.id.tv_tel);
        if (staffList.size() > 0) {
            tvName.setText(staffList.get(position).getName());
            tvPosition.setText(staffList.get(position).getPosition());
            tvTel.setText(staffList.get(position).getPhone());
            Picasso.with(getContext())
                    .load(Constant.HOME + staffList.get(position).getPhoto())
                    .placeholder(R.mipmap.load)
                    .error(R.mipmap.app_icon)
                    .resize(128, 128)
                    .centerCrop()
                    .into(ivStaffPhoto);
        }
        if (staffList.get(position).getPhone() == "") {
            builder.setView(view)
                    .setNegativeButton("关闭", (dialog, which) -> {
                    })
                    .setPositiveButton("拨打电话", (dialog, which) -> {
                        Toast.makeText(mActivity, "没有号码可以拨打！", Toast.LENGTH_SHORT).show();
                    })
                    .setCancelable(true)
                    .show();
        } else {
            builder.setView(view)
                    .setNegativeButton("关闭", (dialog, which) -> {
                    })
                    .setPositiveButton("拨打电话", (dialog, which) -> callPhone(position))
                    .setCancelable(true)
                    .show();
        }
    }

    /**
     * 拨打电话
     */
    private void callPhone(int position) {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
//            phonePosisiton = position;
            String phone = staffList.get(position).getPhone();
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 系统授权结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LogUtils.e("onRequestPermissionsResult", "请求权限");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    String phone = staffList.get(phonePosisiton).getPhone();
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    LogUtils.e("else", "没有权限");
                    ToastUtils.makeText(mActivity, "授权失败");
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private NewsAdapter.OnRecyclerItemClick onRecyclerItemClick =
            (v, position) -> {
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra(NewsContentActivity.NEWS_NAME, newsBeenArray.get(position).getTitle());
                intent.putExtra(NewsContentActivity.NEWS_CONTENT, newsBeenArray.get(position).getContents());
                intent.putExtra(NewsContentActivity.NEWS_ICON, newsBeenArray.get(position).getImage());
                startActivity(intent);
            };

    /**
     * GradeView 适配器
     */
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
//            if (role == ENGINEER) {
//                return mTitileStrs.length;
//            }
            return mTitileStrs.length + 4;
        }

        @Override
        public Object getItem(int position) {
            return mTitileStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position <= 3 ) {
                // 第一排显示员工图片
                if (convertView == null) {
                    convertView = View.inflate(mActivity, R.layout.item_staff_info, null);
                }
                RoundRectImageView ivStaffPhoto = (RoundRectImageView) convertView.findViewById(R.id.iv_staff_photo);
                TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
                TextView tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
                if (staffList.size() > 0) {
                    tvName.setText(staffList.get(position).getName());
                    tvPosition.setText(staffList.get(position).getPosition());
                    Picasso.with(getContext()).load(Constant.HOME + staffList.get(position).getPhoto()).placeholder(R.mipmap.load).error(R.mipmap.app_icon).resize(128, 128).centerCrop().into(ivStaffPhoto);
                }
                return convertView;
            } else {
                //第二排显示功能按钮
                // 复用convertView
                if (convertView == null) {
                    convertView = View.inflate(mActivity, R.layout.item_home_function_btn, null);
                }
                ImageView iv_item = (ImageView) convertView.findViewById(R.id.iv_item);
                TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//                if (role != ENGINEER) {
                    iv_item.setBackgroundResource(mDrawableIds[position - 4]);
                    tv_title.setText(mTitileStrs[position - 4]);
//                }
                return convertView;
            }
        }
    }
}
