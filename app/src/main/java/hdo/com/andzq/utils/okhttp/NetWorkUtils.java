package hdo.com.andzq.utils.okhttp;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import hdo.com.andzq.R;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SpUtils;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * description 封装网络请求的单例类
 * author 邓杰
 * version 1.0
 * Created 2017/3/30.
 * modified 单例模式改为饿汉式 避免多线程问题 陈锐 2017/4/17
 * modified on 2017/8/31 by 张建银 修改维修申请与更新进度的文件传输参数请求模型为ArrayList<File>
 */
public class NetWorkUtils {
    /**
     * 本类静态变量
     */
    private static NetWorkUtils netWorkUtils = new NetWorkUtils();
    private OkHttpClient client;


    private NetWorkUtils() {
        //okhttp3 日志拦截器
        HttpLoggingInterceptor hli = new HttpLoggingInterceptor();

        LogUtils.e("network", Debug.isDebuggerConnected() + "");
        //设置级别
        hli.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS)
                .addInterceptor(hli)//添加日志拦截器
                .build();

        //okgo 日志拦截器
        com.lzy.okgo.interceptor.HttpLoggingInterceptor interceptor = new com.lzy.okgo
                .interceptor.HttpLoggingInterceptor("okgo");
        interceptor.setPrintLevel(com.lzy.okgo.interceptor.HttpLoggingInterceptor.Level.BODY);
        interceptor.setColorLevel(Level.INFO);
        OkGo.getInstance().addInterceptor(interceptor);
    }

    /**
     * 得到本类对象
     *
     * @return 本类对象
     */
    public static NetWorkUtils getInstance() {
        return netWorkUtils;
    }

    /**
     * 登录请求
     *
     * @param username 用户名(code)
     * @param password 密码
     * @return call模型
     */
    public Call login(String username, String password) {
        //请求表单
        RequestBody requestBody = new FormBody.Builder()
                .add("uCode", username)
                .add("uPwd", password)
                .build();
        Request request = new Request.Builder()
                .url(Constant.LOGIN)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 加载报修单基本信息
     */
    public Call loadRepairsInfo(Context ctx) {
        //请求表单
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .build();
        Request request = new Request.Builder()
                .url(Constant.GET_REPAIRS)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 请求网络获取 维修人员 我的维修单
     * //登陆成功 将token保存在本地
     * SpUtils.putString(mContext, mContext.getString(R.string.preference_token), bean.getToken());
     * //角色码
     * SpUtils.putString(mContext, mContext.getString(R.string.preference_role_id), bean
     * .getRole_id());
     * //用户名
     * SpUtils.putString(mContext, mContext.getString(R.string.preference_code), bean.getCode());
     */
    public Call loadRepairsList(Context ctx) {
        //请求表单
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .build();
        Request request = new Request.Builder()
                .url(Constant.GET_MY_REPAIRS_LIST)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 维修人员上传维修进度
     */
    public PostRequest upLoadRepairsProgress(Context ctx, ArrayList<File> files, Map<String,
            String> map) {
        PostRequest postRequest = OkGo.post(Constant.SUBMIT_MY_REPAIRS_PROCESS)
                .tag(this)
                .isMultipart(true);
        map = putToken(ctx, map);
        postRequest.params(map, false);
        postRequest.addFileParams("imgList", files);
//        //封装照片
//        for (File f : files) {
//            postRequest.params(f.getName(), f);
//        }
        return postRequest;
    }

    /**
     * 上传报修单
     */
    public PostRequest upLoadRepairs(Context ctx, ArrayList<File> files, Map<String, String> map) {
        PostRequest postRequest = OkGo.post(Constant.SUBMIT_REPAIRS)
                .tag(this)//
                .isMultipart(true);// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
        map = putToken(ctx, map);
        //封装map
        postRequest.params(map, false);
//        封装照片
        postRequest.addFileParams("imgList", files);
//        for (File f : files) {
//            postRequest.params(f.getName(), f);
//        }
        return postRequest;
    }

    /**
     * 上传投诉建议
     */
    public PostRequest upLoadComplain(Context ctx, Map<String, String> map) {
        PostRequest postRequest = OkGo.post(Constant.SUBMIT_COMPLAIN)
                .tag(this);
        map = putToken(ctx, map);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 上传客户信息
     */
    public PostRequest upLoadNewCustomer(Context ctx, Map<String, String> map) {
        PostRequest postRequest = OkGo.post(Constant.SUBMIT_NEW_CUSTOMER)
                .tag(this);
        map = putToken(ctx, map);
        Log.e("msg", "onSuccess: "+map.get("name"));
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }


    /**
     * 推送服务请求
     */
    public Call push(Context ctx) {
        //请求表单
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .build();
        Request request = new Request.Builder()
                .url(Constant.PUSH)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 保修时 根据专线号 获取A端 Z端地址
     */
    public Call getAZLocation(Context ctx, String line) {
        //请求表单
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .add("line", line)
                .build();
        Request request = new Request.Builder()
                .url(Constant.GET_AZ_LOCATION)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 上传报修单
     *
     * @return call
     */
    @Deprecated
    public Call upLoadRepairs1(ArrayList<File> file) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            for (File f : file) {
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"file\";filename=\"" + f.getName() + "\"")
                        , RequestBody.create(MediaType.parse("image/png"), f)).build();
            }
        }

        //封装参数
        builder.addFormDataPart("msg", "upload pic");
        RequestBody body = builder.build();

        //构建带进度回调的requestBody
        // TODO: 2017/4/11 文件上传进度监听
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(body) {
            @Override
            public void loading(long current, long total, boolean done) {
                LogUtils.e("loading...", " c" + current + " t" + total + " d" + done);
            }
        };
        Request request = new Request.Builder().url(Constant.SUBMIT_REPAIRS)
                .post(body).build();
        return client.newCall(request);
    }


    /**
     * 我的保修单
     */
    public Call getMyRepairsList(Context ctx) {
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .build();
        Request request = new Request.Builder()
                .url(Constant.GET_MY_REPAIRS_LIST_l)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 提交评价
     */
    public Call postRepairsEvaluate(Context ctx, String id, String star, String comment) {
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .add("repair_id", id)
                .add("star", star)
                .add("mark", comment)
                .build();
        Request request = new Request.Builder()
                .url(Constant.POST_MY_REPAIRS_LIST_COMMENT)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 提交评价
     */
    public Call postComplaintEvaluate(Context ctx, String id, String star, String comment) {
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .add("complaint_id", id)
                .add("star", star)
                .add("mark", comment)
                .build();
        Request request = new Request.Builder()
                .url(Constant.COMPLAIN_APPCOMMENT)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 获取客服人员信息
     */
    public Call getSupportStaff(Context ctx) {
        RequestBody requestBody = new FormBody.Builder()
                .add(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_token), ""))
                .add(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_code), ""))
                .add(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx
                        .getString(R.string
                                .preference_role_id), ""))
                .build();
        Request request = new Request.Builder()
                .url(Constant.SERVICE_STAFF)
                .post(requestBody)
                .build();
        return client.newCall(request);
//        PostRequest postRequest = OkGo.post(Constant.SERVICE_STAFF).tag(this);
//        Map<String, String> map = new HashMap<>();
//        map = putToken(ctx, map);
//        postRequest.params(map, false).connTimeOut(1000L);
//        return postRequest;
    }

    /*
    * 获取分公司名字
    * */
    public Call getCompanyName(Context ctx) {
        RequestBody requestBody = new FormBody.Builder()
                .add("token", SpUtils.getString(ctx, ctx.getString(R.string.preference_company_name), ""))
                .build();
        Request request = new Request.Builder()
                .url(Constant.GET_COMPANY_NAME)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    /**
     * 获取我的投诉
     */
    public PostRequest getMyComplain(Context ctx) {
        PostRequest postRequest = OkGo.post(Constant.GET_COMPLAIN).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 获取我的新建进度
     */
    public PostRequest getNewProgress(Context ctx) {
        PostRequest postRequest = OkGo.post(Constant.GET_NEW_PROGRESS).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 获取我的产品
     */
    public PostRequest getProduct(Context ctx) {
        PostRequest postRequest = OkGo.post(Constant.GET_PRODUCT).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 更新月报
     */
    public PostRequest getMonthly(Context ctx, String start, String end) {
        PostRequest postRequest = OkGo.post(Constant.GET_MONTHLY).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        map.put("start", start);
        map.put("end", end);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 用户加载我的信息
     */
    public PostRequest loadMyData(Context ctx) {
        PostRequest postRequest = OkGo.post(Constant.GET_MY_DATA).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 加载维修工程师的信息
     */
    public PostRequest loadRepairData(Context ctx) {
        PostRequest postRequest = OkGo.post(Constant.GET_REPAIR_DATA).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        postRequest.params(map, false).connTimeOut(1000L);
        return postRequest;
    }

    /**
     * 修改密码
     */
    public PostRequest changePsw(Context ctx, Map<String, String> map) {
        PostRequest postRequest = OkGo.post(Constant.CHANGE_PSW).tag(this);
        map = putToken(ctx, map);
        postRequest.params(map, false);//封装map
        return postRequest;
    }

    /**
     * 获取新闻
     */
    public PostRequest loadNews(Context ctx, Map<String, String> map) {
        PostRequest postRequest = OkGo.post(Constant.GET_NEWS).tag(this);
        map = putToken(ctx, map);
        postRequest.params(map, false);//封装map
        return postRequest;
    }


    /**
     * 检查apk版本
     */
    public PostRequest downLoadApk(Context ctx) {
        PostRequest postRequest = OkGo.post(Constant.GET_APK_VERSION).tag(this);
        Map<String, String> map = new HashMap<>();
        map = putToken(ctx, map);
        postRequest.params(map, false);//封装map
        return postRequest;
    }


    private Map<String, String> putToken(Context ctx, Map<String, String> map) {
        map.put(ctx.getString(R.string.preference_token), SpUtils.getString(ctx, ctx.getString(R
                .string
                .preference_token), ""));
        map.put(ctx.getString(R.string.preference_role_id), SpUtils.getString(ctx, ctx.getString
                (R.string
                        .preference_role_id), ""));
        map.put(ctx.getString(R.string.preference_code), SpUtils.getString(ctx, ctx.getString(R
                .string
                .preference_code), ""));
        return map;
    }

}
