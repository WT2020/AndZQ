package hdo.com.andzq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hdo.com.andzq.base.BaseActivity;
import hdo.com.andzq.R;
import hdo.com.andzq.adapter.EvaluateRecylerViewAdapter;
import hdo.com.andzq.bean.ComplainResultBean;
import hdo.com.andzq.bean.EvaluateBean;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.LogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.utils.okhttp.UICallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 投诉的处理结果的Activity
 * author 邓杰
 * version 1.0
 * Created by admin on 2017/4/1.
 * modified on 2017/10/17 by 张建银 已评价时处理描述的设置
 */
public class ComplainResultActivity extends BaseActivity {
    /**
     * 投诉原因
     */
    private EditText etComplainReason;
    /**
     * 投诉结果
     */
    private EditText etComplainResult;
    private Toolbar toolbar;
    /**
     * 标题
     */
    private TextView toolbarTitle;
    /**
     * 保存按钮
     */
    private TextView toolbarSave;
    /**
     * 处理状态
     */
    private TextView tvDealState;
    /**
     * 投诉实体类
     */
    private ComplainResultBean.MyComplainBean b;
    private TextView tvCompanyName;
    private TextView tvCompanyPerson;
    private TextView tvCompanyTel;
    private TextView tvComplainResultType;
    private TextView tvDealCompany;
    private TextView tvComplainResultCompany;
    /**
     * 星级评价
     */
    private RatingBar rbLevel;
    /**
     * 选择列表
     */
    private RecyclerView rvEvaluateList;
    /**
     * 适配器
     */
    private EvaluateRecylerViewAdapter ecaAdapter;
    /**
     * 评价建议
     */
    private EditText etEvaluateIdea;
    /**
     * 数据源 评价好
     */
    private List<EvaluateBean> list;
    /**
     * 评价差
     */
    private List<EvaluateBean> badList;
    private Button btnCancel;
    /**
     * 提交
     */
    private Button btnCommit;
    private Context mContext;
    private TextView tvDealPerson;
    private TextView tvDealTime;
    private RatingBar rbCommentStar;
    private TextView tvComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_result);
        mContext = this;
        //初始化控件
        initView();
        setSupportActionBar(toolbar);
        //设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //设置控件
        setView();
        iniData();//初始化数据
        loadData();//加载数据
        ecaAdapter.setList(list);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rvEvaluateList.setLayoutManager(manager);
        rvEvaluateList.setAdapter(ecaAdapter);
        rbLevel.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating >= 4) {//评价好
                handler.sendEmptyMessage(0);
            } else { //评价差
                handler.sendEmptyMessage(1);
            }
        });
        btnCommit.setOnClickListener(this::submitData);
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadData() {
        list.clear();
        list.add(new EvaluateBean("服务不错", false));
        list.add(new EvaluateBean("速度很快", false));
        list.add(new EvaluateBean("技术不错", false));
        list.add(new EvaluateBean("态度很好", false));
        badList.clear();
        badList.add(new EvaluateBean("服务很差", false));
        badList.add(new EvaluateBean("速度太慢", false));
        badList.add(new EvaluateBean("技术太差", false));
        badList.add(new EvaluateBean("态度恶劣", false));
    }

    /**
     * 初始化数据
     */
    private void iniData() {
        list = new ArrayList<>();
        badList = new ArrayList<>();
        ecaAdapter = new EvaluateRecylerViewAdapter(this);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            loadData();
            switch (msg.what) {
                case 0:
                    ecaAdapter.setList(list);
                    break;
                case 1:
                    ecaAdapter.setList(badList);
                    break;
            }
            ecaAdapter.notifyDataSetChanged();//刷新
            return false;
        }
    });

    //设置返回按钮
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
        Intent intent = getIntent();
        b = (ComplainResultBean.MyComplainBean) intent.getSerializableExtra("MyComplainBean");
        toolbarTitle.setText("处理详情"); //设置标题
        toolbarSave.setVisibility(View.GONE);
        tvCompanyName.setText(b.getPCName());//集团名称
        tvCompanyPerson.setText(b.getPPerson());
        tvCompanyTel.setText(SpUtils.getString(mContext, mContext.getString(R.string
                .preference_people_phone), "未知"));//sp取 取不到显示未知
        tvComplainResultType.setText(mContext.getResources().getStringArray(R.array
                .complain_type)[Integer.parseInt(b.getPType())]);//投诉类型
        tvComplainResultCompany.setText(b.getPObj());//投诉对象
        etComplainReason.setText(b.getPDetail().length() > 0 ? b.getPDetail() : "暂无描述");//投诉详情
        etComplainReason.setKeyListener(null); //设置不可编辑
        etComplainResult.setKeyListener(null); //设置不可编辑
        tvDealPerson.setText(b.getPManager().length() > 0 ? b.getPManager() : "暂无");
        tvDealTime.setText(TimeUtils.Long2Time(b.getPTime()));
        if (b.getPState().equals("0")) {
            etComplainResult.setText("请耐心等待工作人员处理");
            tvDealState.setText("未处理"); //设置状态

            btnCancel.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            findViewById(R.id.item_evaluate).setVisibility(View.GONE);
        } else if (b.getPState().equals("1")) {
            etComplainResult.setText(b.getPMark().length() > 0 ? b.getPMark() : "已收到投诉，正在处理");
            tvDealState.setText("已处理"); //设置状态
        } else if (b.getPState().equals("2")) {
            findViewById(R.id.item_evaluate).setVisibility(View.GONE);
            findViewById(R.id.item_comment).setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            etComplainResult.setText(b.getPMark().length() > 0 ? b.getPMark() : "已处理");
            if(!TextUtils.isEmpty(b.getpStar())) rbCommentStar.setRating(Float.parseFloat(b.getpStar()));
            tvComment.setText(b.getpCom());
            LogUtils.e("getpCom",b.getpCom());
        }
    }

    /**
     * 提交评论
     */
    private void submitData(View v) {
        //确定对话框 展示进度对话框
        DialogUtils.submit(ComplainResultActivity.this).setPositiveButton("确定", (dialog, which) -> {
            final ProgressDialog progressDialog = DialogUtils.progress
                    (ComplainResultActivity.this, "是否提交评价？");
            progressDialog.show();
            List<EvaluateBean> postList = ecaAdapter.getList();
            StringBuffer sb = new StringBuffer();
            for (EvaluateBean eb : postList) {
                if (eb.isCheck()) {
                    sb.append(eb.getComment() + ",");
                }
            }
            sb.append(etEvaluateIdea.getText().toString());
            Call call = NetWorkUtils.getInstance().postComplaintEvaluate(ComplainResultActivity
                            .this,
                    b.getCode() + "", (int) rbLevel.getRating() + ""
                    , sb.toString().length() > 0 ? sb.toString() : "服务很好");
            call.enqueue(new UICallBack() {
                @Override
                public void UIonFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    SnackbarUtils.uploadErr(ComplainResultActivity.this, etEvaluateIdea);
                }

                @Override
                public void UIonResponse(Call call, Response response) throws IOException {
                    //成功上传 提示
                    progressDialog.dismiss();
                    String result = response.body().string();
                    Log.e("msg", "UIonResponse: " + result);
                    JSONObject js;
                    try {
                        js = new JSONObject(result);
                        String state = js.getString("RESP_STATE");
                        if (state.equals("STATE_SUCCESS")) {
                            SnackbarUtils.uploadSucc(ComplainResultActivity.this, etEvaluateIdea);
                            finish();
                        } else SnackbarUtils.uploadErr(ComplainResultActivity.this, etEvaluateIdea);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).show();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        etComplainReason = (EditText) findViewById(R.id.et_complain_reason);
        etComplainResult = (EditText) findViewById(R.id.et_complain_result);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarSave = (TextView) findViewById(R.id.toolbar_save);
        tvDealState = (TextView) findViewById(R.id.tv_deal_state);
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvCompanyPerson = (TextView) findViewById(R.id.tv_company_person);
        tvCompanyTel = (TextView) findViewById(R.id.tv_company_tel);
        tvComplainResultType = (TextView) findViewById(R.id.tv_complain_result_type);
        tvDealCompany = (TextView) findViewById(R.id.tv_deal_company);
        tvComplainResultCompany = (TextView) findViewById(R.id.tv_complain_result_company);
        tvDealPerson = (TextView) findViewById(R.id.tv_deal_person);
        tvDealTime = (TextView) findViewById(R.id.tv_deal_time);
        rbLevel = (RatingBar) findViewById(R.id.rb_level);
        rvEvaluateList = (RecyclerView) findViewById(R.id.rv_evaluate_list);
        etEvaluateIdea = (EditText) findViewById(R.id.et_evaluate_idea);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        rbCommentStar = (RatingBar) findViewById(R.id.rb_comment_star);
        tvComment = (TextView) findViewById(R.id.tv_comment);
    }
}
