package hdo.com.andzq.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import hdo.com.andzq.R;
import hdo.com.andzq.activity.AboutActivity;
import hdo.com.andzq.activity.ComplainListActivity;
import hdo.com.andzq.activity.CustomerDataActivity;
import hdo.com.andzq.activity.LoginActivity;
import hdo.com.andzq.activity.MyDataActivity;
import hdo.com.andzq.activity.MyMonthlyActivity;
import hdo.com.andzq.activity.MyProductActivity;
import hdo.com.andzq.activity.RepairsListActivity;
import hdo.com.andzq.base.BaseFragment;
import hdo.com.andzq.global.Constant;
import hdo.com.andzq.utils.SpUtils;
import hdo.com.andzq.utils.ToastUtils;
import hdo.com.andzq.utils.okhttp.NetWorkUtils;
import hdo.com.andzq.view.MineView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * description 我的Fragment
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 * modify 我的页面的显示布局 by 邓杰 on 2017/4/1
 */

public class MineFragment extends BaseFragment {
    /**
     * 我的资料
     */
    private MineView mvMineData;
    /**
     * 我的报修
     */
    private MineView mvMineRepairs;
    /**
     * 我的投诉
     */
    private MineView mvMineCompain;
    /**
     * 我的进程
     */
    private MineView mvMineSchedule;
    /**
     * 我的产品按钮
     */
    private MineView mvMineProduct;
    private MineView mvMineMonthly;

    private MineView mvMineAbout; //关于我们按钮
    private MineView mvMineExit; // 退出登录
    /**
     * 角色
     */
    private int role;
    private MineView mvChangePassword;
    Handler handler = new Handler();


    @Override
    public View initView(ViewGroup container) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_mine, container,
                false);
        //初始化控件
        iniView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        role = Integer.parseInt(SpUtils.getString(mActivity, mActivity.getString(R.string
                .preference_role_id), "2"));
        switch (role) {
            case 3://维修人员
                mvMineCompain.setVisibility(View.GONE);
                mvMineRepairs.setVisibility(View.GONE);
                mvMineSchedule.setVisibility(View.GONE);
                mvMineProduct.setVisibility(View.GONE);
                mvMineMonthly.setVisibility(View.GONE);
        }
        mvMineData.setMineTitle("我的资料");
        mvMineCompain.setMineTitle("我的投诉");
        mvMineRepairs.setMineTitle("我的报修");
        mvMineSchedule.setMineTitle("我的新建");
        mvMineProduct.setMineTitle("我的产品");
        mvMineMonthly.setMineTitle("我的月报");
        mvChangePassword.setMineTitle("修改密码");
        mvMineAbout.setMineTitle("关于我们");
        mvMineExit.setMineTitle("退出应用");
        //设置监听,跳转投诉结果页面
        mvMineCompain.setOc(new MineView.onLayoutClick() {
            @Override
            public void onViewClick(View v) {
                toActivity(mActivity, ComplainListActivity.class);
            }
        });
//        //跳转我的资料页面
//        if (role == 3){
//            mvMineData.setOc(new MineView.onLayoutClick() {
//                @Override
//                public void onViewClick(View v) {
//                    toActivity(mActivity, RepairDataActivity.class);
//                }
//            });
//        }else {
//            mvMineData.setOc(new MineView.onLayoutClick() {
//                @Override
//                public void onViewClick(View v) {
//                    toActivity(mActivity, MyDataActivity.class);
//                }
//            });
//        }


        //跳转我的资料页面1位普通客户
        if (role == 1){
            mvMineData.setOc(new MineView.onLayoutClick() {
                @Override
                public void onViewClick(View v) {
                    toActivity(mActivity, CustomerDataActivity.class);   //CustomerDataActivity
                }
            });
        }else {
            mvMineData.setOc(new MineView.onLayoutClick() {
                @Override
                public void onViewClick(View v) {
                    toActivity(mActivity, MyDataActivity.class);     //ManagerDataActivity内部人员
                }
            });
        }

        //跳转我的保修结果页面
        mvMineRepairs.setOc(new MineView.onLayoutClick() {
            @Override
            public void onViewClick(View v) {
                toActivity(mActivity, RepairsListActivity.class);
            }
        });
        //跳转到我的新建页面
        mvMineSchedule.setOc(new MineView.onLayoutClick() {
            @Override
            public void onViewClick(View v) {
                ToastUtils.makeText(getActivity(),"您的账户没有该权限！");
                //toActivity(mActivity, NewListActivity.class);
            }
        });
        mvMineProduct.setOc(new MineView.onLayoutClick() {
            @Override
            public void onViewClick(View v) {
                toActivity(mActivity, MyProductActivity.class);
            }
        });
        mvMineMonthly.setOc(new MineView.onLayoutClick() {
            @Override
            public void onViewClick(View v) {
                toActivity(mActivity, MyMonthlyActivity.class);
            }
        });
        mvChangePassword.setOc(this::showChangePswDialog);//显示修改密码对话框
        mvMineAbout.setOc(this::toAboutActivity);//跳转关于页面
        mvMineExit.setOc(this::exitLogin);//退出应用
    }

    /**
     * 退出应用
     */
    private void exitLogin(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示")
                .setMessage("确定要退出吗")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", (dialog, which) -> {
                    //清除 token
                    SpUtils.putString(mActivity,mActivity.getString(R.string.preference_token),"");
                    toActivity(mActivity,LoginActivity.class);
                    mActivity.finish();
                })
                .show();
    }
    /**
     * 跳转到关于Activity
     */
    private void toAboutActivity(View v){
        toActivity(mActivity, AboutActivity.class);
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePswDialog(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_change_psw, null);

        EditText etOldPsw = (EditText) view.findViewById(R.id.et_old_psw);
        EditText etNewPsw1 = (EditText) view.findViewById(R.id.et_new_psw_1);
        EditText etNewPsw2 = (EditText) view.findViewById(R.id.et_new_psw_2);
        dialog.setView(view);
        dialog
                .setNegativeButton("取消", (dialog1, which) -> {

                })
                .setPositiveButton("修改", (dialog12, which) -> {
                    String oldPsw = etOldPsw.getText().toString();
                    //检查两个新密码是否一致
                    String newPsw1 = etNewPsw1.getText().toString();
                    String newPsw2 = etNewPsw2.getText().toString();
                    if (TextUtils.isEmpty(oldPsw) | TextUtils.isEmpty(newPsw1)) {
                        mActivity.runOnUiThread(() -> Snackbar.make(mvChangePassword, "输入信息不完整",
                                Snackbar.LENGTH_SHORT).setAction("知道了", v1 -> {
                        }).show());
                    } else if (newPsw1.equals(newPsw2)) {//密码一致 请求网络
                        Map<String, String> map = new HashMap<>();
                        map.put("old_psw", oldPsw);
                        map.put("new_psw", newPsw1);
                        startChangePsw(map);
                    } else {//两次密码不一致 弹出提示
                        mActivity.runOnUiThread(() -> Snackbar.make(mvChangePassword, "两次密码不一致",
                                Snackbar.LENGTH_SHORT).setAction("知道了", v1 -> {
                        }).show());
                    }
                }).show();
    }

    private void startChangePsw(Map<String, String> map) {
        ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setTitle("修改密码中...");
        progressDialog.setMessage("请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        NetWorkUtils.getInstance().changePsw(mActivity, map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        progressDialog.dismiss();
                        if (s.contains(Constant.STATE_SUCCESS)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                            dialog
                                    .setTitle("提示")
                                    .setMessage("密码已更改,即将重新登录")
                                    .setCancelable(false)
                                    .show();
                            handler.postDelayed(() -> {
                                startActivity(new Intent(mActivity, LoginActivity.class));
                                mActivity.finish();
                            },2000);
                        }else if(s.contains(Constant.STATE_ERR)){
                            mActivity.runOnUiThread(() -> Snackbar.make(mvChangePassword,
                                    "密码错误,密码修改失败", Snackbar.LENGTH_LONG).setAction
                                    ("知道了", v1 -> {}).show());
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        progressDialog.dismiss();
                            mActivity.runOnUiThread(() -> Snackbar.make(mvChangePassword, "网络连接失败", Snackbar.LENGTH_SHORT).setAction("知道了", v1 -> {}).show());
                    }
                });
    }

    @Override
    public void initData() {

    }

    /**
     * 初始化控件
     *
     * @param view 当前布局
     */
    private void iniView(View view) {
        mvMineData = (MineView) view.findViewById(R.id.mv_mine_data);
        mvMineRepairs = (MineView) view.findViewById(R.id.mv_mine_repairs);
        mvMineCompain = (MineView) view.findViewById(R.id.mv_mine_compain);
        mvMineSchedule = (MineView) view.findViewById(R.id.mv_mine_schedule);
        mvMineProduct = (MineView) view.findViewById(R.id.mv_mine_product);
        mvMineMonthly = (MineView) view.findViewById(R.id.mv_mine_monthly);
        mvChangePassword = (MineView) view.findViewById(R.id.mv_change_password);

        mvMineAbout = (MineView) view.findViewById(R.id.mv_mine_about);

        mvMineExit = (MineView) view.findViewById(R.id.mv_mine_exit);
    }
}
