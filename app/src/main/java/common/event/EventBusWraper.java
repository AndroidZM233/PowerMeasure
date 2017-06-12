/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.event;


import de.greenrobot.event.EventBus;

/**
 * 调用HTTP或本地数据库API接口后，返回数据事件消息对象封装
 *
 * @author 颜德情
 */
public class EventBusWraper {

    private static EventBusWraper mEventBusUtil;
    private static EventBus mEventBus;
    private static byte[] mByte = new byte[0];

    private EventBusWraper() {
        super();
        mEventBus = EventBus.getDefault();
    }

    public static EventBusWraper getInstance() {
        synchronized (mByte) {
            if (mEventBusUtil == null) {
                mEventBusUtil = new EventBusWraper();
            }
            return mEventBusUtil;
        }
    }

    public void register(Object context) {
        if (context != null && !mEventBus.isRegistered(context)) {
            mEventBus.register(context);
        }
    }

    public void unregister(Object context) {
        if (context != null && mEventBus.isRegistered(context)) {
            mEventBus.unregister(context);
        }
    }

    public void post(Object message) {
        if (null != message) {
            mEventBus.post(message);
        }
    }

}
