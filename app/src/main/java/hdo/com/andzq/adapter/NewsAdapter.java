package hdo.com.andzq.adapter;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hdo.com.andzq.R;

import hdo.com.andzq.bean.NewsBodyBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.FilterHtmlUtil;
import hdo.com.andzq.utils.LogUtils;

/**
 * description 新闻RecyclerView适配器
 * author 陈锐
 * version 1.0
 * created 2017/3/31
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_FOOTER = 100;
    private static int TYPE_NORMAL = 200;
    private Context mContext;
    /**
     * 新闻数组
     */
    private ArrayList<NewsBodyBean> mNewsArray;

    private OnRecyclerItemClick mOnRecyclerItemClickListener;
    private final LayoutInflater inflater;

    public NewsAdapter(Context ctx, ArrayList<NewsBodyBean> newsArray, OnRecyclerItemClick
            onRecyclerItemClick) {
        mContext = ctx;
        mNewsArray = newsArray;
        mOnRecyclerItemClickListener = onRecyclerItemClick;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = inflater.inflate(R.layout.item_news, parent, false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_FOOTER)
            return new FooterViewHolder(inflater.inflate(R.layout.list_footer, parent, false));
        else return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mNewsArray.size()) return NewsAdapter.TYPE_FOOTER;
        else return NewsAdapter.TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_NORMAL) ((ViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return mNewsArray.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView newsImage;
        private TextView newsTitle;
        private TextView newsContent;
        private TextView newsTime;

        ViewHolder(View itemView) {
            super(itemView);
            newsImage = (ImageView) itemView.findViewById(R.id.news_image);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsContent = (TextView) itemView.findViewById(R.id.news_content);
            newsTime = (TextView) itemView.findViewById(R.id.news_time);
            itemView.setOnClickListener(this);
        }

        public void setData(int position) {

            Glide.with(mContext).load(Constant.HOME + mNewsArray.get(position).getImage()).centerCrop()
                    .override(200,200).placeholder(R.mipmap.icon_place_holder).fitCenter()
                    .error(R.mipmap.icon_place_holder).into(newsImage);

            newsTitle.setText(mNewsArray.get(position).getTitle());
            String pretext = mNewsArray.get(position).getPretext();
            newsContent.setText(pretext);
            newsTime.setText(mNewsArray.get(position).getPublishDate());
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClickListener != null) {
                int itemPosition = getAdapterPosition();
                mOnRecyclerItemClickListener.onItemClick(v, itemPosition);
            } else {
                LogUtils.e("NewsAdapter", "没有点击监听");
            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {super(itemView);}
    }

    public interface OnRecyclerItemClick {
        void onItemClick(View v, int position);
    }

}
