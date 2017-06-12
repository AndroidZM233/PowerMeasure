/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.utils;

/**
 * AndroidAnnotations框架工具类
 *
 * @author elsw1
 */
public class AbInnerUtil {

    @SuppressWarnings("rawtypes")
    public static Class parse(Class clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.getCanonicalName().endsWith("_")) {
            return clazz;
        }
        String name = clazz.getCanonicalName() + "_";
        try {
            Class result = Class.forName(name);
            return result;
        } catch (ClassNotFoundException e) {
            new RuntimeException("Cannot find class for" + name, e);
        }
        return null;
    }

}