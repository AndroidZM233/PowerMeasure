package speedata.com.powermeasure.bean;

import java.util.List;

/**
 * Created by 张明_ on 2016/9/12.
 */
public class OwnerControlClass {
    /**
     * RT_LIST : [{"ZS":270,"MC":"单方向复费率(居民用)[2.0 单相 5(60)A220V]","DY":"01","YWC":0,"MS":"01单元工况:单方向复费率(居民用)[2.0 单相 5(60)A220V],总数270只,完成0只","BM":"0101"}]
     * RT_F : 1
     * RT_D : 系统接口提示,工况运行信息查询成功!
     */

    private String RT_F;
    private String RT_D;
    /**
     * ZS : 270
     * MC : 单方向复费率(居民用)[2.0 单相 5(60)A220V]
     * DY : 01
     * YWC : 0
     * MS : 01单元工况:单方向复费率(居民用)[2.0 单相 5(60)A220V],总数270只,完成0只
     * BM : 0101
     */

    private List<RTLISTBean> RT_LIST;
    private List<DYLISTBean> DY_LIST;

    public List<DYLISTBean> getDY_LIST() {
        return DY_LIST;
    }

    public void setDY_LIST(List<DYLISTBean> DY_LIST) {
        this.DY_LIST = DY_LIST;
    }

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
    public static class DYLISTBean {
        private String SYS_NO;
        private String CNT;

        public String getSYS_NO() {
            return SYS_NO;
        }

        public void setSYS_NO(String SYS_NO) {
            this.SYS_NO = SYS_NO;
        }

        public String getCNT() {
            return CNT;
        }

        public void setCNT(String CNT) {
            this.CNT = CNT;
        }
    }
    public static class RTLISTBean {
        private int ZS;
        private String MC;
        private String DY;
        private int YWC;
        private String MS;
        private String BM;
        private String HGL;


        public String getHGL() {
            return HGL;
        }

        public void setHGL(String HGL) {
            this.HGL = HGL;
        }

        public int getZS() {
            return ZS;
        }

        public void setZS(int ZS) {
            this.ZS = ZS;
        }

        public String getMC() {
            return MC;
        }

        public void setMC(String MC) {
            this.MC = MC;
        }

        public String getDY() {
            return DY;
        }

        public void setDY(String DY) {
            this.DY = DY;
        }

        public int getYWC() {
            return YWC;
        }

        public void setYWC(int YWC) {
            this.YWC = YWC;
        }

        public String getMS() {
            return MS;
        }

        public void setMS(String MS) {
            this.MS = MS;
        }

        public String getBM() {
            return BM;
        }

        public void setBM(String BM) {
            this.BM = BM;
        }
    }
}
