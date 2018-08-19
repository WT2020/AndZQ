package hdo.com.andzq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.RepairsStateBean;

/**
 * @author 邓杰
 * @version 1.0
 * @description 维修状态列表的适配器
 * @created on 2017/4/19.
 */

public class RepairsStateListAdapter extends BaseAdapter {
    private Context context;
    /**
     * 数据源
     */
    private List<RepairsStateBean> list;
    private RepairsStateGridImgListAdapter adapter;
    private onGridViewClick oc;

    public onGridViewClick getOc() {
        return oc;
    }

    public void setOc(onGridViewClick oc) {
        this.oc = oc;
    }

    public List<RepairsStateBean> getList() {
        return list;
    }

    public void setList(List<RepairsStateBean> list) {
        this.list = list;
    }

    public RepairsStateListAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
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
        RepairsStateViewHolder holder;
        if (convertView == null) {
            holder = new RepairsStateViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_repairs_state, null);
            holder.state = (TextView) convertView.findViewById(R.id.tv_repairs_state);
            holder.time = (TextView) convertView.findViewById(R.id.tv_repairs_time);
            holder.detail = (TextView) convertView.findViewById(R.id.tv_repairs_detail);
            holder.gvImg = (GridView) convertView.findViewById(R.id.gv_repairs_img);
            convertView.setTag(holder);
        } else {
            holder = (RepairsStateViewHolder) convertView.getTag();
        }
        RepairsStateBean bean = list.get(position);
        holder.state.setText(context.getResources().getStringArray(R.array.repairs_state)[Integer.parseInt(bean
                .getState())]);
        holder.time.setText(bean.getTime());
        holder.detail.setText(bean.getDetail());
        //设置图片
        adapter = new RepairsStateGridImgListAdapter(context);
        if (bean.getImgUrl() == null || bean.getImgUrl().size() == 0) {
            holder.gvImg.setVisibility(View.GONE);
        } else {
            adapter.setUrlList(bean.getImgUrl());
            holder.gvImg.setAdapter(adapter);
        }
        int ItemPosition = position;
        holder.gvImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (oc != null) {
                    oc.OnItemClick(position, ItemPosition);
                }
            }
        });
        return convertView;
    }


    class RepairsStateViewHolder {
        TextView state, time, detail;
        GridView gvImg;
    }

    public interface onGridViewClick {
        void OnItemClick(int position, int ItemPosition);
    }
}
