package hdo.com.andzq.bean;

import java.io.Serializable;

/**
 * description 用户实体类
 * author 邓杰
 * version 1.0
 * Created 2017/4/1.
 */
public class UserBean implements Serializable{

    /**
     * 用户姓名 Column: user.u_name
     */
    private String uName;
    /**
     * 登录帐号
     */
    private String uCode;

    /**
     * 用户密码 Column: user.u_pwd
     */
    private String uPwd;

    /**
     * 用户角色 Column: user.u_roles
     */
    private String uRoles;

    /**
     * 所属地区所在的公司
     */
    private String uCompanyId;

    /**
     * 用户电话 Column:user.u_tel
     */
    private String tel;

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuCode() {
        return uCode;
    }

    public void setuCode(String uCode) {
        this.uCode = uCode;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    public String getuRoles() {
        return uRoles;
    }

    public void setuRoles(String uRoles) {
        this.uRoles = uRoles;
    }

    public String getuCompanyId() {
        return uCompanyId;
    }

    public void setuCompanyId(String uCompanyId) {
        this.uCompanyId = uCompanyId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
