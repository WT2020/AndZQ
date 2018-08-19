package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.QueryListAdapter;
import hdo.com.andzq.bean.NewProgressBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 新建进度列表activity
 * author 陈锐
 * version 1.0
 * created 2017/4/5
 */

public class NewListActivity extends BaseActivity {

    private EmptyRecyclerView mRecyclerView;
    private Context mContext;
    private AutoSwipeRefreshLayout mSwipeLayout;
    private List<NewProgressBean.BodyBean> list = new ArrayList<>();
    private QueryListAdapter adapter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        mContext = this;
        initView();
        initToolbar();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        View id_empty_view = findViewById(R.id.rl_empty);
        mRecyclerView.setEmptyView(id_empty_view);

        mRecyclerView.setAdapter( adapter= new QueryListAdapter(mContext, list,onRecyclerItemClick));

        //进入时调用一次刷新
        mSwipeLayout.autoRefresh();
        mSwipeLayout.setOnRefreshListener(() -> {
            PostRequest newProgress = NetWorkUtils.getInstance().getNewProgress(mContext);
            newProgress.execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if(ExamineUtils.isTokenErr(s)){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        SpUtils.putString(mContext,mContext.getString(R.string.preference_token),"");
                        dialog
                                .setTitle("提示")
                                .setMessage("登录已经失效、即将重新登录")
                                .setCancelable(false);
                        runOnUiThread(dialog::show);
                        handler.postDelayed(() -> {
                            ActivityCollector.finishAll();
                            startActivity(new Intent(mContext, LoginActivity.class));
                        },2000);
                    }

                    if(!TextUtils.isEmpty(s)&&s.contains(Constant.STATE_SUCCESS)){
                        Gson gson = new Gson();
                        LogUtils.e("新建",s);
//                        s = mContext.getString(R.string.new_progress);
                        NewProgressBean bean = gson.fromJson(s, NewProgressBean.class);
                        list.clear();
                        list.addAll(bean.getBody());
                        runOnUiThread(adapter::notifyDataSetChanged);
                        runOnUiThread(()->mSwipeLayout.setRefreshing(false));
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    runOnUiThread(()->{
                        mSwipeLayout.setRefreshing(false);
                        SnackbarUtils.networkErr(NewListActivity.this,mRecyclerView);
                    });
                }
            });
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.rv_content);
        mSwipeLayout = (AutoSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    /**
     * recyclerView的点击监听
     */
    private QueryListAdapter.OnRecyclerItemClick onRecyclerItemClick =
            new QueryListAdapter.OnRecyclerItemClick() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = getIntent();
                    intent.putExtra("progress",list.get(position));
                    intent.setClass(mContext, NewProgressActivity.class);
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
        title.setText("新建进度");
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
