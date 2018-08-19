package hdo.com.andzq.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.utils.LogUtils;

/**
 * description Activity基类，用来管理Activity 方便一次性退出
 * author 陈锐
 * version 1.0
 * created 2017/5/19
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("BaseActivity",getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
