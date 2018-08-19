package hdo.com.andzq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import hdo.com.andzq.R;
import hdo.com.andzq.utils.DensityUtils;
import hdo.com.andzq.utils.ToastUtils;

/**
 * description 自定义spinner适配器 用来改变spinner文字大小
 * author 陈锐
 * version 1.0
 * created 2017/3/31
 * modified by 张建银 on 2017/10/12 实现Spinner选项选择后的文字多行显示
 */

public class MySpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;

    /**
     * spinner绑定的数组
     */
    private String[] mStringArray;

    /**
     *
     * @param context Context对象
     * @param stringArray spinner绑定的数组
     */
    public MySpinnerAdapter(Context context, String[] stringArray) {
        super(context, android.R.layout.simple_spinner_dropdown_item, stringArray);
        mContext = context;
        mStringArray = stringArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        //此处是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(14);

        //tv.setTextColor(Color.RED);

        return convertView;

    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.muiltline_spinner_item, parent, false);
        }

        //此处是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.text1);
        tv.setText(mStringArray[position]);
        tv.setTextSize(14);
        //tv.setTextColor(Color.BLUE);
        return convertView;
    }

}