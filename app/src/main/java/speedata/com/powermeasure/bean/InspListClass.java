package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 74118 on 2016/8/8.
 */
public class InspListClass {


    /**
     * RT_LIST : [{"PROC_CODE":"IOM01","SYS_NO":"01","INSP_MUST_TIME":"2016-11-17 09:15:00","INSP_NAME":"00003号巡检点","INSP_NO":"10100003","WORK_ORDER_NO":"A2016111710210010255"},{"PROC_CODE":"IOM01","SYS_NO":"01","INSP_MUST_TIME":"2016-11-17 09:15:00","INSP_NAME":"00004号巡检点","INSP_NO":"10100004","WORK_ORDER_NO":"A2016111710210010255"}]
     * DY_LIST : [{"SYS_NO":"01","CNT":2}]
     * RT_F : 1
     * RT_D : 系统接口提示,待办巡检点查询成功!
     */

    private String RT_F;
    private String RT_D;
    /**
     * PROC_CODE : IOM01
     * SYS_NO : 01
     * INSP_MUST_TIME : 2016-11-17 09:15:00
     * INSP_NAME : 00003号巡检点
     * INSP_NO : 10100003
     * WORK_ORDER_NO : A2016111710210010255
     */

    private List<RTLISTBean> RT_LIST;
    /**
     * SYS_NO : 01
     * CNT : 2
     */

    private List<DYLISTBean> DY_LIST;

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

    public List<DYLISTBean> getDY_LIST() {
        return DY_LIST;
    }

    public void setDY_LIST(List<DYLISTBean> DY_LIST) {
        this.DY_LIST = DY_LIST;
    }

    public static class RTLISTBean {
        private String PROC_CODE;
        private String SYS_NO;
        private String INSP_MUST_TIME;
        private String INSP_NAME;
        private String INSP_NO;
        private String WORK_ORDER_NO;

        public String getPROC_CODE() {
            return PROC_CODE;
        }

        public void setPROC_CODE(String PROC_CODE) {
            this.PROC_CODE = PROC_CODE;
        }

        public String getSYS_NO() {
            return SYS_NO;
        }

        public void setSYS_NO(String SYS_NO) {
            this.SYS_NO = SYS_NO;
        }

        public String getINSP_MUST_TIME() {
            return INSP_MUST_TIME;
        }

        public void setINSP_MUST_TIME(String INSP_MUST_TIME) {
            this.INSP_MUST_TIME = INSP_MUST_TIME;
        }

        public String getINSP_NAME() {
            return INSP_NAME;
        }

        public void setINSP_NAME(String INSP_NAME) {
            this.INSP_NAME = INSP_NAME;
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

    public static class DYLISTBean {
        private String SYS_NO;
        private int CNT;

        public String getSYS_NO() {
            return SYS_NO;
        }

        public void setSYS_NO(String SYS_NO) {
            this.SYS_NO = SYS_NO;
        }

        public int getCNT() {
            return CNT;
        }

        public void setCNT(int CNT) {
            this.CNT = CNT;
        }
    }
}
