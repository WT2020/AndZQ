package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description 投诉提交单
 * author 陈锐
 * version 1.0
 * Created 2017/3/29.
 */
public class ComplainInfoBean implements Serializable{

    private String name;//姓名
    private String company;//公司名
    private String phone;//电话号码
    private String type;//投诉类型
    private String target;//投诉对象
    private String description;//投诉详情

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String,String> getMap(){
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("target",target);
        map.put("description",description);
        return map;
    }

}
