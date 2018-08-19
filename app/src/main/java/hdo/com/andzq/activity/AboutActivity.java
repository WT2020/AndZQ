package hdo.com.andzq.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.apkUpdate.ApkUpdateVersion;
import hdo.com.andzq.apkUpdate.UpdateAppEngine;

/**
 * description 关于我们Activity
 * author 陈锐
 * version 1.0
 * Created 2017/5/25.
 *
 *
 * modified by 张建银 on 2017/10/20 版本名称动态处理还原
 */
public class AboutActivity extends BaseActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private LinearLayout llVersionName;//检查更新 注册点击事件
    private TextView versionName;//版本名称
    private Context context;//上下文
    private int code;//版本号
    private UpdateAppEngine updateAppEngine;//app更新engine
    private Handler handler = new Handler();//handler对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = this;
        initToolbar();//初始化 toolbar
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        updateAppEngine = new UpdateAppEngine(AboutActivity.this);
        llVersionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApkUpdateVersion(context,AboutActivity.this);
            }
        });
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            String name = pi.versionName;//获取版本名称
            code = pi.versionCode;
            versionName.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 检查版本
//     */
//    private void checkVersion(View v) {
//        ProgressDialog progress = DialogUtils.progress(AboutActivity.this, "检查更新中...");
//
//        progress.show();
//        verifyStoragePermissions(AboutActivity.this);
//        PostRequest request = NetWorkUtils.getInstance().downLoadApk(getApplicationContext());
//        request.execute(new AbsCallback<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject o, Call call, Response response) {
//                handler.postDelayed(progress::dismiss, 500);
//                try {
//                    String time = o.getString("time");
//                    String description = o.getString("description");
//                    int versionCode = o.getInt("versionCode");
//                    String apkUrl = o.getString("url");
//                    Log.e("URL!", "" + apkUrl);
//                    if (versionCode > code) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
//                        builder.setTitle("提示")
//                                .setMessage(description)
//                                .setNegativeButton("取消", null)
//                                .setPositiveButton("更新", (dialog, which) -> {
//                                    loadNewVersionProgress(apkUrl);
////                                    if(!updateAppEngine.isDownLoading()){
////                                        updateAppEngine.startDownLoad(Constant.HOME+apkUrl,"服务一点通");
////                                    }
//                                }).show();
//                    } else {
//                        ToastUtils.makeText(AboutActivity.this, "已是最新");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public JSONObject convertSuccess(Response response) throws Exception {
//                InputStream inputStream = response.body().byteStream();
//                String json = StreamUtil.streamToString(inputStream);
//                return new JSONObject(json);
//            }
//
//            @Override
//            public void onError(Call call, Response response, Exception e) {
//                progress.dismiss();
//                ToastUtils.makeText(AboutActivity.this, "网络访问失败");
//                super.onError(call, response, e);
//            }
//        });
//    }
//
//    //下载新版本
//    private void loadNewVersionProgress(String uri) {
//        final ProgressDialog progressDialog;    //进度条对话框
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        //启动子线程下载任务
//        new Thread() {
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
//            }
//        }.start();
//    }
//
//    /**
//     * 从服务器获取apk文件的代码
//     * 传入网址uri，进度条对象即可获得一个File文件
//     * （要在子线程中执行哦）
//     */
//    public static File getFileFromServer(String uri, ProgressDialog progressDialog) throws Exception {
//        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            URL url = new URL(uri);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            //获取到文件的大小
//            progressDialog.setMax(conn.getContentLength() / 1024);
//            InputStream is = conn.getInputStream();
//            long time = System.currentTimeMillis();//当前时间的毫秒数
//            File file = new File(Environment.getExternalStorageDirectory(), time + "updata.apk");
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedInputStream bis = new BufferedInputStream(is);
//            byte[] buffer = new byte[1024];
//            int len;
//            int total = 0;
//            while ((len = bis.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//                total += len;
//                //获取当前下载量
//                progressDialog.setProgress(total / 1024);
//            }
//            fos.close();
//            bis.close();
//            is.close();
//            return file;
//        } else {
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
     * 初始化布局
     */
    private void initView() {
        llVersionName = (LinearLayout) findViewById(R.id.llVersionName);
        versionName = (TextView) findViewById(R.id.versionName);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("关于我们");
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void verifyStoragePermissions(AboutActivity activity) {
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
