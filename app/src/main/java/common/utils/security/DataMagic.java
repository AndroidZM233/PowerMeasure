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


import java.util.HashMap;
import java.util.Map;

import common.bean.AppConst;


final public class DataMagic {

    public static String d(String exam) {
        if (exam != null && exam.length() > 0) {
            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < exam.length(); i++) {
                sBuilder.append(decodeMap.get(exam.charAt(i)));
            }
            return sBuilder.toString();
        }
        return exam;
    }

    public static String e(String exam) {
        if (exam != null && exam.length() > 0) {
            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < exam.length(); i++) {
                sBuilder.append(encodeMap.get(exam.charAt(i)));
            }
            return sBuilder.toString();
        }
        return exam;
    }

    public static String getDebug() {
        return d(debug);
    }

    private static Map<Character, Character> decodeMap = new HashMap<Character, Character>();
    private static Map<Character, Character> encodeMap = new HashMap<Character, Character>();
    private static String debug = AppConst.QF9_PJ_U;
    private static String src = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM/+=0123456789!";
    private static String tag = "MGSoUyR6Lmjg2eEauPZQCldA/7hx+BTcsIYWK8=fFVk31bDNXrzq5i49OJtHvnw0p-";

    static {
        for (int i = 0; i < src.length(); i++) {
            encodeMap.put(src.charAt(i), tag.charAt(i));
            decodeMap.put(tag.charAt(i), src.charAt(i));
        }
    }

}
