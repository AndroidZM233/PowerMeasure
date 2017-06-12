package com.hxht.testmqttclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 张明_ on 2016/11/3.
 */

public class MQTTBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent();
        myIntent.setAction("com.hxht.testmqttclient.MQTTService");
        myIntent.setClass(context,MQTTService.class);
        context.startService(myIntent);
    }
}
