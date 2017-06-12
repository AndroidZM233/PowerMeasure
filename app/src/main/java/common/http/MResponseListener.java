/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.http;

/**
 * * 大白私人助理
 * http://www.lovedabai.com
 * 服务器请求代理
 *
 * @author LIN
 * @version 1.0
 * @created 2015-06-21
 */
public interface MResponseListener {
    void onSuccess(MResponse response);

    void onError(MResponse response);
}
