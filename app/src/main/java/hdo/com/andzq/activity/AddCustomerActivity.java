package hdo.com.andzq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.MySpinnerAdapter;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.bean.ComplainInfoBean;
import hdo.com.andzq.bean.EvaluateBean;
import hdo.com.andzq.bean.RepairsResponseBean;
import hdo.com.andzq.bean.UserBean;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.utils.okhttp.UICallBack;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * 项目名称：AndZQ
 * 创建人:李洋
 * 创建时间:2018/7/20  14:19
 * modify by 杨宝山 2018/7/25 增加项目经理增加用户信息的功能
 */

public class AddCustomerActivity extends BaseActivity {
    /*
    * 用户名
    * */
    private EditText EditCustomerName;
    /*
    * 用户账号
    * */
    private EditText EditCustomerNum;
    /*
    * 用户密码
    * */
    private EditText EditCustomerPass;
    /*
    * 分公司   //这里需要从后台获取数据，有待实现
    * */
    private TextView tvCustomerCompany;
    /*
    * 用户联系方式
    * */
    private EditText EditCustomerPhone;
    /*
    * 提交按钮
    * */
    private Button BtnCommit;
   /*
   * 取消按钮
   * */
   private ScrollView scrollView;//整个布局的ID
   private Button BtnCancel;
    private String[] mTypeStringArray;//类型
    private Context mContext;//上下文
    private TextView toolbarTitle;//标题栏
    private Handler handler = new Handler();//handler
    private TextView toolbarSave;//保存栏
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        mContext = this;
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initView();//初始化数据
        setView();//设置标题栏
//        initSpinner();//读取本地内容//因为没有需要选取的元素，所以不需要设置方法去实现，参考complianActivit代码的实现方法。
        submitData();

    }


    //控件标题栏
    private void setView() {
        toolbarTitle.setText("用户信息");
        //取消按钮 返回
        BtnCancel.setOnClickListener(v -> finish());
        //确定按钮 提交数据
        BtnCommit.setOnClickListener(v -> submitData());
        //保存
        toolbarSave.setOnClickListener(v -> {
            saveData();
            finish();
        });
    }

    //保存数据
    private void saveData() {
       SpUtils.putString(this, "Customer_name", EditCustomerName.getText().toString());
        SpUtils.putString(this, "Customer_num", EditCustomerNum.getText().toString());
        SpUtils.putString(this, "Customer_pass", EditCustomerPass.getText().toString());
        SpUtils.putString(this, "Customer_company", tvCustomerCompany.getText().toString());
        SpUtils.putString(this, "Customer_phone", EditCustomerPhone.getText().toString());
    }


    //提交数据
    private void submitData() {

            Call call = NetWorkUtils.getInstance().loadRepairsInfo(mContext);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String string = response.body().string();
                    if(ExamineUtils.isTokenErr(string)){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AddCustomerActivity.this);
                        SpUtils.putString(AddCustomerActivity.this,mContext.getString(R.string.preference_token),"");
                        dialog
                                .setTitle("提示")
                                .setMessage("登录已经失效、即将重新登录")
                                .setCancelable(false);
                        runOnUiThread(dialog::show);
                        handler.postDelayed(() -> {
                            startActivity(new Intent(AddCustomerActivity.this, LoginActivity.class));
                            AddCustomerActivity.this.finish();
                        },2000);
                    }else if (ExamineUtils.isStateErr(string)){
                        ToastUtils.makeText(AddCustomerActivity.this,"获取集团信息失败！");
                        EditCustomerName.setText("");
                        EditCustomerNum.setText("");
                        EditCustomerPass.setText("");
                        tvCustomerCompany.setText("");
                        EditCustomerPhone.setText("");
                    }else {
                        Gson gson = new Gson();
                        UserBean bean = gson.fromJson(string, UserBean.class);
                        runOnUiThread(()->{
                            tvCustomerCompany.setText(bean.getuCompanyId());//公司名字
                            EditCustomerName.setText(bean.getuName());
                            EditCustomerPhone.setText(bean.getTel());
                            EditCustomerNum.setText(bean.getuCode());//用户名
                            EditCustomerPass.setText(bean.getuPwd());//密码
                        });
                    }
                }
            });
        }



    private void initView() {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        EditCustomerName=(EditText)findViewById(R.id.edit_add_customer_name);
        EditCustomerNum=(EditText)findViewById(R.id.edit_add_customer_num);
        EditCustomerPass=(EditText)findViewById(R.id.edit_add_customer_pass);
        EditCustomerPhone=(EditText)findViewById(R.id.edit_add_customer_phone);
        BtnCommit=(Button)findViewById(R.id.btn_commit);
        BtnCancel=(Button)findViewById(R.id.btn_cancel);
        tvCustomerCompany=(TextView) findViewById(R.id.tv_customer_company);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);


    }


}




