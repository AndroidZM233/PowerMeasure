package speedata.com.powermeasure.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

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
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.AlListClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_inspect)
public class AlarmAct extends FragActBase {
    private static final int AL_LIST_SUCCESS = 0;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    ListView lv_insp;
    @ViewById
    ImageView btn_1;
    @ViewById
    ImageView btn_2;
    @ViewById
    ImageView btn_3;
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

    private List<AlListClass.RTLISTBean> alarm_list;
    private List<AlListClass.RTLISTBean> new_alarm_list = new ArrayList<>();
    private List<AlListClass.RTLISTBean> noChange_alarm_list = new ArrayList<>();
    private CommonAdapter commonAdapter;


    @Click
    void ll1(){
        setXml("GoToAl","00");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF0000"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#336699"));
        alarm_list.clear();
        alarm_list.addAll(noChange_alarm_list);
        commonAdapter.notifyDataSetChanged();
    }
    @Click
    void ll2() {
        setXml("GoToAl","01");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#FF0000"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#336699"));
        FindNewList("01");
    }
    @Click
    void ll5() {
        setXml("GoToAl","04");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#FF0000"));
        FindNewList("04");
    }

    private void FindNewList(String s) {
        new_alarm_list.clear();
        for (int i = 0; i < noChange_alarm_list.size(); i++) {
            if (noChange_alarm_list.get(i).getSYS_NO().equals(s)) {
                new_alarm_list.add(noChange_alarm_list.get(i));
            }
        }
        alarm_list.clear();
        alarm_list.addAll(new_alarm_list);
        commonAdapter.notifyDataSetChanged();

    }

    private void ListSetAdapter(CommonAdapter adapter01) {
        lv_insp.setAdapter(adapter01);
        lv_insp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setXml("AL_ORDER_NO", alarm_list.get(position).getWORK_ORDER_NO());
                setXml("AL_UNIT_NAME", alarm_list.get(position).getUNIT_NAME());
                setXml("AL_DEVICE_NAME", alarm_list.get(position).getDEVICE_NAME());
                setXml("AL_EQUIP_NAME", alarm_list.get(position).getEQUIP_NAME());
                setXml("AL_EQUIP_NO", alarm_list.get(position).getEQUIP_NO());
                setXml("AL_ALARM_LEVEL", alarm_list.get(position).getALARM_LEVEL());
                setXml("PLAN_BGN_TIME", alarm_list.get(position).getPLAN_BGN_TIME());
                setXml("PLAN_END_TIME", alarm_list.get(position).getPLAN_END_TIME());
                setXml("AL_ALARM_CAUSE_NAME", alarm_list.get(position).getALARM_CAUSE_NAME());
                setXml("AL_ALARM_DEAL_SUGGESTION", "");
                setXml("AL_ALARM_TIME", alarm_list.get(position).getALARM_TIME());
                String order_status = alarm_list
                        .get(position).getWO_STATUS();
                if (order_status.equals("待反馈")) {
                    openAct(AlarmSecondAct.class, true);
                }
                if (order_status.equals("待维修")) {
                    openAct(AlarmThirdAct.class, true);
                }

            }
        });
    }

    @Click
    void ll3() {
        setXml("GoToAl","02");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#FF0000"));
        tv4.setTextColor(Color.parseColor("#336699"));
        tv5.setTextColor(Color.parseColor("#336699"));
        FindNewList("02");
    }

    @Click
    void ll4() {
        setXml("GoToAl","03");
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        lv_insp.startAnimation(animation);
        tv1.setTextColor(Color.parseColor("#FF6600"));
        tv2.setTextColor(Color.parseColor("#336699"));
        tv3.setTextColor(Color.parseColor("#336699"));
        tv4.setTextColor(Color.parseColor("#FF0000"));
        tv5.setTextColor(Color.parseColor("#336699"));
        FindNewList("03");
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AL_LIST_SUCCESS:

                    commonAdapter = new CommonAdapter(mContext, alarm_list,
                            R.layout.adapter_al_new) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            String alarm_level = alarm_list.get(position).getALARM_LEVEL();
                            if (alarm_level.equals("严重")) {
                                helper.setTextColor(R.id.tv_al_ALARM_LEVEL,
                                        "#FF0000");
                                helper.setLLBackgroundIV(R.id.ll_al_list,
                                        R.drawable.al_red);
                            } else if (alarm_level.equals("重要")) {
                                helper.setTextColor(R.id.tv_al_ALARM_LEVEL,
                                        "#FF6600");
                                helper.setLLBackgroundIV(R.id.ll_al_list,
                                        R.drawable.al_or);
                            } else {
                                helper.setTextColor(R.id.tv_al_ALARM_LEVEL,
                                        "#333333");
                                helper.setLLBackgroundIV(R.id.ll_al_list,
                                        R.drawable.al_or);
                            }
                            helper.setText(R.id.tv_al_ALARM_LEVEL,
                                    alarm_level);
                            helper.setText(R.id.tv_al_ALARM_TIME,
                                    alarm_list.get(position).getALARM_TIME());
                            helper.setText(R.id.tv_al_EQUIP_NAME,
                                    alarm_list.get(position).getEQUIP_NAME());
//                            helper.setText(R.id.tv_al_ORDER_STATUS,
//                                    alarm_list.get(position).getWO_STATUS());
                            String wo_status = alarm_list.get(position).getWO_STATUS();
                            if (wo_status.equals("待反馈")) {
                                helper.setImageResource(R.id.tv_al_ORDER_STATUS,
                                        R.drawable.daifankui);
                            } else {
                                helper.setImageResource(R.id.tv_al_ORDER_STATUS,
                                        R.drawable.daiweixiu);
                            }

                            helper.setText(R.id.tv_al_ORDER_NO,
                                    alarm_list.get(position).getWORK_ORDER_NO());
                        }
                    };
                    ListSetAdapter(commonAdapter);
                    String goToAl = getXml("GoToAl", "");
                    if (goToAl!=null) {
                        if (goToAl.equals("01")) {
                            ll2();
                        } else if (goToAl.equals("02")) {
                            ll3();
                        } else if (goToAl.equals("03")) {
                            ll4();
                        }else if (goToAl.equals("04")){
                            ll5();
                        }else if (goToAl.equals("00")){
                            ll1();
                        }
                    }
                    break;
            }
        }
    };

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(AlarmAct.this);
        initTitlebar();
        setSwipeEnable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DoGetAlList();

    }

    //待办查询点查询
    private void DoGetAlList() {
        showLoading("查询中...");
        WebModel.getInstance().getAlarmlList(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
//                callWebService="[{\"RT_F\":\"1\",\"RT_D\":\"\",\"ALARM_LIST\":[{\"ORDER_NO\":\"D2016070212334\",\"UNIT_NAME\":\"单项01单元\"," +
//                        "\"DEVICE_NAME\":\"上料装置\",\"EQUIP_NAME\":\"吸盘\",\"ALARM_LEVEL\":\"严重\",\"ALARM_CAUSE_NAME\":\"信号丢失\",\"ALARM_DEAL_SUGGESTION\":\"\",\"ORDER_STATUS\":\"待反馈\",\"ALARM_TIME\":\"2016-08-02 12:00:00\"},{\"ORDER_NO\":\"D2016070212334\",\"UNIT_NAME\":\"单项01单元\"," +
//                        "\"DEVICE_NAME\":\"上料装置\",\"EQUIP_NAME\":\"吸盘\",\"ALARM_LEVEL\":\"严重\"" +
//                        ",\"ALARM_CAUSE_NAME\":\"信号丢失\",\"ALARM_DEAL_SUGGESTION\":\"\"" +
//                        ",\"ORDER_STATUS\":\"待维修\",\"ALARM_TIME\":\"2016-08-02 12:00:00\"}]}]";
                List<AlListClass> alListClasses = JSON.parseArray(
                        callWebService, AlListClass.class);
                AlListClass alListClass = alListClasses.get(0);
                String rt_f = alListClass.getRT_F();
                if (rt_f.equals("1")) {
                    noChange_alarm_list.clear();
                    alarm_list = alListClass.getRT_LIST();
                    noChange_alarm_list.addAll(alarm_list);
                    Message message = new Message();
                    message.what = AL_LIST_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    showToast("查询失败，" + alListClass.getRT_D());
                    finish();
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
                        finish();
                    }
                });
            }
        }, "IOM04", getXml("OPER_NO", "1"));
    }

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
        }, "报警工单列表",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
