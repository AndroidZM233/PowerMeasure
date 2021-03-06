package speedata.com.powermeasure.broadcast;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mingle.headsUp.HeadsUp;
import com.mingle.headsUp.HeadsUpManager;

import common.utils.AbInnerUtil;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.activity.AlarmAct;

/**
 * Created by 张明_ on 2016/9/2.
 */

public class KeyguardBroadCast extends BroadcastReceiver {
    private int code=1;
    @Override
    public void onReceive(Context context, Intent intent) {
        long[] vibreate= new long[]{1000,1000,1000,1000,1000};
        Intent intent1 = new Intent();
        intent1.setClass(context, AbInnerUtil.parse(AlarmAct.class));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,11,
                intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        HeadsUpManager manage = HeadsUpManager.getInstant(context);
        HeadsUp.Builder builder = new HeadsUp.Builder(context);
        builder.setContentTitle("提醒").setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS)
                //要显示通知栏通知,这个一定要设置
                .setSmallIcon(R.drawable.speedata)
                .setContentText("当前单元有告警待处理")
                //2.3 一定要设置这个参数,负责会报错
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, false);
                //设置是否显示 action 按键
//                .setUsesChronometer(true)
//                .setVibrate(vibreate)
//                .addAction(R.drawable.ic_cloud_queue_black_24dp, "查看", pendingIntent);

        HeadsUp headsUp = builder.buildHeadUp();
        headsUp.setSticky(true);
        manage.notify(code++, headsUp);
    }
}
