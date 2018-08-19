package hdo.com.andzq.Fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hdo.com.andzq.base.BaseFragment;

/**
 * description 搜索Fragment
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 */

public class SearchFragment extends BaseFragment {
    @Override
    public View initView(ViewGroup container) {
        TextView view = new TextView(mActivity);
        view.setText("搜索");
        view.setTextColor(Color.RED);
        view.setTextSize(40);
        view.setGravity(Gravity.CENTER);

        return view;
    }

    @Override
    public void initData() {

    }
}
