package hdo.com.andzq.base;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * description Fragment抽象类
 * author 陈锐
 * version 1.0
 * created 2017/3/24
 * modify 添加了跳转页面的方法 by 邓杰 on 2017/3/30
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;

    //activity创建
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    //初始化fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(container);
    }

    //fragment所依赖的Activity创建onCreated方法执行完成
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * Fragment初始化布局 由子类实现
     * @return 初始化的布局
     */
    public abstract View initView(ViewGroup container);

    /**
     * Fragment初始化数据 由子类实现
     */
    public abstract void initData();

    /**
     * 跳转页面
     */
    public void toActivity(Context context, Class c){
        Intent intent=new Intent(context,c);
        startActivity(intent);
    }

}
