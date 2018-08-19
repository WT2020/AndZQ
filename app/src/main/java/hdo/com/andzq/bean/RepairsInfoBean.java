package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description 保修单信息
 * author 陈锐
 * version 1.0
 * created 2017/3/29
 */

public class RepairsInfoBean implements Serializable {
    /**
     * 姓名
     */
    private String name;

    /**
     * 申请时间
     */
    private String applicationTime;

    /**
     * 部门
     */
    private String department;

    /**
     * 号码
     */
    private String phoneNum;

    /**
     * 公司
     */
    private String company;

    /**
     * 故障时间
     */
    private String downTime;

    /**
     * 地址
     */
    private String location;

    /**
     * 描述
     */
    private String description;

    /**
     * 客户联系电话
     */
    private String customerPhone;
    /**
     * 故障图片url
     */
    private String[] pics;
    /**
     * 紧急程度
     */
    private String UrgencyLevel;

    /**
     * 故障类型
     */
    private String FaultType;

    /**
     * 专线号码
     */
    private String line;

    /**
     * 专线
     */
    private String lineName;


    private String expectedTime;

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("applicationTime", applicationTime);
        map.put("department", department);
        map.put("phoneNum", phoneNum);
        map.put("company",company);
        map.put("downTime",downTime);
        map.put("location",location);
        map.put("customerPhone",customerPhone);
        map.put("description",description);
        map.put("UrgencyLevel",UrgencyLevel);
        map.put("FaultType",FaultType);
        map.put("line",line);
        map.put("lineName",lineName);
        map.put("expectedTime",expectedTime);
        return map;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getLocation() {
        return location;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public String getUrgencyLevel() {
        return UrgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        UrgencyLevel = urgencyLevel;
    }

    public String getFaultType() {
        return FaultType;
    }

    public void setFaultType(String faultType) {
        FaultType = faultType;
    }
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}
