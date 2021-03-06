package speedata.com.powermeasure.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.utils.TimeFormatePresenter;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.InspScanClass;
import speedata.com.powermeasure.bean.LoginOutClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_insp_second_new)
public class InspSecondAct extends FragActBase {

    private static final int INSP_SCAN_CONT_SUCCESS = 1;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_inspSecond_time;
    @ViewById
    TextView tv_inspSecond_name;
    @ViewById
    TextView tv_inspSecond_nowtime;
    @ViewById
    Button btn_inspSecond_fankui;
    @ViewById
    Button btn_inspSecond_trouble;
    @ViewById
    LinearLayout cb_linearLayout;
    @ViewById
    TextView tv_ALARM_CAUSE_NAME;
    @ViewById
    TextView tv_ALARM_TIME;
    @ViewById
    TextView tv_REAL_CAUSE_NAME;
    @ViewById
    TextView tv_DEAL_METHOD;
    @ViewById
    LinearLayout ll_last1;
    @ViewById
    LinearLayout ll_last2;
    @ViewById
            TextView tv_inspSecond_shebei;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INSP_SCAN_SUCCESS:
                    tv_inspSecond_name.setText(inspScanClass.getINSP_ADDER());
                    tv_inspSecond_time.setText(inspScanClass.getINSP_MUST_TIME());
                    tv_inspSecond_shebei.setText(inspScanClass.getINSP_NAME());
                    String currentTime = TimeFormatePresenter.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                    tv_inspSecond_nowtime.setText(currentTime);
                    String alarm_time = inspScanClass.getALARM_TIME();
                    if (!TextUtils.isEmpty(alarm_time)) {
                        ll_last1.setVisibility(View.VISIBLE);
                        ll_last2.setVisibility(View.VISIBLE);
                        tv_DEAL_METHOD.setText(inspScanClass.getDEAL_METHOD());
                        tv_REAL_CAUSE_NAME.setText(inspScanClass.getREAL_CAUSE_NAME());
                        tv_ALARM_TIME.setText(alarm_time);
                        tv_ALARM_CAUSE_NAME.setText(inspScanClass.getALARM_CAUSE_NAME());

                    }

                    setXml("INSP_NO", inspScanClass.getINSP_NO());

                    checkBoxList = new ArrayList<CheckBox>();
                    cont_list= inspScanClass.getRT_LIST();
                    for (int i = 0; i < cont_list.size(); i++) {
                        CheckBox checkBox = new CheckBox(mContext);
                        checkBox.setText(cont_list.get(i).getMC());
                        checkBox.setTextColor(Color.parseColor("#12AADD"));
                        checkBox.setChecked(true);
                        checkBox.setTextSize(18);
                        cb_linearLayout.addView(checkBox);
                        checkBoxList.add(checkBox);
                    }
                    break;
            }
        }
    };
    private List<CheckBox> checkBoxList=new ArrayList<>();
    private boolean isGZSB = false;
    private InspScanClass inspScanClass;
    private static final int INSP_SCAN_SUCCESS = 0;
    private List<InspScanClass.RTLISTBean> cont_list = new ArrayList<>();

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(InspSecondAct.this);
        initTitlebar();
        setSwipeEnable(false);
        inspScan(getXml("RFID", "1"));
    }
    //扫描RFID
    private void inspScan(String barcode) {
        showLoading("查询中...");
        WebModel.getInstance().inspScan(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<InspScanClass> inspScanClasses = JSON.parseArray(callWebService,
                        InspScanClass.class);
                inspScanClass = inspScanClasses.get(0);
                String rt_f = inspScanClass.getRT_F();
                if (rt_f.equals("1")) {
                    cont_list = inspScanClass.getRT_LIST();
                    Message message = new Message();
                    message.what = INSP_SCAN_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    showToast(inspScanClass.getRT_D());
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
        },getXml("OPER_NO", "1"),barcode);
    }
//    private void inspScanCont() {
//        String inspScanList_equip_no = getXml("inspScanList_equip_no", "");
//        String inspScanList_insp_no = getXml("inspScanList_insp_no", "");
//        showLoading("查询中...");
//        WebModel.getInstance().inspScanCont(new MResponseListener() {
//            @Override
//            public void onSuccess(MResponse response) {
//                String callWebService = String.valueOf(response.data);
//                List<InspScanContClass> inspScanContClasses =
//                        JSON.parseArray(callWebService, InspScanContClass.class);
//                inspScanContClass = inspScanContClasses.get(0);
//                String rt_f = inspScanContClass.getRT_F();
//                if (rt_f.equals("1")) {
//                    Message message=new Message();
//                    message.what=INSP_SCAN_CONT_SUCCESS;
//                    handler.sendMessage(message);
//                } else {
//                    final String rt_desc = inspScanContClass.getRT_D();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showToast("查询失败！" + rt_desc);
//                        }
//                    });
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
//                    }
//                });
//            }
//        }, inspScanList_insp_no,inspScanList_equip_no);
//    }


    //反馈
    @Click
    void btn_inspSecond_fankui() {
        showLoading("反馈中...");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < cont_list.size(); i++) {
            boolean checked = checkBoxList.get(i).isChecked();
            if (!checked) {
                showToast("请进行故障上报");
                hideLoading();
                return;
            }
            String cont = cont_list.get(i).getVALUE();
            stringBuffer.append(cont + ":");
            if (i == cont_list.size() - 1) {
                if (checked) {
                    stringBuffer.append("01");
                } else {
                    stringBuffer.append("02");
                }
            } else {
                if (checked) {
                    stringBuffer.append("01@");
                } else {
                    stringBuffer.append("02@");
                }
            }

//            resultList.setCONT_NO(cont_list.get(i).getINSP_CONT());
//            lists.add(resultList);
        }

        WebModel.getInstance().inspFeedback(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                        LoginOutClass.class);
                LoginOutClass inspFeedBack = loginOutClasses.get(0);
                String rt_f = inspFeedBack.getRT_F();
                if (rt_f.equals("1")) {
                    showToast("反馈成功!");
                    InspSecondAct.this.finish();
                } else {
                    showToast("反馈失败，" + inspFeedBack.getRT_D());
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
        },getXml("OPER_NO", "1"), getXml("INSP_NO", "1"), "", String.valueOf(stringBuffer));
    }

    //异常上报
    @Click
    void btn_inspSecond_trouble() {
//        isGZSB = true;
//        btn_inspSecond_fankui();
//        isGZSB = false;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < cont_list.size(); i++) {
            boolean checked = checkBoxList.get(i).isChecked();
            String cont = cont_list.get(i).getVALUE();
            stringBuffer.append(cont + ":");
            if (i == cont_list.size() - 1) {
                if (checked) {
                    stringBuffer.append("01");
                } else {
                    stringBuffer.append("02");
                }
            } else {
                if (checked) {
                    stringBuffer.append("01@");
                } else {
                    stringBuffer.append("02@");
                }
            }
        }
        setXml("inspFeedback_resultList", String.valueOf(stringBuffer));
        openAct(InspThirdAct.class, true);
        finish();
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
                          }, "巡检工单",
                getXml("OPER_NAME", ""), getXml("DEPT_NAME", ""), getXml("ROLE_NAME", "运维"), null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }



}
