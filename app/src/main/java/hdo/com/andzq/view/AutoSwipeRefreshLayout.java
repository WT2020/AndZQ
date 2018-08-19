package hdo.com.andzq.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * description 下拉刷新控件 提供对外刷新函数
 * author 陈锐
 * version 1.0
 * created 2017/4/14
 */
public class AutoSwipeRefreshLayout extends SwipeRefreshLayout {
    public AutoSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public AutoSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化预设
     */
    private void init() {
        setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        setDistanceToTriggerSync(400);
    }

    /**
     * 调用该方法实现刷新 而无需滑动
     */
    public void autoRefresh() {
        try {
            Field mCircleView = SwipeRefreshLayout.class.getDeclaredField("mCircleView");
            mCircleView.setAccessible(true);
            View progress = (View) mCircleView.get(this);
            progress.setVisibility(View.VISIBLE);//显示刷新控件

            //自动刷新的缩放动画
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(progress, "scaleX", 0f, 1f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(progress, "scaleY", 0f, 1f);
            animatorX.setDuration(500);
            animatorY.setDuration(500);
            animatorX.start();
            animatorY.start();

            Method setRefreshing = SwipeRefreshLayout.class.getDeclaredMethod("setRefreshing",
                    boolean.class, boolean.class);
            setRefreshing.setAccessible(true);
            setRefreshing.invoke(this, true, true);//调用刷新方法
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
