/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;

import common.base.App;
import common.bean.AppConst;
import common.bean.ListBeanAnnotation;
import common.utils.AbSysUtil;
import common.utils.LogUtil;
import common.utils.NetUtil;
import common.utils.SendEmailUtil;
import common.utils.SharedXmlUtil;
import common.utils.security.AESUtil;
import common.utils.security.APKUtil;

import static common.utils.LogUtil.DEGUG_MODE;


/**
 * * http://www.lovedabai.com http请求管理 GET POST
 *
 * @author LIN
 * @version 1.0
 * @created 2015-06-21
 */
public class MHttpManager implements MResponseConst {
    private static final String TAG = DEGUG_MODE ? "MHttpManager" : MHttpManager.class
            .getSimpleName();
    private static final boolean debug = true;

    static Context mContext;
    private static Handler mHandler;
    private static onSessionTimeOutListener mSessionTimeOutListener;

    private static String indentifyResouceFile = "/assets/IndicateProtocol.properties";
    private static Properties classPro = null;

    static {
        if (classPro == null) {
            classPro = new Properties();
            try {
                classPro.load(MResponse.class
                        .getResourceAsStream(indentifyResouceFile));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    classPro.load(MResponse.class
                            .getResourceAsStream(indentifyResouceFile));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private MHttpManager() {

    }

    public static void setmSessionTimeOutListener(onSessionTimeOutListener sessionTimeOutListener) {
        mSessionTimeOutListener = sessionTimeOutListener;
    }

    public static void init(Context context) {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MResponseBundle mr = (MResponseBundle) msg.obj;
                MResponseListener listener = mr.listener;
                MResponse m = mr.mResponse;
                if (msg.what == SUCCESS) {
                    if (listener != null) {
                        if (m.getStatus() == MResponse.SUCCESS) {
                            listener.onSuccess(m);
                        } else {
                            if (m.getStatus() == MResponse.SESSION_TIMEOUT) {
                                //登录超时
                                if (mSessionTimeOutListener != null) {
                                    mSessionTimeOutListener.onSessionTimeOutcallBack();
                                }
                                listener.onError(m);
                            } else {
                                listener.onError(m);
                            }
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError(m);
                    }
                }
            }
        };
    }

    /**
     * 创建请求参数
     *
     * @param map
     * @param method
     * @return
     */
    public static NameValuePair[] buildRequestParams(HashMap<String, Object> map, String method) {
        LogUtil.e(debug, TAG, "【MHttpManager.buildRequestParams()】" + "【map = " + map + ", method" +
                " = " + method + "】");
        NameValuePair[] nameValuePairs = null;
        if (map != null) {
            Set<String> strings = map.keySet();
            int size = strings.size();
            nameValuePairs = new NameValuePair[size];
            int i = 0;
            for (String key : map.keySet()) {
                Object value = map.get(key);
                LogUtil.i(debug, TAG, "【MHttpManager.buildRequestParams()】【value=" + value + "," +
                        "key=" + key + "】");
                NameValuePair nameValuePair = new NameValuePair(key, value.toString());
                nameValuePairs[i++] = nameValuePair;
            }
        }
        return nameValuePairs;
    }


    /**
     * 创建请求参数
     *
     * @param method
     * @param params
     * @return
     */
    public static String buildRequestParams(String method,
                                            Object... params) {
        try {
            JSONObject p = new JSONObject();
            p.put(METHOD, method);
            p.put(ID, System.currentTimeMillis() + "");
            p.put(PARAMS, new JSONArray(Arrays.asList(params)));
//            String token =App.getInstance().getToken();
//            if (token != null) {
//                p.put(Config.TOKEN, token);
//            }
            try {
                return new String(p.toString().getBytes(), CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 创建请求参数
     *
     * @param method
     * @return
     */
    public static String buildRequestParams(String method) {
        try {
            JSONObject p = new JSONObject();
            p.put(JSONRPC, "2.0");
            p.put(METHOD, method);
            p.put(ID, System.currentTimeMillis() + "");
            p.put(PARAMS, new JSONArray());
//            String token =App.getInstance().getToken();
//            if (token != null) {
//                p.put(Config.TOKEN, token);
//            }
            try {
                return new String(p.toString().getBytes(), CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void postJson(String method, HashMap<String, Object> map,
                                final MResponseListener l) {
        if (NetUtil.isNetworkConnected()) {
            NameValuePair[] parmas = buildRequestParams(map, method);
            executeHttp(method, parmas, l);
        } else {
            if (l != null) {
                l.onError(MResponse.createNetError());
            }

        }

    }


    public static void postJsonUpdateIcon(String method, String file, String fileName,
                                          MResponseListener l) {
        if (NetUtil.isNetworkConnected()) {
            executeHttp(method, file, fileName, l);
        } else {
            if (l != null) {
                l.onError(MResponse.createNetError());
            }
        }
    }

    public static void postJsonFile(String method, final String query, String file,
                                    MResponseListener l) {
        if (NetUtil.isNetworkConnected()) {
            executeFileHttp(method, query, file, l);
        } else {
            if (l != null) {
                l.onError(MResponse.createNetError());
            }
        }
    }


    public static Cookie[] mCookies = new Cookie[20];

    static void saveCookie(HttpClient httpClient) {
        Cookie[] cookies1 = httpClient.getState().getCookies();
        if (cookies1 != null && cookies1.length > 0) {
            List<Cookie> cookies = Arrays.asList(cookies1);
            int size = cookies.size();
            for (int i = 0; i < size; i++) {
                Cookie cookie = cookies.get(i);
                String name = cookie.getName();
                String value = cookie.getValue();
                LogUtil.i(debug, TAG, "【MHttpManager.saveCookie()】【value=" + value + ",name=" +
                        name + "】");
            }
            mCookies = cookies1;
            if (cookies != null) {
                SharedXmlUtil.getInstance(App.getInstance()).write(AppConst.COOKIE, JSON
                        .toJSONString(cookies));
            }
        }
    }


    public static void setCookies(HttpClient httpClient) {
        if (mCookies == null) {
            String read = SharedXmlUtil.getInstance(App.getInstance()).read(AppConst.COOKIE, null);
            List<Cookie> cookies = JSON.parseArray(read, Cookie.class);
            mCookies = cookies.toArray(new Cookie[cookies.size()]);
        }
        if (mCookies != null) {
            httpClient.getState().addCookies(mCookies);
        }
    }


    private static void executeHttp(final String mm, final NameValuePair[] query, final
    MResponseListener l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = null;
                PostMethod method = null;
                try {
                    httpClient = new HttpClient();
                    String uri = AppConst.serverAddressNew + mm;;
                    LogUtil.e(debug, TAG, "==========【MHttpManager.run()】【uri=" + uri + "】");
                    method = new PostMethod(uri);
                    if (query != null) {
                        method.addParameters(query);
                    }
                    createCommomParms(method);
                    if (isOldMethod(mm)) {
                        method.setRequestHeader("Content-Type",
                                "application/x-www-form-urlencoded;charset=utf-8");
                    } else {
                        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                                "UTF-8");
                    }
                    // 链接超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setConnectionTimeout(CONNECTION_TIMEOUT);
                    // 读取超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setSoTimeout(READ_SO_TIMEOUT);
                    setCookies(httpClient);
                    httpClient.executeMethod(method);
                    saveCookie(httpClient);
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【method=" + mm + ",method" +
                            ".getStatusCode()=" + method.getStatusCode() + "】");
                    int statusCode = method.getStatusCode();
                    if (statusCode == 200) {
                        // parseResponse
                        byte[] b = method.getResponseBody();
                        String responseBodyAsString = null;
                        responseBodyAsString = new String(b, "UTF-8");
                        responseBodyAsString = AESUtil.getInstance().calculateResult
                                (responseBodyAsString);
                        if (false/*mm.contains("findAllMyOrg")*/) {
                            LogUtil.eMail(debug, TAG, "【MHttpManager.run()】【method=" + mm + "," +
                                    "responseBodyAsString=" + responseBodyAsString + "】");
                        } else {
                            LogUtil.d(debug, TAG, "【MHttpManager.run()】【method=" + mm + "," +
                                    "responseBodyAsString=" + responseBodyAsString + "】");
                        }
                        if (isOldMethod(mm)) {
                            MResponse m = parseResponse(
                                    responseBodyAsString, mm);
                            LogUtil.i(debug, TAG, "【MHttpManager.run()】【m=" + m + "】");
                            mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, m))
                                    .sendToTarget();
                        } else {
                            MResponse response = parseNewResponse(method, responseBodyAsString);
                            mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, response))
                                    .sendToTarget();
                        }
                    } else if (statusCode >= 500) {
                        mHandler.obtainMessage(FAIL,
                                new MResponseBundle(l, MResponse.createError(statusCode)))
                                .sendToTarget();
                    } else {
                        mHandler.obtainMessage(FAIL,
                                new MResponseBundle(l, MResponse.createError(statusCode)))
                                .sendToTarget();
                    }
                    method.releaseConnection();
                } catch (Exception e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch HttpException : " +
                            e);
                    MResponse response = MResponse.createErrorbyException(e);
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【response=" + response + "】");
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, response))
                            .sendToTarget();
                    method.releaseConnection();
                }/* catch (IOException e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch IOException : " + e);
                    MResponse serverError = MResponse.createServerError();
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, serverError))
                            .sendToTarget();
                    method.releaseConnection();
                } catch (Exception e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch IOException : " + e);
                    MResponse serverError = MResponse.createServerError();
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, serverError))
                            .sendToTarget();
                    method.releaseConnection();
                }*/
            }

            private MResponse parseNewResponse(final PostMethod method, String
                    responseBodyAsString) {
                MResponse response = new MResponse();
                response.setStatus(MResponse.SUCCESS);
                response.setData(responseBodyAsString);
                return response;
            }
        }).start();
    }

    private static void executeHttp(final String mm, final NameValuePair query, final
    MResponseListener l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = null;
                PostMethod method = null;
                try {
                    httpClient = new HttpClient();
//                    if (Config.isUseHttps) {
//                        //此处为https接口时需要
//                        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory
// (), 443);
//                        Protocol.registerProtocol("https", myhttps);
//                    }
                    String uri = AppConst.serverAddressNew + mm;;
                    LogUtil.e(debug, TAG, "==========【MHttpManager.run()】【uri=" + uri + "】");
                    method = new PostMethod(uri);
//                    String paramValue = AESUtil.getInstance().calculateQuery(query);
//                    if(uri.contains("lovedabai.makeTask")){
//                        LogUtil.eMail(debug, TAG, "==========【MHttpManager.run()】【query=" +
// query + "】");
//                    }
                    method.addParameter(query);
                    createCommomParms(method);

                    method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
                    // 链接超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setConnectionTimeout(CONNECTION_TIMEOUT);
                    // 读取超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setSoTimeout(READ_SO_TIMEOUT);
                    httpClient.executeMethod(method);
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【method=" + mm + ",method" +
                            ".getStatusCode()=" + method.getStatusCode() + "】");
                    int statusCode = method.getStatusCode();
                    if (statusCode == 200) {
                        // parseResponse
                        String responseBodyAsString = method.getResponseBodyAsString();
                        responseBodyAsString = AESUtil.getInstance().calculateResult
                                (responseBodyAsString);
                        if (false/*mm.contains("findAllMyOrg")*/) {
                            LogUtil.eMail(debug, TAG, "【MHttpManager.run()】【method=" + mm + "," +
                                    "responseBodyAsString=" + responseBodyAsString + "】");
                        } else {
                            LogUtil.d(debug, TAG, "【MHttpManager.run()】【method=" + mm + "," +
                                    "responseBodyAsString=" + responseBodyAsString + "】");
                        }
                        MResponse m = parseResponse(
                                responseBodyAsString, mm);
                        LogUtil.i(debug, TAG, "【MHttpManager.run()】【m=" + m + "】");
                        mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, m)).sendToTarget();
                    } else if (statusCode >= 500) {
                        mHandler.obtainMessage(FAIL,
                                new MResponseBundle(l, MResponse.createError(statusCode)))
                                .sendToTarget();
                    } else {
                        mHandler.obtainMessage(FAIL,
                                new MResponseBundle(l, MResponse.createError(statusCode)))
                                .sendToTarget();
                    }
                    method.releaseConnection();
                } catch (Exception e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch HttpException : " +
                            e);
                    MResponse response = MResponse.createErrorbyException(e);
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【response=" + response + "】");
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, response))
                            .sendToTarget();
                    method.releaseConnection();
                }/* catch (IOException e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch IOException : " + e);
                    MResponse serverError = MResponse.createServerError();
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, serverError))
                            .sendToTarget();
                    method.releaseConnection();
                } catch (Exception e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch IOException : " + e);
                    MResponse serverError = MResponse.createServerError();
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, serverError))
                            .sendToTarget();
                    method.releaseConnection();
                }*/
            }
        }).start();
    }


    public static MResponse parseResponse(String result, String method) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(result);
        } catch (org.json.JSONException e) {
            return MResponse.createJsonError();
        }
        MResponse response = JSON.parseObject(result, MResponse.class);
        LogUtil.i(debug, TAG, "【parseResponse()】【response=" + response + "】");
        if (response.getStatus() == SUCCESS) {
            return parseRightResult(response, method);
        } else {
            return response;
        }
    }

    private static MResponse parseRightResult(MResponse response,
                                              final String method) {
        Class<? extends Object> cls = null;
        try {
            cls = getClassByMethod(method);
        } catch (final Exception e) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        SendEmailUtil.sendTextByEmail(App.getInstance(), "method==>" + method +
                                ":" + e.toString());
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }.start();
            response.setMsg("/assets/IndicateProtocol.properties没有配置对应的类" + e.getMessage());
            response.setStatus(NOT_CONFIGURE_ERROR);
            return response;
        }
        try {
            Object data = response.getData();
            String json = JSON.toJSONString(data);
            if (cls.getAnnotation(ListBeanAnnotation.class).Type() == ListBeanAnnotation
                    .BEAN_TYPE.LIST) {
                List<Object> list = new
                        ArrayList<Object>(JSONArray.parseArray(json.toString(),
                        cls));
                response.setData(list);
            } else {
                response.setData(JSON.parseObject(json.toString(), cls));
            }
        } catch (final Exception e) {
            e.printStackTrace();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        SendEmailUtil.sendTextByEmail(App.getInstance(), "json  method==>" +
                                method + ":" + e.toString());
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }.start();
            response.setMsg(e.getMessage());
            response.setStatus(PARSE_JSON_ERROR);
        }
        return response;
    }

    public static Class<? extends Object> getClassByMethod(String method)
            throws IOException, ClassNotFoundException {
        String objName = classPro.getProperty(method);
        LogUtil.i(debug, TAG, "【MResponse.getClassByMethod()】【objName=" + objName + ",method=" +
                method + "】");
        return Class.forName(objName);
    }


    private static void executeHttp(final String mm, final String file, final String fileName,
                                    final MResponseListener l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = null;
                PostMethod method = null;
                try {
                    httpClient = new HttpClient();
                    String uri = AppConst.serverAddressNew + mm;;
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【uri=" + uri + "】");
                    method = new PostMethod(uri);
                    String query = buildRequestParams(mm);
                    String paramValue = AESUtil.getInstance().calculateQuery(query);
                    method.addParameter("query", paramValue);
                    createCommomParms(method);
                    method.addParameter("file", file);
                    method.addParameter("fileName", fileName);
                    method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
                    // 链接超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setConnectionTimeout(CONNECTION_TIMEOUT);
                    // 读取超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setSoTimeout(READ_SO_TIMEOUT);
                    httpClient.executeMethod(method);
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【method=" + mm + ",method" +
                            ".getStatusCode()=" + method.getStatusCode() + "】");
                    int statusCode = method.getStatusCode();
                    if (statusCode == 200) {
                        // parseResponse
                        String responseBodyAsString = method.getResponseBodyAsString();
                        responseBodyAsString = AESUtil.getInstance().calculateResult
                                (responseBodyAsString);
                        LogUtil.i(debug, TAG, "【MHttpManager.run()】【method=" + mm + "," +
                                "responseBodyAsString=" + responseBodyAsString + "】");
                        MResponse m = parseResponse(
                                responseBodyAsString, mm);
                        mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, m)).sendToTarget();
                    } else {
                        mHandler.obtainMessage(FAIL,
                                new MResponseBundle(l, MResponse.createError(statusCode)))
                                .sendToTarget();
                    }
                    method.releaseConnection();
                } catch (Exception e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch IOException : " + e);
                    MResponse serverError = MResponse.createErrorbyException(e);
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, serverError))
                            .sendToTarget();
                    method.releaseConnection();
                }
            }


        }).start();
    }

    private static void executeFileHttp(final String mm, final String query, final String file,
                                        final MResponseListener l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = null;
                PostMethod method = null;
                try {
                    httpClient = new HttpClient();
                    String uri = AppConst.serverAddressNew + mm;;
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【uri=" + uri + "】");
                    method = new PostMethod(uri);
                    LogUtil.e(debug, TAG, "【MHttpManager.run()】【query=" + query + "】");
                    String paramValue = AESUtil.getInstance().calculateQuery(query);
                    method.addParameter("query", paramValue);
                    createCommomParms(method);
                    if (file != null) {
                        method.addParameter("file", file);
                    }
                    method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
                    // 链接超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setConnectionTimeout(CONNECTION_TIMEOUT);
                    // 读取超时
                    httpClient.getHttpConnectionManager().getParams()
                            .setSoTimeout(READ_SO_TIMEOUT);
                    httpClient.executeMethod(method);
                    LogUtil.i(debug, TAG, "【MHttpManager.run()】【method=" + mm + ",method" +
                            ".getStatusCode()=" + method.getStatusCode() + "】");
                    int statusCode = method.getStatusCode();
                    if (statusCode == 200) {
                        // parseResponse
                        String responseBodyAsString = method.getResponseBodyAsString();
                        responseBodyAsString = AESUtil.getInstance().calculateResult
                                (responseBodyAsString);
                        LogUtil.i(debug, TAG, "【MHttpManager.run()】【method=" + mm + "," +
                                "responseBodyAsString=" + responseBodyAsString + "】");
                        MResponse m = parseResponse(
                                responseBodyAsString, mm);
                        mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, m)).sendToTarget();
                    } else {
                        mHandler.obtainMessage(FAIL,
                                new MResponseBundle(l, MResponse.createError(statusCode)))
                                .sendToTarget();
                    }
                    method.releaseConnection();
                } catch (Exception e) {
                    // e.printStackTrace();
                    LogUtil.e(debug, TAG, "MHttpManager-excuteHttp catch IOException : " + e);
                    MResponse serverError = MResponse.createErrorbyException(e);
                    mHandler.obtainMessage(FAIL, new MResponseBundle(l, serverError))
                            .sendToTarget();
                    method.releaseConnection();
                }
            }


        }).start();
    }


    private static void createCommomParms(PostMethod method) {
        String packageName = App.getInstance().getPackageName();
        String hashCode = APKUtil.getSignatureHashCode(packageName, App.getInstance());
        method.addParameter("jsonrpc", "3.0");
        method.addRequestHeader("platform", "Android");
        method.addRequestHeader("appid", "lovedabai");
        method.addRequestHeader("sign", hashCode);
        method.addRequestHeader("version", AbSysUtil.getVersionName(App.getInstance()
                .getApplicationContext()));
    }


//    private static String getCompleteUrl(String method) {
//        if (isOldMethod(method)) {
//            return Config.serverAddress + method;
//        } else {
//            return Config.serverAddressNew + mm;;
//        }
//    }

    private static boolean isOldMethod(String method) {
        return method.endsWith(".do");
    }


    public void get() {

    }

    public interface onSessionTimeOutListener {
        void onSessionTimeOutcallBack();
    }

}
