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
import hdo.com.andzq.bean.ManagerDataInfoBean;
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
public class MyDataActivity extends BaseActivity {
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
    private Context mContext;
    private ManagerDataInfoBean managerDataInfoDataBean;

    private TextView innerManagerName;
    private TextView innerManagerCompany;
    private TextView innerManagerIdentity;
    private TextView innerManagerPhoneNumber;


    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_manage_data);
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
                    managerDataInfoDataBean = gson.fromJson(s, ManagerDataInfoBean.class);
                    runOnUiThread(()->{
                        //请求成功 保存数据
                        SpUtils.putString(mContext,mContext.getString(R.string.inner_manager_name),managerDataInfoDataBean.getBody().getName());
                        SpUtils.putString(mContext,mContext.getString(R.string.inner_manager_company),managerDataInfoDataBean.getBody().getDistrict());
                        SpUtils.putString(mContext,mContext.getString(R.string.inner_manager_identity),managerDataInfoDataBean.getBody().getRole());
                        SpUtils.putString(mContext,mContext.getString(R.string.inner_manager_phone_num),managerDataInfoDataBean.getBody().getPhone());
                        loadData();//读取展示数据
                    });
                }else runOnUiThread(()->SnackbarUtils.networkErr(MyDataActivity.this,innerManagerName));
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.e("ONERROR","error");
                runOnUiThread(()->SnackbarUtils.networkErr(MyDataActivity.this,innerManagerName));
            }
        });
    }

    /**
     * 加载数据到布局
     */
    private void loadData() {
        innerManagerName.setText(SpUtils.getString(mContext,mContext.getString(R.string.inner_manager_name),""));
        innerManagerCompany.setText(SpUtils.getString(mContext,mContext.getString(R.string.inner_manager_company),""));
        innerManagerIdentity.setText(SpUtils.getString(mContext,mContext.getString(R.string.inner_manager_identity),""));
        innerManagerPhoneNumber.setText(SpUtils.getString(mContext,mContext.getString(R.string.inner_manager_phone_num),""));
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

        innerManagerName = (TextView) findViewById(R.id.inner_mananger_name);
        innerManagerCompany = (TextView) findViewById(R.id.inner_mananger_company);
        innerManagerIdentity = (TextView) findViewById(R.id.inner_mananger_identity);
        innerManagerPhoneNumber = (TextView) findViewById(R.id.inner_mananger_phone_num);
    }
}
