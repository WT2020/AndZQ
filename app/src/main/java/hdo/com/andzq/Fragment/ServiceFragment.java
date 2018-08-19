package hdo.com.andzq.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.adapter.ServiceViewPagerAdapter;
import hdo.com.andzq.base.BaseFragment;

/**
 * description 服务Fragment
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 * modify 修改成显示首页的Fragment by 邓杰 on 2017/4/5
 */

public class ServiceFragment extends BaseFragment {
    /**
     * 滑动的ViewPager
     */
    private ViewPager vpService;
    /**
     * 数据源
     */
    private ArrayList<Fragment> list;
    /**
     * 滑动的适配器
     */
    private ServiceViewPagerAdapter adapter;
    /**
     * 碎片管理
     */
    private FragmentManager fm;

    @Override
    public View initView(ViewGroup container) {
        View view= LayoutInflater.from(mActivity).inflate(R.layout.fragment_service,container,false);
        vpService = (ViewPager) view.findViewById(R.id.vp_service);
        fm=getChildFragmentManager();
        return view;
    }

    @Override
    public void initData() {
        list=new ArrayList<>();

        adapter=new ServiceViewPagerAdapter(fm);
        //设置view
        setView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * 设置View
     */
    private void setView() {
        adapter.setList(list);
        vpService.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //加载数据
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        list.clear();
        list.add(new HomeFragment());
        list.add(new StaffInfoFragment());
        adapter.notifyDataSetChanged();
    }

}
