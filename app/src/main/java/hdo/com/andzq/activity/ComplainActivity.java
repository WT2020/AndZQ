package hdo.com.andzq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import java.io.IOException;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.MySpinnerAdapter;
import hdo.com.andzq.bean.ComplainInfoBean;
import hdo.com.andzq.bean.RepairsResponseBean;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.ExamineUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * description 投诉的Activity
 * author 邓杰
 * version 1.0
 * Created 2017/3/29.
 */
public class ComplainActivity extends BaseActivity {
    /**
     * 集团名称
     */
    private TextView tvCompanyName;
    /**
     * 集团投诉人
     */
    private TextView tvCompanyPerson;
    /**
     * 投诉人电话
     */
    private TextView tvCompanyTel;
    /**
     * 投诉类型
     */
    private Spinner spType;
    /**
     * 处理部门
     */
    private TextView tvDealCompany;
    /**
     * 投诉对象
     */
    private Spinner spComplainCompany;
    /**
     * 投诉详情
     */
    private EditText etComplainReason;
    /**
     * 取消
     */
    private Button btnCancel;
    /**
     * 确定
     */
    private Button btnCommit;
    /**
     * Toolbar
     */
    private android.support.v7.widget.Toolbar toolbar;
    /**
     * 标题
     */
    private TextView toolbarTitle;
    /**
     * 保存
     */
    private TextView toolbarSave;
    /**
     * 选择投诉的公司
     */
    private String selectCompany;
    /**
     * 选择公司的位置
     */
    private int companyPosition;
    /**
     * 选择投诉的类型
     */
    private String selectType;
    /**
     * 选择类型的位置
     */
    private int typePosition;
    /**
     * 输入的文本内容
     */
    private Context mContext;//上下文
    private String etSave = "";
    private String[] mComplainCompany;
    private String[] mType;
    private Handler handler = new Handler();// handler对象
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        mContext = this;
        //初始化控件
        initView();
        setSupportActionBar(toolbar);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        initData();
        //设置控件
        setView();
        initSpinner();
        //读取本地内容
        readData();
        //读取网络信息
        readNetData();
    }

    /**
     * 加载网络内容
     */
    private void readNetData() {
        Call call = NetWorkUtils.getInstance().loadRepairsInfo(mContext);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();
                if(ExamineUtils.isTokenErr(string)){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ComplainActivity.this);
                    SpUtils.putString(ComplainActivity.this,mContext.getString(R.string.preference_token),"");
                    dialog
                            .setTitle("提示")
                            .setMessage("登录已经失效、即将重新登录")
                            .setCancelable(false);
                    runOnUiThread(dialog::show);
                    handler.postDelayed(() -> {
                        startActivity(new Intent(ComplainActivity.this, LoginActivity.class));
                        ComplainActivity.this.finish();
                    },2000);
                }else if (ExamineUtils.isStateErr(string)){
                    ToastUtils.makeText(ComplainActivity.this,"获取集团信息失败！");
                    tvCompanyName.setText("");
                    tvCompanyPerson.setText("");
                    tvCompanyTel.setText("");
                }else {
                    Gson gson = new Gson();
                    RepairsResponseBean bean = gson.fromJson(string, RepairsResponseBean.class);
                    runOnUiThread(()->{
                        tvCompanyName.setText(bean.getCompany());
                        tvCompanyPerson.setText(bean.getName());
                        tvCompanyTel.setText(bean.getTel());
                    });
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mComplainCompany = getResources().getStringArray(R.array.complain_company);
        mType = getResources().getStringArray(R.array.complain_type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置控件
     */
    private void setView() {
        toolbarTitle.setText("投诉建议");
        //取消按钮 返回
        btnCancel.setOnClickListener(v -> finish());
        //确定按钮 提交数据
        btnCommit.setOnClickListener(v -> submitData());
        //保存
        toolbarSave.setOnClickListener(v -> {
            saveData();
            finish();
        });
    }

    /**
     * 提交数据
     */
    private void submitData() {
        //确定对话框
        DialogUtils.submit(ComplainActivity.this).setPositiveButton("确定", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                if (tvCompanyName.getText().toString().equals("")){
                    ToastUtils.makeText(ComplainActivity.this,"没有集团存在，您无法提交投诉！");
                }else {

                    //获取已经填过的 bean
                    ComplainInfoBean bean = getFilledInfoBean();
                    //构建 网络请求request
                    PostRequest request = NetWorkUtils.getInstance()
                            .upLoadComplain(mContext, bean.getMap());
                    //展示进度对话框
                    final ProgressDialog progressDialog = DialogUtils.progress(ComplainActivity.this,
                            "上传投诉建议");
                    progressDialog.show();
                    request.execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("msg", "onSuccess: "+s);
                            //成功上传 提示
                            progressDialog.dismiss();
                            ToastUtils.makeText(ComplainActivity.this, "投诉成功");
                            clearData();
                            finish();
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            progressDialog.dismiss();
                            ToastUtils.makeText(ComplainActivity.this, "提交失败,请重试");
                        }
                    });
                }
            }
        }).show();
    }

    /**
     * 获取 填入数据后的InfoBean
     */
    private ComplainInfoBean getFilledInfoBean() {
        ComplainInfoBean bean = new ComplainInfoBean();
        int com=spComplainCompany.getSelectedItemPosition();
        bean.setTarget(com+"");
        int type=spType.getSelectedItemPosition();
        bean.setType(type+"");
        bean.setDescription(etComplainReason.getText().toString());
        return bean;
    }

    /**
     * 读取内容
     */
    private void readData() {
        etSave = SpUtils.getString(this, "reason", null);
        companyPosition = SpUtils.getInt(this, "company", 1);
        typePosition = SpUtils.getInt(this, "type", 1);
        if (etSave != null) {
            etComplainReason.setText(etSave);
        }
        spComplainCompany.setSelection(companyPosition);
        spType.setSelection(typePosition);
    }

    /**
     * 清空数据
     */
    private void clearData() {
        etSave = "";
        companyPosition = 0;
        typePosition = 0;

        //保存到本地
        SpUtils.putString(this, "reason", null);
        SpUtils.putInt(this, "company", companyPosition);
        SpUtils.putInt(this, "type", typePosition);
        etComplainReason.setText("");
        spComplainCompany.setSelection(companyPosition);
        spType.setSelection(typePosition);
    }

    /**
     * 保存功能
     */
    private void saveData() {
        //文本的内容
        etSave = etComplainReason.getText().toString();
        //保存到本地
        SpUtils.putString(this, "reason", etSave);
        SpUtils.putInt(this, "company", companyPosition);
        SpUtils.putInt(this, "type", typePosition);
        makToast("保存成功");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvCompanyPerson = (TextView) findViewById(R.id.tv_company_person);
        tvCompanyTel = (TextView) findViewById(R.id.tv_company_tel);
        spType = (Spinner) findViewById(R.id.sp_complain_type);
        tvDealCompany = (TextView) findViewById(R.id.tv_deal_company);
        spComplainCompany = (Spinner) findViewById(R.id.sp_complain_company);
        etComplainReason = (EditText) findViewById(R.id.et_complain_reason);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCommit = (Button) findViewById(R.id.btn_commit);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
    }

    /**
     * 设置spinner
     */
    private void initSpinner() {
        //拿到数组资源
        final String[] compy = getResources().getStringArray(R.array.complain_company);
        final String[] type = getResources().getStringArray(R.array.complain_type);
        //使用自定义的ArrayAdapter
        MySpinnerAdapter spCC = new MySpinnerAdapter(ComplainActivity.this, compy);
        MySpinnerAdapter typeAdapter = new MySpinnerAdapter(ComplainActivity.this, type);
        //设置适配器
        spComplainCompany.setAdapter(spCC);
        spType.setAdapter(typeAdapter);
        //监听选择状态
        spComplainCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCompany = compy[position];
                companyPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                makToast(type[position]);
                selectType = type[position];
                typePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * toast方法
     */
    private void makToast(String str) {
        Toast.makeText(ComplainActivity.this, str + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存数据(异常状态)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        etSave = etComplainReason.getText().toString();
        outState.putString("et", etSave);
        outState.putInt("com", companyPosition);
        outState.putInt("type", typePosition);

    }

    /**
     * 读取数据(异常状态)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etSave = savedInstanceState.getString("et");
        companyPosition = savedInstanceState.getInt("com");
        typePosition = savedInstanceState.getInt("type");
        etComplainReason.setText(etSave);
        spComplainCompany.setSelection(companyPosition);
        spType.setSelection(typePosition);
    }
}
