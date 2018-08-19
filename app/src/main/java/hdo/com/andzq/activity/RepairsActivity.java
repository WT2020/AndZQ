package hdo.com.andzq.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import hdo.com.andzq.ActivityCollector;
import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.MySpinnerAdapter;
import hdo.com.andzq.bean.RepairsInfoBean;
import hdo.com.andzq.bean.RepairsResponseBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.DensityUtils;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.ImageUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * description 报修Activity
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 * modified by 张建银 on 2017/10/12 添加报修时专线名称的选择，以及专线号码的显示
 * modified by 张建银 on 2017/10/17 添加照片长按删除的实现
 * modified by 张建银 on 2017/10/17 集团名称根据专线变化的实现
 */
public class RepairsActivity extends BaseActivity {
    private static final int MAX_PIC = 3;
    /**
     * 提交按钮
     */
    private Button mBtnCommit;

    /**
     * 取消按钮
     */
    private Button mBtnCancel;

    /**
     * 申请人
     */
    private TextView mTvProposer;

    /**
     * 当前申请时间
     */
    private TextView mTvApplicationTime;
    private String mApplicationTime;

    /**
     * 所在部门
     */
    private TextView mTvDepartment;

    /**
     * 电话号码
     */
    private EditText mEtPhoneNum;
    private String mPhoneNum;

    /**
     * 单位名称
     */
    private TextView mTvCompany;

    /**
     * 故障出现时间
     */
    private TextView mTvDowntime;
    private String mDowntime;


    /**
     * 故障位置
     */
    private EditText mEtLocation;
    private String mLocation;

    /**
     * 故障类型
     */
    private Spinner mSpFaultType;
    private int mFaultType;

    /**
     * 客户联系方式
     */
    private EditText mCustomerPhone;
    private String mCustomerPhoneNum;



    /**
     * 故障描述
     */
    private EditText mEtDescription;
    private String mDescription;

    /**
     * 放置照片的线性布局
     */
    private LinearLayout mLlPhotos;

    /**
     * 照相按钮
     */
    private ImageButton mBtnTakePhoto;

    /**
     * 紧急程度
     */
    private Spinner mSpRepairsUrgencyLevel;
    private int mRepairsUrgencyLevel;

    private ArrayList<String> mPics = new ArrayList<>();//选择的图片

    private String[] mUrgencyLevel;//紧急等级
    private String[] mDescriptionPresuppose;//故障类型

    private String[] mUrgencyStringArray;//紧急等级

    private Context mContext;//上下文
    private String[] mTypeStringArray;//类型

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private ImageView picture;
    private String TAG = "RepairsActivity:";
    private Bitmap mBitmap;

    private ContentResolver mContentResolver;//内容提供者
    private ProgressDialog progressDialog;//进度dialog
    private Spinner spLine;//专线名称
    private TextView tvlineNum;
    private ArrayAdapter<String> mLineAdapter;
    private String[] mLine = new String[]{};//网络获取的 专线数组
    private ArrayList<String> mLineCompanyId = new ArrayList<>();
    private ArrayList<String> mLineCompany = new ArrayList<>();
    private ArrayList<String> mLineNum = new ArrayList<>();
    private int pos = 0;
    private int mRepairsLine;//专线数组选择的第几个
    private MySpinnerAdapter linAdapter; //spinner适配器
//    private RadioButton rbA;
//    private RadioButton rbZ;
//    private RadioGroup rgLine;
//    private String[] AZLocation = new String[2];//az 地址
    private TextView mTvExpectedTime;//预期时间
    private Handler handler = new Handler();//handler
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);
        mContext = RepairsActivity.this;
        initView();
        initSpinner();
        initToolbar();
        initData();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("报修申请");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setOnClickListener(v -> {
            setData();
            saveData();
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnTakePhoto = (ImageButton) findViewById(R.id.btn_take_photo);
        mTvProposer = (TextView) findViewById(R.id.tv_proposer);
        mTvApplicationTime = (TextView) findViewById(R.id.tv_application_time);
        mTvDepartment = (TextView) findViewById(R.id.tv_department);
        mEtPhoneNum = (EditText) findViewById(R.id.tv_phone_num);
        mTvCompany = (TextView) findViewById(R.id.tv_company);
        mTvDowntime = (TextView) findViewById(R.id.tv_downtime);
        mEtLocation = (EditText) findViewById(R.id.tv_location);
        mCustomerPhone = (EditText)findViewById(R.id.tv_customer_phone);
        mEtDescription = (EditText) findViewById(R.id.tv_description);
        mLlPhotos = (LinearLayout) findViewById(R.id.Ll_photos);
        picture = (ImageView) findViewById(R.id.iv_pic);
//        rbA = (RadioButton) findViewById(R.id.rb_a);
//        rbZ = (RadioButton) findViewById(R.id.rb_z);
//        rgLine = (RadioGroup) findViewById(R.id.rg_line);
        mTvExpectedTime = (TextView) findViewById(R.id.tv_expected_time);
        tvlineNum = (TextView)findViewById(R.id.tv_sp_line_num);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //请求网络数据 写入本地框
        readNetworkData();
        //读取数据
        readData();
        //设置当前申请时间
        mTvApplicationTime.setText(TimeUtils.getCurrentTime());
        mTvDowntime.setText(TimeUtils.getCurrentTime());
        //初始化 array
        mUrgencyLevel = getResources().getStringArray(R.array.urgency_level);
        mDescriptionPresuppose = getResources().getStringArray(R.array.description_presuppose);

        //确定按钮点击 提交报修申请
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepairsInfoBean bean = getFilledInfoBean();
                if (bean.getLine().equals("")){
                    ToastUtils.makeText(RepairsActivity.this,"没有专线存在，您无法提交报修申请！");
                }else {
                    progressDialog = new ProgressDialog(RepairsActivity.this);
                    progressDialog.setTitle("上传报修申请");
                    progressDialog.setMessage("请稍候...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    ArrayList<File> files = new ArrayList<>();
                    for (int i = 0; i < mPics.size(); i++) {
                        String pics = mPics.get(i);
                        String picName = getExternalCacheDir() + File.separator + "temp" + mPics.get(i);
                        File outputImage = new File(getExternalCacheDir(), picName);
                        try {
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (pics.contains("take")) {
                            //如果手机拍照的图片
                            files.add(new File(picName));
                            ImageUtils.compressImage(getExternalCacheDir().getPath() + File.separator
                                    + mPics.get(i), picName, 80);
                        } else {
                            //如果是相册选取的图片
                            ImageUtils.compressImage(mPics.get(i), picName, 80);
                            files.add(new File(picName));
                        }
                    }

                    Map<String, String> infoMap = bean.getMap();

                    final PostRequest postRequest = NetWorkUtils.getInstance().upLoadRepairs
                            (mContext, files, infoMap);


                    postRequest.connTimeOut(1000L).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            runOnUiThread(() -> ToastUtils.makeText(RepairsActivity.this,"提交成功"));
                            //提交申请成功
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                finish();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);

                            runOnUiThread(() -> SnackbarUtils.uploadErr(RepairsActivity.this,
                                    mBtnCancel));
                            progressDialog.dismiss();
                            LogUtils.e("error", "连接失败");
                        }

                        @Override
                        public void upProgress(long currentSize, long totalSize, float progress, long
                                networkSpeed) {
                            super.upProgress(currentSize, totalSize, progress, networkSpeed);
                        }
                    });
                }
            }
        });

        //取消按钮 回到主页面
        mBtnCancel.setOnClickListener(v -> finish());

        //选择故障时间
        mTvDowntime.setOnClickListener(v -> showTimeDialog(mTvDowntime));
        //选择预计完成时间
        mTvExpectedTime.setOnClickListener(v -> showTimeDialog(mTvExpectedTime));
        //点击拍照
        mBtnTakePhoto.setOnClickListener(v -> {
            if (mPics.size() >= MAX_PIC) {
                ToastUtils.makeText(RepairsActivity.this, "最多支持" + MAX_PIC + "张图片");
            } else {
                //如果 未达到 最大图片数 可以添加
                addPic();
            }
        });
        //初始化内容解析者
        mContentResolver = getContentResolver();
        spLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //chooseLine(position);
                tvlineNum.setText(mLineNum.get(position));
                mTvCompany.setText(mLineCompany.get(position));
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spLine.setSelection(0);
        //rgLine.setOnCheckedChangeListener(this::chooseAZ);
    }

    /* 选择专线号码后的逻辑:设置AZ端（已经弃用） */
//    public void chooseAZ(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.rb_a:
//                mEtLocation.setText(AZLocation[0]);
//                mEtLocation.setSelection(AZLocation[0].length());
//                break;
//            case R.id.rb_z:
//                mEtLocation.setText(AZLocation[1]);
//                mEtLocation.setSelection(AZLocation[1].length());
//                break;
//        }
//    }

    /* 选择专线号后的逻辑:设置AZ端（已经弃用） */
    /*
    public void chooseLine(int position) {
        Call call = NetWorkUtils.getInstance().getAZLocation(mContext, mLine[position]);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String s = response.body().string();
                    LogUtils.e("azLocation", s);
                    JSONObject jsonObject = new JSONObject(s);
                    AZLocation[0] = (String) jsonObject.get("aAddress");
                    AZLocation[1] = (String) jsonObject.get("zAddress");
                    Long expectedTime = (Long) jsonObject.get("expectedTime");
                    runOnUiThread(() -> {
                        for (int i = 0; i < rgLine.getChildCount(); i++) {
                            //根据是否有z端地址来判断是否需要选择 a z 地址
                            rgLine.getChildAt(i).setEnabled(!TextUtils.isEmpty(AZLocation[1]));
                        }
                        //网络访问成功后 初始化地址
                        chooseAZ(null, rgLine.getCheckedRadioButtonId());
                        mTvExpectedTime.setText(TimeUtils.Long2Time(expectedTime));
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    */

    /**
     * 获取 填入数据后的InfoBean
     */
    private RepairsInfoBean getFilledInfoBean() {
        RepairsInfoBean bean = new RepairsInfoBean();
        bean.setName(mTvProposer.getText().toString());
        bean.setApplicationTime(mTvApplicationTime.getText().toString());
        bean.setDepartment(mTvDepartment.getText().toString());
        bean.setPhoneNum(mEtPhoneNum.getText().toString());
        bean.setCustomerPhone(mCustomerPhone.getText().toString());

        bean.setCompany(mLineCompanyId.get(pos));
        bean.setDownTime(mTvDowntime.getText().toString());
        bean.setLocation(mEtLocation.getText().toString());
        bean.setFaultType(mSpFaultType.getSelectedItemPosition() + "");


        bean.setDescription(mEtDescription.getText().toString());
        bean.setUrgencyLevel(mUrgencyLevel[mSpRepairsUrgencyLevel.getSelectedItemPosition()]);
        bean.setExpectedTime(mTvExpectedTime.getText().toString());
        if (mLine.length > 0) {
            bean.setLine(mLineNum.get(spLine.getSelectedItemPosition()));
            bean.setLineName(mLine[spLine.getSelectedItemPosition()]);
        }else{
            bean.setLine("");
        }
        return bean;
    }

    /**
     * 请求网络数据 填入布局
     */
    private void readNetworkData() {
        Call call = NetWorkUtils.getInstance().loadRepairsInfo(mContext);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> SnackbarUtils.networkErr(RepairsActivity.this, mBtnCancel));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();
                if(ExamineUtils.isTokenErr(string)){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RepairsActivity.this);
                    SpUtils.putString(mContext,mContext.getString(R.string.preference_token),"");
                    dialog
                            .setTitle("提示")
                            .setMessage("登录已经失效、即将重新登录")
                            .setCancelable(false);
                    runOnUiThread(dialog::show);
                    handler.postDelayed(() -> {
                        ActivityCollector.finishAll();
                        startActivity(new Intent(RepairsActivity.this, LoginActivity.class));
                    },2000);
                }
                Gson gson = new Gson();
                LogUtils.e("response", string);
                try {
                    final RepairsResponseBean bean = gson.fromJson(string, RepairsResponseBean.class);
                    //根据网络信息 保修单信息
                    runOnUiThread(() -> {
                        mTvProposer.setText(bean.getName());
                        mTvDepartment.setText(bean.getDepartment());
                        mEtPhoneNum.setText(bean.getTel());
//                        mCustomerPhone.setText(bean.getTel());
//                        SpUtils.putString(mContext, mContext.getString(R.string
//                                .preference_people_phone), bean.getTel());//存储用户信息
                        mTvCompany.setText(bean.getCompany());
                        mTvExpectedTime.setText(TimeUtils.Long2Time(bean.getExpectTime()));
                        //避免获取空地址时输入框出现Its a real Address,应让客户自己输入地址
                        mEtLocation.setText(bean.getAddress());
                        //选择专线的逻辑
                        if(bean.getLines()!=null) {
                            ArrayList<String> numbers = new ArrayList<>();
                            for (RepairsResponseBean.Line line:bean.getLines()) {
                                numbers.add(line.getLineName());
                                mLineNum.add(line.getLineNo());
                                mLineCompany.add(line.getLineCompany());
                                mLineCompanyId.add(line.getLineCompanyId());
                            }
                            mLine = numbers.toArray(mLine);
                            //mLine = SortUtils.bubbleSort(mLine);
                        }
                        linAdapter = new MySpinnerAdapter(RepairsActivity.this, mLine);
                        spLine.setAdapter(linAdapter);
                    });
                }catch (Exception e){
                    LogUtils.e("response", string);
                    e.printStackTrace();
                }
            }
        });
    }

    @Deprecated
    private void okgoRequest(ArrayList<File> files) {
//       Map<String,String> map = new HashMap<>();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
        OkGo.post(Constant.SUBMIT_REPAIRS)//
                .tag(this)//
                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("param1", "paramValue1")        // 这里可以上传参数
                .params("file1", files.get(0))   // 可以添加文件上传
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        Log.e("callback", "上传成功");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("error", "up_error");
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long
                            networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.e("progress....", "progress" + progress);
                    }
                });
    }

    /**
     * 选择故障出现时间 展示选择时间的dialog
     */
    private void showTimeDialog(TextView textView) {

        int year = Integer.parseInt(TimeUtils.getCurrentYear());
        int month = Integer.parseInt(TimeUtils.getCurrentMonth());
        int day = Integer.parseInt(TimeUtils.getCurrentD());
        int hour = Integer.parseInt(TimeUtils.getCurrentH());
        int minute = Integer.parseInt(TimeUtils.getCurrentM());
        final String[] dataP = new String[1];
        final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, (view,
                                                                                  hourOfDay,
                                                                                  minute1) -> {
            String time = "";
            time = (hourOfDay < 10) ? "0" + hourOfDay : hourOfDay + "";
            time = time + ":" + ((minute1 < 10) ? "0" + minute1 : minute1 + "");
            String dTime=dataP[0] + time;
            textView.setText("");
            textView.setText(dTime);
        }, hour, minute, false);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view, year1, month1,
                                                                            dayOfMonth) -> {
            timePickerDialog.show();
        textView.setText(year1 + "/" + (month1 + 1) + "/" + dayOfMonth + " ");
            dataP[0] = year1 + "/" + (month1 + 1) + "/" + dayOfMonth + " ";
    }, year, month - 1, day);
        datePickerDialog.show();
    }

    /**
     * 添加图片逻辑 展示dialog
     */
    private void addPic() {
        final Dialog dialog = new Dialog(RepairsActivity.this, R.style.BottomDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_pic, null);
        view.findViewById(R.id.takePhoto).setOnClickListener(view12 -> {
            dialog.dismiss();
            takePhoto();
        });

        view.findViewById(R.id.selectPhoto).setOnClickListener(view1 -> {
            dialog.dismiss();
            choosePhoto();
        });
        dialog.setContentView(view);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view
                .getLayoutParams();
        layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics
                        ());
        view.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 选择照片
     */
    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(RepairsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RepairsActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        SimpleDateFormat editFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String curDateTime = editFormat.format(curDate);
        String picName = "take" + curDateTime + ".jpg";
        mPics.add(picName);
        File outputImage = new File(getExternalCacheDir(), picName);
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(RepairsActivity.this,
                    mContext.getPackageName() + ".provider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }


    /*初始化 spinner 更改spinner 样式*/
    private void initSpinner() {
        mSpRepairsUrgencyLevel = (Spinner) findViewById(R.id.tv_repairs_urgency_level);
        mSpFaultType = (Spinner) findViewById(R.id.sp_fault_type);
        spLine = (Spinner) findViewById(R.id.sp_line);
        mUrgencyStringArray = getResources().getStringArray(R.array.urgency_level);
        mTypeStringArray = getResources().getStringArray(R.array.description_presuppose);
        //使用自定义的ArrayAdapter
        MySpinnerAdapter urgencyAdapter = new MySpinnerAdapter(RepairsActivity.this,
                mUrgencyStringArray);
        MySpinnerAdapter typeAdapter = new MySpinnerAdapter(RepairsActivity.this, mTypeStringArray);
        //设置下拉列表风格(这句不写也行)
        //mUrgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpRepairsUrgencyLevel.setAdapter(urgencyAdapter);
        mSpFaultType.setAdapter(typeAdapter);
    }

    /**
     * 读取内容
     */
    private void readData() {
        mPhoneNum = SpUtils.getString(this, "repairs_phone", "");
        mDowntime = SpUtils.getString(this, "repairs_downtime", "");
        mLocation = SpUtils.getString(this, "repairs_location", "");
        mDescription = SpUtils.getString(this, "repairs_description", "");
        mCustomerPhoneNum = SpUtils.getString(this, "repairs_customer_num", "");

        mFaultType = SpUtils.getInt(this, "repairs_fault_type", 0);
        mRepairsUrgencyLevel = SpUtils.getInt(this, "repairs_level", 0);
        mRepairsLine = SpUtils.getInt(this, "repairs_level", 0);
        mEtPhoneNum.setText(mPhoneNum);
        mTvDowntime.setText(mDowntime);
        mEtLocation.setText(mLocation);
        mCustomerPhone.setText(mCustomerPhoneNum);
        mEtDescription.setText(mDescription);
        mSpFaultType.setSelection(mFaultType);
        mSpRepairsUrgencyLevel.setSelection(mRepairsUrgencyLevel);
        spLine.setSelection(mRepairsLine);
    }

    /**
     * 清空数据
     */
    private void clearData() {
        SpUtils.putString(this, "repairs_phone", "");
        SpUtils.putString(this, "repairs_downtime", "");
        SpUtils.putString(this, "repairs_location", "");
        SpUtils.putString(this, "repairs_description", "");
        SpUtils.putInt(this, "repairs_fault_type", 0);
        SpUtils.putInt(this, "repairs_level", 0);
        SpUtils.putString(this, "repairs_customer_num", "");
    }

    /**
     * 保存功能
     */
    private void saveData() {

        SpUtils.putString(this, "repairs_phone", mPhoneNum);
        SpUtils.putString(this, "repairs_downtime", mDowntime);
        SpUtils.putString(this, "repairs_location", mLocation);
        SpUtils.putString(this, "repairs_description", mDescription);
        SpUtils.putString(this, "repairs_customer_num", mCustomerPhoneNum);

        SpUtils.putInt(this, "repairs_fault_type", mFaultType);
        SpUtils.putInt(this, "repairs_level", mRepairsUrgencyLevel);
    }

    /**
     * 从输入中获取数据 将数据赋值给全局变量
     */
    public void setData() {
        mPhoneNum = mEtPhoneNum.getText().toString().trim();
        mDowntime = mTvDowntime.getText().toString().trim();
        mLocation = mEtLocation.getText().toString().trim();
        mCustomerPhoneNum = mCustomerPhone.getText().toString().trim();
        mDescription = mEtDescription.getText().toString().trim();
        mFaultType = mSpFaultType.getSelectedItemPosition();
        mRepairsUrgencyLevel = mSpRepairsUrgencyLevel.getSelectedItemPosition();
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * Activity结果处理逻辑
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //拍照
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        displayImage(null);
                    } catch (Exception e) {
                        LogUtils.e(TAG, e.getMessage());
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    //如果 取消拍照 则删除文件地址
                    mPics.remove(mPics.size() - 1);
                }
                break;

            //选择照片
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 请求权限完成后
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    //请求成功打开相册
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    /**
     * android kitkat 选择图片逻辑
     *
     * @param data intent
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse
                        ("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    /**
     * android kitkat版本以下 选择图片逻辑
     *
     * @param data intent
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 获取选择照片的路径
     *
     * @param uri       uri
     * @param selection 选择的图片
     * @return 图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 相册选择 展示image
     *
     * @param imagePath 图片路径
     */
    private void displayImage(String imagePath) {
        File file;
        if (imagePath != null) {
            //将图片路径添加到图片数组 从相册选择的图片是绝对路径
            mPics.add(imagePath);
            file = new File(imagePath);
        }else{
            file = new File(mContext.getExternalCacheDir(), mPics.get(mPics.size() - 1));
        }
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = DensityUtils.dp2px(mContext, 50);
        params.width = DensityUtils.dp2px(mContext, 50);
        params.setMargins(0, 0, DensityUtils.dp2px(mContext, 10), 0);
        imageView.setLayoutParams(params);
        imageView.setClickable(true);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(RepairsActivity.this, ImageCheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("urlList", (ArrayList<String>) mPics);
            int imageViewPos = 0;
            for (int i = 0; i < mLlPhotos.getChildCount(); i++) {
                if (imageView == mLlPhotos.getChildAt(i)) imageViewPos = i;
            }
            bundle.putInt("position", imageViewPos);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                builder.setMessage("确定删除图片").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mLlPhotos.removeView(v);
                        int pos = 0;
                        for (int j = 0; j < mLlPhotos.getChildCount(); j++) {
                            if (v == mLlPhotos.getChildAt(i)) pos = j;
                        }
                        mPics.remove(pos);
                    }
                }).create().show();
                return true;

            }
        });
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mLlPhotos.addView(imageView, mLlPhotos.getChildCount() - 1);
        //picasso这个垃圾 7.0上加载不出图片
//            Picasso.with(RepairsActivity.this).load(new File(imagePath)).resize(500, 500)
//                    .centerCrop().into(imageView);
        Glide.with(RepairsActivity.this).load(file).centerCrop().into(imageView);
    }
}
