package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description 月报bean
 * author 陈锐
 * version 1.0
 * created 2017/4/19
 */

public class MonthlyBean implements Serializable{
    private String RESP_STATE;
    private List<BodyBean> body;

    public String getRESP_STATE() {
        return RESP_STATE;
    }

    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public List<BodyBean> getBody() {
        return body;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }

    public static class BodyBean implements Serializable{
        private String season;
        private String company;
        private String companyCode;
        private String content;

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
        /*  private String complainCount;
        private String faultCount;
        private String faultRate;
        private String faultTime;
        private String faultTimely;
        private String responseTimely;
        private String time;
        private String timeoutCount;

        public String getComplainCount() {
            return complainCount;
        }

        public void setComplainCount(String complainCount) {
            this.complainCount = complainCount;
        }

        public String getFaultCount() {
            return faultCount;
        }

        public void setFaultCount(String faultCount) {
            this.faultCount = faultCount;
        }

        public String getFaultRate() {
            return faultRate;
        }

        public void setFaultRate(String faultRate) {
            this.faultRate = faultRate;
        }

        public String getFaultTime() {
            return faultTime;
        }

        public void setFaultTime(String faultTime) {
            this.faultTime = faultTime;
        }

        public String getFaultTimely() {
            return faultTimely;
        }

        public void setFaultTimely(String faultTimely) {
            this.faultTimely = faultTimely;
        }

        public String getResponseTimely() {
            return responseTimely;
        }

        public void setResponseTimely(String responseTimely) {
            this.responseTimely = responseTimely;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimeoutCount() {
            return timeoutCount;
        }

        public void setTimeoutCount(String timeoutCount) {
            this.timeoutCount = timeoutCount;
        }*/
    }
}
