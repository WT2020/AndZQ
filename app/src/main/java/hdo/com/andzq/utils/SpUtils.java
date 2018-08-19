package hdo.com.andzq.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * description SharedPreferences工具类
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 */

public class SpUtils {

    /**
     * 存储string
     *
     * @param ctx   context对象
     * @param key   需要存储的key
     * @param value 需要存储的key
     */
    public static void putString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 取出string
     *
     * @param ctx      context 对象
     * @param key      需要取出的key
     * @param defValue 如果没有取到默认的值
     * @return 取到的值
     */
    public static String getString(Context ctx, String key, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * 存储int
     *
     * @param ctx   context对象
     * @param key   需要存储的key
     * @param value 需要存储的key
     */
    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 取出int
     *
     * @param ctx      context 对象
     * @param key      需要取出的key
     * @param defValue 如果没有取到默认的值
     * @return 取到的值
     */
    public static int getInt(Context ctx, String key, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    /**
     * 存储boolean
     *
     * @param ctx   context对象
     * @param key   需要存储的key
     * @param value 需要存储的key
     */
    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 取出boolean
     *
     * @param ctx      context 对象
     * @param key      需要取出的key
     * @param defValue 如果没有取到默认的值
     * @return 取到的值
     */
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }


    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public static long getLong(Context ctx, String key, long defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

}
