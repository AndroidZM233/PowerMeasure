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

package common.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * * 大白私人助理
 * http://www.lovedabai.com
 * 对于请求返回bean的类型注释
 * 根据服务器文档定义，设置相应的返回类型，分别进行list和普通对象的定义
 *
 * @author LIN
 * @version 1.0
 * @created 2015-06-21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface ListBeanAnnotation {
    BEAN_TYPE Type() default BEAN_TYPE.PLAIN;
    public enum BEAN_TYPE {
        LIST, PLAIN
    }
}