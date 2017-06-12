package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 张明_ on 2016/8/12.
 */
public class InspScanClass {

    /**
     * RT_LIST : [{"VALUE":"1101206001","MC":"机械动作正常"},{"VALUE":"1101206002","MC":"工作节拍正常"},{"VALUE":"1101206003","MC":"无异常声响"},{"VALUE":"1101206004","MC":"检定结果无异常"},{"VALUE":"1101206005","MC":"表托插针无明显变形"}]
     * RT_F : 1
     * INSP_MUST_TIME : 2016-11-22 09:15:00
     * INSP_NO : 10100004
     * INSP_NAME : 00004号巡检点
     * INSP_ACTL_GJ :
     * RT_D : 系统接口提示,巡检点内容查询成功!
     * INSP_ADDER : 0004号巡检点
     */

    private String RT_F;
    private String INSP_MUST_TIME;
    private String INSP_NO;
    private String INSP_NAME;
    private String INSP_ACTL_GJ;
    private String RT_D;
    private String INSP_ADDER;
    private String ALARM_TIME;
    private String DEAL_METHOD;
    private String REAL_CAUSE_NAME;
    private String ALARM_CAUSE_NAME;

    public String getALARM_TIME() {
        return ALARM_TIME;
    }

    public void setALARM_TIME(String ALARM_TIME) {
        this.ALARM_TIME = ALARM_TIME;
    }

    public String getDEAL_METHOD() {
        return DEAL_METHOD;
    }

    public void setDEAL_METHOD(String DEAL_METHOD) {
        this.DEAL_METHOD = DEAL_METHOD;
    }

    public String getREAL_CAUSE_NAME() {
        return REAL_CAUSE_NAME;
    }

    public void setREAL_CAUSE_NAME(String REAL_CAUSE_NAME) {
        this.REAL_CAUSE_NAME = REAL_CAUSE_NAME;
    }

    public String getALARM_CAUSE_NAME() {
        return ALARM_CAUSE_NAME;
    }

    public void setALARM_CAUSE_NAME(String ALARM_CAUSE_NAME) {
        this.ALARM_CAUSE_NAME = ALARM_CAUSE_NAME;
    }

    /**
     * VALUE : 1101206001
     * MC : 机械动作正常
     */

    private List<RTLISTBean> RT_LIST;

    public String getRT_F() {
        return RT_F;
    }

    public void setRT_F(String RT_F) {
        this.RT_F = RT_F;
    }

    public String getINSP_MUST_TIME() {
        return INSP_MUST_TIME;
    }

    public void setINSP_MUST_TIME(String INSP_MUST_TIME) {
        this.INSP_MUST_TIME = INSP_MUST_TIME;
    }

    public String getINSP_NO() {
        return INSP_NO;
    }

    public void setINSP_NO(String INSP_NO) {
        this.INSP_NO = INSP_NO;
    }

    public String getINSP_NAME() {
        return INSP_NAME;
    }

    public void setINSP_NAME(String INSP_NAME) {
        this.INSP_NAME = INSP_NAME;
    }

    public String getINSP_ACTL_GJ() {
        return INSP_ACTL_GJ;
    }

    public void setINSP_ACTL_GJ(String INSP_ACTL_GJ) {
        this.INSP_ACTL_GJ = INSP_ACTL_GJ;
    }

    public String getRT_D() {
        return RT_D;
    }

    public void setRT_D(String RT_D) {
        this.RT_D = RT_D;
    }

    public String getINSP_ADDER() {
        return INSP_ADDER;
    }

    public void setINSP_ADDER(String INSP_ADDER) {
        this.INSP_ADDER = INSP_ADDER;
    }

    public List<RTLISTBean> getRT_LIST() {
        return RT_LIST;
    }

    public void setRT_LIST(List<RTLISTBean> RT_LIST) {
        this.RT_LIST = RT_LIST;
    }

    public static class RTLISTBean {
        private String VALUE;
        private String MC;

        public String getVALUE() {
            return VALUE;
        }

        public void setVALUE(String VALUE) {
            this.VALUE = VALUE;
        }

        public String getMC() {
            return MC;
        }

        public void setMC(String MC) {
            this.MC = MC;
        }
    }
}
