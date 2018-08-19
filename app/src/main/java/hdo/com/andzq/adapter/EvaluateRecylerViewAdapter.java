package hdo.com.andzq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.R;
import hdo.com.andzq.bean.EvaluateBean;

/**
 * @author 邓杰
 * @version 1.0
 * @description 选择评价的适配器
 * @created on 2017/4/18.
 * modified on 2017/8/31 by 张建银 修改评价选择后改变背景而不是显示小勾
 */

public class EvaluateRecylerViewAdapter extends RecyclerView.Adapter<EvaluateRecylerViewAdapter.EvaluateViewHolder>{
    private Context context;
    private List<EvaluateBean> list;

    public EvaluateRecylerViewAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    @Override
    public EvaluateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_evaluate_rv,null);
        return new EvaluateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EvaluateViewHolder holder, int position) {
        holder.tv.setText(list.get(position).getComment());
        holder.rl.setBackgroundColor(list.get(position).isCheck()?context.getResources().getColor(R.color.blue):Color.WHITE);
//        holder.img.setVisibility(list.get(position).isCheck()?View.VISIBLE:View.INVISIBLE);
        holder.itemView.setOnClickListener(v -> {
            EvaluateBean bean = list.get(position);
            bean.setCheck(!bean.isCheck());
            holder.rl.setBackgroundColor(bean.isCheck()?context.getResources().getColor(R.color.blue):Color.WHITE);
//            holder.img.setVisibility(bean.isCheck()?View.VISIBLE:View.INVISIBLE);
        });
    }
    private void setClear(){

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<EvaluateBean> getList() {
        return list;
    }

    public void setList(List<EvaluateBean> list) {
        this.list = list;
    }

    class EvaluateViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView img;
        RelativeLayout rl;
        public EvaluateViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.tv_item_evaluate);
            img= (ImageView) itemView.findViewById(R.id.img_check);
            rl= (RelativeLayout) itemView.findViewById(R.id.rl_evaluate);
        }
    }
}
