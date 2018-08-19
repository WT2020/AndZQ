package hdo.com.andzq.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import hdo.com.andzq.R;
import hdo.com.andzq.activity.MainActivity;
import hdo.com.andzq.activity.MyRepairsList;
import hdo.com.andzq.activity.MyRepairsListActivity;

/**
 * description 通知栏工具类
 * author 陈锐
 * version 1.0
 * created 2017/4/13
 */

public class NotificationUtils {

    public static void newNotification(Context c, String title, String content,int code) {
        NotificationManager NFM = (NotificationManager) c.getSystemService(Context
                .NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c);
        Intent intent = new Intent(c, MyRepairsListActivity.class);
        PendingIntent activity = PendingIntent.getActivity(c, 0, intent, 0);
        builder
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(Notification.PRIORITY_MAX) //最大权重
                .setTicker("通知到来") //通知首次出现在通知栏，带上升动画效果的
                .setAutoCancel(true) //自动取消
                .setContentIntent(activity)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //在锁屏通知栏可见通知详情
                .setDefaults(Notification.DEFAULT_VIBRATE);
        //等价于setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS |
        // Notification.DEFAULT_VIBRATE);
        SpUtils.putInt(c, c.getString(R.string.preference_repairs_notification), 1024);
        NFM.notify(code, builder.build());
    }
}
