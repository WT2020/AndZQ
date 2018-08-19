package hdo.com.andzq.bean;

import java.io.Serializable;

/**
 * description 投诉的具体信息
 * author 邓杰
 * version 1.0
 * Created 2017/3/29.
 */
public class ComplainBean implements Serializable{

    /**
     * id : 2
     * pCCode : zt10
     * pCName : 中国邮政
     * pCode : 2015
     * pDetail : 效率太慢
     * pHandle : 2017-03-16
     * pManager : 韩山
     * pMark : 处理结果
     * pPerson : 胡宇
     * pState : 已处理
     * pTel : 13529844763
     * pTime : 2017-03-25
     * pType : 投诉类型
     * pUrgent : 1
     */

    private int id;
    /**
     * 投诉单号 Column: complain.p_code
     */
    private String pCCode;
    /**
     * 集团名称 Column: complain.p_c_name
     */
    private String pCName;
    /**
     * 集团编码 Column: complain.p_c_code
     */
    private String pCode;
    /**
     * 投诉详情 Column: complain.p_detail
     */
    private String pDetail;
    /**
     * 处理时间 Column: complain.p_handle
     */
    private String pHandle;
    /**
     * 处理人/管辖区客户经理 Column: complain.p_manager
     */
    private String pManager;
    /**
     * 处理结果 Column: complain.p_mark
     */
    private String pMark;
    /**
     * 集团投诉人 Column: complain.p_person
     */
    private String pPerson;
    /**
     * 处理状态 Column: complain.p_state
     */
    private String pState;
    /**
     * 联系电话 Column: complain.p_tel
     */
    private String pTel;
    /**
     * 投诉时间 Column: complain.p_time
     */
    private String pTime;
    /**
     * 投诉类型Column:complain.p_type
     */
    private String pType;
    /**
     * 紧急程度 Column: complain.p_urgent
     */
    private String pUrgent;
    //新添加
    /**
     * 处理详情
     */
    private String pDealDetail;
    /**
     * 投诉部门
     */
    private String pCompany;

    public String getpCompany() {
        return pCompany;
    }

    public void setpCompany(String pCompany) {
        this.pCompany = pCompany;
    }

    public String getpDealDetail() {
        return pDealDetail;
    }

    public void setpDealDetail(String pDealDetail) {
        this.pDealDetail = pDealDetail;
    }

    public ComplainBean(String pCName, String pDetail, String pHandle, String pPerson,String pCompany, String pState,String pDealDetail, String pTel, String pType, String pTime) {
        this.pCName = pCName;
        this.pDetail = pDetail;
        this.pHandle = pHandle;
        this.pPerson = pPerson;
        this.pCompany=pCompany;
        this.pState = pState;
        this.pDealDetail=pDealDetail;
        this.pTel = pTel;
        this.pType = pType;
        this.pTime = pTime;
    }

    public ComplainBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPCCode() {
        return pCCode;
    }

    public void setPCCode(String pCCode) {
        this.pCCode = pCCode;
    }

    public String getPCName() {
        return pCName;
    }

    public void setPCName(String pCName) {
        this.pCName = pCName;
    }

    public String getPCode() {
        return pCode;
    }

    public void setPCode(String pCode) {
        this.pCode = pCode;
    }

    public String getPDetail() {
        return pDetail;
    }

    public void setPDetail(String pDetail) {
        this.pDetail = pDetail;
    }

    public String getPHandle() {
        return pHandle;
    }

    public void setPHandle(String pHandle) {
        this.pHandle = pHandle;
    }

    public String getPManager() {
        return pManager;
    }

    public void setPManager(String pManager) {
        this.pManager = pManager;
    }

    public String getPMark() {
        return pMark;
    }

    public void setPMark(String pMark) {
        this.pMark = pMark;
    }

    public String getPPerson() {
        return pPerson;
    }

    public void setPPerson(String pPerson) {
        this.pPerson = pPerson;
    }

    public String getPState() {
        return pState;
    }

    public void setPState(String pState) {
        this.pState = pState;
    }

    public String getPTel() {
        return pTel;
    }

    public void setPTel(String pTel) {
        this.pTel = pTel;
    }

    public String getPTime() {
        return pTime;
    }

    public void setPTime(String pTime) {
        this.pTime = pTime;
    }

    public String getPType() {
        return pType;
    }

    public void setPType(String pType) {
        this.pType = pType;
    }

    public String getPUrgent() {
        return pUrgent;
    }

    public void setPUrgent(String pUrgent) {
        this.pUrgent = pUrgent;
    }
}
