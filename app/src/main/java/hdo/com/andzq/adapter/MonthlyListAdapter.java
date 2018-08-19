package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.MonthlyBean;
import hdo.com.andzq.utils.LogUtils;

/**
 * description 我的产品list Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/11
 */

public class MonthlyListAdapter extends RecyclerView.Adapter<MonthlyListAdapter.ViewHolder> {

    private OnRecyclerItemClick mOnRecyclerItemClick;
    private Context mContext;
    private ArrayList<MonthlyBean.BodyBean> list = new ArrayList<>();


    public MonthlyListAdapter(Context ctx, ArrayList<MonthlyBean.BodyBean> list, OnRecyclerItemClick
            mOnRecyclerItemClick) {
        mContext = ctx;
        this.mOnRecyclerItemClick = mOnRecyclerItemClick;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_monthly_list, parent,
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
        private TextView tvSeason;
        private TextView tvCompany;
        private TextView tvCompantCode;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            initView(itemView);
        }

        private void initView(View convertView) {
            tvSeason = (TextView)convertView.findViewById(R.id.tv_season);
            tvCompany = (TextView)convertView.findViewById(R.id.tv_company);
            tvCompantCode = (TextView)convertView.findViewById(R.id.tv_company_code);
        }

        public void setData(int position) {
            tvSeason.setText(list.get(position).getSeason());//截取只看 年月
            tvCompany.setText(list.get(position).getCompany());
            tvCompantCode.setText(list.get(position).getCompanyCode());
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClick != null) {
                int position = getAdapterPosition();
                mOnRecyclerItemClick.onItemClick(v, getAdapterPosition());
            } else {
                LogUtils.e(MonthlyListAdapter.class.getSimpleName(), "没有点击监听");
            }
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View v, int position);
    }
}
