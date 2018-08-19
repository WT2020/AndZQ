package hdo.com.andzq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.StaffInfoBean;

/**
 * description gridView的适配器
 * author 邓杰
 * version 1.0
 * Created 2017/3/31.
 */
public class StaffInfoGridViewAdapter extends BaseAdapter {
    /**
     * 数据源
     */
    private ArrayList<StaffInfoBean> list;
    private Context context;

    public StaffInfoGridViewAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public ArrayList<StaffInfoBean> getList() {
        return list;
    }

    public void setList(ArrayList<StaffInfoBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //缓冲类
        StarffInfoViewHolder sivh;
        //如果已经加载过布局了，则复用
        if(convertView==null){
            sivh=new StarffInfoViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_staff_info,null);
            sivh.img= (ImageView) convertView.findViewById(R.id.iv_staff_photo);
            sivh.name= (TextView) convertView.findViewById(R.id.tv_name);
            sivh.position= (TextView) convertView.findViewById(R.id.tv_position);
            //绑定标签,方便复用布局
            convertView.setTag(sivh);
        }else {
            //复用
            sivh= (StarffInfoViewHolder) convertView.getTag();
        }
        StaffInfoBean staffInfoBean=list.get(position);
        sivh.img.setImageResource(staffInfoBean.getHeadImg());
        sivh.name.setText(staffInfoBean.getName());
        sivh.position.setText(staffInfoBean.getPosition());
        return convertView;
    }
    class StarffInfoViewHolder{
        ImageView img;
        TextView name,position;
    }
}
