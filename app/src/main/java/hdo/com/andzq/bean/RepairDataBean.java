package hdo.com.andzq.bean;

/**
 * description
 * author 陈锐
 * version 1.0
 * created 2017/7/5
 */

public class RepairDataBean {
    private String RESP_STATE;
    private BodyBean body;

    public String getRESP_STATE() {
        return RESP_STATE;
    }

    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String uName;//姓名
        private String company;//分公司
        private String district;//片区
        private String uRoles;//身份令牌
        private String tel;//电话

        public String getuName() {
            return uName;
        }

        public void setuName(String uName) {
            this.uName = uName;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getuRoles() {
            return uRoles;
        }

        public void setuRoles(String uRoles) {
            this.uRoles = uRoles;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
