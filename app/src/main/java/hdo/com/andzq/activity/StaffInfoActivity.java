package hdo.com.andzq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.StaffInfoGridViewAdapter;
import hdo.com.andzq.bean.StaffInfoBean;
import hdo.com.andzq.utils.ToastUtils;

/**
 * description 我的客服页面
 * author 邓杰
 * version 1.0
 * Created 2017/3/31.
 * modified 增加拨打电话功能 by 陈锐 on 2017/04/10
 */
public class StaffInfoActivity extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    /**
     * gridView
     */
    private GridView gvStaff;

    /**
     * 保存按钮
     */
    private TextView toolbarSave;
    /**
     * 客服的集合
     */
    private ArrayList<StaffInfoBean> list;
    /**
     * gridView的适配器
     */
    private StaffInfoGridViewAdapter adapter;

    /**
     * 上下文
     */
    private Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_info);
        mContext = this;
        //初始化控件
        initView();
        //初始化数据
        initData();
        initToolbar();

    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("客服经理");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list=new ArrayList<>();
        list.add(new StaffInfoBean(R.mipmap.app_icon,"何武吉","首席客服经理"));
        list.add(new StaffInfoBean(R.mipmap.app_icon,"邓桀","技术总监"));
        list.add(new StaffInfoBean(R.mipmap.app_icon,"邓杰","维护工程师"));
        adapter=new StaffInfoGridViewAdapter(this);
        adapter.setList(list);
        //设置数据
        adapter.setList(list);
        //设置适配器
        gvStaff.setAdapter(adapter);

        //添加点击事件
        gvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeCall("18980736472");
            }
        });

    }

    /**
     * 拨打电话
     */
    private void makeCall(String phoneNum) {

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            mContext.startActivity(intent);
        }
    }


    /**
     * 返回键按钮
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        gvStaff = (GridView) findViewById(R.id.gv_staff);
    }

    /**
     * 授权结果的回调
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        ToastUtils.makeText(StaffInfoActivity.this, "授权失败");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    //授权成功 拨打电话
                    makeCall("18970736472");
                } else {
                    ToastUtils.makeText(StaffInfoActivity.this, "授权失败");
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
