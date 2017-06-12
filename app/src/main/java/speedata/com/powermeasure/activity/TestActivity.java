package speedata.com.powermeasure.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.uhflibs.r2000_native;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.adapter.CommonAdapter;
import common.adapter.ViewHolder;
import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.utils.SharedXmlUtil;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.HorizScroll.BaseAdapter;
import speedata.com.powermeasure.HorizScroll.HorizontalScrollMenu;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.InspListClass;
import speedata.com.powermeasure.bean.OwnerControlClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_testmain)
public class TestActivity extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;

    private HorizontalScrollMenu hsm_container;
    private List<List<InspListClass.RTLISTBean>> inSpLists = new ArrayList<>();
    private List<List<OwnerControlClass.RTLISTBean>> ownerLists = new ArrayList<>();
    private List<InspListClass.DYLISTBean> dy_list = new ArrayList<>();
    private List<OwnerControlClass.DYLISTBean> owner_dy_list = new ArrayList<>();
    private String[] names = null;

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  finish();
                              }
                          }, "巡检任务",
                getXml("OPER_NAME", ""), getXml("DEPT_NAME", ""), getXml("ROLE_NAME", "运维"), null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    public boolean oneClick = true;

    @AfterViews
    protected void main() {
        setSwipeEnable(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(TestActivity.this);
        initTitlebar();
        initSoundPool();
        showLoading("初始化中...");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (initR2000()) return;
                native_method = new r2000_native();
                native_method.resumeOpenDev();
//                hideLoading();
//            }
//        }).start();
    }

    private int init_progress = 0;
    private PowerManager pM = null;
    private PowerManager.WakeLock wK = null;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F5) {
            if (oneClick) {
                oneClick = false;
                play(3, 0);
                native_method.set_freq_region(2);
                native_method.set_antenna_power(10);
                native_method.reg_handler(handlerR200);
                native_method.inventory_start();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            TestActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    List<TestActivity.EpcDataBase> firm = new ArrayList<TestActivity.EpcDataBase>();
    long scant = 0;
    Handler handlerR200 = new Handler() {
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
                play(2, 0);
                String rfid = String.valueOf(firm.get(0).epc);
                setXml("RFID", rfid);
                openAct(InspSecondAct.class, true);
            } else {
                showToast("请贴紧您要扫描的标签,重新开始扫描");
                play(1, 0);
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

    @Override
    protected void onResume() {
        super.onResume();
        DoGetInspList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private SoundPool sp; //声音池
    private Map<Integer, Integer> mapSRC;

    //初始化声音池
    private void initSoundPool() {
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mapSRC = new HashMap<Integer, Integer>();
        mapSRC.put(1, sp.load(this, R.raw.error, 0));
        mapSRC.put(2, sp.load(this, R.raw.scan, 0));
        mapSRC.put(3, sp.load(this, R.raw.button, 0));
    }

    /**
     * 播放声音池的声音
     */
    private void play(int sound, int number) {
        sp.play(mapSRC.get(sound),//播放的声音资源
                1.0f,//左声道，范围为0--1.0
                1.0f,//右声道，范围为0--1.0
                0, //优先级，0为最低优先级
                number,//循环次数,0为不循环
                1);//播放速率，1为正常速率
    }

    public void initView() {

        hsm_container = (HorizontalScrollMenu) findViewById(R.id.hsm_container);
        hsm_container.setAdapter(new MenuAdapter());
    }

    class MenuAdapter extends BaseAdapter {


        @Override
        public List<String> getMenuItems() {
            // TODO Auto-generated method stub
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews() {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
//            String callWebService = null;
//            callWebService = "[{\"RT_F\":\"1\",\"RT_D\":\"\",\"RT_LIST\":[{\"INSP_NO\":\"12080001\",\"INSP_NAME\":\"上料装置\",\n" + "\"INSP_MUST_TIME\":\"2016-08-02 10：00\"},{\"INSP_NO\":\"12080001\",\"INSP_NAME\":\"上料装置\",\n" +
//                    "\"INSP_MUST_TIME\":\"2016-08-02 10：00\"}]}]";
//            List<InspListClass> inspListClasses = JSON.parseArray(
//                    callWebService, InspListClass.class);
//            InspListClass inspListClass = inspListClasses.get(0);
//            final List<InspListClass.RTLISTBean> rt_list = inspListClass.getRT_LIST();
//
//            inSpLists = new ArrayList<>();
//            inSpLists.add(rt_list);
//            callWebService = "[{\"RT_F\":\"1\",\"RT_D\":\"\",\"RT_LIST\":[{\"INSP_NO\":\"12080001\",\"INSP_NAME\":\"上料装置\",\n" +
//                    "\"INSP_MUST_TIME\":\"2016-08-02 10：00\"}]}]";
//            List<InspListClass> inspListClasses2 = JSON.parseArray(
//                    callWebService, InspListClass.class);
//            InspListClass inspListClass2 = inspListClasses2.get(0);
//            final List<InspListClass.RTLISTBean> rt_list2 = inspListClass2.getRT_LIST();
//            inSpLists.add(rt_list2);
            for (int i = 0; i < inSpLists.size(); i++) {
                final List<InspListClass.RTLISTBean> list = inSpLists.get(i);
                View v = LayoutInflater.from(TestActivity.this).inflate(
                        R.layout.content_view, null);
                ListView tv = (ListView) v.findViewById(R.id.lv_insp);
                final ListView listView2 = (ListView) v.findViewById(R.id.lv_gongkaung);
                LinearLayout ll_gongkuang = (LinearLayout) v.findViewById(R.id.ll_gongkuang);
                final ImageView iv_down = (ImageView) v.findViewById(R.id.iv_down);
                final ImageView iv_up = (ImageView) v.findViewById(R.id.iv_up);
                ll_gongkuang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isClick) {
                            iv_down.setVisibility(View.GONE);
                            iv_up.setVisibility(View.VISIBLE);
                            listView2.setVisibility(View.VISIBLE);
                            isClick = true;
                        } else {
                            iv_down.setVisibility(View.VISIBLE);
                            iv_up.setVisibility(View.GONE);
                            listView2.setVisibility(View.GONE);
                            isClick = false;
                        }
                    }
                });
                CommonAdapter commonAdapter = new CommonAdapter(TestActivity.this, list,
                        R.layout.adapter_insp_new) {
                    @Override
                    public void convert(ViewHolder helper, Object item, int position) {
                        helper.setText(R.id.tv_insp_number, list.get(position)
                                .getINSP_NO());
                        helper.setText(R.id.tv_insp_name, list.get(position)
                                .getINSP_NAME());
                        helper.setText(R.id.tv_insp_time, list.get(position)
                                .getINSP_MUST_TIME());

                    }
                };
                if (ownerLists.size() != 0) {
                    final List<OwnerControlClass.RTLISTBean> rtlistBeen = ownerLists.get(i);
                    CommonAdapter commonAdapter1 = new CommonAdapter(mContext, rtlistBeen,
                            R.layout.adapter_gongkuang) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            helper.setText(R.id.DY, rtlistBeen.get(position).getDY() + "单元工况");
                            helper.setText(R.id.MC, rtlistBeen.get(position).getMC());
                            helper.setText(R.id.ZS, rtlistBeen.get(position).getZS() + "");
                            helper.setText(R.id.YWC, rtlistBeen.get(position).getYWC() + "");
                            String hgl = rtlistBeen.get(position)
                                    .getHGL();
                            helper.setProgessText(R.id.tv_percent, hgl);
                            double rpv = Double.parseDouble(hgl);
                            int result = (int) rpv;
                            helper.setRingProgressView(R.id.rpv_, result);
                        }
                    };
                    listView2.setAdapter(commonAdapter1);
                }

                tv.setAdapter(commonAdapter);
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus) {

        }

    }

    private List<InspListClass.RTLISTBean> insp_list = new ArrayList<>();
    //    private List<InspListClass.RTLISTBean> new_alarm_list = new ArrayList<>();
    private List<InspListClass.RTLISTBean> noChange_alarm_list = new ArrayList<>();
    private static final int GET_INSPLIST_SUCCESS = 0;  //拿list成功

    //待办查询点查询
    private void DoGetInspList() {
//        String callWebService = "[{\"RT_LIST\":[{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号上料装置\",\"INSP_NO\":\"12011001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号输送装置\",\"INSP_NO\":\"12021001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号信息绑定装置\",\"INSP_NO\":\"12031001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号除雾除尘装置\",\"INSP_NO\":\"12041001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号铅封验证装置\",\"INSP_NO\":\"12051001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号耐压装置\",\"INSP_NO\":\"12061001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线002号耐压装置\",\"INSP_NO\":\"12061012\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号外观识别装置\",\"INSP_NO\":\"12071001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号检定装置\",\"INSP_NO\":\"12081001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线002号检定装置\",\"INSP_NO\":\"12081002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线003号检定装置\",\"INSP_NO\":\"12081003\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线004号检定装置\",\"INSP_NO\":\"12081004\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线005号检定装置\",\"INSP_NO\":\"12081005\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线006号检定装置\",\"INSP_NO\":\"12081006\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线007号检定装置\",\"INSP_NO\":\"12081007\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线008号检定装置\",\"INSP_NO\":\"12081008\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线009号检定装置\",\"INSP_NO\":\"12081009\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线010号检定装置\",\"INSP_NO\":\"12081010\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号激光刻码装置\",\"INSP_NO\":\"12091001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号空箱缓存装置\",\"INSP_NO\":\"12101001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号下料装置\",\"INSP_NO\":\"12111001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"01\",\"EQUIP_NO\":\"1101212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"01号线001号刻码验证装置\",\"INSP_NO\":\"12121001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号上料装置\",\"INSP_NO\":\"12012001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号输送装置\",\"INSP_NO\":\"12022001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号信息绑定装置\",\"INSP_NO\":\"12032001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号除雾除尘装置\",\"INSP_NO\":\"12042001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号铅封验证装置\",\"INSP_NO\":\"12052001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号耐压装置\",\"INSP_NO\":\"12062001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线002号耐压装置\",\"INSP_NO\":\"12062002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号外观识别装置\",\"INSP_NO\":\"12072001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号检定装置\",\"INSP_NO\":\"12082001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线002号检定装置\",\"INSP_NO\":\"12082002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线003号检定装置\",\"INSP_NO\":\"12082003\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线004号检定装置\",\"INSP_NO\":\"12082004\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线005号检定装置\",\"INSP_NO\":\"12082005\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线006号检定装置\",\"INSP_NO\":\"12082006\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线007号检定装置\",\"INSP_NO\":\"12082007\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线008号检定装置\",\"INSP_NO\":\"12082008\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线009号检定装置\",\"INSP_NO\":\"12082009\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线010号检定装置\",\"INSP_NO\":\"12082010\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号激光刻码装置\",\"INSP_NO\":\"12092001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号空箱缓存装置\",\"INSP_NO\":\"12102001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号下料装置\",\"INSP_NO\":\"12112001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"02\",\"EQUIP_NO\":\"1102212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"02号线001号刻码验证装置\",\"INSP_NO\":\"12122001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号上料装置\",\"INSP_NO\":\"12013001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号输送装置\",\"INSP_NO\":\"12023001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号信息绑定装置\",\"INSP_NO\":\"12033001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号除雾除尘装置\",\"INSP_NO\":\"12043001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号铅封验证装置\",\"INSP_NO\":\"12053001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号耐压装置\",\"INSP_NO\":\"12063001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线002号耐压装置\",\"INSP_NO\":\"12063002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号外观识别装置\",\"INSP_NO\":\"12073001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号检定装置\",\"INSP_NO\":\"12083001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线002号检定装置\",\"INSP_NO\":\"12083002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线003号检定装置\",\"INSP_NO\":\"12083003\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线004号检定装置\",\"INSP_NO\":\"12083004\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线005号检定装置\",\"INSP_NO\":\"12083005\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线006号检定装置\",\"INSP_NO\":\"12083006\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线007号检定装置\",\"INSP_NO\":\"12083007\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线008号检定装置\",\"INSP_NO\":\"12083008\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线009号检定装置\",\"INSP_NO\":\"12083009\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线010号检定装置\",\"INSP_NO\":\"12083010\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号激光刻码装置\",\"INSP_NO\":\"12093001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号空箱缓存装置\",\"INSP_NO\":\"12103001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号下料装置\",\"INSP_NO\":\"12113001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"03\",\"EQUIP_NO\":\"1103212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"03号线001号刻码验证装置\",\"INSP_NO\":\"12123001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104201001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号上料装置\",\"INSP_NO\":\"12014001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104202001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号输送装置\",\"INSP_NO\":\"12024001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104203001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号信息绑定装置\",\"INSP_NO\":\"12034001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104204001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号除雾除尘装置\",\"INSP_NO\":\"12044001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104205001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号铅封验证装置\",\"INSP_NO\":\"12054001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104206001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号耐压装置\",\"INSP_NO\":\"12064001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104206002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线002号耐压装置\",\"INSP_NO\":\"12064002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104207001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号外观识别装置\",\"INSP_NO\":\"12074001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号检定装置\",\"INSP_NO\":\"12084001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208002000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线002号检定装置\",\"INSP_NO\":\"12084002\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208003000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线003号检定装置\",\"INSP_NO\":\"12084003\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208004000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线004号检定装置\",\"INSP_NO\":\"12084004\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208005000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线005号检定装置\",\"INSP_NO\":\"12084005\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208006000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线006号检定装置\",\"INSP_NO\":\"12084006\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208007000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线007号检定装置\",\"INSP_NO\":\"12084007\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208008000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线008号检定装置\",\"INSP_NO\":\"12084008\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208009000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线009号检定装置\",\"INSP_NO\":\"12084009\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104208010000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线010号检定装置\",\"INSP_NO\":\"12084010\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104209001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号激光刻码装置\",\"INSP_NO\":\"12094001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104210001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号空箱缓存装置\",\"INSP_NO\":\"12104001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104211001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号下料装置\",\"INSP_NO\":\"12114001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"},{\"PROC_CODE\":\"IOM01\",\"SYS_NO\":\"04\",\"EQUIP_NO\":\"1104212001000000000000\",\"INSP_MUST_TIME\":\"2016-10-11 09:00:00\",\"INSP_NAME\":\"04号线001号刻码验证装置\",\"INSP_NO\":\"12124001\",\"WORK_ORDER_NO\":\"A2016101110210006837\"}],\"DY_LIST\":[{\"SYS_NO\":\"01\",\"CNT\":22},{\"SYS_NO\":\"02\",\"CNT\":22},{\"SYS_NO\":\"03\",\"CNT\":22},{\"SYS_NO\":\"04\",\"CNT\":22}],\"RT_F\":\"1\",\"RT_D\":\"系统接口提示,待办巡检点查询成功!\"}]";
//        List<InspListClass> inspListClasses = JSON.parseArray(
//                callWebService, InspListClass.class);
//        InspListClass inspListClass = inspListClasses.get(0);
//        String rt_f = inspListClass.getRT_F();
//        if (rt_f.equals("1")) {
//            insp_list = inspListClass.getRT_LIST();
//            dy_list = inspListClass.getDY_LIST();
//            noChange_alarm_list.clear();
//            noChange_alarm_list.addAll(insp_list);
//            inSpLists.clear();
//            if (names!=null){
//                names=null;
//            }
//            names = new String[dy_list.size()+1];
//            names[0] = " 全部";
//            inSpLists.add(noChange_alarm_list);
//            for (int i = 0; i < dy_list.size(); i++) {
//                String sys_no = dy_list.get(i).getSYS_NO();
//                names[i + 1] =" "+ sys_no+"号线";
//                FindNewList(sys_no);
//            }
//            GetOwnerControlGKRunApply();
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

        showLoading("查询中...");
        WebModel.getInstance().getInspList(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<InspListClass> inspListClasses = JSON.parseArray(
                        callWebService, InspListClass.class);
                InspListClass inspListClass = inspListClasses.get(0);
                String rt_f = inspListClass.getRT_F();
                if (rt_f.equals("1")) {
                    insp_list = inspListClass.getRT_LIST();
                    dy_list = inspListClass.getDY_LIST();
                    noChange_alarm_list.clear();
                    noChange_alarm_list.addAll(insp_list);
                    inSpLists.clear();
                    if (names != null) {
                        names = null;
                    }
                    names = new String[dy_list.size() + 1];
                    names[0] = "全部";
                    inSpLists.add(noChange_alarm_list);
                    for (int i = 0; i < dy_list.size(); i++) {
                        String sys_no = dy_list.get(i).getSYS_NO();
                        names[i + 1] = sys_no + "号线";
                        FindNewList(sys_no);
                    }
                    GetOwnerControlGKRunApply();

                    //**********临时代码
//                    Message message = new Message();
//                    message.what = OWNE_SUCCESS;
//                    handler.sendMessage(message);
//                    hideLoading();
                    //*********
                } else {
                    final String rt_desc = inspListClass.getRT_D();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("查询失败！" + rt_desc);
                        }
                    });
                    hideLoading();
                    finish();
                }
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
                        finish();
                    }
                });
            }
        }, "IOM01", getXml("OPER_NO", ""));
    }

    private List<OwnerControlClass.RTLISTBean> ownerRt_list = new ArrayList<>();
    private List<OwnerControlClass.RTLISTBean> ownerNoChangeRt_list = new ArrayList<>();
    boolean isClick = false;
    private static final int OWNE_SUCCESS = 1;

    //工况信息查询
    private boolean haveResult = false;

    private void GetOwnerControlGKRunApply() {
//        final String callWebService = "[{\"RT_LIST\":[{\"HGL\":99.59,\"HGS\":6633,\"ZS\":6930,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"01\",\"YWC\":6660,\"MS\":\"01单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数6930只,完成6660只,合格率99.59\",\"BM\":\"0101\"},{\"HGL\":99.56,\"HGS\":7073,\"ZS\":7470,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"02\",\"YWC\":7104,\"MS\":\"02单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数7470只,完成7104只,合格率99.56\",\"BM\":\"0102\"},{\"HGL\":99.14,\"HGS\":6781,\"ZS\":7220,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"03\",\"YWC\":6840,\"MS\":\"03单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数7220只,完成6840只,合格率99.14\",\"BM\":\"0103\"},{\"HGL\":99.64,\"HGS\":6905,\"ZS\":7380,\"MC\":\"电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V\",\"DY\":\"04\",\"YWC\":6930,\"MS\":\"04单元工况:电子式-复费率远程费控智能电能表(工业用)2.0单相5(60)A220V,总数7380只,完成6930只,合格率99.64\",\"BM\":\"0104\"}],\"RT_F\":\"1\",\"RT_D\":\"系统接口提示,工况运行信息查询成功!\",\"DY_LIST\":[{\"SYS_NO\":\"01\",\"CNT\":22},{\"SYS_NO\":\"02\",\"CNT\":22},{\"SYS_NO\":\"03\",\"CNT\":22},{\"SYS_NO\":\"04\",\"CNT\":22}]}]";
//        List<OwnerControlClass> ownerControlClasses
//                = JSON.parseArray(callWebService, OwnerControlClass.class);
//        OwnerControlClass ownerControlClass = ownerControlClasses.get(0);
//        String rt_f = ownerControlClass.getRT_F();
//        if (rt_f.equals("1")) {
//            ownerRt_list = ownerControlClass.getRT_LIST();
//            if (ownerRt_list.size()!=0) {
//
//                owner_dy_list = ownerControlClass.getDY_LIST();
//                ownerNoChangeRt_list.clear();
//                ownerNoChangeRt_list.addAll(ownerRt_list);
////                    inSpListstr.clear();
////                    getDYNum(ownerRt_list.size());
//                haveResult = true;
//                ownerLists.clear();
//                ownerLists.add(ownerNoChangeRt_list);
//                for (int i = 0; i < owner_dy_list.size(); i++) {
//                    String sys_no = owner_dy_list.get(i).getSYS_NO();
//                    FindNewOwnerList(sys_no);
//                }
//            }else {
//                haveResult=false;
//            }
//
//            Message message = new Message();
//            message.what = OWNE_SUCCESS;
//            handler.sendMessage(message);
//        } else {
//            showToast(ownerControlClass.getRT_D());
//            haveResult = false;
//
//        }


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
                    if (ownerRt_list.size() != 0) {

//                        owner_dy_list = ownerControlClass.getDY_LIST();
                        ownerNoChangeRt_list.clear();
                        ownerNoChangeRt_list.addAll(ownerRt_list);
//                    inSpListstr.clear();
//                    getDYNum(ownerRt_list.size());
                        haveResult = true;
                        ownerLists.clear();
                        ownerLists.add(ownerNoChangeRt_list);
                        for (int i = 0; i < dy_list.size(); i++) {
                            String sys_no = dy_list.get(i).getSYS_NO();
                            FindNewOwnerList(sys_no);
                        }
                    } else {
                        haveResult = false;
                    }

                } else {
                    showToast(ownerControlClass.getRT_D());
                    haveResult = false;

                }

                Message message = new Message();
                message.what = OWNE_SUCCESS;
                handler.sendMessage(message);
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
        }, getXml("OPER_NO", ""), "IOM01");
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OWNE_SUCCESS:
                    initView();
                    hsm_container.notifyDataSetChanged(new MenuAdapter());
                    break;
            }
        }
    };


    private void FindNewList(String s) {
//        new_alarm_list.clear();
        List<InspListClass.RTLISTBean> new_alarm_list = new ArrayList<>();
        for (int i = 0; i < noChange_alarm_list.size(); i++) {
            if (noChange_alarm_list.get(i).getSYS_NO().equals(s)) {
                new_alarm_list.add(noChange_alarm_list.get(i));
            }
        }
        inSpLists.add(new_alarm_list);
    }

    private void FindNewOwnerList(String s) {
        List<OwnerControlClass.RTLISTBean> ownerNewRt_list = new ArrayList<>();
        for (int i = 0; i < ownerNoChangeRt_list.size(); i++) {
            if (ownerNoChangeRt_list.get(i).getDY().equals(s)) {
                ownerNewRt_list.add(ownerNoChangeRt_list.get(i));
            }
        }
        ownerLists.add(ownerNewRt_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedXmlUtil.getInstance(mContext).write("location", 0);
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


    /**
     * 去除数据重复
     *
     * @param li 数据集合
     * @return
     */
    public static List<String> getNewList(List<InspListClass.RTLISTBean> li) {
        List<String> list = new ArrayList<String>();
        //遍历数据集合
        for (int i = 0; i < li.size(); i++) {
            //得到数据
            String str = li.get(i).getINSP_MUST_TIME();
            //不重复
            //查看新集合中是否有指定的元素，如果没有则加入
            if (!list.contains(str)) {
                //添加数据
                list.add(str);
            }
        }
        return list;  //返回集合
    }

}
