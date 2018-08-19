package hdo.com.andzq.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * description 新闻实体类
 * author 陈锐
 * version 1.0
 * created 2017/3/31
 */

public class NewsBean {

    private String RESP_STATE;
    private List<NewsBodyBean> body;

    @Override
    public String toString() {
        return "NewsBean{" +
                "RESP_STATE='" + RESP_STATE + '\'' +
                ", body=" + body +
                '}';
    }

    public String getRESP_STATE() {
        return RESP_STATE;
    }

    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public List<NewsBodyBean> getBody() {
        return body;
    }

    public void setBody(List<NewsBodyBean> body) {
        this.body = body;
    }


}
