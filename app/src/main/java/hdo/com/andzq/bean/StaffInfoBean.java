package hdo.com.andzq.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/3/31.
 */
public class StaffInfoBean implements Serializable {
    /**
     * 头像
     */
    private int headImg;
    /**
     * 名字
     */
    private String name;
    /**
     * 职位
     */
    private String position;

    public StaffInfoBean() {
    }

    public StaffInfoBean(int headImg, String name, String position) {
        this.headImg = headImg;
        this.name = name;
        this.position = position;
    }

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
