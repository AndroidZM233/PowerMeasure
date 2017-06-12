package common.utils.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import common.utils.LogUtil;

import static common.utils.LogUtil.DEGUG_MODE;


public class APKUtil {

    /**
     * 获取指定包名应用的签名
     *
     * @param packageName 包名
     * @param context
     * @return
     */
    public static String getSignature(String packageName, Context context) {
        try {
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            StringBuilder builder = new StringBuilder();
            /******* 循环遍历签名数组拼接应用签名 *******/
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            /************** 得到应用签名 **************/
            String signature;
            signature = builder.toString();
            return signature;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSignatureHashCode(String packageName, Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName,
                            PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            long hashcode = sign.hashCode();
            LogUtil.i(debug, TAG, "【APKUtil.getSignatureHashCode()】【hashcode=" + hashcode + "】");
            return String.valueOf(Math.abs(sign.hashCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String TAG = DEGUG_MODE ? "APKUtil" : APKUtil.class.getSimpleName();
    private static final boolean debug = true;
}
