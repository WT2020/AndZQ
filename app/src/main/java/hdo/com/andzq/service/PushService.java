package hdo.com.andzq.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.PushBean;
import hdo.com.andzq.broadcastReceiver.AlarmReceiver;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.NotificationUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * description 推送服务的service
 * author 陈锐
 * version 1.0
 * created 2017/4/13
 * modify by 张建银 on 2017/8/31 修改报修通知获取方式为多条（新单/再派）
 */

public class PushService extends Service {
    // TODO: 2017/4/17  推送后面改成双进程守护来做 然后注册电量改变 屏幕解锁锁定广播接收者来重启服务 提高稳定性
    private static final String TAG = "PushService:";
    /**
     * alarm 具有唤醒cpu的功能
     * 从Android 4.4 版本开始，Alarm
     * 任务的触发时间将会变得不准确，有可能会延迟一段时间后任务才能得到执行。这并不是个bug，而是系统在耗电性方面进行的优化。系统会自动检测目前有多少Alarm
     * 任务存在，然后将触发时间将近的几个任务放在一起执行，这就可以大幅度地减少CPU 被唤醒的次数，从而有效延长电池的使用时间。当然，如果你要求Alarm
     * 任务的执行时间必须准备无误，Android 仍然提供了解决方案。使用AlarmManager 的setExact()方法来替代set()方法，就可以保证任务准时执行了。
     */
    private AlarmManager alarmM;
    private int checkPushFrequency;

    @Override
    public void onCreate() {
        super.onCreate();

        alarmM = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        checkPushFrequency = getResources().getInteger(R.integer.check_push_frequency);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //打印当前时间
        LogUtils.e(TAG, "executed at " + new Date().toString());
        //检查推送
        checkPush();
        int time = checkPushFrequency * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //定时任务 发送广播
        if (Build.VERSION.SDK_INT >= 19)
            //android 4.4 以上 AlarmManager变得不准确 使用setExact可以设置准确定时任务
            alarmM.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        else alarmM.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 检查是否有新的推送
     */
    public void checkPush() {
        Call call = NetWorkUtils.getInstance().push(PushService.this);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取推送失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                LogUtils.e(TAG, "获取推送成功" + jsonStr);
                Gson gson = new Gson();
                try {
                    JSONArray array = new JSONArray(jsonStr);
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        PushBean bean = gson.fromJson(object.toString(), PushBean.class);
                        if (bean != null && bean.getState() != 0) {
                            NotificationUtils.newNotification(PushService.this, bean.getTitle()
                                    , bean.getType(),1024+i);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.startService(new Intent(this, PushService.class));
    }
}
