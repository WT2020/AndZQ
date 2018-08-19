package hdo.com.andzq.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.activity.MainActivity;
import hdo.com.andzq.adapter.StaffInfoGridViewAdapter;
import hdo.com.andzq.base.BaseFragment;
import hdo.com.andzq.bean.StaffInfoBean;
import hdo.com.andzq.utils.ToastUtils;

/**
 * description 首页ViewPager的我的经理页面
 * author 邓杰
 * version 1.0
 * Created by admin on 2017/4/5.
 */
public class StaffInfoFragment extends BaseFragment {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    /**
     * 客服的集合
     */
    private ArrayList<StaffInfoBean> list;
    /**
     * gridView的适配器
     */
    private StaffInfoGridViewAdapter adapter;
    /**
     * gridView列表
     */
    private GridView gv;

    @Override
    public View initView(ViewGroup container) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_staff_info,
                container, false);
        gv = (GridView) view.findViewById(R.id.gv_staff);
        return view;
    }

    @Override
    public void initData() {
        adapter = new StaffInfoGridViewAdapter(mActivity);
        list = new ArrayList<>();
        //设置
        setView();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeCall("18980736472");
            }
        });
    }

    /**Num
     * 拨打电话的逻辑
     */
    private void makeCall(String phoneNum) {

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            getContext().startActivity(intent);
        }
    }
    /**
     * 授权结果的回调
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    //授权成功 拨打电话
                    makeCall("18980736472");
                } else {
                    ToastUtils.makeText(mActivity, "授权失败");
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 设置view
     */
    private void setView() {
        //设置数据
        adapter.setList(list);
        //设置适配器
        gv.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        //加载数据
        loadData();
    }

    /**
     * 加载gridView的数据
     */
    private void loadData() {
        list.clear();
        list.add(new StaffInfoBean(R.mipmap.app_icon, "何武吉", "首席客服经理"));
        list.add(new StaffInfoBean(R.mipmap.app_icon, "邓桀", "技术总监"));
        list.add(new StaffInfoBean(R.mipmap.app_icon, "邓杰", "维护工程师"));
        //刷新
        adapter.notifyDataSetChanged();
    }
}
