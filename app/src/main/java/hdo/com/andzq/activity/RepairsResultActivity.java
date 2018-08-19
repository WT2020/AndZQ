package hdo.com.andzq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;

/**
 * description 报修的处理结果的Activity
 * author 陈锐
 * version 1.0
 * Created by admin on 2017/4/1.
 */
public class RepairsResultActivity extends BaseActivity {
    /**
     * 提交按钮
     */
    private Button mBtnCommit;

    /**
     * 取消按钮
     */
    private Button mBtnCancel;

    /**
     * 申请人
     */
    private TextView mTvProposer;

    /**
     * 当前申请时间
     */
    private TextView mTvApplicationTime;

    /**
     * 所在部门
     */
    private TextView mTvDepartment;

    /**
     * 电话号码
     */
    private EditText mEtPhoneNum;

    /**
     * 单位名称
     */
    private TextView mTvUnit;

    /**
     * 故障出现时间
     */
    private TextView mTvDowntime;

    /**
     * 故障位置
     */
    private EditText mEtLocation;

    /**
     * 故障类型
     */
    private Spinner mSpFaultType;

    /**
     * 故障描述
     */
    private EditText mEtDescription;

    /**
     * 放置照片的线性布局
     */
    private LinearLayout mRlPhotos;

    /**
     * 照相按钮
     */
    private ImageButton mBtnTakePhoto;

    /**
     * 紧急程度
     */
    private Spinner mSpRepairsUrgencyLevel;

    private Toolbar toolbar;
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
        setContentView(R.layout.activity_repairs_result);

        //初始化控件
        initView();
        initToolbar();

        //设置控件
        setView();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        //设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    //设置返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置控件
     */
    private void setView() {
        //设置标题
        toolbarTitle.setText("处理详情");
        toolbarSave.setVisibility(View.GONE);
        mEtDescription.setText("网络不通，网线断了");
        mBtnTakePhoto.setVisibility(View.GONE);

        //禁止用输入
        mEtPhoneNum.setKeyListener(null);
        mEtDescription.setKeyListener(null);
        mEtLocation.setKeyListener(null);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);

        mBtnTakePhoto = (ImageButton) findViewById(R.id.btn_take_photo);

        mTvProposer = (TextView) findViewById(R.id.tv_proposer);
        mTvApplicationTime = (TextView) findViewById(R.id.tv_application_time);
        mTvDepartment = (TextView) findViewById(R.id.tv_department);
        mEtPhoneNum = (EditText) findViewById(R.id.tv_phone_num);
        mTvUnit = (TextView) findViewById(R.id.tv_company);
        mTvDowntime = (TextView) findViewById(R.id.tv_downtime);
        mEtLocation = (EditText) findViewById(R.id.tv_location);
        mEtDescription = (EditText) findViewById(R.id.tv_description);
        mRlPhotos = (LinearLayout) findViewById(R.id.Ll_photos);
    }
}
