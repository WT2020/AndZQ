package hdo.com.andzq.utils.okhttp;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * description 封装网络请求的call，使得能在UI里运行
 * author 邓杰
 * version 1.0
 * Created 2017/3/30.
 */
public abstract class UICallBack implements Callback {
    private Handler handler=new Handler(Looper.getMainLooper());
    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                UIonFailure(call,e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    UIonResponse(call,response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //使用时复写这两个方法
    public abstract void UIonFailure(Call call, IOException e);
    public abstract void UIonResponse(Call call, Response response) throws IOException;
}
