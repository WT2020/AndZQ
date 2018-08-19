package hdo.com.andzq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hdo.com.andzq.R;

/**
 * description 我的界面自定义的view
 * author 邓杰
 * version 1.0
 * Created 2017/4/1.
 */
public class MineView extends LinearLayout {
    //设置标题
    private TextView tv;
    private RelativeLayout rl;
    private onLayoutClick oc;
    public MineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_mine,this);
        tv= (TextView) findViewById(R.id.tv_title_mine);
        rl= (RelativeLayout) findViewById(R.id.rl_mine);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oc!=null){
                    oc.onViewClick(v);
                }
            }
        });
    }

    /**
     * 设置标题
     * @param str 标题内容
     */
    public void setMineTitle(String str){
        if(str!=null){
            tv.setText(str);
        }
    }
    public void setOc(onLayoutClick oc) {
        this.oc = oc;
    }

    public interface onLayoutClick{
        public void onViewClick(View v);
    }
}
