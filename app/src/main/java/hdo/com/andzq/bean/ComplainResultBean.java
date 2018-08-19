package hdo.com.andzq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * description
 * author 陈锐
 * version 1.0
 * created 2017/4/27
 */

public class ComplainResultBean implements Serializable{
    private String RESP_STATE;
    private List<MyComplainBean> myComplain;

    public String getRESP_STATE() {
        return RESP_STATE;
    }
    public void setRESP_STATE(String RESP_STATE) {
        this.RESP_STATE = RESP_STATE;
    }

    public List<MyComplainBean> getMyComplain() {
        return myComplain;
    }
    public void setMyComplain(List<MyComplainBean> myComplain) {
        this.myComplain = myComplain;
    }
    public static class MyComplainBean implements Serializable{

        private String pObj;
        private String code;
        private String pCName;
        private String pDetail;
        private String pManager;
        private String pMark;
        private String pPerson;
        private String pState;
        private long pTime;
        private String pType;

        private String pStar;
        private String pCom;

        public String getpStar() {
            return pStar;
        }

        public void setpStar(String pStar) {
            this.pStar = pStar;
        }

        public String getpCom() {
            return pCom;
        }

        public void setpCom(String pCom) {
            this.pCom = pCom;
        }

        public String getPObj() {
            return pObj;
        }

        public void setPObj(String pObj) {
            this.pObj = pObj;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPCName() {
            return pCName;
        }

        public void setPCName(String pCName) {
            this.pCName = pCName;
        }

        public String getPDetail() {
            return pDetail;
        }

        public void setPDetail(String pDetail) {
            this.pDetail = pDetail;
        }

        public String getPManager() {
            return pManager;
        }

        public void setPManager(String pManager) {
            this.pManager = pManager;
        }

        public String getPMark() {
            return pMark;
        }

        public void setPMark(String pMark) {
            this.pMark = pMark;
        }

        public String getPPerson() {
            return pPerson;
        }

        public void setPPerson(String pPerson) {
            this.pPerson = pPerson;
        }

        public String getPState() {
            return pState;
        }

        public void setPState(String pState) {
            this.pState = pState;
        }

        public long getPTime() {
            return pTime;
        }

        public void setPTime(long pTime) {
            this.pTime = pTime;
        }

        public String getPType() {
            return pType;
        }

        public void setPType(String pType) {
            this.pType = pType;
        }
    }
}
