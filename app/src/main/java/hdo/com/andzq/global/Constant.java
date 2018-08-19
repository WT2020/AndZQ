package hdo.com.andzq.global;

/**
 * description 全局常量类
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 */

public class Constant {
    /**
     * 服务器URL http://192.168.0.129:8080/AndZQ/repair/AppReport
     */
    //public final static String HOME = "http://192.168.43.9:8080/AndZQ/";//本地服务器
//    public final static String HOME = "http://127.0.0.1:8080/AndZQ/";//用户服务器
//    public final static String HOME = "http://192.168.0.112:8080/AndZQ1/";//王灿本地服务器
    public final static String HOME = "http://223.86.160.138:7001/AndZQ/";//移动服务器
//    public final static String HOME = "http://192.168.0.118:8080/AndZQ/";//张春翔
//    public final static String HOME = "http://223.86.160.138:7001/AndZQ/";//移动
//    public final static String HOME = "http://122.112.229.247:8080/AndZQ/";//华为云

    /**
     * 获取员工信息
     */
    public final static String SERVICE_STAFF = HOME + "/user/AppMyServices";

    /**
     * 登录请求url
     */
    public final static String LOGIN = HOME + "user/appCheck";

    /**
     * 保修页面中 根据line 获取AZ地址
     */
    public final static String GET_AZ_LOCATION = HOME + "/repair/AppGetAddress";
    /**
     * 提交报修信息
     */
     public final static String SUBMIT_REPAIRS = HOME+"repair/AppSubmitRepairs";
//    public final static String SUBMIT_REPAIRS = "http://192.168.0.121:8080/FamilyWideband//repair/AppSubmitRepairs";

    /**
     * 获取报修单信息
     */
    public final static String GET_REPAIRS = HOME+"repair/AppReport";

    /**
     * 提交投诉
     */
    public final static String SUBMIT_COMPLAIN = HOME+"complain/AppMyComplain";

    /**
     * 提交新建客户
     */
    public final static String SUBMIT_NEW_CUSTOMER = HOME+"/Authority/AppAddUser";
    /**
     * 对投诉处理结果的评价
     */
    public final static String COMPLAIN_APPCOMMENT = HOME+"/complain/AppComment";

    /**
     * 推送链接
     */
    public final static String PUSH = HOME+"push/AppNews";

    /**
     * 维修人员 我的保修单
     */
    public final static String GET_MY_REPAIRS_LIST = HOME +"repair/AppMyRepair";
    /**
     * 维修人员 我的保修单
     */
    public final static String GET_MY_REPAIRS_LIST_l = HOME +"repair/AppMyReport";

    /**
     * 维修人员 我的保修单 提交评价
     */
    public final static String POST_MY_REPAIRS_LIST_COMMENT = HOME +"repair/AppComment";


    /**
     * 维修人员 更新维修进度
     */
    public final static String SUBMIT_MY_REPAIRS_PROCESS = HOME +"repair/AppProcess";

    /**
     * 获取我的投诉
     */
    public final static String GET_COMPLAIN  = HOME+"/complain/AppComplain";

    /**
     * 获取新建进度
     */
    public final static String GET_NEW_PROGRESS  = HOME+"/newBuild/AppNewBuild";
    /**
     * 获取我的产品
     */
    public final static String GET_PRODUCT = HOME+"/user/AppMyPro";
    /**
     * 获取月报
     */
    public final static String GET_MONTHLY  = HOME+"/report/AppReports";

    /**
     * 登录成功状态
     */
    public final static String STATE_SUCCESS = "STATE_SUCCESS";

    /**
     * 帐号不存在
     */
    public final static String LOGIN_ID_ERROR = "STATE_ID_ERR";

    /**
     * 密码错误
     */
    public final static String STATE_PA_ERR = "STATE_PA_ERR";
    public final static String STATE_ERR = "STATE_ERR";
    /**
     * token错误
     */
    public final static String TOKEN_ERR  = "TOKEN_ERR";

    /**
     * 获取客户信息
     */
    public final static String GET_MY_DATA  = HOME+"/user/AppMyInfo";
    /**
     * 获取内部人员的信息
     */
    public final static String GET_REPAIR_DATA  = HOME+"/user/AppMyInfow";
    /**
     * 获取新闻
     */
    public final static String GET_NEWS = HOME+"news/AppNews";


    /**
     * 修改密码
     */
    public final static String CHANGE_PSW = HOME+"user/AppPwd";
    /**
     * 检查版本
     */
    public static final String GET_APK_VERSION = HOME + "files/appInfo.json";

    /*
    * 获取分公司名字
    * */
    public final static String GET_COMPANY_NAME  = HOME+"/user/CompanyName";

}
