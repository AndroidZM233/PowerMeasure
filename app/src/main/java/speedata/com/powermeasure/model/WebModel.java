/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version v1.0
 *
 */

package speedata.com.powermeasure.model;


import android.content.Context;

import java.util.HashMap;
import java.util.LinkedHashMap;

import common.http.MHttpManager;
import common.http.MResponseListener;
import common.utils.LogUtil;
import common.webserice.WebService;
import speedata.com.powermeasure.constant.Constants;

public class WebModel {
    private static final String TAG = LogUtil.DEGUG_MODE ? "HttpModel" : WebModel.class
            .getSimpleName();
    private static final boolean debug = true;
    public static byte[] lock = new byte[0];
    private static WebModel mHttpModel;
    private String ip="http://218.247.237.138:8080/iom/ws/services/IOMWS?wsdl";
//    private String ip="http://192.168.254.103:8080/iom/ws/services/IOMWS?wsdl";
//    private String ip="http://191.168.1.129:8080/iom/ws/services/IOMWS?wsdl";
    public void init(Context context) {
        WebService.init(context);
        MHttpManager.init(context);
    }

    private WebModel() {
    }

    public void SetIp(String new_ip){
        ip=new_ip;
    }
    public static WebModel getInstance() {
        synchronized (lock) {
            if (mHttpModel == null) {
                mHttpModel = new WebModel();
            }
            return mHttpModel;
        }
    }

    //
//    /**
//     * 接口的公共参数
//     *
//     * @param hashMap
//     * @return
//     */
//    public void addCommonParms(HashMap<String, Object> hashMap) {
//        if (hashMap != null) {
//            String grid = SharedXmlUtil.getInstance(App.getInstance()).read(AppConst.USERNAME,
//                    null);
//            String sid = SharedXmlUtil.getInstance(App.getInstance()).read(AppConst.SID, null);
//            LogUtil.i(debug, TAG, "【HttpModel.setCommonParms()】【sid=" + sid + ",grid=" + grid +
//                    "】");
//            hashMap.put("grid", grid);
//            hashMap.put("sid", sid);
//        }
//    }
//
//
//
    //登录
    public void operLogin(MResponseListener l, String OPER_NO, String PASSWORD
            , String IP, String SessionId) {
        HashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();
        hashMap.put("OPER_NO", OPER_NO);
        hashMap.put("PASSWORD", PASSWORD);
        hashMap.put("IP", IP);
        hashMap.put("SessionId", SessionId);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_OPER_LOGIN,
                ip,l);
    }

    //登出
    public void loginOut(MResponseListener l, String OPER_NO,String SessionId) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO", OPER_NO);
        hashMap.put("SessionId",SessionId);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_LOGIN_OUT,
                ip,l);
    }

    //待办查询点查询
    public void getInspList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_GET_INSP_LIST,
                ip,l);
    }

    //RFID芯片扫描
    public void inspScan(MResponseListener l,String OPER_NO, String RFID_ID) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("RFID_ID", RFID_ID);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_INSP_SCAN,
                ip,l);
    }

    //巡检反馈
    public void inspFeedback(MResponseListener l,String OPER_NO, String INSP_NO, String equipNo,String resultLists) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("INSP_NO", INSP_NO);
        hashMap.put("equipNo",equipNo);
        hashMap.put("RESULT_LIST", resultLists);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_INSP_FEEDBACK,
                ip,l);
    }
    public void inspOfEquipQuery(MResponseListener l,String OPER_NO, String INSP_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("INSP_NO", INSP_NO);
        WebService.getInstance().callWebService(hashMap,"inspOfEquipQuery",
                ip,l);
    }
    //巡检故障上报
    /**
     * 巡检异常上报(已改动)
     * @param operNo 操作员编码
     * @param inspNo 巡检点编号
     * @param equipNo 设备编码
     * @param cont 故障描述
     * @param unitNo 部门编码
     * @param fileName 上传文件名称
     * @param atmtName 上传文件描述
     * @return String
     */
    public void inspReport(MResponseListener l,String operNo, String inspNo,
                           String equipNo,String cont,
                           String unitNo,String fileName,String atmtName) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("operNo",operNo);
        hashMap.put("inspNo", inspNo);
        hashMap.put("equipNo", equipNo);
        hashMap.put("cont", cont);
        hashMap.put("unitNo",unitNo);
        hashMap.put("fileName",fileName);
        hashMap.put("atmtName",atmtName);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_INSP_REPORT,
                ip,l);
    }

    //专项维保列表查询
    public void getSpecialList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_GET_SP_LIST,
                ip,l);
    }

    //专项维保反馈
    public void specialFeedback(MResponseListener l,String OPER_NO, String ORDER_NO, String RESULT_MSG) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("RESULT_MSG", RESULT_MSG);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_SP_FEEDBACK,
                ip,l);
    }

    //定期维保列表查询
    public void getRegularlList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_GET_RELIST,
                ip,l);
    }

    //专项维保反馈
    public void regularFeedback(MResponseListener l, String ORDER_NO, String RESULT_MSG) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("RESULT_MSG", RESULT_MSG);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_RE_FEEDBACK,
                ip,l);
    }

    //报警工单列表查询
    public void getAlarmlList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_GET_ALLIST,
                ip,l);
    }

    //反馈状态工单明细
    public void getAlarmKnlg(MResponseListener l,String OPER_NO,  String ORDER_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_GET_ALKNLG,
                ip,l);
    }

    //反馈状态工单反馈
    public void alarmFeedback(MResponseListener l,String OPER_NO, String ORDER_NO, String REAL_CAUSE,
                              String DEAL_METHOD_CODE,String REMARK) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("REAL_CAUSE", REAL_CAUSE);
        hashMap.put("DEAL_METHOD_CODE",DEAL_METHOD_CODE);
        hashMap.put("REMARK",REMARK);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_AL_FEEDBACK,
                ip,l);
    }

    //反馈状态工单维修
//    public void alarmFeedback(MResponseListener l, String ORDER_NO, String REAL_CAUSE,
//                              String DEAL_METHOD_CODE,String MAINT_DESC,String REMARK) {
//        HashMap<String, Object> hashMap = new LinkedHashMap<>();
//        hashMap.put("ORDER_NO", ORDER_NO);
//        hashMap.put("REAL_CAUSE", REAL_CAUSE);
//        hashMap.put("DEAL_METHOD_CODE",DEAL_METHOD_CODE);
//        hashMap.put("MAINT_DESC",MAINT_DESC);
//        hashMap.put("REMARK",REMARK);
//        WebService.getInstance().callWebService(hashMap,Constants.METHOD_AL_FEEDBACK,
//                ip,l);
//    }

    //反馈状态工单维修申请
    public void maintApply(MResponseListener l,String OPER_NO, String ORDER_NO, String EQUIP_NAME,
                              String MAINT_CAUSE,String FAULT_DESC,String startTime,String endTime) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("EQUIP_NAME", EQUIP_NAME);
        hashMap.put("MAINT_CAUSE",MAINT_CAUSE);
        hashMap.put("FAULT_DESC",FAULT_DESC);
        hashMap.put("startTime",startTime);
        hashMap.put("endTime",endTime);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_MAINT_APPLY,
                ip,l);
    }

    //反馈状态工单反馈
    public void maintFeedback(MResponseListener l,String OPER_NO, String ORDER_NO, String REAL_CAUSE,
                           String DEAL_METHOD_CODE,String MAINT_DESC,String REMARK) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("REAL_CAUSE", REAL_CAUSE);
        hashMap.put("DEAL_METHOD_CODE",DEAL_METHOD_CODE);
        hashMap.put("MAINT_DESC",MAINT_DESC);
        hashMap.put("REMARK",REMARK);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_MAINT_FEEDBACK,
                ip,l);
    }

    //维修状态工单转疑难
    public void diftApply(MResponseListener l,String OPER_NO, String ORDER_NO, String DIFT_DESC,
                              String METHOD_DESC) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("DIFT_DESC", DIFT_DESC);
        hashMap.put("METHOD_DESC",METHOD_DESC);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_DIFT_APPLY,
                ip,l);
    }


    /**
     * 维修状态工单延期申请
     * @param l
     * @param OPER_NO 登录账户
     * @param ORDER_NO 工单编号
     * @param DELAY_TIME 维修延期时间
     * @param sfecyq 是否二次延期
     * @param hxfa 延期后续方案
     */
    public void delayApply(MResponseListener l,String OPER_NO, String ORDER_NO, String DELAY_TIME,
                           String sfecyq,String hxfa) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("DELAY_TIME", DELAY_TIME);
        hashMap.put("sfecyq", sfecyq);
        hashMap.put("hxfa", hxfa);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_DELAY_APPLY,
                ip,l);
    }


    //得到数
    public void sysLoginCountPage(MResponseListener l, String OPER_NO) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO", OPER_NO);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_SYS_COUNT,
                ip,l);
    }

    //工矿信息
    public void ownerControlGKRunApply(MResponseListener l,String OPER_NO,String PROC_CODE) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO", OPER_NO);
        hashMap.put("PROC_CODE",PROC_CODE);
        WebService.getInstance().callWebService(hashMap,Constants.METHOD_Owner_CONTROL,
                ip,l);
    }

    //疑难故障查询
    public void knottGzQuery(MResponseListener l,String pcode,String operNo) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("pcode", pcode);
        hashMap.put("operNo",operNo);
        WebService.getInstance().callWebService(hashMap,"knottGzQuery",
                ip,l);
    }

    //疑难故障修复验证
    public void knottFeedback(MResponseListener l,String OPER_NO,String orderNo, String desc) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("orderNo", orderNo);
        hashMap.put("desc",desc);
        WebService.getInstance().callWebService(hashMap,"knottFeedback",
                ip,l);
    }

    //设备升级工单查询
    public void equipUpQuery(MResponseListener l,String pcode,String operNo) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("pcode", pcode);
        hashMap.put("operNo",operNo);
        WebService.getInstance().callWebService(hashMap,"equipUpQuery",
                ip,l);
    }


    //设备升级待执行
    public void equipUpFeedback(MResponseListener l,String OPER_NO,String orderNo) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("orderNo", orderNo);
        WebService.getInstance().callWebService(hashMap,"equipUpFeedback",
                ip,l);
    }


    public void fileLoadUpMedthor(MResponseListener l,String OPER_NO,String orderNo,String fileName,String dec) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("orderNo", orderNo);
        hashMap.put("fileName", fileName);
        hashMap.put("dec", dec);
        WebService.getInstance().callWebService(hashMap,"fileLoadUpMedthor",
                ip,l);
    }

    //获得子设备内容
    public void inspScanCont(MResponseListener l,String OPER_NO,String inspNo,String equip_no) {
        HashMap<String, Object> hashMap = new LinkedHashMap<>();
        hashMap.put("OPER_NO",OPER_NO);
        hashMap.put("inspNo", inspNo);
        hashMap.put("equip_no", equip_no);
        WebService.getInstance().callWebService(hashMap,"inspScanCont",
                ip,l);
    }
}