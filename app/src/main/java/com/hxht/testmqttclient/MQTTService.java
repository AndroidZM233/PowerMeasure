package com.hxht.testmqttclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import common.utils.AbInnerUtil;
import common.utils.FTPUtils;
import common.utils.SharedXmlUtil;
import common.utils.SysInfoUtil;
import common.utils.TimeFormatePresenter;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.activity.AlarmActNew;
import speedata.com.powermeasure.activity.TestActivity;

import static common.utils.NetUtil.mContext;

/**
 * Created by 张明_ on 2016/11/3.
 */

public class MQTTService extends Service {

        private String host = "tcp://191.168.1.61:1883";
//    private String host = "tcp://191.168.1.63:1883";
//    private String host = "tcp://192.168.1.80:1883";
    private String userName = "admin";
    private String passWord = "password";
    private int i = 1;

    private Handler handler;

    private MqttClient client;

    private String myTopic = "test/topic";

    private MqttConnectOptions options;

    private ScheduledExecutorService scheduler;
    private String oper_no;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    oper_no = SharedXmlUtil.getInstance(mContext).read("OPER_NO", "");
                    String result = String.valueOf(msg.obj);
//                    String mqtt_msg = SharedXmlUtil.getInstance(mContext).read("mqtt_msg", "");
//                    if (result.equals(mqtt_msg)){
//                        return;
//                    }
                    JSONArray jsonArray = JSON.parseArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String taskId1 = jsonObject.getString("taskId");
                    long currentTimeMillis = System.currentTimeMillis();
                    String timeFromTimeStampMin = TimeFormatePresenter
                            .getTimeFromTimeStampMin(currentTimeMillis);
                    if (TextUtils.isEmpty(taskId1)) {
                        return;
                    }
                    int taskId = Integer.parseInt(taskId1);
                    switch (taskId) {
                        //设备检修派工推送
                        case 1:
                            JSONArray resultArray1 = jsonObject.getJSONArray("result");
                            for (int j = 0; j < resultArray1.size(); j++) {
                                JSONObject resultObject = resultArray1.getJSONObject(j);
                                String operNo = "";
                                String msgResult="";
                                try {
                                    operNo = resultObject.getString("operNo");
                                    msgResult=resultObject.getString("msg");
                                } catch (Exception e) {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                    notification.contentView.setTextViewText(R.id.tv_desc, "json格式不正确");
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    notification.defaults = Notification.DEFAULT_VIBRATE;
                                    long[] vibrate = {0, 100, 200, 300};
                                    notification.vibrate = vibrate;
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    Intent intent = new Intent();
                                    intent.setClass(MQTTService.this, AbInnerUtil.parse(AlarmActNew.class));
                                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
                                            intent, 0);
                                    notification.contentIntent = contentIntent;

                                    manager.notify(i++, notification);
                                    e.printStackTrace();
                                }
                                if (TextUtils.isEmpty(oper_no)) {
                                    return;
                                }
                                if (oper_no.equals(operNo)) {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                    notification.contentView.setTextViewText(R.id.tv_desc, msgResult+"\n"+timeFromTimeStampMin);
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    long[] vibrate = {0, 2000, 1000, 1000, 1000};
                                    notification.vibrate = vibrate;
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    Intent intent = new Intent();
                                    intent.setClass(MQTTService.this, AbInnerUtil.parse(AlarmActNew.class));
                                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
                                            intent, 0);
                                    notification.contentIntent = contentIntent;
                                    manager.notify(i++, notification);
                                }
                            }

                            break;

                        //设备待维修到期推送
                        case 2:
                            JSONArray resultArray = jsonObject.getJSONArray("result");
                            for (int j = 0; j < resultArray.size(); j++) {
                                JSONObject resultObject = resultArray.getJSONObject(j);
                                String OPER_USER = "";
                                String msgResult="";
                                try {
                                    OPER_USER = resultObject.getString("operNo");
                                    msgResult=resultObject.getString("msg");
                                } catch (Exception e) {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                    notification.contentView.setTextViewText(R.id.tv_desc, "json格式不正确");
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    notification.defaults = Notification.DEFAULT_VIBRATE;
                                    long[] vibrate = {0, 100, 200, 300};
                                    notification.vibrate = vibrate;
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    Intent intent = new Intent();
                                    intent.setClass(MQTTService.this, AbInnerUtil.parse(AlarmActNew.class));
                                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
                                            intent, 0);
                                    notification.contentIntent = contentIntent;

                                    manager.notify(i++, notification);
                                    e.printStackTrace();
                                }
                                if (TextUtils.isEmpty(oper_no)) {
                                    return;
                                }
                                if (oper_no.equals(OPER_USER)) {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                    notification.contentView.setTextViewText(R.id.tv_desc, msgResult+"\n"+timeFromTimeStampMin);
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    long[] vibrate = {0, 2000, 1000, 1000, 1000};
                                    notification.vibrate = vibrate;

                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    Intent intent = new Intent();
                                    intent.setClass(MQTTService.this, AbInnerUtil.parse(AlarmActNew.class));
                                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
                                            intent, 0);
                                    notification.contentIntent = contentIntent;
                                    manager.notify(i++, notification);
                                }
                            }

                            break;

                        //设备巡检到期推送
                        case 3:
                            JSONArray resultArray3 = jsonObject.getJSONArray("result");
                            for (int j = 0; j < resultArray3.size(); j++) {
                                JSONObject resultObject = resultArray3.getJSONObject(j);
                                String operNo3 = "";
                                String msgResult="";
                                try {
                                    operNo3 = resultObject.getString("operNo");
                                    msgResult=resultObject.getString("msg");
                                } catch (Exception e) {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                    notification.contentView.setTextViewText(R.id.tv_desc, "json格式不正确");
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    notification.defaults = Notification.DEFAULT_VIBRATE;
                                    long[] vibrate = {0, 100, 200, 300};
                                    notification.vibrate = vibrate;
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    Intent intent = new Intent();
                                    intent.setClass(MQTTService.this, AbInnerUtil.parse(AlarmActNew.class));
                                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
                                            intent, 0);
                                    notification.contentIntent = contentIntent;

                                    manager.notify(i++, notification);
                                    e.printStackTrace();
                                }
                                if (TextUtils.isEmpty(oper_no)) {
                                    return;
                                }
                                if (oper_no.equals(operNo3)) {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                    notification.contentView.setTextViewText(R.id.tv_desc, msgResult+"\n"+timeFromTimeStampMin);
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    long[] vibrate = {0, 2000, 1000, 1000, 1000};
                                    notification.vibrate = vibrate;

                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    Intent intent = new Intent();
                                    intent.setClass(MQTTService.this, AbInnerUtil.parse(TestActivity.class));
                                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
                                            intent, 0);
                                    notification.contentIntent = contentIntent;
                                    manager.notify(i++, notification);
                                }
                            }
                            break;

                        case 4:
                            try {
                                JSONArray resultArray4 = jsonObject.getJSONArray("result");
                                for (int j = 0; j < resultArray4.size(); j++) {
                                    JSONObject resultObject = resultArray4.getJSONObject(j);
                                    String msg4 = "";
                                    String version = "";
                                    msg4=resultObject.getString("msg");
                                    version=resultObject.getString("versionId");
                                    String[] split = version.split("\\.");
                                    String lastVersion=split[1]+"."+split[2];
                                    String versionName = SysInfoUtil.getVersionName(mContext);
                                    if (!lastVersion.equals(versionName)){
                                        DownFromFTP(lastVersion);

                                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                                        notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                                        notification.contentView.setTextViewText(R.id.tv_desc, msg4+"\n"+timeFromTimeStampMin);
                                        notification.defaults = Notification.DEFAULT_SOUND;
                                        long[] vibrate = {0, 2000, 1000, 1000, 1000};
                                        notification.vibrate = vibrate;
                                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                                        manager.notify(i++, notification);

                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }

                } else if (msg.what == 2) {
                    System.out.println("连接成功");
                    try {
                        client.subscribe(myTopic, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == 3) {
                    System.out.println("连接失败，系统正在重连");
                }
            }
        };
        startReconnect();
    }
    //从FTP下载
    private void DownFromFTP(final String version) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downFile = false;
                try {
                    FTPUtils ftpUtils=FTPUtils.getInstance();
                    downFile = ftpUtils.downFile("191.168.1.61", 21, "ftpiom", "ftpiom", "/home/ftpiom", version+".apk"
                            , Environment.getExternalStorageDirectory() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (downFile){
                    File file = new File(Environment.getExternalStorageDirectory(),version+".apk");
                    installApk(file);
                }else {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification(R.drawable.tuisong, "Mqtt即时推送", System.currentTimeMillis());
                    notification.contentView = new RemoteViews("speedata.com.powermeasure", R.layout.activity_notification);
                    notification.contentView.setTextViewText(R.id.tv_desc, "软件自动更新失败，FTP链接失败");
                    notification.defaults = Notification.DEFAULT_SOUND;
                    long[] vibrate = {0, 2000, 1000, 1000, 1000};
                    notification.vibrate = vibrate;

                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                    Intent intent = new Intent();
//                    intent.setClass(MQTTService.this, AbInnerUtil.parse(TestActivity.class));
//                    PendingIntent contentIntent = PendingIntent.getActivity(MQTTService.this, 0,
//                            intent, 0);
//                    notification.contentIntent = contentIntent;
                    manager.notify(i++, notification);
                }


            }
        }).start();

    }
    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 120 * 1000, TimeUnit.MILLISECONDS);
    }

    private void init() {
        try {
            String currentTime = TimeFormatePresenter.getCurrentTime();
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(host, currentTime,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(false);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(30);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(5);
            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
//                    startReconnect();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());

                }

                @Override
                public void messageArrived(String topicName, MqttMessage message)
                        throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    System.out.println("messageArrived----------");
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = message.toString();
                    handler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    client.connect(options);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
