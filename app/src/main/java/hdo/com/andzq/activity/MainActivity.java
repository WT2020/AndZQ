package hdo.com.andzq.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.Fragment.NewsFragment;
import hdo.com.andzq.Fragment.HomeFragment;
import hdo.com.andzq.Fragment.MineFragment;
import hdo.com.andzq.R;
import hdo.com.andzq.apkUpdate.ApkUpdateVersion;
import hdo.com.andzq.service.PushService;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.view.NoScroolViewPager;


/**
 * description 主Activity
 * author 陈锐
 * version 1.0
 * created 2017/3/24
 * modify 修改了主页的底部导航栏 by 邓杰 on 2017/3/30
 * modify 删除投诉的底部导航栏 by 邓杰 on 2017/4/1
 */
public class MainActivity extends BaseActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * 不允许滑动的ViewPager
     */
    private NoScroolViewPager vpContent;
    /**
     * 底部的NavigationBar
     */
    private BottomNavigationBar bottomNav;
    /**
     * 标题
     */
    private TextView toobarTitle;
    private int code;//版本号
    private BadgeItem numberBadgeItem;
    TabAdapter tabAdapter;
    CharSequence[] titles = {"首页","新闻","我的"};
    private Handler handler = new Handler();//handler对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initView();
        setView();
        LogUtils.e("MainActivity", "开启服务");
        startService( new Intent(this, PushService.class));//开启推送检查服务
        new ApkUpdateVersion(this,MainActivity.this);
    }
//    public  void initData(){
//        PackageManager pm = getPackageManager();
//        try {
//            PackageInfo pi = pm.getPackageInfo(getPackageName(),PackageManager.GET_CONFIGURATIONS);
//            String name = pi.versionName;//获取版本名称
//            code = pi.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 检查版本
//     */
//    private void checkVersion() {
//        ProgressDialog progress = DialogUtils.progress(MainActivity.this, "检查更新中...");
//
//        progress.show();
//        verifyStoragePermissions(MainActivity.this);
//        PostRequest request = NetWorkUtils.getInstance().downLoadApk(getApplicationContext());
//        request.execute(new AbsCallback<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject o, Call call, Response response) {
//                handler.postDelayed(progress::dismiss,500);
//                try{
//                    String time = o.getString("time");
//                    String description = o.getString("description");
//                    int versionCode = o.getInt("versionCode");
//                    String apkUrl = o.getString("url");
//                    Log.e("URL!",""+apkUrl);
//                    if(versionCode>code){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle("提示")
//                                .setMessage(description)
//                                .setNegativeButton("取消",null)
//                                .setPositiveButton("更新", (dialog, which) -> {
//                                    loadNewVersionProgress(apkUrl);
////                                    if(!updateAppEngine.isDownLoading()){
////                                        updateAppEngine.startDownLoad(Constant.HOME+apkUrl,"服务一点通");
////                                    }
//                                }).show();
//                    }else {
//                        ToastUtils.makeText(MainActivity.this,"已是最新");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public JSONObject convertSuccess(Response response) throws Exception {
//                InputStream inputStream = response.body().byteStream();
//                String json = StreamUtil.streamToString(inputStream);
//                return new JSONObject(json);
//            }
//            @Override
//            public void onError(Call call, Response response, Exception e) {
//                progress.dismiss();
//                ToastUtils.makeText(MainActivity.this,"网络访问失败");
//                super.onError(call, response, e);
//            }
//        });
//    }
//    //下载新版本
//    private void loadNewVersionProgress(String uri) {
////        ProgressDialog progressDialog;    //进度条对话框
//        ProgressDialog progressDialog = new  ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMessage("正在下载更新");
//        progressDialog.show();
//        //启动子线程下载任务
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    File file = getFileFromServer(uri, progressDialog);
//                    sleep(3000);
//                    installApk(file);
//                    progressDialog.dismiss(); //结束掉进度条对话框
//                } catch (Exception e) {
//                    //下载apk失败
//                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//            }}.start();
//    }
//    /**
//     * 从服务器获取apk文件的代码
//     * 传入网址uri，进度条对象即可获得一个File文件
//     * （要在子线程中执行哦）
//     */
//    public static File getFileFromServer(String uri, ProgressDialog progressDialog) throws Exception{
//        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
//        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            URL url = new URL(uri);
//            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            //获取到文件的大小
//            progressDialog.setMax(conn.getContentLength()/1024);
//            InputStream is = conn.getInputStream();
//            long time= System.currentTimeMillis();//当前时间的毫秒数
//            File file = new File(Environment.getExternalStorageDirectory(), time+"updata.apk");
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedInputStream bis = new BufferedInputStream(is);
//            byte[] buffer = new byte[1024];
//            int len ;
//            int total=0;
//            while((len =bis.read(buffer))!=-1){
//                fos.write(buffer, 0, len);
//                total+= len;
//                //获取当前下载量
//                progressDialog.setProgress(total/1024);
//            }
//            fos.close();
//            bis.close();
//            is.close();
//            return file;
//        }
//        else{
//            return null;
//        }
//    }
//
//    /**
//     * 安装apk
//     */
//    protected void installApk(File file) {
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
//    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        //隐藏保存按钮
        save.setVisibility(View.GONE);
        //toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            //是否显示返回按钮
            //supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 设置控件
     */
    private void setView() {
        //设置Nav的模式
        bottomNav.setMode(BottomNavigationBar.MODE_DEFAULT);
        //设置背景样式
        bottomNav.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //设置选中颜色
        bottomNav.setActiveColor(R.color.blue600);
        //设置ViewPager

        vpContent.setAdapter(tabAdapter=new TabAdapter(getSupportFragmentManager()));
        vpContent.setOffscreenPageLimit(2);

        numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.red400)
                .setText("5")
                .setHideOnSelect(true);
        //添加Nav的Item
        bottomNav.addItem(new BottomNavigationItem(R.mipmap.home_def, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.form_def, "新闻"))
                .addItem(new BottomNavigationItem(R.mipmap.me_def, "我的"))
                .setFirstSelectedPosition(0)//默认为第一个
                .initialise();
//        .addItem(new BottomNavigationItem(R.mipmap.me_def, "我的")
//                .setBadgeItem(numberBadgeItem))

        //设置默认的fragment
        setDefault();
        //Nav的选择监听
        bottomNav.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                vpContent.setCurrentItem(position,false);
                toobarTitle.setText(tabAdapter.getPageTitle(position));
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        RichText.initCacheDir(MainActivity.this);
    }

    /**
     * 设置默认的fragment
     */
    private void setDefault() {
        toobarTitle.setText("首页");
    }

    /**
     * 初始化布局
     */
    private void initView() {
        vpContent = (NoScroolViewPager) findViewById(R.id.vp_content);
        bottomNav = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar_main);
        toobarTitle = (TextView) findViewById(R.id.toolbar_title);
    }

    class TabAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        // 标题数组
        TabAdapter(FragmentManager fm) {
            super(fm);
            fragmentList.add(new HomeFragment());
            fragmentList.add(new NewsFragment());
            fragmentList.add(new MineFragment());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
    public static void verifyStoragePermissions(MainActivity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    }
