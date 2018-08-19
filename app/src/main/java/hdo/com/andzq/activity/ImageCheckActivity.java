package hdo.com.andzq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.view.ScaleView;

/**
 * @author 邓杰
 * @version 1.0
 * @description 查看图片
 * @created on 2017/4/20.
 */

public class ImageCheckActivity extends BaseActivity {
    /**
     * 返回按钮
     */
    private RelativeLayout rlBack;
    /**
     * viewPager展示图片
     */
    private ViewPager vpImgCheck;
    /**
     * 数据源
     */
    private List<String> list;
    /**
     * 适配器
     */
    private ImageVpAdapter adapter;
    /**
     * 图片位置
     */
    private int position;
    /**
     * 当前页数
     */
    private TextView tvCurrent;
    /**
     * 总页数
     */
    private TextView tvTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_check);
        initView();
        iniData();
        setView();
    }

    /**
     * 初始化数据
     */
    private void iniData() {
        list = new ArrayList<>();
        adapter = new ImageVpAdapter();
        position = 0;
    }

    /**
     * 设置控件
     */
    private void setView() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vpImgCheck.setAdapter(adapter);
        vpImgCheck.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int current = i + 1;
                tvCurrent.setText(current + "");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        list.clear();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            list = bundle.getStringArrayList("urlList");
            position = bundle.getInt("position");
            tvTotal.setText(list.size() + "");
            vpImgCheck.setCurrentItem(position);
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        vpImgCheck = (ViewPager) findViewById(R.id.vp_img_check);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
        tvTotal = (TextView) findViewById(R.id.tv_total);
    }

    /**
     * 适配器
     */
    private class ImageVpAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ScaleView scaleView = new ScaleView(ImageCheckActivity.this);
            scaleView.setScaleType(ImageView.ScaleType.MATRIX);
            LogUtils.e("log",list.get(position));
            if (list.get(position).contains("take")&&!list.get(position).contains("files")) {
                Glide.with(ImageCheckActivity.this).load(getExternalCacheDir().getPath() + File
                        .separator + list.get(position)).placeholder(R.mipmap.load).error(R
                        .mipmap.error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//添加缓存
                        .override(500,500)
                        .into(new SimpleTarget<GlideDrawable>() {//图片下载完成后再设置上去
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                    super GlideDrawable> glideAnimation) {
                                scaleView.setImageDrawable(resource);
                            }
                        });
            } else {
                Glide.with(ImageCheckActivity.this).load(list.get(position)).placeholder(R.mipmap
                        .load).error(R.mipmap.error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//添加缓存
                        .override(500,500)
                        .into(new SimpleTarget<GlideDrawable>() {//图片下载完成后再设置上去
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<?
                                    super GlideDrawable> glideAnimation) {
                                scaleView.setImageDrawable(resource);
                            }
                        });
            }
            container.addView(scaleView);
            return scaleView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof ScaleView) {
                ScaleView view = (ScaleView) object;
                container.removeView(view);
            }
        }
    }
}
