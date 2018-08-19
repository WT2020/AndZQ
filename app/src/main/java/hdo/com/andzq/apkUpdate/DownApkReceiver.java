package hdo.com.andzq.apkUpdate;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
/**
 * description 下载apk 广播接收者
 * author 陈锐
 * version 1.0
 * created 2017/5/25
 */
public class DownApkReceiver extends BroadcastReceiver {
    private static final String TAG = "DownApkReceiver";
    SharedPreferences mSharedP;
    DownloadManager mManager;
    Context ctx;
    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            mSharedP = context.getSharedPreferences("downloadApk", Context.MODE_PRIVATE);
            long saveApkId = mSharedP.getLong("downloadId", -1L);
            if (downloadApkId == saveApkId) {
                checkDownloadStatus(context, downloadApkId);
            }
        }
    }

    private void checkDownloadStatus(final Context context, long downloadId) {
        mManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    installApk(context);
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.d("DownApkReceiver", "下载失败.....");
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Log.d("DownApkReceiver", "正在下载.....");
                    break;
                default:
                    break;
            }
        }
    }

    private void installApk(Context context) {
        String apkName = mSharedP.getString("apkName", null);
        if (apkName != null) {
            Log.d("DownApkReceiver", "apkName 为" + apkName);
            File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + File.separator + apkName);
            if (file != null) {
                Intent install = new Intent("android.intent.action.VIEW");
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if(Build.VERSION.SDK_INT> Build.VERSION_CODES.N) {
                    //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                    Uri apkUri = FileProvider.getUriForFile(context, "com.example.chenfengyao.installapkdemo", file);
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                }else {
                    //安卓7.0一下的安装逻辑
                    Uri downloadFileUri = Uri.fromFile(file);
                    install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                }
                context.startActivity(install);
            } else {
                Log.d("DownApkReceiver", "下载失败");
            }
        } else {
            Log.d("DownApkReceiver", "apkName 为 null");
        }
    }
}