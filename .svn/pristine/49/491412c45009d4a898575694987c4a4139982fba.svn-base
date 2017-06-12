/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.utils.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import common.utils.LogUtil;

public class DES {


	private static byte[] iv = { 2, 0, 1, 5, 1, 1, 2, 5 };


	public static String encryptDES(String encryptString, String encryptKey)
			throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		String encode = Base64.encode(encryptedData);
		return encode.trim();
	}


	public static String encryptDES(String encryptString)
			throws Exception {
		String encryptDES = encryptDES(encryptString, DataMagic.getDebug());
		return encryptDES;
	}

	public static String decryptDES(String encryptString)
			throws Exception {
		String decryptDES = decryptDES(encryptString, DataMagic.getDebug());
		return decryptDES;
	}



	public static String decryptDES(String decryptString, String decryptKey)
			throws Exception {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}


	public static void main(String[] args) {
		try {
			String decryptDES = encryptDES("颜德s0123", "12345678");
			LogUtil.i(debug, TAG, "【DES.main()】【decryptDES=" + decryptDES
					+ "】");
			String decryptDES1 = decryptDES(decryptDES,"12345678");
			LogUtil.i(debug, TAG, "【DES.main()】【decryptDES1=" + decryptDES1
					+ "】");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final String TAG = LogUtil.DEGUG_MODE ? "DES" : DES.class
			.getSimpleName();
	private static final boolean debug = true;


}