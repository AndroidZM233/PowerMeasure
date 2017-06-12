package speedata.com.powermeasure.activity;

import android.content.Context;

import org.androidannotations.annotations.EActivity;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import speedata.com.powermeasure.R;

/**
 * Created by 张明_ on 2016/11/19.
 */
 @EActivity(R.layout.act_insp_scan_list)
public class InspScanListAct extends FragActBase {
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
//    @ViewById
//    CustomTitlebar titlebar;
//    @ViewById
//    ListView insp_scan_lv;
//    private CommonAdapter commonAdapter;
//
//    @AfterViews
//    protected void main() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        App.getInstance().addActivity(InspScanListAct.this);
//        initTitlebar();
//        insp_scan_lv= (ListView) findViewById(R.id.insp_scan_lv);
//        setSwipeEnable(false);
//        inspScan(getXml("RFID", "1"));
//    }
//
//    @Override
//    protected Context regieterBaiduBaseCount() {
//        return null;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        String fankuiSuccess = getXml("fankuiSuccess", "");
//        if (fankuiSuccess.equals("1")){
//            int inspScanList_position = Integer.parseInt(getXml("inspScanList_position", ""));
//            String noChange_equip_name = noChange_cont_list.get(inspScanList_position).getEQUIP_NAME();
//            cont_list.get(inspScanList_position).setEQUIP_NAME(noChange_equip_name+"(已反馈)");
//            cont_list.get(inspScanList_position).setState("1");
//            commonAdapter.notifyDataSetChanged();
//            setXml("fankuiSuccess","2");
//        }
//    }
//
//    @Override
//    protected void initTitlebar() {
//        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
//        titlebar.setAttrs(new View.OnClickListener() {
//                              @Override
//                              public void onClick(View v) {
//                                  finish();
//                              }
//                          }, "巡检工单列表",
//                getXml("OPER_NAME", ""), getXml("DEPT_NAME", ""), getXml("ROLE_NAME", "运维"), null);
//    }
//
//    @Override
//    public void onEventMainThread(ViewMessage viewMessage) {
//
//    }
//    private InspScanClass inspScanClass;
//    private static final int INSP_SCAN_SUCCESS = 0;
//    private List<InspScanClass.RTLISTBean> cont_list=new ArrayList<>();
//    private List<InspScanClass.RTLISTBean> noChange_cont_list=new ArrayList<>();
//
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case INSP_SCAN_SUCCESS:
////                    String insp_actl_gj = inspScanClass.getINSP_ACTL_GJ();
////                    String[] split = insp_actl_gj.split("@");
////                    if (!TextUtils.isEmpty(insp_actl_gj)) {
////                        setXml("GoToAl", split[0]);
////                        setXml("clickGoToAL", "1");
////                        Intent intent = new Intent();
////                        intent.setAction("com.test");
////                        sendBroadcast(intent);
////                    }
//
//                    commonAdapter = new CommonAdapter(mContext, cont_list, R.layout.adapter_inspscan) {
//                        @Override
//                        public void convert(ViewHolder helper, Object item, int position) {
//                            String state = cont_list.get(position).getState();
//                            if (TextUtils.isEmpty(state)){
//                                helper.setTextColor(equip_name,"#EA2000");
//                            }else {
//                                helper.setTextColor(equip_name,"#9492AD");
//                            }
//                            helper.setText(equip_name, cont_list.get(position).getEQUIP_NAME());
//                        }
//                    };
//                    insp_scan_lv.setAdapter(commonAdapter);
//                    insp_scan_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            String equip_no = cont_list.get(position).getEQUIP_NO();
//                            String insp_no = cont_list.get(position).getINSP_NO();
//                            setXml("inspScanList_equip_no",equip_no);
//                            setXml("inspScanList_insp_no",insp_no);
//                            setXml("inspScanList_position",position+"");
//                            openAct(InspSecondAct.class,true);
//                        }
//                    });
//
//
//                    break;
//
//            }
//        }
//    };
//    //扫描RFID
//    private void inspScan(String barcode) {
//        showLoading("查询中...");
//        WebModel.getInstance().inspScan(new MResponseListener() {
//            @Override
//            public void onSuccess(MResponse response) {
//                String callWebService = String.valueOf(response.data);
//                List<InspScanClass> inspScanClasses = JSON.parseArray(callWebService,
//                        InspScanClass.class);
//                inspScanClass = inspScanClasses.get(0);
//                String rt_f = inspScanClass.getRT_F();
//                if (rt_f.equals("1")) {
//                    cont_list = inspScanClass.getRT_LIST();
//                    if (noChange_cont_list.size()==0){
//                        noChange_cont_list.addAll(cont_list);
//                    }
//                    Message message = new Message();
//                    message.what = INSP_SCAN_SUCCESS;
//                    handler.sendMessage(message);
//                } else {
//                    showToast(inspScanClass.getRT_D());
//                    finish();
//                }
//                hideLoading();
//            }
//
//            @Override
//            public void onError(final MResponse response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.msg != null) {
//                            showToast(response.msg.toString());
//                        } else {
//                            showToast("出错了！");
//                        }
//                        hideLoading();
//                        finish();
//                    }
//                });
//            }
//        }, getXml("OPER_NO", ""), barcode);
//    }
}
