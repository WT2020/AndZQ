package hdo.com.andzq.apkUpdate;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hdo.com.andzq.activity.AboutActivity;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.StreamUtil;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 将检查更新类封装，便于多次利用，只需传入context和activity即可
 * author 章浩
 * version 1.0
 * created 2017/7/6
 *
 *
 */

public class ApkUpdateVersion {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int code;//版本号
    private Handler handler = new Handler();//handler对象

    public ApkUpdateVersion(Context context,Activity activity) {
        initData(activity);     //得到版本号名name和版本码code
        checkVersion(context,activity);
    }
    //获取版本名称
    public  void initData(Activity activity){
        PackageManager pm = activity.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(activity.getPackageName(),PackageManager.GET_CONFIGURATIONS);
            String name = pi.versionName;//获取版本名称
            code = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 检查版本
     */
    private void checkVersion(Context context,Activity activity) {
        ProgressDialog progress = DialogUtils.progress(context, "检查更新中...");

        if(!progress.isShowing()){
            progress.show();
        }
        verifyStoragePermissions(activity);
        PostRequest request = NetWorkUtils.getInstance().downLoadApk(context);
        request.execute(new AbsCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject o, Call call, Response response) {
                handler.postDelayed(progress::dismiss, 500);
                try {
                    String time = o.getString("time");
                    String description = o.getString("description");
                    int versionCode = o.getInt("versionCode");
                    String apkUrl = o.getString("url");
                    Log.e("URL!", "" + apkUrl);
                    if (versionCode > code) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("提示")
                                .setMessage(description)
                                .setNegativeButton("取消", null)
                                .setPositiveButton("更新", (dialog, which) -> {
                                    loadNewVersionProgress(apkUrl,context,activity);
//                                    if(!updateAppEngine.isDownLoading()){
//                                        updateAppEngine.startDownLoad(Constant.HOME+apkUrl,"服务一点通");
//                                    }
                                }).show();
                    } else {
                        ToastUtils.makeText(activity, "已是最新");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //解析网络返回的数据回调
            @Override
            public JSONObject convertSuccess(Response response) throws Exception {
                InputStream inputStream = response.body().byteStream();
                String json = StreamUtil.streamToString(inputStream);
                return new JSONObject(json);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                progress.dismiss();
                ToastUtils.makeText(activity, "网络访问失败");
                super.onError(call, response, e);
            }
        });
    }

    //下载新版本
    private void loadNewVersionProgress(String uri,Context context,Activity activity) {
       // ProgressDialog progressDialog;    //进度条对话框
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载更新");
        progressDialog.show();
        //启动子线程下载任务
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(uri, progressDialog);
//                    sleep(3000);
                    installApk(file,activity);
                    progressDialog.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    //下载apk失败
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public static File getFileFromServer(String uri, ProgressDialog progressDialog) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            progressDialog.setMax(conn.getContentLength() / 1024);
            InputStream is = conn.getInputStream();
            long time = System.currentTimeMillis();//当前时间的毫秒数
            File file = new File(Environment.getExternalStorageDirectory(), time + "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                progressDialog.setProgress(total / 1024);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }
    /**
     * 安装apk
     */
    private void installApk(File file, Activity activity) {
        // 7.0以上自动更新 闪退异常权限
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = activity.getPackageName()+".provider";
            Uri contentUri= FileProvider.getUriForFile(activity,authority,file);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
            activity.startActivity(intent);
        }
        else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            activity.startActivity(intent);
        }
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    public static void verifyStoragePermissions(Activity activity) {
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
