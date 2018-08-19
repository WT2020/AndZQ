package hdo.com.andzq.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * description 日志打印工具类
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 */

public class LogUtils {
    /**
     * 是否打印log 当应用发布时 需手动到这里来将PRINT_LOG设置为false
     */
    public final static boolean PRINT_LOG = true;

    /**
     * DEBUG
     *
     * @param tag  标志
     * @param text log
     */
    public static void d(String tag, String text) {
        if (PRINT_LOG) Log.d(tag, text);
    }

    /**
     * WARN
     *
     * @param tag  标志
     * @param text log
     */
    public static void w(String tag, String text) {
        if (PRINT_LOG) Log.w(tag, text);
    }

    /**
     * VERBOSE
     *
     * @param tag  标志
     * @param text log
     */
    public static void v(String tag, String text) {
        if (PRINT_LOG) Log.v(tag, text);
    }

    /**
     * INFO
     *
     * @param tag  标志
     * @param text log
     */
    public static void i(String tag, String text) {
        if (PRINT_LOG) Log.i(tag, text);
    }

    /**
     * ERROR
     *
     * @param tag  标志
     * @param text log
     */
    public static void e(String tag, String text) {
        if (PRINT_LOG) Log.e(tag, text);
    }
}
