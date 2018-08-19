package hdo.com.andzq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.R;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.bean.CustomerDataInfoBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 我的资料的Activity
 * author 邓杰
 * version 1.0
 * Created by admin on 2017/4/1.
 */
public class CustomerDataActivity extends BaseActivity {
    /**
     * toolbar
     */
    private Toolbar toolbar;
    /**
     * 标题
     */
    private TextView toolbarTitle;
    /**
     * 保存
     */
    private TextView toolbarSave;
    private TextView tvName;
    private Context mContext;
    private TextView tvCompany;
    private TextView tvCompanyCode;
    private TextView tvLocation;
    private TextView tvPhoneNum;
    private CustomerDataInfoBean customerDataInfoBean;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        mContext = this;
        //初始化控件
        initView();
        setSupportActionBar(toolbar);
        //设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setView();//设置控件
        loadData();//加载本地数据
        loadNetData();//加载网络数据

    }

    /**
     * 加载网络数据
     */
    private void loadNetData() {
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
                    customerDataInfoBean = gson.fromJson(s, CustomerDataInfoBean.class);
                    runOnUiThread(()->{
                        //请求成功 保存数据
                        SpUtils.putString(mContext,mContext.getString(R.string.preference_people_name),customerDataInfoBean.getBody().getName());
                        SpUtils.putString(mContext,mContext.getString(R.string.preference_company_name),customerDataInfoBean.getBody().getCompany());
                        SpUtils.putString(mContext,mContext.getString(R.string.preference_company_code),customerDataInfoBean.getBody().getCompany_code());
                        SpUtils.putString(mContext,mContext.getString(R.string.preference_company_location),customerDataInfoBean.getBody().getLocation());
                        SpUtils.putString(mContext,mContext.getString(R.string.preference_people_phone),customerDataInfoBean.getBody().getPhone());
                        loadData();//读取展示数据
                    });
                }else runOnUiThread(()-> SnackbarUtils.networkErr(CustomerDataActivity.this,tvName));
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e("ONERROR","error");
                runOnUiThread(()-> SnackbarUtils.networkErr(CustomerDataActivity.this,tvName));
            }
        });
    }

    /**
     * 加载数据到布局
     */
    private void loadData() {
        tvName.setText(SpUtils.getString(mContext,mContext.getString(R.string.preference_people_name),""));
        tvCompany.setText(SpUtils.getString(mContext,mContext.getString(R.string.preference_company_name),""));
        tvCompanyCode.setText(SpUtils.getString(mContext,mContext.getString(R.string.preference_company_code),""));
        tvLocation.setText(SpUtils.getString(mContext,mContext.getString(R.string.preference_company_location),""));
        tvPhoneNum.setText(SpUtils.getString(mContext,mContext.getString(R.string.preference_people_phone),""));
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
        toolbarTitle.setText("我的资料");
        //隐藏保存
        toolbarSave.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvCompanyCode = (TextView) findViewById(R.id.tv_company_code);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);
    }
}
