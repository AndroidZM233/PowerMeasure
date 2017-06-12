/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 网络工具类
 *
 * @author Administrator
 */
public class NetUtil {
    /**
     * 网络未连接，无网络类型
     */
    public static final int TYPE_NONE = 0;
    /**
     * WIFI类型
     */
    public static final int TYPE_WIFI = 1;
    /**
     * WAP 2G类型
     */
    public static final int TYPE_WAP = 2;
    /**
     * NET 3G类型
     */
    public static final int TYPE_NET = 3;
    private static final String TAG = LogUtil.DEGUG_MODE ? "NetUtil"
            : NetUtil.class.getSimpleName();
    private static final boolean debug = true;
    public static Context mContext;

    private NetUtil() {

    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * @return 是否为wifi连接，一般用于数据较大的下载或者更新时候， 使用该方法检测网络链接，并提示用户！
     */
    public static boolean checkWifi() {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // check the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * <hr>
     * http://www.oschina.net/question/54100_34632
     * </hr>
     *
     * @return 判断网络是否连接上了！
     */
    public static boolean isNetworkConnected() {
        /* 根据系统服务获取手机连接管理对象 */
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 判断当前网络是否连接
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前活动网络的类型
     *
     * @param context
     * @return
     */
    public static int getActiveNetworkType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return TYPE_NONE;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String netInfo = networkInfo.getExtraInfo();
            if (netInfo != null) {
                if (netInfo.toLowerCase().equals("cmnet")) {
                    return TYPE_NET;
                } else {
                    return TYPE_WAP;
                }
            } else {
                return TYPE_NET;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return TYPE_WIFI;
        }
        return TYPE_NONE;

    }

    /**
     * 判断是否连接了网络
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            LogUtil.i(debug, TAG, "【NetUtil.isConnect()】【info=" + info + "】");
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
