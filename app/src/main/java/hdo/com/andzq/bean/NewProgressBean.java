package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description
 * author 陈锐
 * version 1.0
 * created 2017/5/5
 * modified by 张建银 on 2017/10/22 专线名称的添加
 */

public class NewProgressBean implements Serializable{
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
        private String lineName;//专线名称
        private String lineType;//专线类型
        private String state;//状态
        private String time;//时间
        private List<ProgressBean> progress;//进度

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public String getLineType() {
            return lineType;
        }

        public void setLineType(String lineType) {
            this.lineType = lineType;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ProgressBean> getProgress() {
            return progress;
        }

        public void setProgress(List<ProgressBean> progress) {
            this.progress = progress;
        }

        public static class ProgressBean implements Serializable{
            private String detail;
            private String time;
            private String title;

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
