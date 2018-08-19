package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.ComplainResultBean;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.TimeUtils;

/**
 * description 我的投诉Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/13
 */

public class ComplainListAdapter extends RecyclerView.Adapter<ComplainListAdapter.ViewHolder> {

    private OnRecyclerItemClick mOnRecyclerItemClick;
    private Context mContext;
    private ArrayList<ComplainResultBean.MyComplainBean> list;

    public ComplainListAdapter(Context ctx, ArrayList<ComplainResultBean.MyComplainBean> list,
                               OnRecyclerItemClick mOnRecyclerItemClick) {
        mContext = ctx;
        this.mOnRecyclerItemClick = mOnRecyclerItemClick;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_complain_list, parent, false);
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
        private TextView tvTarget;
        private TextView tvState;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            initView(itemView);
        }

        public void setData(int position) {
            tvTime.setText(TimeUtils.Long2Time(list.get(position).getPTime()));
            tvTarget.setText(list.get(position).getPObj());
            tvState.setText(mContext.getResources().getStringArray(R.array.deal_state)[Integer
                    .parseInt(list.get(position).getPState())]);
        }

        private void initView(View convertView) {
            tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            tvTarget = (TextView) convertView.findViewById(R.id.tv_target);
            tvState = (TextView) convertView.findViewById(R.id.tv_state);
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClick != null) {
                mOnRecyclerItemClick.onItemClick(v, getAdapterPosition());
            } else {
                LogUtils.e("NewsAdapter", "没有点击监听");
            }
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View v, int position);
    }
}
