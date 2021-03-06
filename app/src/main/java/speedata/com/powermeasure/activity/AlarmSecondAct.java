package speedata.com.powermeasure.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.AlKnlgClass;
import speedata.com.powermeasure.bean.LoginOutClass;
import speedata.com.powermeasure.model.WebModel;
import speedata.com.powermeasure.view.SelectDateDialog;
import speedata.com.powermeasure.view.SelectTimeDialog;

@EActivity(R.layout.activity_alarm_second_new)
public class AlarmSecondAct extends FragActBase {
    private static final int AL_KNLG_SUCCESS = 0;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    EditText et_alSecond_beizhu;
    @ViewById
    Button btn_alSecond_fankui;
    @ViewById
    Button btn_alSecond_weixiu;
    @ViewById
    TextView tv_alSecond_time;
    @ViewById
    TextView tv_alSecond_reason;
    @ViewById
    TextView tv_alSecond_method;
    @ViewById
    LinearLayout cb_dealLL;
    @ViewById
    LinearLayout cb_causeLL;
    @ViewById
    RadioGroup dialog_al_rg;
    @ViewById
    RadioGroup deal_al_rg;
    @ViewById
    LinearLayout ll_alSecond_planStart;
    @ViewById
    LinearLayout ll_alSecond_planEnd;
    @ViewById
    TextView tv_alSecond_planStart;
    @ViewById
    TextView tv_alSecond_planEnd;
    @ViewById
    EditText et_alSecond_cl;
    @ViewById
    EditText et_alSecond_yy;
    private List<AlKnlgClass.REALCAUSELISTBean> real_cause_list;
    private List<AlKnlgClass.REALDEALLISTBean> real_deal_list;
    private List<RadioButton> causeLists;
    private List<RadioButton> dealLists;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AL_KNLG_SUCCESS:
                    //显示实际原因选项
                    AlKnlgClass.REALCAUSELISTBean bean=new AlKnlgClass.REALCAUSELISTBean();
                    bean.setREAL_CAUSE("99999999");
                    bean.setREAL_CAUSE_NAME("误报");
                    real_cause_list.add(bean);
                    for (int i = 0; i < real_cause_list.size(); i++) {
                        RadioButton radioButton = new RadioButton(mContext);
                        radioButton.setText(real_cause_list.get(i).getREAL_CAUSE_NAME());
                        radioButton.setTextColor(Color.parseColor("#12AADD"));
                        dialog_al_rg.addView(radioButton);
                        causeLists.add(radioButton);
                    }
                    //显示实际处理方法选项
                    for (int j = 0; j < real_deal_list.size(); j++) {
                        RadioButton radioButton = new RadioButton(mContext);
                        radioButton.setText(real_deal_list.get(j).getDEAL_METHOD());
                        radioButton.setTextColor(Color.parseColor("#12AADD"));
                        deal_al_rg.addView(radioButton);
                        dealLists.add(radioButton);
                    }
                    break;
            }
        }
    };
    private AlertDialog alertDialog;
    private TextView dialog_al_endTime;
    private TextView dialog_al_startTime;
    private TextView dialog_al_startTime_hms;
    private TextView dialog_al_endTime_hms;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(AlarmSecondAct.this);
        setSwipeEnable(false);
        initUi();
        initDatas();
        AlKnlg();
    }

    private void initUi() {
        tv_alSecond_time.setText(getXml("AL_ALARM_TIME", "1"));
        tv_alSecond_reason.setText(getXml("AL_ALARM_CAUSE_NAME", "1"));
        String plan_bgn_time = getXml("PLAN_BGN_TIME", "");
        if (!TextUtils.isEmpty(plan_bgn_time)) {
            ll_alSecond_planStart.setVisibility(View.VISIBLE);
            tv_alSecond_planStart.setText(plan_bgn_time);
        } else {
            ll_alSecond_planStart.setVisibility(View.GONE);
        }
        String plan_end_time = getXml("PLAN_END_TIME", "");
        if (!TextUtils.isEmpty(plan_end_time)) {
            ll_alSecond_planEnd.setVisibility(View.VISIBLE);
            tv_alSecond_planEnd.setText(plan_end_time);
        } else {
            ll_alSecond_planEnd.setVisibility(View.GONE);
        }
    }

    private void initDatas() {
        causeLists = new ArrayList<RadioButton>();
        dealLists = new ArrayList<RadioButton>();
        initTitlebar();
    }

    //反馈
    @Click
    void btn_alSecond_fankui() {
        showLoading("反馈中...");
        String REAL_CAUSE = "";
        String DEAL_METHOD_CODE = "";
        for (int i = 0; i < real_cause_list.size(); i++) {
            if (causeLists.get(i).isChecked()) {
                REAL_CAUSE = real_cause_list.get(i).getREAL_CAUSE();
            }
        }
        for (int j = 0; j < real_deal_list.size(); j++) {
            if (dealLists.get(j).isChecked()) {
                DEAL_METHOD_CODE = real_deal_list.get(j).getDEAL_METHOD_CODE();
            }
        }
        String finalREAL_CAUSE ="";
        String finalDEAL_METHOD_CODE = "";
        String et_alSecond_yy_str = this.et_alSecond_yy.getText().toString();
        String et_alSecond_cl_str = et_alSecond_cl.getText().toString();
        if (TextUtils.isEmpty(et_alSecond_yy_str)){
            finalREAL_CAUSE=REAL_CAUSE;
        }else {
            finalREAL_CAUSE="IOM"+et_alSecond_yy_str;
        }

        if (TextUtils.isEmpty(et_alSecond_cl_str)){
            finalDEAL_METHOD_CODE=DEAL_METHOD_CODE;
        }else {
            finalDEAL_METHOD_CODE="IOM"+et_alSecond_cl_str;
        }

        WebModel.getInstance().alarmFeedback(new MResponseListener() {
                                                 @Override
                                                 public void onSuccess(MResponse response) {
                                                     String callWebService = String.valueOf(response.data);
                                                     List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                                                             LoginOutClass.class);
                                                     LoginOutClass alarmFeedback = loginOutClasses.get(0);
                                                     String rt_f = alarmFeedback.getRT_F();
                                                     if (rt_f.equals("1")) {
                                                         showToast("反馈成功！");
                                                         AlarmSecondAct.this.finish();
                                                     } else {
                                                         showToast(alarmFeedback.getRT_D());
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
                                             },getXml("OPER_NO", "1"), getXml("AL_ORDER_NO", ""), finalREAL_CAUSE, finalDEAL_METHOD_CODE,
                et_alSecond_beizhu.getText().toString());
    }

    //维修
    private TextView tv1;
    private TextView tv2;
    private EditText et;
    private RadioGroup rg;

    @Click
    void btn_alSecond_weixiu() {
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.dialog_al_second_new
                , (ViewGroup) findViewById(R.id.ll_main));
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.dialog_al_rg);
        final RadioButton radioButton = (RadioButton) view.findViewById(R.id.rb2);
        Button sureBtn= (Button) view.findViewById(R.id.btn_wxsq_sure);
        Button cancelBtn= (Button) view.findViewById(R.id.btn_wxsq_cancel);
        dialog_al_endTime = (TextView) view.findViewById(R.id.dialog_al_endTime);
        dialog_al_startTime = (TextView) view.findViewById(R.id.dialog_al_startTime);
        dialog_al_startTime_hms = (TextView) view.findViewById(R.id.dialog_al_startTime_hms);
        dialog_al_endTime_hms = (TextView) view.findViewById(R.id.dialog_al_endTime_hms);
        dialog_al_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                SelectDateDialog dateDialog = new SelectDateDialog(mContext, new SelectDateDialog
                        .ShowDate() {
                    @Override
                    public void showDate(String time) {
                        dialog_al_startTime.setText(time);
                    }
                }, str);
                dateDialog.show();
            }
        });

        dialog_al_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                SelectDateDialog dateDialog = new SelectDateDialog(mContext, new SelectDateDialog
                        .ShowDate() {
                    @Override
                    public void showDate(String time) {
                        dialog_al_endTime.setText(time);
                    }
                }, str);
                dateDialog.show();
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String format = simpleDateFormat.format(curDate);
        final String[] split = format.split(" ");
        dialog_al_startTime_hms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeDialog dateDialog = new SelectTimeDialog(mContext, new SelectTimeDialog
                        .ShowDate() {
                    @Override
                    public void showDate(String time) {
                        dialog_al_startTime_hms.setText(time);
                    }
                }, split[1]);
                dateDialog.show();
            }
        });

        dialog_al_endTime_hms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeDialog dateDialog = new SelectTimeDialog(mContext, new SelectTimeDialog
                        .ShowDate() {
                    @Override
                    public void showDate(String time) {
                        dialog_al_endTime_hms.setText(time);
                    }
                }, split[1]);
                dateDialog.show();
            }
        });

        radioGroup.check(radioButton.getId());
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmSecondAct.this);
        builder.setView(view);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXSQsure(radioButton);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        tv1 = (TextView) alertDialog.findViewById(R.id.dialog_al_equip_name);
        tv2 = (TextView) alertDialog.findViewById(R.id.dialog_al_order_no);
        et = (EditText) alertDialog.findViewById(R.id.dialog_al_et);
        rg = (RadioGroup) alertDialog.findViewById(R.id.dialog_al_rg);
        RadioButton radioButton1= (RadioButton) alertDialog.findViewById(R.id.rb1);
        radioButton1.setChecked(true);

        tv2.setText(getXml("AL_EQUIP_NAME", "1"));
        tv1.setText(getXml("AL_ORDER_NO", "1"));

    }

    private void WXSQsure(RadioButton radioButton) {
        showLoading("申请中...");
        String MAINT_CAUSE = "";
        if (radioButton.isChecked()) {
            MAINT_CAUSE = "02";
        } else {
            MAINT_CAUSE = "01";
        }
        final String finalMAINT_CAUSE = MAINT_CAUSE;
        String startTime = dialog_al_startTime.getText().toString()+" "+dialog_al_startTime_hms.getText().toString();
        String endTime = dialog_al_endTime.getText().toString()+" "+dialog_al_endTime_hms.getText().toString();

        WebModel.getInstance().maintApply(new MResponseListener() {
                                              @Override
                                              public void onSuccess(MResponse response) {
                                                  String callWebService = String.valueOf(response.data);
                                                  List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                                                          LoginOutClass.class);
                                                  LoginOutClass maintApply = loginOutClasses.get(0);
                                                  String rt_f = maintApply.getRT_F();
                                                  if (rt_f.equals("1")) {
                                                      showToast("申请成功");
                                                      AlarmSecondAct.this.finish();
                                                  } else {
                                                      showToast(maintApply.getRT_D());
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
                                          }, getXml("OPER_NO", "1"),tv1.getText().toString(), getXml("AL_EQUIP_NO","")
                , finalMAINT_CAUSE, et.getText().toString(), startTime, endTime);
    }

    //反馈状态工单明细
    private void AlKnlg() {
        showLoading("查询中...");
        WebModel.getInstance().getAlarmKnlg(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
//                callWebService="[{\"RT_F\":\"1\",\"RT_D\":\"\",\"REAL_CAUSE_LIST\"" +
//                        ":[{\"REAL_CAUSE\":\"20200101\",\"REAL_CAUSE_NAME\":\"上料机器人执行抓表动作超时\"}] " +
//                        ",\"REAL_DEAL_LIST\":[{\"DEAL_METHOD_CODE\":\"2020010102\",\"DEAL_METHOD\":" +
//                        "\"检修电机空开辅助触点接线\"}]}]";
                List<AlKnlgClass> alKnlgClasses = JSON.parseArray(callWebService,
                        AlKnlgClass.class);
                AlKnlgClass alKnlgClass = alKnlgClasses.get(0);
                String rt_f = alKnlgClass.getRT_F();
                if (rt_f.equals("1")) {
                    real_cause_list = alKnlgClass.getREAL_CAUSE_LIST();
                    real_deal_list = alKnlgClass.getREAL_DEAL_LIST();
                    Message message = new Message();
                    message.what = AL_KNLG_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    showToast(alKnlgClass.getRT_D());
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
        },getXml("OPER_NO", "1"), getXml("AL_ORDER_NO", "1"));
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
        }, "反馈状态工单",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
