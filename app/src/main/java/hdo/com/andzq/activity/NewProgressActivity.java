package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.ProgressAdapter;
import hdo.com.andzq.bean.NewProgressBean;

/**
 * description 新建进度Activity
 * author 陈锐
 * version 1.0
 * created 2017/4/6
 */

public class NewProgressActivity extends BaseActivity {

    private Context mContext;
    private ListView mLvProgress;
    ArrayList<NewProgressBean.BodyBean.ProgressBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_new_progress);
        mContext=this;
        initView();
        initToolbar();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        NewProgressBean.BodyBean body = (NewProgressBean.BodyBean) intent.getSerializableExtra("progress");
        list.clear();
        list.addAll(body.getProgress());
        ProgressAdapter myAdapter = new ProgressAdapter(mContext, list);
        mLvProgress.setAdapter(myAdapter);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("进度详情");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setVisibility(View.GONE);
    }

    /**
     * toolbar 按钮回调
     * @param item item
     * @return  boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mLvProgress = (ListView) findViewById(R.id.lv_progress);
    }
}
