package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.MyRepairsListAdapter;
import hdo.com.andzq.bean.MyRepairsDetailsBean;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * description 我的维修单Activity
 * author 陈锐
 * version 1.0
 * created 2017/4/7
 */

public class MyRepairsListActivity extends BaseActivity {
    private EmptyRecyclerView mRvContent;//内容
    private AutoSwipeRefreshLayout mRefreshLayout;//滑动刷新控件
    private List<MyRepairsDetailsBean.MyRepairBean> mRepairsBeanList = new ArrayList<>();//维修单详情
    private MyRepairsListAdapter mAdapter;//适配器

    private Context mContext;//上下文

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_repairs_list);
        mContext = this;
        initView();
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        mRefreshLayout.autoRefresh();//自动刷新
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRvContent.setLayoutManager(new LinearLayoutManager(MyRepairsListActivity.this));
        View id_empty_view = findViewById(R.id.rl_empty);
        mRvContent.setEmptyView(id_empty_view);
        mRvContent.setAdapter(mAdapter = new MyRepairsListAdapter(MyRepairsListActivity.this,
                mRepairsBeanList, (v, position) -> {
            Intent intent = new Intent(MyRepairsListActivity.this, MyRepairsDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.RepairsDetailsBean), mRepairsBeanList.get(position));//将类序列化后 传到bundle中
            intent.putExtras(bundle);
            startActivity(intent);
        }));
        //设置刷新进度监听
        mRefreshLayout.setOnRefreshListener(() -> {
            Call call = NetWorkUtils.getInstance().loadRepairsList(MyRepairsListActivity.this);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtils.makeText(MyRepairsListActivity.this, "网络连接失败");
                    runOnUiThread(() -> mRefreshLayout.setRefreshing(false));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    if (ExamineUtils.success(mContext, string)) {
                        LogUtils.e("list", "string" + string);
                        Gson gson = new Gson();
                        MyRepairsDetailsBean b = gson.fromJson(string, MyRepairsDetailsBean.class);
                        //先刷新一下 再添加数据
                        mRepairsBeanList.clear();
                        //如果直接赋值 不能刷新
                        for (int i=0;i<b.getMyRepair().size();i++) {
                            MyRepairsDetailsBean.MyRepairBean bean = b.getMyRepair().get(i);
                            if (bean.getRstate()==6){
                                b.getMyRepair().remove(bean);
                                i--;
                            }
                        }
                        mRepairsBeanList.addAll(b.getMyRepair());
                        Log.e("response", string);
                        runOnUiThread(() -> {
                            mAdapter.notifyDataSetChanged();
                            mRefreshLayout.setRefreshing(false);
                        });
                    } else {
                        //处理 登录失败逻辑
                        runOnUiThread(() -> mRefreshLayout.setRefreshing(false));
                    }
                }
            });
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mRvContent = (EmptyRecyclerView) findViewById(R.id.rv_content);
        mRefreshLayout = (AutoSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("我的维修单");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
