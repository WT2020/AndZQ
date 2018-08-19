package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 邓杰
 * @version 1.0
 * @description 我的维修列表的实体类
 * @created on 2017/4/19.
 */

public class RepairsStateBean implements Serializable {
    /**
     * 状态
     */
    private String state;
    /**
     * 时间
     */
    private String time;
    /**
     * 细节
     */
    private String detail;
    /**
     * 图片集合
     */
    private List<String> imgUrl;

    public RepairsStateBean(String state, String time, String detail) {
        this.state = state;
        this.time = time;
        this.detail = detail;
    }

    public RepairsStateBean(String state, String time, String detail, List<String> imgUrl) {
        this.state = state;
        this.time = time;
        this.detail = detail;
        this.imgUrl = imgUrl;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }
}
