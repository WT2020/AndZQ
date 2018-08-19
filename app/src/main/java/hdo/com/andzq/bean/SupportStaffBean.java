package hdo.com.andzq.bean;

import java.util.List;

/**
 * description 员工信息bean
 * author 陈锐
 * version 1.0
 * created 2017/5/4
 */

public class SupportStaffBean {
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

    public static class BodyBean {
        private String name;//姓名
        private String phone;//电话
        private String photo;//照片
        private String position;//地址

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
}
