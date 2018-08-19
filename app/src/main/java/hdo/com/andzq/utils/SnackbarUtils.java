package hdo.com.andzq.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * description snackbarUtils
 * author 陈锐
 * version 1.0
 * created 2017/4/26
 */

public class SnackbarUtils {

    /**
     * 网络故障
     */
    public static void networkErr(final Activity ctx, View view) {
        ctx.runOnUiThread(() -> Snackbar.make(view, "网络连接失败", Snackbar.LENGTH_SHORT).setAction("知道了", v -> {}).show());
    }

    /**
     * 上传失败
     */
    public static void uploadErr(final Activity ctx, View view) {
        ctx.runOnUiThread(() -> Snackbar.make(view, "上传失败，请重试", Snackbar.LENGTH_SHORT).setAction("知道了", v -> {}).show());
    }

    /**
     * 上传成功
     */
    public static void uploadSucc(final Activity ctx, View view) {
        ctx.runOnUiThread(() -> Snackbar.make(view, "上传成功", Snackbar.LENGTH_SHORT).setAction("知道了", v -> {}).show());
    }

}
