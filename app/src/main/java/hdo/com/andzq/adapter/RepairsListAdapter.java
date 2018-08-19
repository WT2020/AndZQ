package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.MyRepairsListBean;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.ToastUtils;

/**
 * description 新建查询Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/5
 * modified by 邓杰 增加显示数据 2017/4/21
 */

public class RepairsListAdapter extends RecyclerView.Adapter<RepairsListAdapter.ViewHolder> {

    private OnRecyclerItemClick mOnRecyclerItemClick;
    private Context mContext;
    /**
     * 数据源
     */
    private List<MyRepairsListBean.MyRepairBean> list;

    public RepairsListAdapter(Context ctx, OnRecyclerItemClick mOnRecyclerItemClick) {
        mContext = ctx;
        this.mOnRecyclerItemClick = mOnRecyclerItemClick;
        list = new ArrayList<>();
    }
    public List<MyRepairsListBean.MyRepairBean> getList() {
        return list;
    }

    public void setList(List<MyRepairsListBean.MyRepairBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_repairs_list, parent, false);
        return new RepairsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
        holder.itemView.setOnClickListener(v -> {
            if (mOnRecyclerItemClick != null) {
                mOnRecyclerItemClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvType, tvState;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_repairs_time);
            tvType = (TextView) itemView.findViewById(R.id.tv_repairs_type);
            tvState = (TextView) itemView.findViewById(R.id.tv_deal_state);
        }

        public void setData(int position) {
            if (list != null && list.size() != 0) {
                MyRepairsListBean.MyRepairBean bean = list.get(position);
                //设置的申请时间
                tvTime.setText(TimeUtils.Long2Time(bean.getApplyTime()));
                tvType.setText(mContext.getResources().getStringArray(R.array.description_presuppose)[Integer
                        .parseInt(bean.getType())]);
                tvState.setText(getState(bean.getRstate()));//维修状态
            } else {
                Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getState(int state) {
        return mContext.getResources().getStringArray(R.array.repairs_state)[state];
    }

    public interface OnRecyclerItemClick {
        void onItemClick(int position);
    }
}
