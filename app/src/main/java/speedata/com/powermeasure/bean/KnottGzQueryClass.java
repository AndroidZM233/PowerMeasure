package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 张明_ on 2016/9/27.
 */
public class KnottGzQueryClass {

    /**
     * RT_LIST : [{"SYS_NO":"01","REPAIR_SCHEME":"故障","ALARM_TIME":"2016-09-27 14:08:00","APP_TIME":"2016-09-27 14:14:57","EQUIP_NO":"1101201001002001000000","ALARM_CAUSE_NAME":"抓取备试品后夹具传感器信号丢失","ALARM_ID":8000008341,"APP_DESC":"故障","APPR_TIME":"2016-09-27 14:15:12","EQUIP_NAME":"自动化单相电能表检定系统01号线001号上料装置001号气动横推装置","WORK_ORDER_NO":"E2016092710210003996","ALARM_CAUSE":"20100109"}]
     * RT_F : 1
     * RT_D : 系统接口提示,疑难故障查询成功!
     */

    private String RT_F;
    private String RT_D;
    /**
     * SYS_NO : 01
     * REPAIR_SCHEME : 故障
     * ALARM_TIME : 2016-09-27 14:08:00
     * APP_TIME : 2016-09-27 14:14:57
     * EQUIP_NO : 1101201001002001000000
     * ALARM_CAUSE_NAME : 抓取备试品后夹具传感器信号丢失
     * ALARM_ID : 8000008341
     * APP_DESC : 故障
     * APPR_TIME : 2016-09-27 14:15:12
     * EQUIP_NAME : 自动化单相电能表检定系统01号线001号上料装置001号气动横推装置
     * WORK_ORDER_NO : E2016092710210003996
     * ALARM_CAUSE : 20100109
     */

    private List<RTLISTBean> RT_LIST;

    public String getRT_F() {
        return RT_F;
    }

    public void setRT_F(String RT_F) {
        this.RT_F = RT_F;
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
        private String SYS_NO;
        private String REPAIR_SCHEME;
        private String ALARM_TIME;
        private String APP_TIME;
        private String EQUIP_NO;
        private String ALARM_CAUSE_NAME;
        private long ALARM_ID;
        private String APP_DESC;
        private String APPR_TIME;
        private String EQUIP_NAME;
        private String WORK_ORDER_NO;
        private String ALARM_CAUSE;

        public String getSYS_NO() {
            return SYS_NO;
        }

        public void setSYS_NO(String SYS_NO) {
            this.SYS_NO = SYS_NO;
        }

        public String getREPAIR_SCHEME() {
            return REPAIR_SCHEME;
        }

        public void setREPAIR_SCHEME(String REPAIR_SCHEME) {
            this.REPAIR_SCHEME = REPAIR_SCHEME;
        }

        public String getALARM_TIME() {
            return ALARM_TIME;
        }

        public void setALARM_TIME(String ALARM_TIME) {
            this.ALARM_TIME = ALARM_TIME;
        }

        public String getAPP_TIME() {
            return APP_TIME;
        }

        public void setAPP_TIME(String APP_TIME) {
            this.APP_TIME = APP_TIME;
        }

        public String getEQUIP_NO() {
            return EQUIP_NO;
        }

        public void setEQUIP_NO(String EQUIP_NO) {
            this.EQUIP_NO = EQUIP_NO;
        }

        public String getALARM_CAUSE_NAME() {
            return ALARM_CAUSE_NAME;
        }

        public void setALARM_CAUSE_NAME(String ALARM_CAUSE_NAME) {
            this.ALARM_CAUSE_NAME = ALARM_CAUSE_NAME;
        }

        public long getALARM_ID() {
            return ALARM_ID;
        }

        public void setALARM_ID(long ALARM_ID) {
            this.ALARM_ID = ALARM_ID;
        }

        public String getAPP_DESC() {
            return APP_DESC;
        }

        public void setAPP_DESC(String APP_DESC) {
            this.APP_DESC = APP_DESC;
        }

        public String getAPPR_TIME() {
            return APPR_TIME;
        }

        public void setAPPR_TIME(String APPR_TIME) {
            this.APPR_TIME = APPR_TIME;
        }

        public String getEQUIP_NAME() {
            return EQUIP_NAME;
        }

        public void setEQUIP_NAME(String EQUIP_NAME) {
            this.EQUIP_NAME = EQUIP_NAME;
        }

        public String getWORK_ORDER_NO() {
            return WORK_ORDER_NO;
        }

        public void setWORK_ORDER_NO(String WORK_ORDER_NO) {
            this.WORK_ORDER_NO = WORK_ORDER_NO;
        }

        public String getALARM_CAUSE() {
            return ALARM_CAUSE;
        }

        public void setALARM_CAUSE(String ALARM_CAUSE) {
            this.ALARM_CAUSE = ALARM_CAUSE;
        }
    }
}
