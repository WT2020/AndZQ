package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.NewProgressBean;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.TimeUtils;

/**
 * description 新建查询Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/5
 * modified by 张建银 on 2017/10/22 实现专线名称的显示
 */

public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.ViewHolder> {

    private OnRecyclerItemClick mOnRecyclerItemClick;
    private Context mContext;
    private List<NewProgressBean.BodyBean> list = new ArrayList<>();


    public QueryListAdapter(Context ctx, List<NewProgressBean.BodyBean> list, OnRecyclerItemClick
            mOnRecyclerItemClick) {
        mContext = ctx;
        this.mOnRecyclerItemClick = mOnRecyclerItemClick;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_progress_list, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTime;
        private TextView tvType;
        private TextView tvState;
        private TextView tvNum;
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            initView(itemView);
        }

        public void setData(int position) {
            tvTime.setText(TimeUtils.Long2Time(Long.parseLong(list.get(position).getTime())));
            tvState.setText(list.get(position).getState());
            tvType.setText(list.get(position).getLineType());
            tvNum.setText(list.get(position).getLineName());
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClick != null) {
                mOnRecyclerItemClick.onItemClick(v, getAdapterPosition());
            } else {
                LogUtils.e("NewsAdapter", "没有点击监听");
            }
        }
        private void initView(View convertView) {
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvType = (TextView) convertView.findViewById(R.id.tv_type);
            tvState = (TextView) convertView.findViewById(R.id.tv_state);
            tvNum = (TextView)convertView.findViewById(R.id.tv_line_num);
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View v, int position);
    }
}
