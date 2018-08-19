package hdo.com.andzq.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hdo.com.andzq.service.PushService;

/**
 * description 保持 推送服务的service 长期后台存在的广播接收者
 * author 陈锐
 * version 1.0
 * created 2017/4/13
 */

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //收到广播后 发送服务
        Intent i = new Intent(context, PushService.class);
        context.startService(i);
    }
}
