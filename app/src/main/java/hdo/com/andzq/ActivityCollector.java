package hdo.com.andzq;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * description Activity管理器
 * author 陈锐
 * version 1.0
 * created 2017/5/19
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity:activities){
            if (!activity.isFinishing()) activity.finish();
        }
    }
}
