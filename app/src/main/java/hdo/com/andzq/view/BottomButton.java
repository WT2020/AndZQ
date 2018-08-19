package hdo.com.andzq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * description 底部的自定义button
 * author 陈锐
 * version 1.0
 * created 2017/3/29
 */

public class BottomButton extends View {

    private Paint mPaint;

    public BottomButton(Context context) {
        super(context);
        init();
    }

    public BottomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        String namespace = "http://schemas.android.com/apk/res-auto";
        //默认的开关状态
        int pressed = attrs.getAttributeResourceValue(namespace, "background_color_pressed", -1);
        //滑动背景
        int unpressed = attrs.getAttributeResourceValue(namespace, "background_color_unpressed", -1);
        //开关背景
        int text_color = attrs.getAttributeResourceValue(namespace, "text_color", -1);
        int text_size = attrs.getAttributeResourceValue(namespace, "text_size", -1);

        int text = attrs.getAttributeResourceValue(namespace, "switch_background", -1);

        init();

    }

    public BottomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
