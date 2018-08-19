package hdo.com.andzq.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;

import hdo.com.andzq.R;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.bean.MonthlyBean;
import hdo.com.andzq.global.Constant;

/**
 * description 月报详情Activity
 * author 吴小雪
 * version 1.0
 * Created 2017/12/5.
 */

public class MonthContentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_content);

        WebView tvContent = (WebView) findViewById(R.id.tv_content);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");

        //将服务器中的图片地址改为绝对地址
        String s = content.replaceAll("H_O_M_E", Constant.HOME);

        /*
        RichText利用Html.fromHtml(source)来显示html的，对于style、table等等一系列的标签它都没有解析的
        故改为WebView解析富文本内容
        */
        //RichText.fromHtml(s).bind(this).into(tvContent);
        tvContent.getSettings().setDefaultTextEncodingName("UTF-8");
        tvContent.loadData(s, "text/html; charset=UTF-8", "UTF-8");

        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("月报详情");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setVisibility(View.INVISIBLE);
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
