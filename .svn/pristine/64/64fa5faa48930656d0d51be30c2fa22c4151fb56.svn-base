package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 张明_ on 2016/8/15.
 */
public class AlKnlgClass {
    /**
     * RT_F : 1
     * RT_D :
     * REAL_CAUSE_LIST : [{"REAL_CAUSE":"20200101","REAL_CAUSE_NAME":"上料机器人执行抓表动作超时"},"\u2026\u2026"]
     * REAL_DEAL_LIST : [{"DEAL_METHOD_CODE":"2020010102","DEAL_METHOD":"检修电机空开辅助触点接线"},"\u2026\u2026"]
     */

    private String RT_F;
    private String RT_D;
    /**
     * REAL_CAUSE : 20200101
     * REAL_CAUSE_NAME : 上料机器人执行抓表动作超时
     */

    private List<REALCAUSELISTBean> REAL_CAUSE_LIST;
    /**
     * DEAL_METHOD_CODE : 2020010102
     * DEAL_METHOD : 检修电机空开辅助触点接线
     */

    private List<REALDEALLISTBean> REAL_DEAL_LIST;

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

    public List<REALCAUSELISTBean> getREAL_CAUSE_LIST() {
        return REAL_CAUSE_LIST;
    }

    public void setREAL_CAUSE_LIST(List<REALCAUSELISTBean> REAL_CAUSE_LIST) {
        this.REAL_CAUSE_LIST = REAL_CAUSE_LIST;
    }

    public List<REALDEALLISTBean> getREAL_DEAL_LIST() {
        return REAL_DEAL_LIST;
    }

    public void setREAL_DEAL_LIST(List<REALDEALLISTBean> REAL_DEAL_LIST) {
        this.REAL_DEAL_LIST = REAL_DEAL_LIST;
    }

    public static class REALCAUSELISTBean {
        private String REAL_CAUSE;
        private String REAL_CAUSE_NAME;

        public String getREAL_CAUSE() {
            return REAL_CAUSE;
        }

        public void setREAL_CAUSE(String REAL_CAUSE) {
            this.REAL_CAUSE = REAL_CAUSE;
        }

        public String getREAL_CAUSE_NAME() {
            return REAL_CAUSE_NAME;
        }

        public void setREAL_CAUSE_NAME(String REAL_CAUSE_NAME) {
            this.REAL_CAUSE_NAME = REAL_CAUSE_NAME;
        }
    }

    public static class REALDEALLISTBean {
        private String DEAL_METHOD_CODE;
        private String DEAL_METHOD;

        public String getDEAL_METHOD_CODE() {
            return DEAL_METHOD_CODE;
        }

        public void setDEAL_METHOD_CODE(String DEAL_METHOD_CODE) {
            this.DEAL_METHOD_CODE = DEAL_METHOD_CODE;
        }

        public String getDEAL_METHOD() {
            return DEAL_METHOD;
        }

        public void setDEAL_METHOD(String DEAL_METHOD) {
            this.DEAL_METHOD = DEAL_METHOD;
        }
    }
}
