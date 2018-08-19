package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import java.util.ArrayList;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.MonthlyListAdapter;
import hdo.com.andzq.bean.MonthlyBean;
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
 * description 月报Activity
 * author 陈锐
 * version 1.0
 * modified by 吴小雪  2017/12/5
 */
public class MyMonthlyActivity extends BaseActivity {
    private EmptyRecyclerView recyclerView;//空的recyclerview
    private Context mContext;//上下文
    private AutoSwipeRefreshLayout mRefreshLayout;//自动刷新refreshlayout
    private MonthlyBean mMonthlybean;//月报内容
    private ArrayList<MonthlyBean.BodyBean> list = new ArrayList<>();//月报内容
    private MonthlyListAdapter adpter;//适配器
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_monthly);
        mContext = this;
        initToolbar();
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View id_empty_view = findViewById(R.id.rl_empty);
        recyclerView.setEmptyView(id_empty_view);
        recyclerView.setAdapter(adpter = new MonthlyListAdapter(mContext, list, onRecyclerItemClick));//月报没有详情页
        handler.postDelayed(() -> mRefreshLayout.autoRefresh(),500);//延迟一段时间自动加载 为了更好的效果
        //设置刷新进度监听
        mRefreshLayout.setOnRefreshListener(this::getMonthly);
    }

    public void getMonthly() {
        PostRequest request = NetWorkUtils.getInstance().getMonthly(mContext,String.valueOf(1),String.valueOf(10));
        request.execute(new StringCallback() {
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
                LogUtils.e("getMonthly",s);
                if (s.contains(Constant.STATE_SUCCESS)) {
                    Gson gson = new Gson();
//                    s = getString(R.string.monthly);
                    mMonthlybean = gson.fromJson(s, MonthlyBean.class);
                    list.clear();
                    list.addAll(mMonthlybean.getBody());
                    adpter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                } else SnackbarUtils.networkErr(MyMonthlyActivity.this, mRefreshLayout);//连接失败
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                //网络连接失败请重试
                SnackbarUtils.networkErr(MyMonthlyActivity.this, mRefreshLayout);
                mRefreshLayout.setRefreshing(false);
                super.onError(call, response, e);
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        recyclerView = (EmptyRecyclerView) findViewById(R.id.rv_content);
        mRefreshLayout = (AutoSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    private MonthlyListAdapter.OnRecyclerItemClick onRecyclerItemClick = new MonthlyListAdapter.OnRecyclerItemClick() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(mContext, MonthContentActivity.class);
            intent.putExtra("content",list.get(position).getContent());
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
        title.setText("我的月报");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setVisibility(View.GONE);
    }

    /**
     * 点击菜单回调
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
