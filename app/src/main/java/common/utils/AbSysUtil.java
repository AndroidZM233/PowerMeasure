/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.IOException;

/**
 * 系统信息工具类
 *
 * @author Administrator
 */
public class AbSysUtil {
    public static final String TAG = "AbSysUtil";
    public static final String DISPLAY_SPLIT_STR = "x";
    public static final String NONE = "none";
    public static final String WAP = "wap";
    public static final String NET = "net";
    public static final String WIFI = "wifi";
    private static final String SYSTEM_ROOT_SU = "su";

    /**
     * 获取当前活动的网络类型名称，例如：wifi、wap、net
     *
     * @return
     */
    public static String getActiveNetwork(Context context) {
        switch (NetUtil.getActiveNetworkType(context)) {
            case NetUtil.TYPE_NONE:
                return null;
            case NetUtil.TYPE_WAP:
                return WAP;
            case NetUtil.TYPE_NET:
                return NET;
            case NetUtil.TYPE_WIFI:
                return WIFI;
        }
        return null;
    }

    /**
     * 获取手机IMEI设备号
     *
     * @param context
     */
    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return imei;
    }

    /**
     * 获取手机SIM卡的IMSI号
     *
     * @param context
     */
    public static String getIMSI(Context context) {
        String imsi = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        return imsi;
    }

    /**
     * 获取手机的厂商型号
     */
    public static String getProduct() {
        return Build.MODEL;
    }

    /**
     * 获取手机屏幕分辨率
     *
     * @param context
     * @return 返回格式为 屏幕宽x屏幕高 的字符串，例如：480x800
     */
    public static String getScreen(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels + DISPLAY_SPLIT_STR + dm.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWith(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取手机androidSDK的版本号,例如：8，10
     */
    public static String getSDKVersionCode() {
        return Build.VERSION.SDK;
    }

    /**
     * 获取手机androidSDK的版本名称，例如：2.2、2.3.5
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取客户端软件版本号信息
     *
     * @param context
     */
    public static String getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return Integer.toString(versionCode);
    }

    /**
     * 获取客户端软件版本名称信息
     *
     * @param context
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        String versionName = null;
        try {
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断系统是否root
     *
     * @return
     */
    public static boolean isRootSystem() {
        boolean b = false;
        try {
            if (Runtime.getRuntime().exec(SYSTEM_ROOT_SU) != null) {
                b = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
