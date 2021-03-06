/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package speedata.com.powermeasure.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.uhflibs.r2000_native;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import common.base.App;
import common.base.act.FragActBase;
import common.base.dialog.ToastUtils;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.utils.FTPUtils;
import common.utils.GetNetworkIpUtil;
import common.utils.StringUtil;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.LoginOutClass;
import speedata.com.powermeasure.bean.OperLoginClass;
import speedata.com.powermeasure.model.WebModel;

import static common.utils.LogUtil.DEGUG_MODE;

@EActivity(R.layout.act_login)
public class LoginAct extends FragActBase {
    private static final String TAG = DEGUG_MODE ? "LoginAct" : LoginAct.class.getSimpleName();
    private static final boolean debug = true;
    @ViewById
    ImageView imageView7;
    @ViewById
    EditText telEt;
    @ViewById
    ImageView telEtClearbtn;
    @ViewById
    ImageView imageView8;
    @ViewById
    EditText pwdEt;
    @ViewById
    ImageView pwdEtClearbtn;
    @ViewById
    Button loginBtn;
    @ViewById
    Button exitBtn;
    @ViewById
    ImageView iv_setting;


    private String userName;
    private String pwd;
    private String ip;

    @Click
    void iv_setting() {
        openAct(SetIPActivity.class);
    }

    @Click
    void loginBtn() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL("http://218.247.237.138:8080/iom/ws/services/IOMWS?wsdl");
//                    HttpURLConnection urlConnection =
//                            (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("POST");
//                    urlConnection.setConnectTimeout(8000);
//                    InputStream inputStream = urlConnection.getInputStream();
//                    String session_value = urlConnection.getHeaderField("Set-Cookie");
//                    String sessionId = session_value.split(";")[0];
//                    System.out.println("sessionId = " + sessionId);
//                    byte[] byte_result = new byte[inputStream.available()];
//                    inputStream.read(byte_result);
//                    String result = new String(byte_result);
//                    System.out.println("返回结果 result = " + result);
//                    inputStream.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

//        checkParms();

        openLogin();

    }

    public void openLogin() {
        showLoading("登录中...");
        userName = telEt.getText().toString();
        pwd = pwdEt.getText().toString();

        final String sessionID = CreateSessionID(userName);
        WebModel.getInstance().operLogin(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<OperLoginClass> operLoginClasses = JSON.parseArray(callWebService,
                        OperLoginClass.class);
                OperLoginClass operLoginClass = operLoginClasses.get(0);
                String rt_f = operLoginClass.getRT_F();

                if (rt_f.equals("1")) {
                    home_msg = operLoginClass.getRT_LIST();
                    for (int i = 0; i < home_msg.size(); i++) {
                        int count = i + 1;
                        String IOM = "IOM0" + count;
                        for (int j = 0; j < home_msg.size(); j++) {
                            if (home_msg.get(j).getPROC_CODE().equals(IOM)) {
                                setXml(i + "COUNT", home_msg.get(j).getCOUNT() + "");
                                break;
                            }
                        }
                    }
//                    String old_oper_no = getXml("OPER_NO", "");
//                    if (!TextUtils.isEmpty(old_oper_no)&&!old_oper_no.equals(userName)){
//                        Intent myIntent = new Intent();
//                        myIntent.setAction("com.hxht.testmqttclient.MQTTService");
//                        myIntent.setClass(mContext,MQTTService.class);
//                        mContext.stopService(myIntent);
//                    }
                    setXml("IOM_PATH", operLoginClass.getIOM_PATH());
                    setXml("RTLIST_SIZE", home_msg.size() + "");
                    setXml("OPER_NO", userName);
                    setXml("IP", ip);
                    setXml("PWD", pwd);
                    setXml("DEPT_NO", operLoginClass.getDEPT_NO());
                    setXml("SESSIONID", sessionID);
                    setXml("OPER_NAME",operLoginClass.getOPER_NAME());
                    setXml("DEPT_NAME",operLoginClass.getDEPT_NAME());
                    setXml("ROLE_NAME",operLoginClass.getROLE_NAME());
//                    setXml("USER_NAME",userName);
                    openAct(MainAct.class, true);
                    showToast(operLoginClass.getRT_D());
                } else {
                    showToast("登录失败，" + operLoginClass.getRT_D());
                }
                hideLoading();

            }

            @Override
            public void onError(final MResponse response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.msg != null) {
                            showToast(response.msg.toString());
                        } else {
                            showToast("出错了！");
                        }
                        hideLoading();
                    }
                });
            }
        }, userName, pwd, ip, sessionID);
    }

    private String CreateSessionID(String userName) {
        Random random = new Random();
        int nextInt = random.nextInt(9999);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        String result = "IOM" + time + userName + nextInt;
        return result;
    }

    private void loginOut() {
        showLoading("登出中...");
        String sessionid = getXml("SESSIONID", "");
        WebModel.getInstance().loginOut(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<LoginOutClass> loginOutClass = JSON.parseArray(callWebService,
                        LoginOutClass.class);
                String rt_f = loginOutClass.get(0).getRT_F();
                if (rt_f.equals("1")) {
                    showToast(loginOutClass.get(0).getRT_D());
                    App.getInstance().exit();
                } else {
                    showToast(loginOutClass.get(0).getRT_D());
                }
                hideLoading();
            }

            @Override
            public void onError(final MResponse response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.msg != null) {
                            showToast(response.msg.toString());
                        } else {
                            showToast("出错了！");
                        }
                        hideLoading();
                    }
                });
            }
        }, telEt.getText().toString(), sessionid);
    }


    //登出
    @Click
    void exitBtn() {
        loginOut();
    }

    @AfterViews
    protected void main() {
        setSwipeEnable(false);
        setClearBtnListener(telEt, telEtClearbtn);
        setClearBtnListener(pwdEt, pwdEtClearbtn);
        telEt.setText(getXml("OPER_NO","DCDZ003"));
        pwdEt.setText(getXml("PWD",""));
        ip = GetNetworkIpUtil.getLocalIpAddress(mContext);
        if (initR2000()) return;
        Intent intent = new Intent();
        intent.setAction("com.tuisong");
        sendBroadcast(intent);
//        DownFromFTP("1.0");
    }
    //从FTP下载
    private void DownFromFTP(final String version) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FTPUtils ftpUtils=FTPUtils.getInstance();
                boolean downFile = ftpUtils.downFile("192.168.1.80", 621, "wa", "123", "/apk/iom", version+".apk"
                        , Environment.getExternalStorageDirectory() + "");
                if (downFile){
                    File file = new File(Environment.getExternalStorageDirectory(),version+".apk");
                    installApk(file);
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

    private r2000_native native_method;

    private boolean initR2000() {
        native_method = new r2000_native();
        if (native_method.OpenDev() != 0) {
            new AlertDialog.Builder(this).setTitle(R.string.DIA_ALERT).setMessage(R.string.DEV_OPEN_ERR).setPositiveButton(R.string.DIA_CHECK, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    finish();
                }
            }).show();
            return true;
        }
        init_progress++;

        pM = (PowerManager) getSystemService(POWER_SERVICE);
        if (pM != null) {
            wK = pM.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "lock3992");
            if (wK != null) {
                wK.acquire();
                init_progress++;
            }
        }

        if (init_progress == 1) {
            Log.w("3992_6C", "wake lock init failed");
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        });
        return false;
    }

    private int init_progress = 0;
    private PowerManager pM = null;
    private PowerManager.WakeLock wK = null;

    @Override
    protected void onResume() {
        super.onResume();
        WebModel.getInstance().SetIp(getXml("new_ip","http://191.168.1.61:8080/iom/ws/services/IOMWS?wsdl"));
//        WebModel.getInstance().SetIp("http://191.168.1.61:8080/iom/ws/services/IOMWS?wsdl");
    }


    public boolean checkParms(String... parms) {
        if (parms != null && parms.length == 2) {
            if (StringUtil.isBlank(parms[0])) {
                ToastUtils.showShort("用户名不能为空");
                return false;
            }
            if (StringUtil.isBlank(parms[1])) {
                ToastUtils.showShort("密码为空");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {

    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (init_progress) {
                    case 2:
                        wK.release();
                    case 1:
                        native_method.CloseDev();
                    case 0:
                    default:
                        init_progress = 0;
                }
            }
        }).start();
    }
}

