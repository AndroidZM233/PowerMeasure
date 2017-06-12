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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import common.http.MHttpManager;
import common.http.MResponseListener;
import common.utils.LogUtil;
import common.utils.SharedXmlUtil;
import common.webserice.WebService;
import speedata.com.powermeasure.bean.ResultList;
import speedata.com.powermeasure.constant.Constants;

public class HttpModel {
    private static final String TAG = LogUtil.DEGUG_MODE ? "HttpModel" : HttpModel.class
            .getSimpleName();
    private static final boolean debug = true;
    public static byte[] lock = new byte[0];
    private static HttpModel mHttpModel;

    public void init(Context context) {
        WebService.init(context);
        MHttpManager.init(context);
    }

    private HttpModel() {
    }

    public static HttpModel getInstance() {
        synchronized (lock) {
            if (mHttpModel == null) {
                mHttpModel = new HttpModel();
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
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("OPER_NO", OPER_NO);
        hashMap.put("PASSWORD", PASSWORD);
        hashMap.put("IP", IP);
        hashMap.put("SessionId", SessionId);
        MHttpManager.postJson(Constants.METHOD_OPER_LOGIN, hashMap, l);
    }

    //登出
    public void loginOut(MResponseListener l, String OPER_NO) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("OPER_NO", OPER_NO);
        MHttpManager.postJson(Constants.METHOD_LOGIN_OUT, hashMap, l);
    }

    //待办查询点查询
    public void getInspList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        MHttpManager.postJson(Constants.METHOD_GET_INSP_LIST, hashMap, l);
    }

    //RFID芯片扫描
    public void inspScan(MResponseListener l, String RFID_ID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("RFID_ID", RFID_ID);
        MHttpManager.postJson(Constants.METHOD_INSP_SCAN, hashMap, l);
    }

    //巡检反馈
    public void inspFeedback(MResponseListener l, String INSP_NO, List<ResultList> resultLists) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("INSP_NO", INSP_NO);
        hashMap.put("RESULT_LIST", resultLists);
        MHttpManager.postJson(Constants.METHOD_INSP_FEEDBACK, hashMap, l);
    }

    //巡检故障上报
    public void inspReport(MResponseListener l, String INSP_NO, String ALARM_CAUSE_NAME) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("INSP_NO", INSP_NO);
        hashMap.put("ALARM_CAUSE_NAME", ALARM_CAUSE_NAME);
        MHttpManager.postJson(Constants.METHOD_INSP_REPORT, hashMap, l);
    }

    //专项维保列表查询
    public void getSpecialList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        MHttpManager.postJson(Constants.METHOD_GET_SP_LIST, hashMap, l);
    }

    //专项维保反馈
    public void specialFeedback(MResponseListener l, String ORDER_NO, String RESULT_MSG) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("RESULT_MSG", RESULT_MSG);
        MHttpManager.postJson(Constants.METHOD_SP_FEEDBACK, hashMap, l);
    }

    //定期维保列表查询
    public void getRegularlList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        MHttpManager.postJson(Constants.METHOD_GET_RELIST, hashMap, l);
    }

    //专项维保反馈
    public void regularFeedback(MResponseListener l, String ORDER_NO, String RESULT_MSG) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("RESULT_MSG", RESULT_MSG);
        MHttpManager.postJson(Constants.METHOD_RE_FEEDBACK, hashMap, l);
    }

    //报警工单列表查询
    public void getAlarmlList(MResponseListener l, String PROC_CODE, String OPER_NO) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PROC_CODE", PROC_CODE);
        hashMap.put("OPER_NO", OPER_NO);
        MHttpManager.postJson(Constants.METHOD_GET_ALLIST, hashMap, l);
    }

    //反馈状态工单明细
    public void getAlarmKnlg(MResponseListener l, String ORDER_NO) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        MHttpManager.postJson(Constants.METHOD_GET_ALKNLG, hashMap, l);
    }

    //反馈状态工单反馈
    public void alarmFeedback(MResponseListener l, String ORDER_NO, String REAL_CAUSE,
                              String DEAL_METHOD_CODE,String REMARK) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("REAL_CAUSE", REAL_CAUSE);
        hashMap.put("DEAL_METHOD_CODE",DEAL_METHOD_CODE);
        hashMap.put("REMARK",REMARK);
        MHttpManager.postJson(Constants.METHOD_AL_FEEDBACK, hashMap, l);
    }

    //反馈状态工单维修
    public void alarmFeedback(MResponseListener l, String ORDER_NO, String REAL_CAUSE,
                              String DEAL_METHOD_CODE,String MAINT_DESC,String REMARK) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("REAL_CAUSE", REAL_CAUSE);
        hashMap.put("DEAL_METHOD_CODE",DEAL_METHOD_CODE);
        hashMap.put("MAINT_DESC",MAINT_DESC);
        hashMap.put("REMARK",REMARK);
        MHttpManager.postJson(Constants.METHOD_AL_FEEDBACK, hashMap, l);
    }

    //反馈状态工单维修申请
    public void maintApply(MResponseListener l, String ORDER_NO, String EQUIP_NAME,
                              String MAINT_CAUSE,String FAULT_DESC) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("EQUIP_NAME", EQUIP_NAME);
        hashMap.put("MAINT_CAUSE",MAINT_CAUSE);
        hashMap.put("FAULT_DESC",FAULT_DESC);
        MHttpManager.postJson(Constants.METHOD_MAINT_APPLY, hashMap, l);
    }

    //反馈状态工单反馈
    public void maintFeedback(MResponseListener l, String ORDER_NO, String REAL_CAUSE,
                           String DEAL_METHOD_CODE,String MAINT_DESC,String REMARK) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("REAL_CAUSE", REAL_CAUSE);
        hashMap.put("DEAL_METHOD_CODE",DEAL_METHOD_CODE);
        hashMap.put("MAINT_DESC",MAINT_DESC);
        hashMap.put("REMARK",REMARK);
        MHttpManager.postJson(Constants.METHOD_MAINT_FEEDBACK, hashMap, l);
    }

    //维修状态工单转疑难
    public void diftApply(MResponseListener l, String ORDER_NO, String DIFT_DESC,
                              String METHOD_DESC) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("DIFT_DESC", DIFT_DESC);
        hashMap.put("METHOD_DESC",METHOD_DESC);
        MHttpManager.postJson(Constants.METHOD_DIFT_APPLY, hashMap, l);
    }

    //维修状态工单延期申请
    public void delayApply(MResponseListener l, String ORDER_NO, Date DELAY_TIME) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ORDER_NO", ORDER_NO);
        hashMap.put("DELAY_TIME", DELAY_TIME);
        MHttpManager.postJson(Constants.METHOD_DELAY_APPLY, hashMap, l);
    }
}