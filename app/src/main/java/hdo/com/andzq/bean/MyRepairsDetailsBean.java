package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description 维修人员维修单
 * author 陈锐
 * version 1.0
 * created 2017/4/17
 * modified by 张建银 on 2017/10/13 添加再派单状态
 */

public class MyRepairsDetailsBean implements Serializable {

    private String RESP_STATE;
    private List<MyRepairBean> myRepair;

    public String getRESP_STATE() {
        return RESP_STATE;
    }

    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public List<MyRepairBean> getMyRepair() {
        return myRepair;
    }

    public void setMyRepair(List<MyRepairBean> myRepair) {
        this.myRepair = myRepair;
    }

    public static class MyRepairBean implements Serializable{
        private String isSend;//是否是再派单
        private String address;//地址
        private int id;
        private String type;//类型
        private long startTime;//开始时间
        private long accept;//提交时间
        private String applyer;//申请人
        private Object arrive;//到达时间
        private long applyTime;//申请时间
        private long predict;//预期完成时间
        private String img;//图片
        private int rstate;//状态
        private String urgent;//紧急程度
        private String detail;//内容
        private long report;//申请时间
        private String department;//部门
        private String lineCode;//专线号码
        private String company;//公司名称
        private String tel;//电话号码
        private String customerPhone;//客户联系电话
        private List<ProgressBean> progress;//进度

        public String getIsSend() {
            return isSend;
        }

        public void setIsSend(String isSend) {
            this.isSend = isSend;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        private String star;
        private String comment;

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getAccept() {
            return accept;
        }

        public void setAccept(long accept) {
            this.accept = accept;
        }

        public String getApplyer() {
            return applyer;
        }

        public void setApplyer(String applyer) {
            this.applyer = applyer;
        }

        public Object getArrive() {
            return arrive;
        }

        public void setArrive(Object arrive) {
            this.arrive = arrive;
        }

        public long getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(long applyTime) {
            this.applyTime = applyTime;
        }

        public long getPredict() {
            return predict;
        }

        public void setPredict(long predict) {
            this.predict = predict;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getRstate() {
            return rstate;
        }

        public void setRstate(int rstate) {
            this.rstate = rstate;
        }

        public String getUrgent() {
            return urgent;
        }

        public void setUrgent(String urgent) {
            this.urgent = urgent;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public long getReport() {
            return report;
        }

        public void setReport(long report) {
            this.report = report;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getLineCode() {
            return lineCode;
        }

        public void setLineCode(String lineCode) {
            this.lineCode = lineCode;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public List<ProgressBean> getProgress() {
            return progress;
        }

        public void setProgress(List<ProgressBean> progress) {
            this.progress = progress;
        }

        public static class ProgressBean implements Serializable{

            private long date;
            private String description;
            private String img;
            private String state;
            public long getDate() {
                return date;
            }

            public void setDate(long date) {
                this.date = date;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }
        }
    }
}
