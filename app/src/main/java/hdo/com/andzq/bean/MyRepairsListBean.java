package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 邓杰
 * @version 1.0
 * @description 我的保修的列表
 * @created on 2017/4/21.
 */

public class MyRepairsListBean implements Serializable {

    /**
     * myRepair : [{"address":"成都市成华区","type":"网络故障","id":23,"startTime":1473490800000,"lineCode":"10180154626","company":"×××邮政局","tel":"18683209890","img":"[\"/files/-2ff7549507338b59.jpg\"]","rstate":3,"applyTime":1492690920000,"predict":1492690996000,"urgent":"0","applyer":"邓婕","report":1492690920000,"arrive":1492691024000,"detail":"网络不通","department":"企划部","progress":[{"state":"","date":1492691024701,"description":"信号发射器中断引起周边网络故障，信号发射器中断引起周边网络故障。","img":"[]"},{"state":"","date":1492691037975,"description":"信号发射器中断引起周边网络故障，信号发射器中断引起周边网络故障。","img":"[\"/files/-2ff7549507338b59.jpg\"]"},{"state":"3","date":1492741145307,"description":"信号发射器中断引起周边网络故障，信号发射器中断引起周边网络故障。","img":"[\"/files/-54d0673d08d16b0d.jpg\",\"/files/77c38d4baef59697.jpg\"]"}],"accept":1492691016000}]
     * RESP_STATE : STATE_SUCCESS
     */

    private String RESP_STATE;
    private List<MyRepairBean> myReport;

    public String getRESP_STATE() {
        return RESP_STATE;
    }

    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public List<MyRepairBean> getMyRepair() {
        return myReport;
    }

    public void setMyRepair(List<MyRepairBean> myRepair) {
        this.myReport = myRepair;
    }

    public static class MyRepairBean implements Serializable{


        private String address;
        private String type;
        private int id;
        private long startTime;
        private String lineCode;
        private String company;
        private String tel;
        private String img;
        private int rstate;
        private long applyTime;
        private long predict;
        private String urgent;
        private String applyer;
        private long report;
        private long arrive;
        private String detail;
        private String department;
        private long accept;
        private List<ProgressBean> progress;
        private String comment;
        private String star;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
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

        public String getUrgent() {
            return urgent;
        }

        public void setUrgent(String urgent) {
            this.urgent = urgent;
        }

        public String getApplyer() {
            return applyer;
        }

        public void setApplyer(String applyer) {
            this.applyer = applyer;
        }

        public long getReport() {
            return report;
        }

        public void setReport(long report) {
            this.report = report;
        }

        public long getArrive() {
            return arrive;
        }

        public void setArrive(long arrive) {
            this.arrive = arrive;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public long getAccept() {
            return accept;
        }

        public void setAccept(long accept) {
            this.accept = accept;
        }

        public List<ProgressBean> getProgress() {
            return progress;
        }

        public void setProgress(List<ProgressBean> progress) {
            this.progress = progress;
        }

        public static class ProgressBean implements Serializable{
            /**
             * state :
             * date : 1492691024701
             * description : 信号发射器中断引起周边网络故障，信号发射器中断引起周边网络故障。
             * img : []
             */

            private String state;
            private long date;
            private String description;
            private String img;

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

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
        }
    }
}
