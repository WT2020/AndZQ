package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hdo.com.andzq.R;

/**
 * Created by admin on 2017/4/5.
 */
public class PermissionManageListAdapter extends RecyclerView.Adapter<PermissionManageListAdapter.PermissionManageViewHolder>{
    private Context context;

    public PermissionManageListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PermissionManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_permission_manage,parent,false);
        return new PermissionManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PermissionManageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class PermissionManageViewHolder extends RecyclerView.ViewHolder{
        private TextView name,companyName,permission;

        public PermissionManageViewHolder(View view) {
            super(view);
            name= (TextView) view.findViewById(R.id.tv_name);
            companyName= (TextView) view.findViewById(R.id.tv_company_name);
            permission= (TextView) view.findViewById(R.id.tv_permission);
        }
    }
}
