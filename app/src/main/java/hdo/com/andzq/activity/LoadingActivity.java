package hdo.com.andzq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.utils.SpUtils;

/**
 * description z载入页面
 * author 邓杰
 * version 1.0
 * Created 2017/3/31.
 */
public class LoadingActivity extends BaseActivity {

    Handler handler = new Handler();
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Thread(() -> {
            if (!TextUtils.isEmpty(SpUtils.getString(this,this.getString(R.string.preference_token),""))) {
                intent = new Intent(this, MainActivity.class);//如果有token 直接进入主界面
            }else intent = new Intent(this, LoginActivity.class);//没有token 进入登录界面
            handler.postDelayed(()->{
                startActivity(intent);
                finish();
            },1000);
        }).start();
    }
}
