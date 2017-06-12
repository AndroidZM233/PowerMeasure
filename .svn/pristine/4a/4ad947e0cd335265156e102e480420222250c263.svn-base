/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.http;

import java.io.IOException;
import java.net.UnknownHostException;

import common.utils.LogUtil;


/**
 * *  服务器返回自定义response 包含解析服务器协议 注意
 *
 * @author LIN
 * @version 1.0
 * @created 2015-06-21
 */
public class MResponse implements MResponseConst {
    private static final String TAG = LogUtil.DEGUG_MODE ? "MResponse" : MResponse.class.getSimpleName();
    private static final boolean debug = true;


    public Object data;
    public String msg;
    public String header;
    public int status = -1;





    //    public static MResponse createServerError() {
//        MResponse m = new MResponse();
//        m.setStatus(SERVER_ERROR);
//        m.setMsg("服务器开小差了");
//        return m;
//    }
    public static MResponse createError(int statusCode) {
        MResponse m = new MResponse();
        m.setStatus(statusCode);
        m.setMsg("网络异常,错误码" + statusCode);
        return m;
    }
    public static MResponse createError(int statusCode,String msg) {
        MResponse m = new MResponse();
        m.setStatus(statusCode);
        m.setMsg(msg + statusCode);
        return m;
    }

    public static MResponse createErrorbyException(Exception e) {
        LogUtil.e(debug, TAG, "【MResponse.createErrorbyException()】" + "【e = " + e + "】");
        MResponse m = new MResponse();
        if (e instanceof UnknownHostException) {
            m.setStatus(NETWORK_ERROR);
            m.setMsg("网络异常");
        } else if (e instanceof IOException) {
            m.setStatus(NETWORK_ERROR);
            m.setMsg("网络异常");
        } else if (e instanceof Exception) {
            m.setStatus(NETWORK_ERROR);
            m.setMsg("网络异常");
        } else {
            m.setStatus(NETWORK_ERROR);
            m.setMsg("未知错误");
        }
        return m;
    }

    public static MResponse createNetError() {
        MResponse m = new MResponse();
        m.setStatus(NETWORK_ERROR);
        m.setMsg(NO_INTENET);
        return m;
    }

    public static MResponseConst createParamsError() {
        MResponse m = new MResponse();
        m.setStatus(PARAMS_ERROR);
        m.setMsg("参数错误");
        return m;
    }

    public static MResponse createJsonError() {
        MResponse m = new MResponse();
        m.setStatus(PARSE_JSON_ERROR);
        m.setMsg("JSON解析异常");
        return m;
    }



    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MResponse{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", status=" + status +
                '}';
    }
}