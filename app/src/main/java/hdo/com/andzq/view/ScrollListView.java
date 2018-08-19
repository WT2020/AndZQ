package hdo.com.andzq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author 邓杰
 * @version 1.0
 * @description 嵌套滑动srcollview
 * @created on 2017/4/22.
 */

public class ScrollListView extends ListView {
    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }
}
