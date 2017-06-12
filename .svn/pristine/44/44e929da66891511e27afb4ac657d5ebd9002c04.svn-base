/*
 *
 * @author Echo
 * @created 2016.6.5
 * @email bairu.xu@speedatagroup.com
 * @version v1.0
 *
 */

package common.webserice;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.base.App;
import common.bean.AppConst;
import common.http.MHttpManager;
import common.http.MResponse;
import common.http.MResponseBundle;
import common.http.MResponseConst;
import common.http.MResponseListener;
import common.utils.LogUtil;
import common.utils.SharedXmlUtil;

public class WebService implements MResponseConst {
    private static Context mContext;
    private static Handler mHandler;
    private static final String NAMESPACE = "http://tempuri.org/";
//    private String URL = "http://121.69.42.34:12501/ZQTWebService.asmx";
//    private String serviceURL;
    private static final String TAG = LogUtil.DEGUG_MODE ? "WebService" : WebService.class
            .getSimpleName();
    private static final boolean debug = true;


    public static byte[] lock = new byte[0];
    private static WebService mWebService;

    private WebService() {
    }

    public static WebService getInstance() {
        synchronized (lock) {
            if (mWebService == null) {
                mWebService = new WebService();
            }
            return mWebService;
        }
    }

    private static MHttpManager.onSessionTimeOutListener mSessionTimeOutListener;


    public static void setmSessionTimeOutListener(MHttpManager.onSessionTimeOutListener
                                                          sessionTimeOutListener) {
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
     * 请求web service
     *
     * @param params
     * @param nameSpace
     * @param methodName
     * @return
     */
    public Object callWebService(HashMap<String, Object> params, String url, String nameSpace,
                                 String methodName) throws IOException, XmlPullParserException {
        LogUtil.i(debug, TAG, "【WebService.callWebService()】【url=" + url + ",params=" + params +
                "】");
        String SOAP_ACTION = nameSpace + methodName;
        LogUtil.i(debug, TAG, "【WebService.callWebService()】【SOAP_ACTION=" + SOAP_ACTION + "】");
        //创建SoapObject实例
        SoapObject request = new SoapObject(nameSpace, methodName);
        //生成调用web service方法的soap请求消息
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        new MarshalBase64().register(envelope);
        //设置.net web service
        envelope.dotNet = true;
        envelope.encodingStyle = "UTF-8";
        //发送请求
        envelope.setOutputSoapObject(request);
        //请求参数
        if (params != null && !params.isEmpty()) {
            for (Iterator it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry e = (Map.Entry) it.next();
                request.addProperty(e.getKey().toString(), e.getValue());
            }
        }
        HttpTransportSE androidHttpTrandsport = new HttpTransportSE(url);
        //web service请求  XmlPullParserException
        androidHttpTrandsport.call(SOAP_ACTION, envelope);
//        List headerList=androidHttpTrandsport.call(SOAP_ACTION, envelope,null);
//        for (Object header : headerList) {
//            HeaderProperty headerProperty = (HeaderProperty) header;
//            String headerKey = headerProperty.getKey();
//            String headerValue = headerProperty.getValue();
//            System.out.println(headerKey +" : " + headerValue);
//            SharedXmlUtil.getInstance(App.getInstance())
//                    .write(AppConst.WEB_COOKIE, headerValue);
//        }
        //得到返回结果
        Object result = envelope.getResponse();
        return result;
    }


    public String callWebService(HashMap<String, Object> params, String methodName,String url) throws
            IOException, XmlPullParserException {
        Object result = callWebService(params, url, NAMESPACE, methodName);
        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        WebService t = new WebService();

    }

    public void callWebService(final HashMap<String, Object> hashMap, final String method, final String url, final
    MResponseListener l) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String responseBodyAsString = callWebService(hashMap, method,url);
                    MResponse m = new MResponse();
                    m.setStatus(SUCCESS);
                    m.setData(responseBodyAsString);
                    mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, m)).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.i(debug, TAG, "【WebService.run()】【e=" + e + "】");
                    mHandler.obtainMessage(FAIL,
                            new MResponseBundle(l, MResponse.createError(-1, "网络连接异常")))
                            .sendToTarget();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    LogUtil.i(debug, TAG, "【WebService.run()】【e=" + e + "】");
                    mHandler.obtainMessage(FAIL,
                            new MResponseBundle(l, MResponse.createError(-1, "数据解析异常" + e
                                    .getMessage()))).sendToTarget();
                }

            }
        }.start();
    }

//    public void callWebService(final HashMap<String, Object> params, final String method, final
//    MResponseListener l) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SoapObject object = new SoapObject(NAMESPACE, method);
//                if (params != null && !params.isEmpty()) {
//                    for (Iterator<?> it = params.entrySet().iterator(); it
//                            .hasNext(); ) {
//                        // 遍历MAP
//                        @SuppressWarnings("unchecked")
//                        Map.Entry<String, Object> e = (Map.Entry<String, Object>) it
//                                .next();
//                        object.addProperty(e.getKey().toString(), e.getValue());
//                    }
//                    LogUtil.i(debug, TAG, "【WebService.run()】【params=" + params.toString() + "】");
//                    LogUtil.i(debug, TAG, "【WebService.run()】【METHOD=" + method + "】");
//                }
//                // 通过SOAP1.1协议得到envelop对象
//                SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
//                        SoapEnvelope.VER11);
//                // 将soapObject对象设置为envelop对象，传出消息
//                envelop.dotNet = true;
//                envelop.setOutputSoapObject(object);
//                HttpTransportSE httpSE = new HttpTransportSE(URL);
//                try {
//                    httpSE.call(NAMESPACE + method, envelop);
//                    // 得到远程方法返回的SOAP对象
//                    Object resultObj = envelop.getResponse();
//                    MResponse m = MResponse.parseResponse(
//                            resultObj.toString(), method);
//                    mHandler.obtainMessage(SUCCESS, new MResponseBundle(l, m)).sendToTarget();
////                // 得到服务器传回的数据
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    mHandler.obtainMessage(FAIL,
//                            new MResponseBundle(l, MResponse.createError(-1, "网络连接异常")))
//                            .sendToTarget();
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                    LogUtil.i(debug, TAG, "【WebService.run()】【e=" + e + "】");
//                    mHandler.obtainMessage(FAIL,
//                            new MResponseBundle(l, MResponse.createError(-1, "数据解析异常" + e
//                                    .getMessage()))).sendToTarget();
//                }
//            }
//        }).start();
//
//    }

    public void Initlogin(MResponseListener l, String userName, String pwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("grid", userName);
////        hashMap.put("SITECODE", "00512");
//        hashMap.put("pass", pwd);
        hashMap.put("grid", userName);
        hashMap.put("pass", pwd);
//        hashMap.put("barPassword", pwd);
//        WebService.getInstance().callWebService(hashMap, APIMethod.W_LOGIN, l);
    }




}