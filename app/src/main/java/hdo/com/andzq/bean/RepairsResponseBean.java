package hdo.com.andzq.bean;

import java.util.List;

/**
 * description 申请报修服务器返回的信息
 * author 陈锐
 * version 1.0
 * created 2017/4/12
 * modified by 张建银 on 2017/10/12 添加专线数据
 */

public class RepairsResponseBean {

    /**
     * 专线类型(号码与名称)
     */
    private List<Line> lines;

    /**
     * 期望完成时间
     */
    private long expectTime;

    @Override
    public String toString() {
        return "RepairsResponseBean{" +
                "address='" + address + '\'' +
                ", department='" + department + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
/**
     * address :
     * department :
     * name :
     * tel :
     * company : 刘洋
     */
    /**
     * 地址
     */
    private String address;
    /**
     * 部门
     */
    private String department;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话号码
     */
    private String tel;
    /**
     * 单位名称
     */
    private String company;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public long getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(long expectTime) {
        this.expectTime = expectTime;
    }

    public class Line {
        private String lineNo;
        private String lineName;
        private String lineCompany;
        private String lineCompanyId;

        public String getLineNo() {
            return lineNo;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public String getLineCompany() {
            return lineCompany;
        }

        public void setLineCompany(String lineCompany) {
            this.lineCompany = lineCompany;
        }

        public String getLineCompanyId() {
            return lineCompanyId;
        }

        public void setLineCompanyId(String lineCompanyId) {
            this.lineCompanyId = lineCompanyId;
        }
    }

}
