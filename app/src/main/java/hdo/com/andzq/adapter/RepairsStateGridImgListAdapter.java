package hdo.com.andzq.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.R;
import hdo.com.andzq.global.Constant;

/**
 * @author 邓杰
 * @version 1.0
 * @description 图片显示的适配器
 * @created on 2017/4/19.
 */

public class RepairsStateGridImgListAdapter extends BaseAdapter {
    private Context context;
    private List<String> urlList;

    public RepairsStateGridImgListAdapter(Context context) {
        this.context = context;
        urlList=new ArrayList<>();
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public int getCount() {
        return urlList.size();
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
        ImageView img=new ImageView(context);
        String url=getUrlList().get(position);
        Picasso.with(context.getApplicationContext()).load(url).placeholder(R.mipmap.load).error(R.mipmap.error).resize(128,128).centerCrop().into(img);
        //Glide.with(context).load(url).placeholder(R.mipmap.load).error(R.mipmap.error).override(128,128).into(img);
        return img;
    }
}
