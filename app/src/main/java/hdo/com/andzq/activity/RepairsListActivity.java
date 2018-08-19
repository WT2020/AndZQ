package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.RepairsListAdapter;
import hdo.com.andzq.bean.MyRepairsListBean;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.utils.okhttp.UICallBack;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 报修列表显示界面
 * author 陈锐
 * version 1.0
 * Created 2017/4/6.
 * modified by 邓杰 增加从网络获取并显示数据 2017/4/21
 */
public class RepairsListActivity extends BaseActivity {
    Handler handler = new Handler();
    public static String TAG = "msg";
    private EmptyRecyclerView mRecyclerView;
    private Context mContext;
    private AutoSwipeRefreshLayout mRefreshLayout;
    private boolean mIsRefreshing;
    /**
     * 数据源
     */
    private List<MyRepairsListBean.MyRepairBean> list = new ArrayList<>();

    /**
     * 适配器
     */
    private RepairsListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_list);
        mIsRefreshing = true;
        mContext = this;
        initView();
        initToolbar();
        initData();
    }

    @Override
    protected void onResume() {
        mIsRefreshing = true;
        mRefreshLayout.autoRefresh();//自动刷新
        mIsRefreshing = false;
        super.onResume();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
        View id_empty_view = findViewById(R.id.rl_empty);
        mRecyclerView.setEmptyView(id_empty_view);
        adapter = new RepairsListAdapter(this, onRecyclerItemClick);
        adapter.setList(list);
        mRecyclerView.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                loadData();
            }
        });//下拉刷新
    }

    /**
     * 加载数据
     */
    private void loadData() {
        //清空数据
        list.clear();
        new Thread(() -> {
            Call call = NetWorkUtils.getInstance().getMyRepairsList(mContext);
            call.enqueue(new UICallBack() {
                @Override
                public void UIonFailure(Call call, IOException e) {
                    SnackbarUtils.networkErr(RepairsListActivity.this,mRecyclerView);
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void UIonResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if(ExamineUtils.isTokenErr(result)){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RepairsListActivity.this);
                        SpUtils.putString(RepairsListActivity.this,mContext.getString(R.string.preference_token),"");
                        dialog
                                .setTitle("提示")
                                .setMessage("登录已经失效、即将重新登录")
                                .setCancelable(false);

                        runOnUiThread(dialog::show);
                        handler.postDelayed(() -> {
                            ActivityCollector.finishAll();
                            startActivity(new Intent(RepairsListActivity.this, LoginActivity.class));
                        },2000);
                    }
                    if (analysisJson(result) != null) {
                        //解析数据
                        list.addAll(analysisJson(result));
                        if (mRefreshLayout.isRefreshing()) {
                            if (list.size() > 0) {
                                mRefreshLayout.setRefreshing(false);
                            } else {
                                new Handler().post(() -> {
                                    Toast.makeText(RepairsListActivity.this, "暂无数据!服务器无数据!", Toast
                                            .LENGTH_SHORT).show();
                                    mRefreshLayout.setRefreshing(false);
                                });
                            }
                        }
                        adapter.notifyDataSetChanged();
                        mIsRefreshing = false;
                    }
                }
            });
        }).start();

    }

    /**
     * 解析数据
     */
    private List<MyRepairsListBean.MyRepairBean> analysisJson(String result) {
        List<MyRepairsListBean.MyRepairBean> newList = new ArrayList<>();
        try {
            JSONObject js = new JSONObject(result);
            String res = js.getString("RESP_STATE");
            if (res.equals("STATE_SUCCESS") && res != null) {
                MyRepairsListBean bean = new Gson().fromJson(js.toString(), MyRepairsListBean.class);
                Log.e(TAG, "analysisJson: " + bean.getRESP_STATE() + bean.getMyRepair().size());
                newList = bean.getMyRepair();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newList;
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
    private RepairsListAdapter.OnRecyclerItemClick onRecyclerItemClick =
            new RepairsListAdapter.OnRecyclerItemClick() {
                @Override
                public void onItemClick(int position) {
                    if(list.size()>0&&list.get(position).getProgress().size()>0){
                        Intent intent = getIntent();
                        intent.setClass(mContext, RepairsProgressActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("progress", list.get(position));
//                        Log.e(TAG, "onItemClick: position"+position );
                        list.size();
//                        Log.e(TAG, "onItemClick: "+list.get(position).getAddress());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
//                        Toast.makeText(mContext, "详情为空", Toast.LENGTH_SHORT).show();
                        Snackbar.make(mRecyclerView, "维修人员暂未接单", Snackbar.LENGTH_SHORT).setAction("知道了", v -> {}).show();
                    }
                }
            };

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("我的报修");
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
     *
     * @param item item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


}
