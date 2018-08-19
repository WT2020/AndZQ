package hdo.com.andzq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.R;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.bean.FenGongSiInfoBean;
import hdo.com.andzq.bean.NewCustomerInfoBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 客服经新增新建客户的功能
 * author 王腾
 * version 1.0
 * Created 2018/7/30
 */
public class NewCustomerActivity extends BaseActivity {

    private Button btnCancel;
    /**
     * 确定
     */
    private Button btnCommit;
    /**
     * Toolbar
     */
    private android.support.v7.widget.Toolbar toolbar;
    /**
     * 标题
     */
    private TextView toolbarTitle;
    /**
     * 保存
     */
    private TextView toolbarSave;

    /**
     *新建客户姓名
     */
    private EditText editNewCustomerName;

    /**
     *新建客户账号
     */
    private EditText editNewCustomerNum;

    /**
     *新建密码
     */
    private EditText editNewCustomerPass;


    private TextView tvfenGongSi;


    /**
     *二级机构类型
     */
    private EditText editNewCustomerOrganizationType;

    /**
     *职位
     */
    private TextView editNewCustomerType;

    /**
     *联系方式
     */
    private EditText editNewCustomerPhone;
    /**
     * 分公司类型数据
     */
    private String[] fenGongsiType;

    /**
     * 选择分公司类型位置
     */
    private int companyPosition;

    /**
     * 选中的分公司的值
     */
    private String selectCompany;
    /**
     * 分公司的值
     */
    private String tvFenGongSiData;

    /**
     * 集团号控件
     */
    private EditText editZhuanXian;
    /**
     * 专线号控件
     */
    private EditText editSpecialLineNumber;


    private FenGongSiInfoBean fenGongSiInfoBean;


    /**
     * 选择类型的位置
     */
    private int typePosition;
    /**
     * 输入的文本内容
     */
    private Context mContext;//上下文

    private String nameData = "";
    private String numData = "";
    private String passData = "";
    private String erjiData = "";
    private String phoneData = "";
    private String zhuanxianData = "";
    private String editSpecialLineNumberData = "";

    private String[] mType;
    private Handler handler = new Handler();// handler对象
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        mContext = this;
        //初始化控件
        initView();
        setSupportActionBar(toolbar);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //initData();
        //设置控件
        setView();
        //initSpinner();
        //读取网络信息
        readNetData();
        //读取本地内容
        readData();

    }

    private void readNetData() {
        PostRequest request = NetWorkUtils.getInstance().loadMyData(mContext);
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

                LogUtils.e("loadMyData",s);
                if(!TextUtils.isEmpty(s)&&s.contains(Constant.STATE_SUCCESS)){
                    Log.e("ONERROR","success");
                    Gson gson = new Gson();
                    fenGongSiInfoBean = gson.fromJson(s, FenGongSiInfoBean.class);
                    runOnUiThread(()->{
                        tvfenGongSi.setText(fenGongSiInfoBean.getBody().getDistrict());
                        //请求成功 保存数据
                        SpUtils.putString(mContext,"newcustomerfengongsi",fenGongSiInfoBean.getBody().getDistrict());
                    });
                }else runOnUiThread(()-> SnackbarUtils.networkErr(NewCustomerActivity.this,editNewCustomerName));
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e("ONERROR","error");
                runOnUiThread(()->SnackbarUtils.networkErr(NewCustomerActivity.this,editNewCustomerName));
            }
        });
    }


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
        toolbarTitle.setText("新建客户");
        //取消按钮 返回
        btnCancel.setOnClickListener(v -> finish());
        //确定按钮 提交数据
        btnCommit.setOnClickListener(v -> submitData());
        //保存
        toolbarSave.setOnClickListener(v -> {
            saveData();
            finish();
        });
    }

    /**
     * 提交数据
     */
    private void submitData() {
        //确定对话框
        DialogUtils.submit(NewCustomerActivity.this).setPositiveButton("确定", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (editNewCustomerName.getText().toString().equals("")){
                    ToastUtils.makeText(NewCustomerActivity.this,"用户名为空！");
                }else if (editNewCustomerNum.getText().toString().equals("")){
                    ToastUtils.makeText(NewCustomerActivity.this,"账号为空！");
                }else if (editNewCustomerPass.getText().toString().equals("")){
                    ToastUtils.makeText(NewCustomerActivity.this,"密码为空！");
                }else if (editNewCustomerOrganizationType.getText().toString().equals("")){
                    ToastUtils.makeText(NewCustomerActivity.this,"二级机构为空！");
                }else if (editNewCustomerPhone.getText().toString().equals("")){
                    ToastUtils.makeText(NewCustomerActivity.this,"联系方式为空！");
                }
                else {
                    //获取已经填过的 bean
                    NewCustomerInfoBean bean = getFilledInfoBean();
                    //构建 网络请求request
                    PostRequest request = NetWorkUtils.getInstance()
                            .upLoadNewCustomer(mContext, bean.getMap());
                    //展示进度对话框
                    final ProgressDialog progressDialog = DialogUtils.progress(NewCustomerActivity.this,
                            "上传客户信息");
                    progressDialog.show();

                    request.execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            //成功上传 提示
                            progressDialog.dismiss();
                            ToastUtils.makeText(NewCustomerActivity.this, "提交成功");
                            Log.e("msg", "onSuccess: "+s);
                            clearData();
                            finish();
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            progressDialog.dismiss();
                            ToastUtils.makeText(NewCustomerActivity.this, "提交失败,请重试");
                        }
                    });
                }
            }
        }).show();
    }

    /**
     * 获取 填入数据后的InfoBean
     */
    private NewCustomerInfoBean getFilledInfoBean() {
        NewCustomerInfoBean bean = new NewCustomerInfoBean();
//        int com=spCompanyType.getSelectedItemPosition();
//        bean.setFengongsitype(com+"");
        bean.setName(editNewCustomerName.getText().toString());
        bean.setNum(editNewCustomerNum.getText().toString());
        bean.setPass(editNewCustomerPass.getText().toString());
        bean.setErji(editNewCustomerOrganizationType.getText().toString());
        bean.setZhiwei(editNewCustomerType.getText().toString());
        bean.setPhone(editNewCustomerPhone.getText().toString());
        bean.setZhuanxianhao(editZhuanXian.getText().toString());
        bean.setFengongsitype(tvfenGongSi.getText().toString());
        bean.setEditSpecialLineNumber(editSpecialLineNumber.getText().toString());
        return bean;
    }

    /**
     * 读取内容
     */
    private void readData() {
        nameData = SpUtils.getString(this, "newcustomername", null);
        numData = SpUtils.getString(this, "newcustomernum", null);
        passData = SpUtils.getString(this, "newcustomerpass", null);
        erjiData = SpUtils.getString(this, "newcustomererji", null);
        phoneData = SpUtils.getString(this, "newcustomerphone", null);
        zhuanxianData = SpUtils.getString(this, "newcustomerzhuanxian", null);
        editSpecialLineNumberData = SpUtils.getString(this, "newcustomerspeciallinenumber", null);
        tvFenGongSiData = SpUtils.getString(this, "newcustomerfengongsi", null);
//        companyPosition = SpUtils.getInt(this, "newcustomerposition", 1);
        if (nameData != null) {
            editNewCustomerName.setText(nameData);
        }if (numData != null) {
            editNewCustomerNum.setText(numData);
        }if (passData != null) {
            editNewCustomerPass.setText(passData);
        }if (erjiData != null) {
            editNewCustomerOrganizationType.setText(erjiData);
        }if (phoneData != null) {
            editNewCustomerPhone.setText(phoneData);
        }if (zhuanxianData != null) {
            editZhuanXian.setText(zhuanxianData);
        }if (tvFenGongSiData != null) {
            tvfenGongSi.setText(tvFenGongSiData);
        }if (editSpecialLineNumberData != null) {
            editSpecialLineNumber.setText(editSpecialLineNumberData);
        }
//        spCompanyType.setSelection(companyPosition);
    }

    /**
     * 清空数据
     */
    private void clearData() {
        nameData = "";
        numData = "";
        passData = "";
        erjiData = "";
        phoneData = "";
        zhuanxianData = "";
        editSpecialLineNumberData = "";
//        companyPosition = 0;
        //保存到本地
        SpUtils.putString(this, "newcustomername", null);
        SpUtils.putString(this, "newcustomernum", null);
        SpUtils.putString(this, "newcustomerpass", null);
        SpUtils.putString(this, "newcustomererji", null);
        SpUtils.putString(this, "newcustomerphone", null);
        SpUtils.putString(this, "newcustomerzhuanxian", null);
        SpUtils.putString(this, "newcustomerspeciallinenumber", null);
//        SpUtils.putInt(this, "newcustomerposition", companyPosition);
        editNewCustomerName.setText("");
        editNewCustomerNum.setText("");
        editNewCustomerPass.setText("");
        editNewCustomerOrganizationType.setText("");
        editNewCustomerPhone.setText("");
        editZhuanXian.setText("");
        editSpecialLineNumber.setText("");
//        spCompanyType.setSelection(companyPosition);

    }

    /**
     * 保存功能
     */
    private void saveData() {
        //新建客户数据
        nameData = editNewCustomerName.getText().toString();
        numData = editNewCustomerNum.getText().toString();
        passData = editNewCustomerPass.getText().toString();
        erjiData = editNewCustomerOrganizationType.getText().toString();
        phoneData = editNewCustomerPhone.getText().toString();
        zhuanxianData = editZhuanXian.getText().toString();
        tvFenGongSiData = tvfenGongSi.getText().toString();
        //保存到本地
        SpUtils.putString(this, "newcustomername", nameData);
        SpUtils.putString(this, "newcustomernum", numData);
        SpUtils.putString(this, "newcustomerpass", passData);
        SpUtils.putString(this, "newcustomererji", erjiData);
        SpUtils.putString(this, "newcustomerphone", phoneData);
        SpUtils.putString(this, "newcustomerzhuanxian", zhuanxianData);
        SpUtils.putString(this, "newcustomerfengongsi", tvFenGongSiData);
        SpUtils.putString(this, "newcustomerspeciallinenumber", editSpecialLineNumberData);
//        SpUtils.putInt(this, "newcustomerposition", companyPosition);
        makToast("保存成功");
    }

    /**
     * 初始化控件
     */
    private void initView() {

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCommit = (Button) findViewById(R.id.btn_commit);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);

        editNewCustomerName = (EditText) findViewById(R.id.edit_new_customer_name);
        editNewCustomerNum = (EditText) findViewById(R.id.edit_new_customer_num);
        editNewCustomerPass = (EditText) findViewById(R.id.edit_new_customer_pass);
        tvfenGongSi = (TextView) findViewById(R.id.tv_new_customer_fen_gong_si);
        editNewCustomerOrganizationType = (EditText) findViewById(R.id.edit_new_customer_organization_type);
        editNewCustomerType = (TextView) findViewById(R.id.edit_new_customer_type);
        editNewCustomerPhone = (EditText) findViewById(R.id.edit_new_customer_phone);
        editZhuanXian = (EditText) findViewById(R.id.edit_zhuanxian);
        editSpecialLineNumber = (EditText) findViewById(R.id.edit_special_line_number);

    }


    /**
     * toast方法
     */
    private void makToast(String str) {
        Toast.makeText(NewCustomerActivity.this, str + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存数据(异常状态)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        nameData = editNewCustomerName.getText().toString();
        numData = editNewCustomerNum.getText().toString();
        passData = editNewCustomerPass.getText().toString();
        erjiData = editNewCustomerOrganizationType.getText().toString();
        phoneData = editNewCustomerPhone.getText().toString();
        editSpecialLineNumberData = editSpecialLineNumber.getText().toString();
        outState.putString("name", nameData);
        outState.putString("num", numData);
        outState.putString("pass", passData);
        outState.putString("erji", erjiData);
        outState.putString("phone", phoneData);
        outState.putString("specialline", editSpecialLineNumberData);
        outState.putString("fengongsidata", tvFenGongSiData);
//        outState.putInt("position", companyPosition);
    }

    /**
     * 读取数据(异常状态)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        nameData = savedInstanceState.getString("name");
        numData = savedInstanceState.getString("num");
        passData = savedInstanceState.getString("pass");
        erjiData = savedInstanceState.getString("erji");
        phoneData = savedInstanceState.getString("phone");
        phoneData = savedInstanceState.getString("zhuanxian");
        tvFenGongSiData = savedInstanceState.getString("fengongsidata");
        editSpecialLineNumberData = savedInstanceState.getString("specialline");
//        companyPosition = savedInstanceState.getInt("position");

        editNewCustomerName.setText(nameData);
        editNewCustomerNum.setText(numData);
        editNewCustomerPass.setText(passData);
        editNewCustomerOrganizationType.setText(erjiData);
        editNewCustomerPhone.setText(phoneData);
        editZhuanXian.setText(zhuanxianData);
        editZhuanXian.setText(editSpecialLineNumberData);
        tvfenGongSi.setText(tvFenGongSiData);
//        spCompanyType.setSelection(companyPosition);
    }
}
