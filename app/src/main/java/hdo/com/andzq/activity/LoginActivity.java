package hdo.com.andzq.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.IOException;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.bean.LoginInfoBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.utils.okhttp.UICallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 登录Activity
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 * modify 修改了网络请求 by 邓杰 on 2017/3/30
 * modify 修复登录闪退问题 by 张建银 on 2017/9/1
 * modify 修复登录网络错误无提示问题 by 张建银 on 2017/9/1
 */
public class LoginActivity extends BaseActivity {

    //登录时展示的图片
    private ImageView mIvPic;

    //登录帐号输入框
    private EditText mEtId;

    //登录密码输入框
    private EditText mEtPassword;

    //登录按钮
    private Button mBtnLogin;

    //通过输入框拿到用户登录的帐号
    private String mId;

    //通过输入框拿到用户登录的密码
    private String mPassword;

    private String token;

    private Context mContext;
    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        initView();
        initData();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mIvPic = (ImageView) findViewById(R.id.iv_pic);
        mEtId = (EditText) findViewById(R.id.et_id);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mBtnLogin.setOnClickListener(this::login);//设置登录点击
        //回显帐号
        mEtId.setText(SpUtils.getString(mContext,mContext.getString(R.string.preference_code),""));
        mEtId.setSelection(mEtId.getText().toString().length());//设置光标选择位置


    }


    /**
     * 登录逻辑的判断 如果帐号密码不为空 发送异步http请求
     */
    private void login(View v) {
        //帐号
        mId = mEtId.getText().toString().trim();
        //密码
        mPassword = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mId)) {
            ToastUtils.makeText(LoginActivity.this, "请输入帐号");
        } else if (TextUtils.isEmpty(mPassword)) {
            ToastUtils.makeText(LoginActivity.this, "请输入密码");
        } else {
            SpUtils.putString(mContext, mContext.getString(R.string.preference_code), mId);//本地保存帐号密码
            SpUtils.putString(mContext, mContext.getString(R.string.preference_password), mPassword);//
            postAsynHttp();
        }
    }


    /**
     * post异步http请求
     * modify 修改了网络请求 by 邓杰 on 2017/3/30
     */
    private void postAsynHttp() {
        progress = DialogUtils.progress(this, "登录中...");
        progress.setCancelable(false);
        progress.show();
        String code = mId;
        String psw = mPassword;
        Call call = NetWorkUtils.getInstance().login(code, psw);
        call.enqueue(new UICallBack() {
            @Override
            public void UIonFailure(Call call, IOException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void UIonResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                if(jsonStr.contains("Error")){
                    Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                }else{
                    disposeLogin(jsonStr);
                }
                progress.dismiss();
            }
        });

    }

    /**
     * 网络访问成功 处理登录
     *
     * @param jsonStr json字符串
     */
    private void disposeLogin(String jsonStr) {
        Gson gson = new Gson();
        LoginInfoBean bean = gson.fromJson(jsonStr, LoginInfoBean.class);
        String status = bean.getRESP_STATE();
        switch (status) {
            case Constant.STATE_SUCCESS:
                //登陆成功 将token保存在本地
                SpUtils.putString(mContext, mContext.getString(R.string.preference_token), bean.getToken());
                //角色码
                SpUtils.putString(mContext, mContext.getString(R.string.preference_role_id), bean.getRole_id());
                //用户名
                SpUtils.putString(mContext, mContext.getString(R.string.preference_code), bean.getCode());
                loginSuccess();
                break;
            case Constant.LOGIN_ID_ERROR:
                runOnUiThread(() -> ToastUtils.makeText(LoginActivity.this, "帐号不存在"));
                break;
            case Constant.STATE_PA_ERR:
                runOnUiThread(() -> ToastUtils.makeText(LoginActivity.this, "密码错误"));
                break;
            default:
                runOnUiThread(() -> ToastUtils.makeText(LoginActivity.this, "服务器异常"));
                break;
        }
    }

    /**
     * 登录成功 存储帐号密码 个人信息 跳转到主页面
     */
    private void loginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
