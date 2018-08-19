package hdo.com.andzq.bean;

/**
 * description
 * author 陈锐
 * version 1.0
 * created 2017/5/3
 */

public class CustomerDataInfoBean {
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
        private String company;//公司名称
        private String company_code;//集团代码
        private String location;//地址
        private String name;//名称
        private String phone;//电话

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCompany_code() {
            return company_code;
        }

        public void setCompany_code(String company_code) {
            this.company_code = company_code;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}

