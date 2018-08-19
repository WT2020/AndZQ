package hdo.com.andzq.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.RepairsStateGridImgListAdapter;
import hdo.com.andzq.bean.MyRepairsDetailsBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.DensityUtils;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.ImageUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 维修人员维修单详情
 * author 陈锐
 * version 1.0
 * created 2017/4/7
 * modified by 张建银 on 2017/10/17 添加照片长按删除的实现
 */

public class MyRepairsDetailsActivity extends BaseActivity {

    private String TAG = "MyRepairsDetailsActivity";//tag
    private static final int MAX_PIC = 3;
    private static final int TAKE_PHOTO = 100;
    private static final int CHOOSE_PHOTO = 200;
    private Button mBtnCommit;//提交按钮
    private Button mBtnCancel;//取消按钮
    private EditText mEtProgress;//维修人员编辑的进度描述
    private ImageButton mTakePhoto;//拍照按钮
    private ArrayList<String> mPics = new ArrayList<>();//拍照图片
    private ContentResolver mContentResolver;//内容解析者
    private Context mContext;//上下文对象
    private Uri imageUri;//图片uri
    private LinearLayout mLlProgressPhotos;//进度图片
    private LinearLayout mLlDescriptionPhotos;//描述图片
    private MyRepairsDetailsBean.MyRepairBean mDetailsBean;//保修单详情
    private TextView mTvProposer;//申请人
    private TextView mTvApplicationTime;//申请时间
    private TextView mTvDepartment;//部门
    private TextView mTvPhoneNum;//电话号码
    private TextView mTvCompany;//公司
    private TextView mTvLine;//专线号
    private TextView mTvDowntime;//故障时间
    private TextView mTvLocation;//地址
    private TextView mTvFaultType;//故障类型
    private TextView mTvCustomerPhone;//客户联系电话
    private TextView mTvDescription;//描述
    private TextView mTvRepairsUrgencyLevel;//紧急程度
    private TextView mTvPredictTime;//预计完成事件
    private RadioGroup mRgRepairsState;//维修状态--进度
    private AppCompatRadioButton mRbRepairing;//维修中rb
    private AppCompatRadioButton mRbRepairComplete;//维修完成rb
    private AppCompatRadioButton mRbArrived;//到达现场rb
    private LinearLayout mLlProgress;//进度图片LinearLayout
    private ScrollView mScrollView;//scroolview
    private Handler handler = new Handler();
    private LinearLayout mLlShowEdit;//编辑框
    private LinearLayout mLlProgressList;//进度LinearLayout
    private View itemComment;
    private RatingBar rbLevel;
    private TextView tvEvaluateIdea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_repairs_details);
        mContext = this;
        initToolbar();
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mBtnCancel.setOnClickListener(v -> finish());
        mBtnCommit.setOnClickListener(v -> submit());
        //点击拍照
        mTakePhoto.setOnClickListener(v -> {
            if (mPics.size() >= MAX_PIC) {
                ToastUtils.makeText(MyRepairsDetailsActivity.this, "最多支持" + MAX_PIC + "张图片");
            } else {
                addPic();//如果 未达到 最大图片数 可以添加
            }
        });
        //初始化内容解析者
        mContentResolver = getContentResolver();
        Intent intent = getIntent();
        mDetailsBean = (MyRepairsDetailsBean.MyRepairBean) intent.getSerializableExtra(mContext
                .getString(R.string.RepairsDetailsBean));
        //加载网络数据
        showNetData();
        checkState();
    }

    /**
     * 初始化 维修进度 状态 -- 接单 到达现场 维修中 维修完成
     */
    private void checkState() {
        mRgRepairsState.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == mRbArrived.getId()) {
                mBtnCommit.setText("到达现场");
                mLlProgress.setVisibility(View.VISIBLE);
            } else if (checkedId == mRbRepairing.getId()) {
                mBtnCommit.setText("提交");
                mLlProgress.setVisibility(View.VISIBLE);
                SpUtils.putString(mContext, mContext.getString(R.string.preference_repairs_state)
                        , "3");
                //addView完之后，不等于马上就会显示，而是在队列中等待处理，虽然很快，但是如果立即调用fullScroll，
                //view可能还没有显示出来，所以会失败 应该通过handler在消息队列中处理更新
                handler.post(() -> mScrollView.fullScroll(View.FOCUS_DOWN));
            } else if (checkedId == mRbRepairComplete.getId()) {
                mBtnCommit.setText("完成");
                SpUtils.putString(mContext, mContext.getString(R.string.preference_repairs_state)
                        , "4");
                mLlProgress.setVisibility(View.VISIBLE);
            }
        });
        switch (mDetailsBean.getRstate()) {
            case 0:
                mBtnCommit.setText("接单");
                mLlShowEdit.setVisibility(View.GONE);
                SpUtils.putString(mContext, mContext.getString(R.string.preference_repairs_state)
                        , "1");
                break;
            case 1://到达现场
                mRbArrived.setChecked(true);
                mRbRepairing.setEnabled(false);
                mRbRepairComplete.setEnabled(false);
                SpUtils.putString(mContext, mContext.getString(R.string.preference_repairs_state)
                        , "2");
                break;
            case 2://维修中
                mRbArrived.setEnabled(false);
                mRbRepairing.setChecked(true);
                SpUtils.putString(mContext, mContext.getString(R.string.preference_repairs_state)
                        , "3");
                break;
            case 3:
                mRbArrived.setEnabled(false);
                mRbRepairComplete.setChecked(true);
                SpUtils.putString(mContext, mContext.getString(R.string.preference_repairs_state)
                        , "4");
                break;
            case 4://维修完成 将不能再提交申请
                mLlShowEdit.setVisibility(View.GONE);
                mBtnCommit.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                break;
            case 5://已经评论
                mLlShowEdit.setVisibility(View.GONE);
                mBtnCommit.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                itemComment.setVisibility(View.VISIBLE);
                rbLevel.setRating(Float.parseFloat(mDetailsBean.getStar()));
                tvEvaluateIdea.setText(mDetailsBean.getComment());
                break;
        }
    }

    /**
     * 展示网络传来的数据
     */
    private void showNetData() {
        for (MyRepairsDetailsBean.MyRepairBean.ProgressBean progress : mDetailsBean.getProgress()) {
            List<String> progress_pic = new ArrayList<>();
            String imgStr = progress.getImg();
            if (!TextUtils.isEmpty(imgStr)) {
                imgStr = imgStr.substring(imgStr.indexOf("[") + 1, imgStr.lastIndexOf("]"));
                String[] imgUrlArr = imgStr.split(",");
                for (String imgUrl : imgUrlArr) {
                    if (imgUrl.length() > 1) {
                        imgUrl = imgUrl.substring(1, imgUrl.lastIndexOf("\""));
                        progress_pic.add(Constant.HOME + imgUrl);
                    }
                }
            }
            RepairsStateGridImgListAdapter adapter = new RepairsStateGridImgListAdapter(mContext);
            adapter.setUrlList(progress_pic);
            View view = View.inflate(mContext, R.layout.item_my_repairs_state, null);
            ((TextView) view.findViewById(R.id.tv_progress_state)).setText(
                    mContext.getResources().getStringArray(R.array.repairs_state)
                            [Integer.parseInt(progress.getState())]);
            ((TextView) view.findViewById(R.id.tv_progress_time)).setText(TimeUtils.Long2Time
                    (progress.getDate()));
            ((TextView) view.findViewById(R.id.tv_progress_detail)).setText(progress
                    .getDescription());
            ((GridView) view.findViewById(R.id.gv_repairs_img)).setAdapter(adapter);
            ((GridView) view.findViewById(R.id.gv_repairs_img)).setOnItemClickListener((parent,
                                                                                        view1,
                                                                                        position,
                                                                                        id) -> {
                Intent intent = new Intent(MyRepairsDetailsActivity.this, ImageCheckActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("urlList", (ArrayList<String>) progress_pic);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            mLlProgressList.addView(view);
        }

        String imgStr = mDetailsBean.getImg();
        if(!TextUtils.isEmpty(imgStr)) {
            imgStr = imgStr.substring(imgStr.indexOf("[") + 1, imgStr.lastIndexOf("]"));
            String[] imgUrlArr = imgStr.split(",");
            ArrayList<String> picList = new ArrayList<>();

            for (String imgUrl : imgUrlArr) {
                if (imgUrl.length() > 1) {
                    imgUrl = imgUrl.substring(1, imgUrl.lastIndexOf("\""));
                    picList.add(Constant.HOME + imgUrl);
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams
                                    .WRAP_CONTENT);
                    params.height = DensityUtils.dp2px(mContext, 50);
                    params.width = DensityUtils.dp2px(mContext, 50);
                    params.setMargins(0, 0, DensityUtils.dp2px(mContext, 10), 0);
                    imageView.setLayoutParams(params);
                    imageView.setOnClickListener(v -> {
                        Intent intent = new Intent(MyRepairsDetailsActivity.this, ImageCheckActivity
                                .class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("urlList", picList);
                        int imageViewPos = 0;
                        for (int i = 0; i < mLlDescriptionPhotos.getChildCount(); i++) {
                            if (imageView == mLlDescriptionPhotos.getChildAt(i)) imageViewPos = i;
                        }
                        bundle.putInt("position", imageViewPos);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mLlDescriptionPhotos.addView(imageView, mLlDescriptionPhotos.getChildCount());

                    Glide.with(MyRepairsDetailsActivity.this).load(Constant.HOME + imgUrl).centerCrop

                            ().into(imageView);
                }
            }
        }
        mTvProposer.setText(mDetailsBean.getApplyer());
        mTvApplicationTime.setText(TimeUtils.Long2Time(mDetailsBean.getApplyTime()));
        mTvDepartment.setText(mDetailsBean.getDepartment());
        mTvPhoneNum.setText(mDetailsBean.getTel());
        mTvCustomerPhone.setText(mDetailsBean.getCustomerPhone());


        mTvCompany.setText(mDetailsBean.getCompany());
        mTvLine.setText(mDetailsBean.getLineCode());
        mTvDowntime.setText(TimeUtils.Long2Time(mDetailsBean.getStartTime()));
        mTvLocation.setText(mDetailsBean.getAddress());
        mTvFaultType.setText(mContext.getResources().getStringArray(R.array
                .description_presuppose)[Integer.parseInt
                (mDetailsBean.getType())]);

        mTvDescription.setText(mDetailsBean.getDetail());
        mTvRepairsUrgencyLevel.setText(mContext.getResources().getStringArray(R.array
                .urgency_level)[Integer.parseInt(mDetailsBean.getUrgent())]);
        mTvPredictTime.setText(TimeUtils.Long2Time(mDetailsBean.getPredict()));
    }

    /**
     * 添加图片逻辑 展示dialog
     */
    private void addPic() {
        final Dialog dialog = new Dialog(MyRepairsDetailsActivity.this, R.style.BottomDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_pic, null);
        view.findViewById(R.id.takePhoto).setOnClickListener(v -> {
            dialog.dismiss();
            takePhoto();
        });
        view.findViewById(R.id.selectPhoto).setOnClickListener(v -> {
            dialog.dismiss();
            choosePhoto();
        });
        dialog.setContentView(view);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view
                .getLayoutParams();
        layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                        getResources().getDisplayMetrics());
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
        if (ContextCompat.checkSelfPermission(MyRepairsDetailsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyRepairsDetailsActivity.this,
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
            //得到文件绝对路径
            String absolutePath = outputImage.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MyRepairsDetailsActivity.this,
                    mContext.getPackageName() + ".provider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
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
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setClickable(true);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(MyRepairsDetailsActivity.this, ImageCheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("urlList", (ArrayList<String>) mPics);
            int imageViewPos = 0;
            for (int i = 0; i < mLlProgressPhotos.getChildCount(); i++) {
                if (imageView == mLlProgressPhotos.getChildAt(i)) imageViewPos = i;
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
                        mLlProgressPhotos.removeView(v);
                        int pos = 0;
                        for (int j = 0; j < mLlProgressPhotos.getChildCount(); j++) {
                            if (v == mLlProgressPhotos.getChildAt(i)) pos = j;
                        }
                        mPics.remove(pos);
                    }
                }).create().show();
                return true;
            }
        });
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mLlProgressPhotos.addView(imageView, mLlProgressPhotos.getChildCount() - 1);
        Glide.with(MyRepairsDetailsActivity.this).load(file)
                .centerCrop().into(imageView);
    }


    /**
     * 提交进度
     */
    private void submit() {
        AlertDialog.Builder builder = DialogUtils.submit(MyRepairsDetailsActivity.this);
        builder.setNegativeButton("取消", (dialog, which) -> {

        }).setPositiveButton("确定", (dialog, which) -> {
            Map<String, String> map = new HashMap<>();
            // 保修单id
            map.put("repairs_id", mDetailsBean.getId() + "");
            LogUtils.e("mDetailsBean.getId()",mDetailsBean.getId()+"");
            //token
            map.put(mContext.getString(R.string.preference_token), SpUtils.getString(mContext,
                    mContext.getString(R.string.preference_token), ""));
            // 状态
            map.put(mContext.getString(R.string.preference_repairs_state), SpUtils.getString
                    (mContext, mContext.getString(R.string.preference_repairs_state), "0"));
            String str = "";
            // 描述
            if(mRbRepairing.isChecked()){
                if(mEtProgress.getText().toString().equals("")){
                    str = "维修中";
                }else{
                    str = "维修中：" + mEtProgress.getText().toString();
                }
            }else{
                str = mEtProgress.getText().toString();
            }
            map.put("progress", str);

            //封装文件
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

            PostRequest postRequest = NetWorkUtils.getInstance().upLoadRepairsProgress(mContext,
                    files, map);
            final ProgressDialog progress = DialogUtils.progress(MyRepairsDetailsActivity.this,
                    "上传维修进度");
            progress.show();
            postRequest.connTimeOut(1000L).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    progress.dismiss();
                    ToastUtils.makeText(MyRepairsDetailsActivity.this, "提交成功");
                    finish();
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    progress.dismiss();
                    ToastUtils.makeText(MyRepairsDetailsActivity.this, "提交失败，请重试");
                }
            });
        }).show();
    }

    /**
     * 初始化VIew
     */
    private void initView() {
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mEtProgress = (EditText) findViewById(R.id.et_progress);
        mTakePhoto = (ImageButton) findViewById(R.id.btn_take_photo);
        mLlProgressPhotos = (LinearLayout) findViewById(R.id.Ll_progress_photos);
        mLlDescriptionPhotos = (LinearLayout) findViewById(R.id.Ll_photos);

        mTvProposer = (TextView) findViewById(R.id.tv_proposer);
        mTvApplicationTime = (TextView) findViewById(R.id.tv_application_time);
        mTvDepartment = (TextView) findViewById(R.id.tv_department);
        mTvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);
        mTvCustomerPhone = (TextView)findViewById(R.id.tv_customer_num);
        mTvCompany = (TextView) findViewById(R.id.tv_company);
        mTvLine = (TextView) findViewById(R.id.tv_line);
        mTvDowntime = (TextView) findViewById(R.id.tv_downtime);
        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mTvFaultType = (TextView) findViewById(R.id.sp_fault_type);
        mTvDescription = (TextView) findViewById(R.id.tv_description);
        mTvRepairsUrgencyLevel = (TextView) findViewById(R.id.tv_repairs_urgency_level);
        mTvPredictTime = (TextView) findViewById(R.id.tv_predict_time);

        mRgRepairsState = (RadioGroup) findViewById(R.id.rg_repairs_state);
        mRbRepairing = (AppCompatRadioButton) findViewById(R.id.rb_repairing);
        mRbRepairComplete = (AppCompatRadioButton) findViewById(R.id.rb_repair_complete);
        mRbArrived = (AppCompatRadioButton) findViewById(R.id.rb_arrived);
        mLlProgress = (LinearLayout) findViewById(R.id.ll_progress);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mLlShowEdit = (LinearLayout) findViewById(R.id.ll_show_edit);
        mLlProgressList = (LinearLayout) findViewById(R.id.ll_progress_list);

        //评论框
        itemComment = findViewById(R.id.item_comment);
        rbLevel = (RatingBar) findViewById(R.id.rb_comment_star);
        tvEvaluateIdea = (TextView) findViewById(R.id.tv_comment);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        setSupportActionBar(toolbar);
        title.setText("维修单详情");
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setText("保存");
        save.setOnClickListener(v -> {
            finish();
            // TODO: 2017/4/8 保存逻辑 用维修单号做key
        });
    }


    /**
     * 返回按钮
     *
     * @param item 点击的item
     * @return 是否拦截事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
