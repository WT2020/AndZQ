package hdo.com.andzq.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * description 不允许滑动的viewpager
 * author 陈锐
 * version 1.0
 * created 2017/3/29
 */

public class NoScroolViewPager extends ViewPager {
    public NoScroolViewPager(Context context) {
        super(context);
    }

    public NoScroolViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //ViewPager不拦截子控件的事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //触摸时什么都不做
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
