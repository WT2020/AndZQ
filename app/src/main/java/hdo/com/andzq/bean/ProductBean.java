package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description
 * author 陈锐
 * version 1.0
 * created 2017/5/2
 * modified by 张建银 on 2017/10/22 专线名称的添加
 */

public class ProductBean implements Serializable{

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

        private String bandwidth;//带宽
        private String level;//等级
        private String line;//专线号
        private String lineName;//专线名
        private String type;//类型

        public String getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(String bandwidth) {
            this.bandwidth = bandwidth;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
