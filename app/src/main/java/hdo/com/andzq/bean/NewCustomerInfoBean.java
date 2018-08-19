package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * description 新建客户提交单
 * author 王腾
 * version 1.0
 * Created 2017/3/29.
 */
public class NewCustomerInfoBean implements Serializable{

    private String name;//客户名
    private String num;//账号
    private String pass;//密码
    private String fengongsitype;//分公司
    private String erji;//二级机构类型
    private String zhiwei;//职位政企客户
    private String phone;//联系方式
    private String zhuanxianhao;//集团代码
    private String editSpecialLineNumber;//专线号码

    public String getEditSpecialLineNumber() {
        return editSpecialLineNumber;
    }

    public void setEditSpecialLineNumber(String editSpecialLineNumber) {
        this.editSpecialLineNumber = editSpecialLineNumber;
    }

    public String getZhuanxianhao() {
        return zhuanxianhao;
    }

    public void setZhuanxianhao(String zhuanxianhao) {
        this.zhuanxianhao = zhuanxianhao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFengongsitype() {
        return fengongsitype;
    }

    public void setFengongsitype(String fengongsitype) {
        this.fengongsitype = fengongsitype;
    }

    public String getErji() {
        return erji;
    }

    public void setErji(String erji) {
        this.erji = erji;
    }

    public String getZhiwei() {
        return zhiwei;
    }

    public void setZhiwei(String zhiwei) {
        this.zhiwei = zhiwei;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String,String> getMap(){
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("num",num);
        map.put("pass",pass);
        map.put("fengongsitype", fengongsitype);
        map.put("erji",erji);
        map.put("zhiwei",zhiwei);
        map.put("phone",phone);
        map.put("zhuanxianhao",zhuanxianhao);
        map.put("editspeciallinenumber",editSpecialLineNumber);
        return map;
    }

}
