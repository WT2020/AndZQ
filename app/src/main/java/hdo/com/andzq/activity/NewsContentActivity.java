package hdo.com.andzq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
//取消原生CollapsingToolbarLayout的导入
//import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzhoujay.richtext.RichText;
//导入第三方CollapsingToolbarLayout（标题可多行显示）
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.LogUtils;

/**
 * description 新闻详情页
 * author 陈锐
 * version 1.0
 * created 2017/4/1
 * modified 2017/7/18 by 张建银 实现新闻详情页标题多行显示（仅更改CollapsingToolbarLayout的导入源：从原生到第三方）
 */

public class NewsContentActivity extends BaseActivity {
    public static final String NEWS_NAME = "news_name";
    public static final String NEWS_ICON = "news_icon";
    public static final String NEWS_CONTENT = "news_content";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        Intent intent = getIntent();
        String newsName = intent.getStringExtra(NEWS_NAME);
        String newsIconUrl = intent.getStringExtra(NEWS_ICON);
        String newsContent = intent.getStringExtra(NEWS_CONTENT);

        Toolbar tbToolbar = (Toolbar) findViewById(R.id.tb_toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id
                .collapsing_toolbar);
        tbToolbar.setTitle("新闻名称");
        ImageView ivIcon = (ImageView) findViewById(R.id.iv_icon);
        TextView tvContent = (TextView) findViewById(R.id.tv_content);



        if (!TextUtils.isEmpty(newsIconUrl)) {
            Glide.with(this).load(Constant.HOME+newsIconUrl).override(500,500).centerCrop().into(ivIcon);
        }


        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(newsName);
        //将服务器中的图片地址改为绝对地址
        String s=  newsContent.replaceAll("H_O_M_E",Constant.HOME);
        LogUtils.e("replaceAll",s);
        RichText.fromHtml(s).bind(this).into(tvContent);

//        tvContent.setText(newsContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
    }
}
