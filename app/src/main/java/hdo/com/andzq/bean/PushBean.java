package hdo.com.andzq.bean;

/**
 * description 推送通知
 * author 陈锐
 * version 1.0
 * created 2017/4/14
 */

public class PushBean {

    /**
     * title : 您有一条新消息!
     * type : 报修新单
     */

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String type;
    /**
     * state : 1
     */

    private int state;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
