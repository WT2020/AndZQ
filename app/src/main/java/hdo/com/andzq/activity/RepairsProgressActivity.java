package hdo.com.andzq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import hdo.com.andzq.adapter.RepairsStateListAdapter;
import hdo.com.andzq.bean.EvaluateBean;
import hdo.com.andzq.bean.MyRepairsListBean;
import hdo.com.andzq.bean.RepairsStateBean;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.DialogUtils;
import hdo.com.andzq.utils.SnackbarUtils;
import hdo.com.andzq.utils.TimeUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.utils.okhttp.UICallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 维修进度详情Activity
 * author 陈锐
 * version 1.0
 * created 2017/4/6
 * modify by 邓杰 2017/4/19
 * modified by 张建银 on 2017/10/17 修复7.0无图片问题，界面布局问题
 * modified by 张建银 on 2017/10/18 报修单添加已归档处理
 */

public class RepairsProgressActivity extends BaseActivity {
    private Context mContext;
    private ListView mLvProgress;
    private int role = 1;
    /**
     * 列表
     */
    private ListView lvRepairsState;
    /**
     * list的adapter
     */
    private RepairsStateListAdapter adapter;
    /**
     * 数据源
     */
    private List<RepairsStateBean> list;
    /**
     * 评论的布局
     */
//    private LinearLayout llEvaluate;
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
    private List<EvaluateBean> goodList;
    /**
     * 评价差
     */
    private List<EvaluateBean> badList;
    /**
     * 按钮的布局
     */
    private LinearLayout llBtn;
    private Button btnCancel;
    /**
     * 提交
     */
    private Button btnCommit;
    /**
     * 实体类
     */
    private MyRepairsListBean.MyRepairBean bean;
    private View footView;
    private LinearLayout footViewParent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (role != 1) {//测试的

        } else {
            setContentView(R.layout.activity_repairs_state);
        }
        mContext = this;
        initView();
        initToolbar();

        goodList = new ArrayList<>();
        badList = new ArrayList<>();
        ecaAdapter = new EvaluateRecylerViewAdapter(this);

        list = new ArrayList<>();
        adapter = new RepairsStateListAdapter(mContext);
        //点击图片跳转
        adapter.setOc((position, itemPosition) -> {
            Intent intent = new Intent(RepairsProgressActivity.this, ImageCheckActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("urlList", (ArrayList<String>) list.get(itemPosition).getImgUrl());
            bundle.putInt("position", position);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        adapter.setList(list);
        //加载底部评价
        initFootView();
        lvRepairsState.addFooterView(footViewParent);
        lvRepairsState.setAdapter(adapter);

        //加载评论的数据
        loadEvaData();


        //取消按钮
        btnCancel.setOnClickListener(v -> finish());
        //确认提交
        btnCommit.setOnClickListener(v -> submitData());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
//        setListHeight();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            loadEvaData();
            switch (msg.what) {
                case 0:
                    ecaAdapter.setList(goodList);
                    break;
                case 1:
                    ecaAdapter.setList(badList);
                    break;
                case 2:
                    int rstate = (int) msg.obj;
                    switch (rstate){
                        case 1:
                        case 2:
                        case 3:
                            llBtn.setVisibility(View.GONE);
                            footView.setVisibility(View.GONE);
                            break;
                        case 4://维修完成
                            llBtn.setVisibility(View.VISIBLE);
                            footView.setVisibility(View.VISIBLE);
                            break;
                        case 5://已评论
                        case 6:
                            llBtn.setVisibility(View.GONE);
                            footView.setVisibility(View.VISIBLE);
                            rvEvaluateList.setVisibility(View.GONE);
                            if(!bean.getStar().isEmpty()){
                                rbLevel.setRating(Float.parseFloat(bean.getStar()));
                            }
                            rbLevel.setIsIndicator(true);
                            etEvaluateIdea.setText(bean.getComment());
                            etEvaluateIdea.setKeyListener(null);
                            break;
                    }
                    break;
            }
            //刷新
            ecaAdapter.notifyDataSetChanged();
            return false;
        }
    });

    private void loadEvaData() {
        goodList.clear();
        goodList.add(new EvaluateBean("服务不错", false));
        goodList.add(new EvaluateBean("速度很快", false));
        goodList.add(new EvaluateBean("技术不错", false));
        goodList.add(new EvaluateBean("态度很好", false));
        badList.clear();
        badList.add(new EvaluateBean("服务很差", false));
        badList.add(new EvaluateBean("速度太慢", false));
        badList.add(new EvaluateBean("技术太差", false));
        badList.add(new EvaluateBean("态度恶劣", false));
    }

    /**
     * 加载数据
     */
    private void loadData() {
        list.clear();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            bean = (MyRepairsListBean.MyRepairBean) bundle.getSerializable("progress");
            List<MyRepairsListBean.MyRepairBean.ProgressBean> listBean = bean.getProgress();
            Log.e("msg", "loadData: " + listBean.get(0).getDescription());
            //向handler发送数据，显示隐藏布局
            Message msg = new Message();
            msg.obj = bean.getRstate();
            msg.what = 2;
            handler.sendMessage(msg);
            //解析图片地址
            if (listBean != null) {
                for (int i = 0; i < listBean.size(); i++) {
                    MyRepairsListBean.MyRepairBean.ProgressBean progressBean = listBean.get(i);
                    List<String> url = new ArrayList<>();
                    //c从字符串解析成地址
                    String imgUrl = progressBean.getImg();
                    if (!TextUtils.isEmpty(imgUrl)) {
                        imgUrl = imgUrl.substring(imgUrl.indexOf("[") + 1, imgUrl.lastIndexOf("]"));
                        String[] urlList = imgUrl.split(",");
                        for (String str : urlList) {
                            if (str.length() > 1) {
                                str = str.substring(1, str.lastIndexOf("\""));
                                url.add(Constant.HOME + str);
                            }
                        }
                    }
                    list.add(new RepairsStateBean(progressBean.getState(), "" + TimeUtils
                            .Long2Time(progressBean.getDate()), progressBean.getDescription(),
                            url));
                }
            }
        }
       adapter.notifyDataSetChanged();
    }


    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView save = (TextView) findViewById(R.id.toolbar_save);
        title.setText("维修详情");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        save.setVisibility(View.GONE);
    }

    private void setListHeight() {
        ListAdapter listAdapter = lvRepairsState.getAdapter();
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, lvRepairsState);
            listItem.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams
                    .WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lvRepairsState.getLayoutParams();
        params.height = totalHeight + (lvRepairsState.getDividerHeight() * (listAdapter.getCount
                () - 1));
        lvRepairsState.setLayoutParams(params);
    }

    /**
     * 提交数据
     */
    private void submitData() {
        //确定对话框 展示进度对话框
        DialogUtils.submit(RepairsProgressActivity.this).setPositiveButton("确定", (dialog, which) -> {
            final ProgressDialog progressDialog = DialogUtils.progress
                    (RepairsProgressActivity.this, "是否提交评价？");
            progressDialog.show();
            List<EvaluateBean> postList = ecaAdapter.getList();
            StringBuffer sb = new StringBuffer();
            for (EvaluateBean eb : postList) {
                if (eb.isCheck()) {
                    sb.append(eb.getComment() + ",");
                }
            }
            sb.append(etEvaluateIdea.getText().toString());
            Call call = NetWorkUtils.getInstance().postRepairsEvaluate(RepairsProgressActivity.this,
                    bean.getId() + "", (int) rbLevel.getRating() + ""
                    , sb.toString().length() > 0 ? sb.toString() : "服务很好");
            call.enqueue(new UICallBack() {
                @Override
                public void UIonFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    SnackbarUtils.uploadErr(RepairsProgressActivity.this, etEvaluateIdea);
                }

                @Override
                public void UIonResponse(Call call, Response response) throws IOException {
                    //成功上传 提示
                    progressDialog.dismiss();
                    String result = response.body().string();
                    Log.e("msg", "UIonResponse: " + result);
                    JSONObject js = null;
                    try {
                        js = new JSONObject(result);
                        String state = js.getString("RESP_STATE");
                        if (state.equals("STATE_SUCCESS")) {
                            SnackbarUtils.uploadSucc(RepairsProgressActivity.this, etEvaluateIdea);
                            finish();
                        } else SnackbarUtils.uploadErr(RepairsProgressActivity.this, etEvaluateIdea);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).show();
    }

    /**
     * toolbar 按钮回调
     *
     * @param item item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private void initView() {
        mLvProgress = (ListView) findViewById(R.id.lv_progress);
        lvRepairsState = (ListView) findViewById(R.id.lv_repairs_state);
//        llEvaluate = (LinearLayout) findViewById(R.id.ll_evaluate);
        llBtn = (LinearLayout) findViewById(R.id.ll_btn);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCommit = (Button) findViewById(R.id.btn_commit);
    }

    private void initFootView() {
        footView = LayoutInflater.from(this).inflate(R.layout.item_evaluate, lvRepairsState, false);
        footViewParent = new LinearLayout(this);
        footViewParent.addView(footView);
        rbLevel = (RatingBar) footView.findViewById(R.id.rb_level);
        rvEvaluateList = (RecyclerView) footView.findViewById(R.id.rv_evaluate_list);
        etEvaluateIdea = (EditText) footView.findViewById(R.id.et_evaluate_idea);

        //评论
        ecaAdapter.setList(goodList);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rvEvaluateList.setLayoutManager(manager);
        rvEvaluateList.setAdapter(ecaAdapter);
        //选择星级
        rbLevel.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating >= 4) {//评价好
                handler.sendEmptyMessage(0);
            } else { //评价差
                handler.sendEmptyMessage(1);
            }
        });
    }
}
