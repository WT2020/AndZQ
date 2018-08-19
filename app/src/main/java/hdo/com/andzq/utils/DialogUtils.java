package hdo.com.andzq.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import hdo.com.andzq.activity.ComplainActivity;

/**
 * description 提示框工具类
 * author 陈锐
 * version 1.0
 * created 2017/4/14
 */

public class DialogUtils {

    public static AlertDialog.Builder submit(Context ctx){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        return builder.setTitle("提示")
                .setMessage("确认要提交吗？")
                .setNegativeButton("取消", (dialog, which) -> {
                });
    }

    public static ProgressDialog progress(Context ctx,String title){
        ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setTitle(title);
        dialog.setMessage("请稍候...");
        return dialog;
    }

}
