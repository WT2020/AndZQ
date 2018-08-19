package hdo.com.andzq.bean;

/**
 * description 登录请求时 返回的数据
 * author 陈锐
 * version 1.0
 * created 2017/3/24
 */

public class LoginInfoBean {

    private String token;
    private String role_id;


//    登录状态值
//    登录成功 STATE_SUCCESS
//    帐号不存在 LOGIN_ID_ERROR
//    密码错误 STATE_PA_ERR
    private String RESP_STATE;
    private String code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRESP_STATE() {
        return RESP_STATE;
    }

    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
