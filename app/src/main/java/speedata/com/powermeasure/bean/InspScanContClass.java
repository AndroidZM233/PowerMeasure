package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 张明_ on 2016/11/15.
 */

public class InspScanContClass {


    /**
     * RT_LIST : [{"INSP_MC":"00003号巡检点","SYS_NO":"01","INSP_TYPE":"M1208","EQUIP_NO":"1101208010000000000000","INSP_CONT_MC":"机械动作正常","INSP_MUST_TIME":"2016-11-17 09:15:00","INSP_CONT":"001","INSP_NO":"10100003","WORK_ORDER_NO":"A2016111710210010255"},{"INSP_MC":"00003号巡检点","SYS_NO":"01","INSP_TYPE":"M1208","EQUIP_NO":"1101208010000000000000","INSP_CONT_MC":"工作节拍正常","INSP_MUST_TIME":"2016-11-17 09:15:00","INSP_CONT":"002","INSP_NO":"10100003","WORK_ORDER_NO":"A2016111710210010255"},{"INSP_MC":"00003号巡检点","SYS_NO":"01","INSP_TYPE":"M1208","EQUIP_NO":"1101208010000000000000","INSP_CONT_MC":"无异常声响","INSP_MUST_TIME":"2016-11-17 09:15:00","INSP_CONT":"003","INSP_NO":"10100003","WORK_ORDER_NO":"A2016111710210010255"}]
     * RT_F : 1
     * INSP_MUST_TIME : 2016-11-17 09:15:00
     * INSP_NO : 10100003
     * INSP_NAME : 00003号巡检点
     * INSP_ACTL_TIME :
     * INSP_ACTL_GJ :
     * RT_D : 系统接口提示,巡检点查询成功!
     */

    private String RT_F;
    private String INSP_MUST_TIME;
    private String INSP_NO;
    private String INSP_NAME;
    private String INSP_ACTL_TIME;
    private String INSP_ACTL_GJ;
    private String ALARM_TIME;
    private String DEAL_METHOD;
    private String REAL_CAUSE_NAME;
    private String ALARM_CAUSE_NAME;
    private String RT_D;

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
     * INSP_MC : 00003号巡检点
     * SYS_NO : 01
     * INSP_TYPE : M1208
     * EQUIP_NO : 1101208010000000000000
     * INSP_CONT_MC : 机械动作正常
     * INSP_MUST_TIME : 2016-11-17 09:15:00
     * INSP_CONT : 001
     * INSP_NO : 10100003
     * WORK_ORDER_NO : A2016111710210010255
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

    public String getINSP_ACTL_TIME() {
        return INSP_ACTL_TIME;
    }

    public void setINSP_ACTL_TIME(String INSP_ACTL_TIME) {
        this.INSP_ACTL_TIME = INSP_ACTL_TIME;
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

    public List<RTLISTBean> getRT_LIST() {
        return RT_LIST;
    }

    public void setRT_LIST(List<RTLISTBean> RT_LIST) {
        this.RT_LIST = RT_LIST;
    }

    public static class RTLISTBean {
        private String INSP_MC;
        private String SYS_NO;
        private String INSP_TYPE;
        private String EQUIP_NO;
        private String INSP_CONT_MC;
        private String INSP_MUST_TIME;
        private String INSP_CONT;
        private String INSP_NO;
        private String WORK_ORDER_NO;

        public String getINSP_MC() {
            return INSP_MC;
        }

        public void setINSP_MC(String INSP_MC) {
            this.INSP_MC = INSP_MC;
        }

        public String getSYS_NO() {
            return SYS_NO;
        }

        public void setSYS_NO(String SYS_NO) {
            this.SYS_NO = SYS_NO;
        }

        public String getINSP_TYPE() {
            return INSP_TYPE;
        }

        public void setINSP_TYPE(String INSP_TYPE) {
            this.INSP_TYPE = INSP_TYPE;
        }

        public String getEQUIP_NO() {
            return EQUIP_NO;
        }

        public void setEQUIP_NO(String EQUIP_NO) {
            this.EQUIP_NO = EQUIP_NO;
        }

        public String getINSP_CONT_MC() {
            return INSP_CONT_MC;
        }

        public void setINSP_CONT_MC(String INSP_CONT_MC) {
            this.INSP_CONT_MC = INSP_CONT_MC;
        }

        public String getINSP_MUST_TIME() {
            return INSP_MUST_TIME;
        }

        public void setINSP_MUST_TIME(String INSP_MUST_TIME) {
            this.INSP_MUST_TIME = INSP_MUST_TIME;
        }

        public String getINSP_CONT() {
            return INSP_CONT;
        }

        public void setINSP_CONT(String INSP_CONT) {
            this.INSP_CONT = INSP_CONT;
        }

        public String getINSP_NO() {
            return INSP_NO;
        }

        public void setINSP_NO(String INSP_NO) {
            this.INSP_NO = INSP_NO;
        }

        public String getWORK_ORDER_NO() {
            return WORK_ORDER_NO;
        }

        public void setWORK_ORDER_NO(String WORK_ORDER_NO) {
            this.WORK_ORDER_NO = WORK_ORDER_NO;
        }
    }
}
