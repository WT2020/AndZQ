package hdo.com.andzq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.NewProgressBean;
import hdo.com.andzq.bean.ProgressBean;
import hdo.com.andzq.utils.TimeUtils;

/**
 * description 进度Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/6
 */

public class ProgressAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<NewProgressBean.BodyBean.ProgressBean> progressList;

    public ProgressAdapter(Context ctx, ArrayList<NewProgressBean.BodyBean.ProgressBean> progressList) {
        mContext = ctx;
        this.progressList = progressList;
    }

    @Override
    public int getCount() {
        return progressList.size();
    }

    @Override
    public Object getItem(int position) {
        return progressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProgressAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_progress, parent,false);
        }
        holder = ViewHolder.getHolder(convertView);
        NewProgressBean.BodyBean.ProgressBean bean = progressList.get(position);
        holder.detail.setText(bean.getDetail());
        holder.state.setText(bean.getTitle());
        holder.time.setText(TimeUtils.Long2Time(Long.parseLong(bean.getTime())));
        return convertView;
    }


    static class ViewHolder {
        TextView state, detail, time;
        public ViewHolder(View v) {
            state = (TextView) v.findViewById(R.id.tv_progress_state);
            detail = (TextView) v.findViewById(R.id.tv_progress_detail);
            time = (TextView) v.findViewById(R.id.tv_progress_time);
        }
        static ViewHolder getHolder(View v) {
            ViewHolder holder = (ViewHolder) v.getTag();
            if (holder == null) {
                holder = new ViewHolder(v);
                v.setTag(holder);
            }
            return holder;
        }
    }
}
