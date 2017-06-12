package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 张明_ on 2016/9/11.
 */
public class SysCountClass {
    /**
     * RT_LIST : [{"PROC_CODE":"IOM01","COUNT":0,"PROC_NAME":"设备巡检管理"},{"PROC_CODE":"IOM02","COUNT":0,"PROC_NAME":"专项维保管理"},{"PROC_CODE":"IOM03","COUNT":0,"PROC_NAME":"定期维保管理"},{"PROC_CODE":"IOM04","COUNT":1,"PROC_NAME":"设备检修管理"},{"PROC_CODE":"IOM05","COUNT":0,"PROC_NAME":"疑难故障管理"},{"PROC_CODE":"IOM06","COUNT":0,"PROC_NAME":"设备更换管理"},{"PROC_CODE":"IOM07","COUNT":0,"PROC_NAME":"设备升级管理"}]
     * RT_F : 1
     * RT_D : 系统接口提示,系统首页数据获取成功!
     */

    private String RT_F;
    private String RT_D;
    /**
     * PROC_CODE : IOM01
     * COUNT : 0
     * PROC_NAME : 设备巡检管理
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
        private String PROC_CODE;
        private int COUNT;
        private String PROC_NAME;

        public String getPROC_CODE() {
            return PROC_CODE;
        }

        public void setPROC_CODE(String PROC_CODE) {
            this.PROC_CODE = PROC_CODE;
        }

        public int getCOUNT() {
            return COUNT;
        }

        public void setCOUNT(int COUNT) {
            this.COUNT = COUNT;
        }

        public String getPROC_NAME() {
            return PROC_NAME;
        }

        public void setPROC_NAME(String PROC_NAME) {
            this.PROC_NAME = PROC_NAME;
        }
    }
}
