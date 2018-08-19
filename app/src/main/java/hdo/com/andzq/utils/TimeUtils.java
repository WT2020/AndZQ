package hdo.com.andzq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.format;

/**
 * description 获取当前时间工具类
 * author 陈锐
 * version 1.0
 * created 2017/4/7
 */

public class TimeUtils {

    /**
     * 绝对时间转时间
     */
    public static String Long2Time(Long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(new Date(l));
    }

    /**
     * 获取当前时间
     *
     * @return 返回时间的str
     */
    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 比较两者时间
     */
    public static int compareHour(long now,long formerly) {
        long l = now - formerly;
        long l1 = l / (1000 * 60 * 60);
        return (int)l1;
    }


    /**
     * 获得当前 年
     *
     * @return 当前年
     */
    public static String getCurrentYear() {
        String currentTime = getCurrentTime();
        String[] split = currentTime.split("/");
        return split[0];
    }

    /**
     * 获得当前 月
     *
     * @return 当前月
     */
    public static String getCurrentMonth() {
        String currentTime = getCurrentTime();
        String[] split = currentTime.split("/");

        return split[1];
    }

    /**
     * 获得当前 日
     *
     * @return 当前日
     */
    public static String getCurrentD() {
        String currentTime = getCurrentTime();
        int start = currentTime.indexOf("/", 7);
        int end = currentTime.indexOf(" ");
        return currentTime.substring(start + 1, end);
    }

    /**
     * 获得当前 小时
     *
     * @return 当前小时
     */
    public static String getCurrentH() {
        String currentTime = getCurrentTime();
        int start = currentTime.indexOf(" ");
        int end = currentTime.indexOf(":");

        return currentTime.substring(start + 1, end);
    }

    /**
     * 获得当前 分钟
     *
     * @return 当前分钟
     */
    public static String getCurrentM() {
        String currentTime = getCurrentTime();
        int start = currentTime.indexOf(":");

        return currentTime.substring(start + 1);
    }
}
