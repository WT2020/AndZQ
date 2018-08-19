package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.ProductListAdapter;
import hdo.com.andzq.bean.ProductBean;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.AutoSwipeRefreshLayout;
import hdo.com.andzq.view.EmptyRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 我的产品列表Activity
 * author 陈锐
 * version 1.0
 * created 2017/4/11
 */
public class MyProductActivity extends BaseActivity {

    /**
     * 内容recyclerView
     */
    private EmptyRecyclerView mRecyclerView;

    /**
     * 上下文
     */
    private Context mContext;
    private AutoSwipeRefreshLayout mRefreshLayout;
    private ArrayList<ProductBean.BodyBean> list = new ArrayList<>();
    private ProductListAdapter adapter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        mContext = this;
        initToolbar();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();//自动刷新
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("我的产品");
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /* 初始化数据 */
    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        View id_empty_view = findViewById(R.id.rl_empty);
        mRecyclerView.setEmptyView(id_empty_view);
        mRecyclerView.setAdapter(adapter = new ProductListAdapter(mContext, list, (v, position) -> {

        }));
        mRefreshLayout.setOnRefreshListener(this::getProduct);//下拉刷新
    }

    private void getProduct() {
        PostRequest product = NetWorkUtils.getInstance().getProduct(mContext);
        product.execute(new StringCallback() {
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

                if (!TextUtils.isEmpty(s) && s.contains("STATE_SUCCESS")) {
                    Gson gson = new Gson();
                    ProductBean productBean = gson.fromJson(s, ProductBean.class);
//                    ProductBean productBean = gson.fromJson(mContext.getString(R.string.product),
//                            ProductBean.class);
                    list.clear();
                    list.addAll(productBean.getBody());
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    });
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                runOnUiThread(() -> {
                    SnackbarUtils.networkErr(MyProductActivity.this, mRecyclerView);
                    mRefreshLayout.setRefreshing(false);
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
}
