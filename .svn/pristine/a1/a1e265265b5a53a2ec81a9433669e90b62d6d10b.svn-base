package common.utils.security;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import common.utils.LogUtil;

import static common.utils.LogUtil.DEGUG_MODE;


/**
 * Author：sunger
 */
public class AESUtil {
    final static boolean isUseAES = false;
    private final static String HEX = "0123456789ABCDEF";
    public static byte[] lock = new byte[0];
    private static AESUtil mAESUtil;

    private AESUtil() {
    }

    public static AESUtil getInstance() {
        synchronized (lock) {
            if (mAESUtil == null) {
                mAESUtil = new AESUtil();
            }
            return mAESUtil;
        }
    }

    public static String calculateQuery(String cleartext)
            throws Exception {
        if (isUseAES) {
            String w = DES.encryptDES(cleartext);
            LogUtil.i(debug, TAG, "【AESUtil.calculateQuery()】【w=" + w + "】");
            String de = DES.decryptDES(w);
            LogUtil.i(debug, TAG, "【AESUtil.calculateQuery()】【de=" + de + "】");
            return w;
        } else {
            return cleartext;
        }

    }

    private static final String TAG = DEGUG_MODE ? "AESUtil" : AESUtil.class.getSimpleName();
    private static final boolean debug = true;

    public static String calculateResult(String encrypted)
            throws Exception {
        return encrypted;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {

        KeyGenerator kgen = KeyGenerator.getInstance("AES");

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");

        sr.setSeed(seed);

        kgen.init(128, sr); // 192 and 256 bits may not be available

        SecretKey skey = kgen.generateKey();

        byte[] raw = skey.getEncoded();

        return raw;

    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));

        byte[] encrypted = cipher.doFinal(clear);

        return encrypted;

    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));

        byte[] decrypted = cipher.doFinal(encrypted);

        return decrypted;

    }

    private static byte[] toByte(String hexString) {

        int len = hexString.length() / 2;

        byte[] result = new byte[len];

        for (int i = 0; i < len; i++)

            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();

        return result;

    }

    private static String toHex(byte[] buf) {

        if (buf == null)

            return "";

        StringBuffer result = new StringBuffer(2 * buf.length);

        for (int i = 0; i < buf.length; i++) {

            appendHex(result, buf[i]);

        }

        return result.toString();

    }

    private static void appendHex(StringBuffer sb, byte b) {

        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));

    }

}