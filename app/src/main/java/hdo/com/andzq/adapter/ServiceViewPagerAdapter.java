package hdo.com.andzq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * description 首页ViewPager的适配器
 * author 邓杰
 * version 1.0
 * Created by admin on 2017/4/5.
 */
public class ServiceViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> list;
    public ServiceViewPagerAdapter(FragmentManager fm) {
        super(fm);
        list=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public ArrayList<Fragment> getList() {
        return list;
    }

    public void setList(ArrayList<Fragment> list) {
        this.list = list;
    }
}
