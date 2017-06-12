package speedata.com.powermeasure.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.uhflibs.r2000_native;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import common.adapter.CommonAdapter;
import common.adapter.ViewHolder;
import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.utils.PlaySoundPool;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.InspListClass;
import speedata.com.powermeasure.bean.OwnerControlClass;
import speedata.com.powermeasure.model.WebModel;
import speedata.com.powermeasure.view.RingProgressView;

@EActivity(R.layout.activity_inspect_new)
public class InspectAct extends FragActBase {
    private static final int OWNE_SUCCESS = 1;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    ListView lv_insp;
    @ViewById
    ListView lv_gongkaung;
    @ViewById
    LinearLayout ll_gongkuang;
    @ViewById
    ImageView iv_down;
    @ViewById
    ImageView iv_up;
    @ViewById
    LinearLayout ll1;
    @ViewById
    LinearLayout ll2;
    @ViewById
    LinearLayout ll3;
    @ViewById
    LinearLayout ll4;
    @ViewById
    LinearLayout ll5;
    @ViewById
    TextView tv1;
    @ViewById
    TextView tv2;
    @ViewById
    TextView tv3;
    @ViewById
    TextView tv4;
    @ViewById
    TextView tv5;
    private List<InspListClass.RTLISTBean> insp_list;
    private List<InspListClass.RTLISTBean> new_alarm_list = new ArrayList<>();
    private List<InspListClass.RTLISTBean> noChange_alarm_list = new ArrayList<>();
    private static final int GET_INSPLIST_SUCCESS = 0;  //拿list成功
    private RingProgressView mRingProgressView;
    private CommonAdapter commonAdapter;
    private CommonAdapter commonAdapter1;
    private List<String> listStr = new ArrayList<>();

    @Click
    void ll1() {
        setXml("GoToInsp", "00");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        lv_gongkaung.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF0000"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#336699"));
        insp_list.clear();
        insp_list.addAll(noChange_alarm_list);
        commonAdapter.notifyDataSetChanged();
        if (haveResult){
            ownerRt_list.clear();
            ownerRt_list.addAll(ownerNoChangeRt_list);
//            listStr.clear();
//            getDYNum(ownerRt_list.size());
            commonAdapter1.notifyDataSetChanged();
        }

    }

    private void getDYNum(int size) {
        for (int i = 0; i < size; i++) {
            int count = i + 1;
            String DY = "0" + count;
            listStr.add(DY);
        }
    }

    @Click
    void ll2() {
        setXml("GoToInsp", "01");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        lv_gongkaung.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#FF0000"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#336699"));
        FindNewList("01");
//        listStr.clear();
//        listStr.add("01");
        if (haveResult){
            FindNewOwnerList("01");
        }

    }

    @Click
    void ll3() {
        setXml("GoToInsp", "02");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        lv_gongkaung.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#FF0000"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#336699"));
        FindNewList("02");
//        listStr.clear();
//        listStr.add("02");
        if (haveResult){
            FindNewOwnerList("02");
        }
    }

    @Click
    void ll4() {
        setXml("GoToInsp", "03");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        lv_gongkaung.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#FF0000"));
        tv5.setTextColor(Color.parseColor("#336699"));
        FindNewList("03");
//        listStr.clear();
//        listStr.add("03");
        if (haveResult){
            FindNewOwnerList("03");
        }
    }

    @Click
    void ll5() {
        setXml("GoToInsp", "04");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        lv_gongkaung.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#FF0000"));
        FindNewList("04");
//        listStr.clear();
//        listStr.add("04");
        if (haveResult){
            FindNewOwnerList("04");
        }
    }

    private void FindNewList(String s) {
        new_alarm_list.clear();
        for (int i = 0; i < noChange_alarm_list.size(); i++) {
            if (noChange_alarm_list.get(i).getSYS_NO().equals(s)) {
                new_alarm_list.add(noChange_alarm_list.get(i));
            }
        }
        insp_list.clear();
        insp_list.addAll(new_alarm_list);
        commonAdapter.notifyDataSetChanged();

    }

    private void FindNewOwnerList(String s) {
        ownerNewRt_list.clear();

        for (int i = 0; i < ownerNoChangeRt_list.size(); i++) {
            if (ownerNoChangeRt_list.get(i).getDY().equals(s)) {
                ownerNewRt_list.add(ownerNoChangeRt_list.get(i));
            }
        }
        ownerRt_list.clear();
        ownerRt_list.addAll(ownerNewRt_list);
        commonAdapter1.notifyDataSetChanged();

    }

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_INSPLIST_SUCCESS:
                    commonAdapter = new CommonAdapter(mContext, insp_list,
                            R.layout.adapter_insp_new) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            helper.setText(R.id.tv_insp_number, insp_list.get(position)
                                    .getINSP_NO());
                            helper.setText(R.id.tv_insp_name, insp_list.get(position)
                                    .getINSP_NAME());
                            helper.setText(R.id.tv_insp_time, insp_list.get(position)
                                    .getINSP_MUST_TIME());

                        }
                    };
                    lv_insp.setAdapter(commonAdapter);
                    GetOwnerControlGKRunApply();
                    break;
                case OWNE_SUCCESS:
                    commonAdapter1 = new CommonAdapter(mContext, ownerRt_list,
                            R.layout.adapter_gongkuang) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            helper.setText(R.id.DY, ownerRt_list.get(position).getDY() + "单元工况");
                            helper.setText(R.id.MC, ownerRt_list.get(position).getMC());
                            helper.setText(R.id.ZS, ownerRt_list.get(position).getZS() + "");
                            helper.setText(R.id.YWC, ownerRt_list.get(position).getYWC() + "");
                            String hgl = ownerRt_list.get(position)
                                    .getHGL();
                            helper.setProgessText(R.id.tv_percent, hgl);
                            double rpv = Double.parseDouble(hgl);
                            int result = (int) rpv;
                            helper.setRingProgressView(R.id.rpv_, result);
                        }
                    };
                    lv_gongkaung.setAdapter(commonAdapter1);
                    String goToAl = getXml("GoToInsp", "");
                    if (goToAl != null) {
                        if (goToAl.equals("01")) {
                            ll2();
                        } else if (goToAl.equals("02")) {
                            ll3();
                        } else if (goToAl.equals("03")) {
                            ll4();
                        } else if (goToAl.equals("04")) {
                            ll5();
                        } else if (goToAl.equals("00")) {
                            ll1();
                        }
                    }
                    break;
            }
        }
    };
    private boolean isFirst = true;
    private List<OwnerControlClass.RTLISTBean> ownerRt_list;
    private List<OwnerControlClass.RTLISTBean> ownerNewRt_list = new ArrayList<>();
    private List<OwnerControlClass.RTLISTBean> ownerNoChangeRt_list = new ArrayList<>();
    boolean isClick = false;

    @Click
    void ll_gongkuang() {

        if (!isClick) {
            iv_down.setVisibility(View.GONE);
            iv_up.setVisibility(View.VISIBLE);
            lv_gongkaung.setVisibility(View.VISIBLE);
            isClick = true;
        } else {
            iv_down.setVisibility(View.VISIBLE);
            iv_up.setVisibility(View.GONE);
            lv_gongkaung.setVisibility(View.GONE);
            isClick = false;
        }

    }

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(InspectAct.this);
        initTitlebar();
        setSwipeEnable(false);
        showLoading("初始化中...");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (initR2000()) return;
        native_method = new r2000_native();
        native_method.resumeOpenDev();
//                setXml("destroy", "1");
        hideLoading();
//            }
//        }).start();


//        native_method = new r2000_native();
        //突破锁屏
//        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
//        keyguardLock.disableKeyguard();
    }


    private boolean haveResult=true;
    private void GetOwnerControlGKRunApply() {
//        String callWebService="[{\"RT_LIST\":[{\"HGL\":99.88,\"HGS\":1708,\"ZS\":2520,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"01\",\"YWC\":1710,\"MS\":\"01单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数2520只,完成1710只,合格率99.88\",\"BM\":\"0101\"},{\"HGL\":99.81,\"HGS\":2156,\"ZS\":3330,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"02\",\"YWC\":2160,\"MS\":\"02单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数3330只,完成2160只,合格率99.81\",\"BM\":\"0102\"},{\"HGL\":99.6,\"HGS\":1972,\"ZS\":2790,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"03\",\"YWC\":1980,\"MS\":\"03单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数2790只,完成1980只,合格率99.6\",\"BM\":\"0103\"},{\"HGL\":99.67,\"HGS\":1525,\"ZS\":2520,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"04\",\"YWC\":1530,\"MS\":\"04单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数2520只,完成1530只,合格率99.67\",\"BM\":\"0104\"}],\"RT_F\":\"1\",\"RT_D\":\"系统接口提示,工况运行信息查询成功!\"}]";
//        List<OwnerControlClass> ownerControlClasses
//                = JSON.parseArray(callWebService, OwnerControlClass.class);
//        OwnerControlClass ownerControlClass = ownerControlClasses.get(0);
//        String rt_f = ownerControlClass.getRT_F();
//        if (rt_f.equals("1")) {
//            ownerRt_list = ownerControlClass.getRT_LIST();
//            ownerNoChangeRt_list.clear();
//            ownerNoChangeRt_list.addAll(ownerRt_list);
////                    listStr.clear();
////                    getDYNum(ownerRt_list.size());
//            haveResult=true;
//            Message message = new Message();
//            message.what = OWNE_SUCCESS;
//            handler1.sendMessage(message);
//        } else {
//            showToast(ownerControlClass.getRT_D());
//            haveResult=false;
//
//        }


//        showLoading("查询中...");

        //工况信息
        WebModel.getInstance().ownerControlGKRunApply(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                final String callWebService = String.valueOf(response.data);
                List<OwnerControlClass> ownerControlClasses
                        = JSON.parseArray(callWebService, OwnerControlClass.class);
                OwnerControlClass ownerControlClass = ownerControlClasses.get(0);
                String rt_f = ownerControlClass.getRT_F();
                if (rt_f.equals("1")) {
                    ownerRt_list = ownerControlClass.getRT_LIST();
                    ownerNoChangeRt_list.clear();
                    ownerNoChangeRt_list.addAll(ownerRt_list);
//                    listStr.clear();
//                    getDYNum(ownerRt_list.size());
                    haveResult=true;
                    Message message = new Message();
                    message.what = OWNE_SUCCESS;
                    handler1.sendMessage(message);
                } else {
                    showToast(ownerControlClass.getRT_D());
                    haveResult=false;

                }
//                hideLoading();
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
//                        hideLoading();
                    }
                });
            }
        }, getXml("OPER_NO", ""), "IOM01");
    }


    private int init_progress = 0;
    private PowerManager pM = null;
    private PowerManager.WakeLock wK = null;

    @Override
    public void onDestroy() {

        super.onDestroy();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//        switch (init_progress) {
//            case 2:
//                wK.release();
//            case 1:
//                native_method.CloseDev();
//            case 0:
//            default:
//                init_progress = 0;
//        }
//            }
//        }).start();


    }

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


    @Override
    protected void onResume() {
        super.onResume();


        DoGetInspList();

//        if (isFirst) {
//            isFirst = false;
//        } else {
//            native_method.resumeOpenDev();
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        native_method.postCloseDev();
    }

    //待办查询点查询
    private void DoGetInspList() {
//        String callWebService="[{\"RT_LIST\":[{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号上料装置\",\"INSP_NO\":\"12011001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号输送装置\",\"INSP_NO\":\"12021001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号信息绑定装置\",\"INSP_NO\":\"12031001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号除雾除尘装置\",\"INSP_NO\":\"12041001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号铅封验证装置\",\"INSP_NO\":\"12051001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号耐压装置\",\"INSP_NO\":\"12061001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线002号耐压装置\",\"INSP_NO\":\"12061012\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号外观识别装置\",\"INSP_NO\":\"12071001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号检定装置\",\"INSP_NO\":\"12081001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线002号检定装置\",\"INSP_NO\":\"12081002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线003号检定装置\",\"INSP_NO\":\"12081003\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线004号检定装置\",\"INSP_NO\":\"12081004\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线005号检定装置\",\"INSP_NO\":\"12081005\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线006号检定装置\",\"INSP_NO\":\"12081006\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线007号检定装置\",\"INSP_NO\":\"12081007\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线008号检定装置\",\"INSP_NO\":\"12081008\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线009号检定装置\",\"INSP_NO\":\"12081009\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线010号检定装置\",\"INSP_NO\":\"12081010\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号激光刻码装置\",\"INSP_NO\":\"12091001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号空箱缓存装置\",\"INSP_NO\":\"12101001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号下料装置\",\"INSP_NO\":\"12111001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"01号线001号刻码验证装置\",\"INSP_NO\":\"12121001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号上料装置\",\"INSP_NO\":\"12012001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号输送装置\",\"INSP_NO\":\"12022001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号信息绑定装置\",\"INSP_NO\":\"12032001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号除雾除尘装置\",\"INSP_NO\":\"12042001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号铅封验证装置\",\"INSP_NO\":\"12052001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号耐压装置\",\"INSP_NO\":\"12062001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线002号耐压装置\",\"INSP_NO\":\"12062002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号外观识别装置\",\"INSP_NO\":\"12072001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号检定装置\",\"INSP_NO\":\"12082001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线002号检定装置\",\"INSP_NO\":\"12082002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线003号检定装置\",\"INSP_NO\":\"12082003\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线004号检定装置\",\"INSP_NO\":\"12082004\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线005号检定装置\",\"INSP_NO\":\"12082005\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线006号检定装置\",\"INSP_NO\":\"12082006\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线007号检定装置\",\"INSP_NO\":\"12082007\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线008号检定装置\",\"INSP_NO\":\"12082008\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线009号检定装置\",\"INSP_NO\":\"12082009\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线010号检定装置\",\"INSP_NO\":\"12082010\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号激光刻码装置\",\"INSP_NO\":\"12092001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号空箱缓存装置\",\"INSP_NO\":\"12102001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号下料装置\",\"INSP_NO\":\"12112001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"02号线001号刻码验证装置\",\"INSP_NO\":\"12122001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号上料装置\",\"INSP_NO\":\"12013001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号输送装置\",\"INSP_NO\":\"12023001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号信息绑定装置\",\"INSP_NO\":\"12033001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号除雾除尘装置\",\"INSP_NO\":\"12043001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号铅封验证装置\",\"INSP_NO\":\"12053001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号耐压装置\",\"INSP_NO\":\"12063001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线002号耐压装置\",\"INSP_NO\":\"12063002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号外观识别装置\",\"INSP_NO\":\"12073001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号检定装置\",\"INSP_NO\":\"12083001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线002号检定装置\",\"INSP_NO\":\"12083002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线003号检定装置\",\"INSP_NO\":\"12083003\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线004号检定装置\",\"INSP_NO\":\"12083004\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线005号检定装置\",\"INSP_NO\":\"12083005\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线006号检定装置\",\"INSP_NO\":\"12083006\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线007号检定装置\",\"INSP_NO\":\"12083007\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线008号检定装置\",\"INSP_NO\":\"12083008\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线009号检定装置\",\"INSP_NO\":\"12083009\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线010号检定装置\",\"INSP_NO\":\"12083010\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号激光刻码装置\",\"INSP_NO\":\"12093001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号空箱缓存装置\",\"INSP_NO\":\"12103001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号下料装置\",\"INSP_NO\":\"12113001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"03号线001号刻码验证装置\",\"INSP_NO\":\"12123001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号上料装置\",\"INSP_NO\":\"12014001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号输送装置\",\"INSP_NO\":\"12024001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号信息绑定装置\",\"INSP_NO\":\"12034001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号除雾除尘装置\",\"INSP_NO\":\"12044001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号铅封验证装置\",\"INSP_NO\":\"12054001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号耐压装置\",\"INSP_NO\":\"12064001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线002号耐压装置\",\"INSP_NO\":\"12064002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号外观识别装置\",\"INSP_NO\":\"12074001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号检定装置\",\"INSP_NO\":\"12084001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线002号检定装置\",\"INSP_NO\":\"12084002\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线003号检定装置\",\"INSP_NO\":\"12084003\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线004号检定装置\",\"INSP_NO\":\"12084004\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线005号检定装置\",\"INSP_NO\":\"12084005\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线006号检定装置\",\"INSP_NO\":\"12084006\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线007号检定装置\",\"INSP_NO\":\"12084007\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线008号检定装置\",\"INSP_NO\":\"12084008\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线009号检定装置\",\"INSP_NO\":\"12084009\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线010号检定装置\",\"INSP_NO\":\"12084010\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号激光刻码装置\",\"INSP_NO\":\"12094001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号空箱缓存装置\",\"INSP_NO\":\"12104001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号下料装置\",\"INSP_NO\":\"12114001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-10 09:40:00\",\"INSP_NAME\":\"04号线001号刻码验证装置\",\"INSP_NO\":\"12124001\",\"WORK_ORDER_NO\":\"A2016101010210006578\"}],\"DY_LIST\":[{\"SYS_NO\":\"01\",\"CNT\":22},{\"SYS_NO\":\"02\",\"CNT\":22},{\"SYS_NO\":\"03\",\"CNT\":22},{\"SYS_NO\":\"04\",\"CNT\":22}],\"RT_F\":\"1\",\"RT_D\":\"系统接口提示,待办巡检点查询成功!\"}]";
//        List<InspListClass> inspListClasses = JSON.parseArray(
//                callWebService, InspListClass.class);
//        InspListClass inspListClass = inspListClasses.get(0);
//        String rt_f = inspListClass.getRT_F();
//        if (rt_f.equals("1")) {
//            insp_list = inspListClass.getRT_LIST();
//            noChange_alarm_list.clear();
//            noChange_alarm_list.addAll(insp_list);
//            Message message = new Message();
//            message.what = GET_INSPLIST_SUCCESS;
//            handler1.sendMessage(message);
//        } else {
//            final String rt_desc = inspListClass.getRT_D();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    showToast("查询失败！" + rt_desc);
//                }
//            });
//            finish();
//        }


//        showLoading("查询中...");
        WebModel.getInstance().getInspList(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
//                callWebService = "[{\"RT_F\":\"1\",\"RT_D\":\"\",\"RT_LIST\":[{\"INSP_NO\":\"12080001\",\"INSP_NAME\":\"上料装置\",\n" +
//                        "\"INSP_MUST_TIME\":\"2016-08-02 10：00\"}]}]";
                List<InspListClass> inspListClasses = JSON.parseArray(
                        callWebService, InspListClass.class);
                InspListClass inspListClass = inspListClasses.get(0);
                String rt_f = inspListClass.getRT_F();
                if (rt_f.equals("1")) {
                    insp_list = inspListClass.getRT_LIST();
                    noChange_alarm_list.clear();
                    noChange_alarm_list.addAll(insp_list);
                    Message message = new Message();
                    message.what = GET_INSPLIST_SUCCESS;
                    handler1.sendMessage(message);
                } else {
                    final String rt_desc = inspListClass.getRT_D();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("查询失败！" + rt_desc);
                        }
                    });
                    finish();
                }
//                hideLoading();
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
//                        hideLoading();
                        finish();
                    }
                });
            }
        }, "IOM01", getXml("OPER_NO", ""));
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InspectAct.this.finish();
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "巡检任务",getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"), null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }


    public boolean oneClick = true;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F5) {
            if (oneClick) {
                oneClick = false;
                PlaySoundPool.getPlaySoundPool(mContext).playButton();
                native_method.set_freq_region(2);
                native_method.set_antenna_power(5);
                native_method.reg_handler(handler);
                native_method.inventory_start();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            InspectAct.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    List<EpcDataBase> firm = new ArrayList<EpcDataBase>();
    long scant = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                scant++;
                ArrayList<r2000_native.Tag_Data> ks = (ArrayList<r2000_native.Tag_Data>) msg.obj;
                String tmp[] = new String[ks.size()];

                if (ks.size() != 0) {
                    native_method.inventory_stop();
                }

                for (int i = 0; i < ks.size(); i++) {
                    byte[] nq = ks.get(i).epc;
                    if (nq != null) {
                        tmp[i] = new String();
                        for (int j = 0; j < nq.length; j++) {
                            tmp[i] += String.format("%02x ", nq[j]);
                        }
                    }
                }
                int i, j;
                for (i = 0; i < tmp.length; i++) {
                    for (j = 0; j < firm.size(); j++) {
                        if (tmp[i].equals(firm.get(j).epc)) {
                            firm.get(j).valid++;
                            break;
                        }
                    }
                    if (j == firm.size()) {
                        firm.add(new EpcDataBase(tmp[i], 1));
                    }
                }
            }
            oneClick = true;
            if (firm.size() == 1) {
                String rfid = String.valueOf(firm.get(0).epc);
                PlaySoundPool.getPlaySoundPool(mContext).playLaser();

                setXml("RFID", rfid);
                openAct(InspSecondAct.class, true);
            } else {
                showToast("请贴紧您要扫描的标签,重新开始扫描");
                PlaySoundPool.getPlaySoundPool(mContext).playError();
            }
            firm.clear();

        }
    };

    class EpcDataBase {
        String epc;
        int valid;

        public EpcDataBase(String e, int v) {
            // TODO Auto-generated constructor stub
            epc = e;
            valid = v;
        }

        @Override
        public String toString() {
            return epc + "  ( " + valid + " )";
        }
    }

    private r2000_native native_method;
}
