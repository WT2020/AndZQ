package hdo.com.andzq.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import hdo.com.andzq.global.Constant;

/**
 * description Toast工具类
 * author 陈锐
 * version 1.0
 * created 2017/3/29
 */

public class ToastUtils {

    private static Toast toast;

    /**
     * 发出Toast 自动判断线程
     *
     * @param ctx  context对象
     * @param text 文本
     */
    public static void makeText(final Activity ctx, final String text) {

        if ("main".equals(Thread.currentThread().getName())) {

            show(ctx, text);
        } else {
            ctx.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    show(ctx, text);
                }
            });
        }
    }
    /**
     * 显示
     */
    private static void show(Context ctx, String text) {
        if (toast == null) {
            toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
