package hdo.com.andzq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.PermissionManageListAdapter;

/**
 * description 权限管理界面
 * author 邓杰
 * version 1.0
 * Created by admin on 2017/4/5.
 */
public class PermissionManageActivity extends BaseActivity {
    /**
     * RecyclerView列表
     */
    private RecyclerView rvPermission;
    /**
     * 适配器
     */
    private PermissionManageListAdapter adapter;
    /**
     * 布局管理器
     */
    private LinearLayoutManager manager;
    private android.support.v7.widget.Toolbar toolbar;
    /**
     * 标题
     */
    private TextView toolbarTitle;
    /**
     * 保存按钮
     */
    private TextView toolbarSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_manage);


        //初始化数据
        iniData();
        //初始化控件
        initView();
        setSupportActionBar(toolbar);
        //设置返回按钮
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //设置控件
        setView();
    }
    //设置返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置控件
     */
    private void setView() {
        //设置布局管理器
        rvPermission.setLayoutManager(manager);
        //设置适配器
        rvPermission.setAdapter(adapter);
        toolbarSave.setVisibility(View.INVISIBLE);
        toolbarTitle.setText("权限管理");
    }

    /**
     * 初始化数据
     */
    private void iniData() {
        adapter = new PermissionManageListAdapter(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void initView() {
        rvPermission = (RecyclerView) findViewById(R.id.rv_permission);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);
    }
}
