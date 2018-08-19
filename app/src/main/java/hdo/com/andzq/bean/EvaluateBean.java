package hdo.com.andzq.bean;

import java.io.Serializable;

/**
 * @author 邓杰
 * @version 1.0
 * @description 评价的实体类
 * @created on 2017/4/18.
 */

public class EvaluateBean implements Serializable {
    /**
     * 选项
     */
    private String comment;
    /**
     * 是否被选择
     */
    private boolean isCheck;

    public EvaluateBean(String comment, boolean isCheck) {
        this.comment = comment;
        this.isCheck = isCheck;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
