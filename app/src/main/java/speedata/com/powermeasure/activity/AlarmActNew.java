package speedata.com.powermeasure.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.adapter.CommonAdapter;
import common.adapter.ViewHolder;
import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.HorizScroll.BaseAdapter;
import speedata.com.powermeasure.HorizScroll.HorizontalScrollMenu;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.AlListClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_alarm_act_new)
public class AlarmActNew extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;

    private HorizontalScrollMenu hsm_container;
    private List<List<AlListClass.RTLISTBean>> alLists=new ArrayList<>();

    private List<AlListClass.RTLISTBean> alarm_list=new ArrayList<>();
    private List<AlListClass.RTLISTBean> noChange_alarm_list = new ArrayList<>();
    private List<AlListClass.DYLISTBean> dy_list=new ArrayList<>();
    private String[] names;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(AlarmActNew.this);
        initTitlebar();
        setSwipeEnable(false);

    }
    public void initView() {

        hsm_container = (HorizontalScrollMenu) findViewById(R.id.hsm_container);
        hsm_container.setAdapter(new MenuAdapter());
    }


    @Override
    protected void onResume() {
        super.onResume();
        DoGetAlList();
    }

    class MenuAdapter extends BaseAdapter {
//        String[] names = new String[]
//                {"菜单一", "菜单二", "菜单三", "菜单四", "菜单五", "菜单六", "菜单七"};


        @Override
        public List<String> getMenuItems() {
            // TODO Auto-generated method stub
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews() {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            for (final List<AlListClass.RTLISTBean> list: alLists) {
                View v = LayoutInflater.from(AlarmActNew.this).inflate(
                        R.layout.content_view_al, null);
                final ListView listView2= (ListView) v.findViewById(R.id.lv_insp);
                CommonAdapter commonAdapter = new CommonAdapter(AlarmActNew.this, list,
                        R.layout.adapter_al_new) {
                    @Override
                    public void convert(ViewHolder helper, Object item, int position) {
                        String alarm_level = list.get(position).getALARM_LEVEL();
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
                                list.get(position).getALARM_TIME());
                        helper.setText(R.id.tv_al_EQUIP_NAME,
                                list.get(position).getEQUIP_NAME());
                        String wo_status = list.get(position).getWO_STATUS();
//                        if (TextUtils.isEmpty(wo_status)){
//                        }
                        if (wo_status.equals("待反馈")) {
                            helper.setImageResource(R.id.tv_al_ORDER_STATUS,
                                    R.drawable.daifankui);
                        } else {
                            helper.setImageResource(R.id.tv_al_ORDER_STATUS,
                                    R.drawable.daiweixiu);
                        }

                        helper.setText(R.id.tv_al_ORDER_NO,
                                list.get(position).getWORK_ORDER_NO());
                    }
                };
                listView2.setAdapter(commonAdapter);
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setXml("AL_ORDER_NO", list.get(position).getWORK_ORDER_NO());
                        setXml("AL_UNIT_NAMEd", list.get(position).getUNIT_NAME());
                        setXml("AL_DEVICE_NAME", list.get(position).getDEVICE_NAME());
                        setXml("AL_EQUIP_NAME", list.get(position).getEQUIP_NAME());
                        setXml("AL_EQUIP_NO", list.get(position).getEQUIP_NO());
                        setXml("AL_ALARM_LEVEL", list.get(position).getALARM_LEVEL());
                        setXml("PLAN_BGN_TIME", list.get(position).getPLAN_BGN_TIME());
                        setXml("PLAN_END_TIME", list.get(position).getPLAN_END_TIME());
                        setXml("AL_ALARM_CAUSE_NAME", list.get(position).getALARM_CAUSE_NAME());
                        setXml("AL_SECFLAG",list.get(position).getSECFLAG());
                        setXml("AL_ALARM_DEAL_SUGGESTION", "");
                        setXml("AL_ALARM_TIME", list.get(position).getALARM_TIME());
                        String order_status = list
                                .get(position).getWO_STATUS();
                        if (order_status.equals("待反馈")) {
                            openAct(AlarmSecondAct.class, true);
                        }
                        if (order_status.equals("待维修")) {
                            openAct(AlarmThirdAct.class, true);
                        }

                    }
                });
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus) {

        }

    }

    //按单元查询List
    private void FindNewList(String s) {
        List<AlListClass.RTLISTBean> new_alarm_list = new ArrayList<>();
        for (int i = 0; i < noChange_alarm_list.size(); i++) {
            if (noChange_alarm_list.get(i).getSYS_NO().equals(s)) {
                new_alarm_list.add(noChange_alarm_list.get(i));
            }
        }

        alLists.add(new_alarm_list);
    }
    private static final int AL_LIST_SUCCESS = 0;
    //待办查询点查询
    private void DoGetAlList() {
//        String callWebService = "[{\"RT_LIST\":[{\"WO_STATUS_ID\":\"05\",\"UNIT_NAME\":\"自动化单相电能表检定系统03号线\",\"EQUIP_NO\":\"1103202001007012000000\",\"ALARM_TIME\":\"2016-10-10 09:30:16\",\"DEVICE_NAME\":\"自动化单相电能表检定系统03号线输送装置\",\"PLAN_END_TIME\":null,\"EQUIP_NAME\":\"自动化单相电能表检定系统03号线001号输送装置012号旋转装置\",\"WORK_ORDER_NO\":\"D2016101010210006587\",\"ALARM_LEVEL\":\"严重\",\"WO_STATUS\":\"待反馈\",\"PLAN_BGN_TIME\":null,\"SYS_NO\":\"03\",\"ALARM_CAUSE_NAME\":\"工装板计数值>0，挡停位置长时间未检测到工装板\",\"ALARM_LEVEL_ID\":\"01\"}],\"RT_F\":\"1\",\"RT_D\":\"系统接口提示,报警工单查询成功!\",\"DY_LIST\":[{\"SYS_NO\":\"01\",\"CNT\":22},{\"SYS_NO\":\"02\",\"CNT\":22},{\"SYS_NO\":\"03\",\"CNT\":22},{\"SYS_NO\":\"04\",\"CNT\":22}],\"RT_F\":\"1\",\"RT_D\":\"系统接口提示,待办巡检点查询成功!\"}]";
//        List<AlListClass> alListClasses = JSON.parseArray(
//                callWebService, AlListClass.class);
//        AlListClass alListClass = alListClasses.get(0);
//        String rt_f = alListClass.getRT_F();
//        if (rt_f.equals("1")) {
//            noChange_alarm_list.clear();
//            alarm_list = alListClass.getRT_LIST();
//            dy_list = alListClass.getDY_LIST();
//            noChange_alarm_list.addAll(alarm_list);
//            alLists.clear();
//            names = new String[dy_list.size()+1];
//            names[0]=" 全部";
//            alLists.add(noChange_alarm_list);
//            for (int i = 0; i < dy_list.size(); i++) {
//
//                String sys_no = dy_list.get(i).getSYS_NO();
//                names[i+1]=" "+sys_no+"号线";
//                FindNewList(sys_no);
//            }
//            Message message = new Message();
//            message.what = AL_LIST_SUCCESS;
//            handler.sendMessage(message);
//        } else {
//            showToast("查询失败，" + alListClass.getRT_D());
//            finish();
//        }


        showLoading("查询中...");
        WebModel.getInstance().getAlarmlList(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<AlListClass> alListClasses = JSON.parseArray(
                        callWebService, AlListClass.class);
                AlListClass alListClass = alListClasses.get(0);
                String rt_f = alListClass.getRT_F();
                if (rt_f.equals("1")) {
                    noChange_alarm_list.clear();
                    alarm_list = alListClass.getRT_LIST();
                    dy_list = alListClass.getDY_LIST();
                    noChange_alarm_list.addAll(alarm_list);
                    alLists.clear();
                    names = new String[dy_list.size()+1];
                    names[0]="全部";
                    alLists.add(noChange_alarm_list);
                    for (int i = 0; i < dy_list.size(); i++) {

                        String sys_no = dy_list.get(i).getSYS_NO();
                        names[i+1]=sys_no+"号线";
                        FindNewList(sys_no);
                    }
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AL_LIST_SUCCESS:
                    initView();
                    hsm_container.notifyDataSetChanged(new MenuAdapter());

                    String clickGoToAL = getXml("clickGoToAL", "");
                    if (clickGoToAL.equals("1")){
                        String goToAl = getXml("GoToAl", "");
                        for (int i = 0; i < names.length; i++) {
                            if (names[i].equals(goToAl)){
                                hsm_container.clickItem(i);
                                hsm_container.moveItemToCenterByLocation(i);
                            }
                        }
                        setXml("clickGoToAL","0");
                    }

                    break;
            }
        }
    };

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
