package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.ProductBean;
import hdo.com.andzq.utils.LogUtils;

/**
 * description 我的产品list Adapter
 * author 陈锐
 * version 1.0
 * created 2017/4/11
 * modified by 张建银 on 2017/10/22 实现专线名称的显示
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final ArrayList<ProductBean.BodyBean> list;
    private OnRecyclerItemClick mOnRecyclerItemClick;
    private Context mContext;


    public ProductListAdapter(Context ctx, ArrayList<ProductBean.BodyBean> list, OnRecyclerItemClick mOnRecyclerItemClick) {
        mContext = ctx;
        this.mOnRecyclerItemClick = mOnRecyclerItemClick;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_list, parent, false);
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
        private TextView tvLine;
        private TextView tvLineName;
        private TextView tvLevel;
        private TextView tvType;
        private TextView tvBandwidth;

        ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {
            tvLine.setText(list.get(position).getLine());
            tvLineName.setText(list.get(position).getLineName());
            tvLevel.setText(list.get(position).getLevel());
            tvType.setText(list.get(position).getType());
            tvBandwidth.setText(list.get(position).getBandwidth().trim());
        }

        private void initView(View convertView) {
            tvLine = (TextView) convertView.findViewById(R.id.tv_line);
            tvLineName = (TextView)convertView.findViewById(R.id.tv_line_name);
            tvLevel = (TextView) convertView.findViewById(R.id.tv_level);
            tvType = (TextView) convertView.findViewById(R.id.tv_type);
            tvBandwidth = (TextView) convertView.findViewById(R.id.tv_bandwidth);
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClick != null) {
                mOnRecyclerItemClick.onItemClick(v, getAdapterPosition());
            } else {
                LogUtils.e(ProductListAdapter.class.getSimpleName(), "没有点击监听");
            }
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View v, int position);
    }
}
