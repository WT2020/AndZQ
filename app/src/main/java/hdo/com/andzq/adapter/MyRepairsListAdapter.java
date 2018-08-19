package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.MyRepairsDetailsBean;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.TimeUtils;

/**
 * description 我的维修单Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/7
 * modified by 张建银 on 2017/10/13 添加再派单状态的显示
 * modified by 张建银 on 2017/10/20 修复维修单列表文字重叠问题
 */

public class MyRepairsListAdapter extends RecyclerView.Adapter<MyRepairsListAdapter.ViewHolder> {

    private OnRecyclerItemClick mOnRecyclerItemClick;
    private Context mContext;
    private List<MyRepairsDetailsBean.MyRepairBean> mList = new ArrayList<>();


    public MyRepairsListAdapter(Context ctx, List<MyRepairsDetailsBean.MyRepairBean> list,
                                OnRecyclerItemClick mOnRecyclerItemClick) {
        if (list != null) mList = list;
        mContext = ctx;
        this.mOnRecyclerItemClick = mOnRecyclerItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_repairs_list, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
//        if(mList==null) return 0;
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvStare;
        private LinearLayout rlTitle;
        private TextView tvUrgency;
        private TextView tvCompany;
        private TextView tvTime;
        private TextView tvType;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvUrgency = (TextView) itemView.findViewById(R.id.tv_urgency);
            tvCompany = (TextView) itemView.findViewById(R.id.tv_company);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            rlTitle = (LinearLayout) itemView.findViewById(R.id.rl_title);
            tvStare = (TextView) itemView.findViewById(R.id.tv_state);

        }

        public void setData(int position) {
            MyRepairsDetailsBean.MyRepairBean bean = mList.get(position);
            int urgent = Integer.parseInt(bean.getUrgent());
            tvCompany.setText(bean.getCompany());//公司名称
            tvTime.setText(TimeUtils.Long2Time(bean.getReport()));//申请时间
            tvType.setText(mContext.getResources().getStringArray(R.array.description_presuppose)[Integer.parseInt
                    (bean.getType())]);   //故障类型
            tvUrgency.setText(mContext.getResources().getStringArray(R.array.urgency_level)
                    [urgent]);
            String state = mContext.getResources().getStringArray(R.array.serviceman_repairs_state)[bean.getRstate()];
            String isSend = bean.getIsSend();
            if (isSend!=null&&isSend.equals("1")){
                tvStare.setText(state + "(再派单)");
            }else{
                tvStare.setText(state);
            }
            switch (bean.getRstate()){
                case 0://未接单
                    rlTitle.setBackgroundColor(mContext.getResources().getColor(R.color.red300));
                    break;
                case 1://接单
                case 2://到达现场
                case 3://维修中
                    rlTitle.setBackgroundColor(mContext.getResources().getColor(R.color.blue400));
                    break;
                case 4://完成
                case 5:
                    rlTitle.setBackgroundColor(mContext.getResources().getColor(R.color.blue200));
                    break;
            }

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
