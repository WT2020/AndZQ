package hdo.com.andzq.utils;

import android.content.Context;
import android.content.Intent;

import hdo.com.andzq.activity.LoginActivity;
import hdo.com.andzq.global.Constant;

/**
 * description 检查util
 * author 陈锐
 * version 1.0
 * created 2017/4/12
 */

public class ExamineUtils {
    /**
     * 检查是否需要登录 需要登录直接跳到登录页面
     */
    public static void shouldLogin(Context ctx, String s) {
        //如果登录成功 直接返回 不做处理
        if (s.contains("STATE_SUCCESS"))
            return;
        //如果登录失败 直接跳转页面
        Intent intent = new Intent();
        intent.setClass(ctx, LoginActivity.class);
    }

    /**
     * 检查是否需要登录 需要登录直接跳到登录页面
     */
    public static boolean success(Context ctx, String s) {
        //如果登录成功 直接返回 不做处理
        return s.contains("STATE_SUCCESS");
    }

    public static boolean isTokenErr(String s){
        return s.contains(Constant.TOKEN_ERR);
    }

    public static boolean isStateErr(String s){
        return s.contains(Constant.STATE_ERR);
    }
}
