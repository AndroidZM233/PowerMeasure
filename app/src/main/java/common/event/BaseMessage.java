/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.event;

/**
 * 标准事件消息对象封装
 *
 * @author 桑龙佳
 */
public class BaseMessage {

    /**
     * 事件消息
     */
    public int event;

    /**
     * 事件消息中传递的数据
     */
    public Object data;

    public BaseMessage() {
        super();
    }

    public BaseMessage(int event, Object data) {
        super();
        this.event = event;
        this.data = data;
    }

}
