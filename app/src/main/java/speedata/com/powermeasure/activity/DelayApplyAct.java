package speedata.com.powermeasure.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.LoginOutClass;
import speedata.com.powermeasure.model.WebModel;
import speedata.com.powermeasure.view.SelectDateDialog;
import speedata.com.powermeasure.view.SelectTimeDialog;

@EActivity(R.layout.activity_delay_apply)
public class DelayApplyAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_delay_time;
    @ViewById
    TextView tv_delay_year;
    @ViewById
    TextView tv_delay_hour;
    @ViewById
    Button btn_delay_sure;
    @ViewById
    LinearLayout ll_planEnd;
    @ViewById
    TextView tv_alSecond_planEnd;
    @ViewById
    EditText AL_SECFLAG;
    @ViewById
    LinearLayout ll_AL_SECFLAG;

    private SimpleDateFormat formatter;
    private String str;
    private Date date;
    private String[] hour;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(DelayApplyAct.this);
        setSwipeEnable(false);
        initTitlebar();
        String al_secflag = getXml("AL_SECFLAG", "3");
        if (al_secflag.equals("1")){
            ll_AL_SECFLAG.setVisibility(View.VISIBLE);
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        str = formatter.format(curDate);
        hour = str.split(" ");
        tv_delay_hour.setText(hour[1]);
        tv_delay_year.setText(hour[0]);
        String plan_end_time = getXml("PLAN_END_TIME", "");
        if (TextUtils.isEmpty(plan_end_time)){
            ll_planEnd.setVisibility(View.GONE);
        }else {
            ll_planEnd.setVisibility(View.VISIBLE);
            tv_delay_time.setText(getXml("PLAN_BGN_TIME",""));
            tv_alSecond_planEnd.setText(plan_end_time);
        }

    }

    @Click
    void tv_delay_year() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        SelectDateDialog dateDialog = new SelectDateDialog(mContext, new SelectDateDialog
                .ShowDate() {
            @Override
            public void showDate(String time) {
                tv_delay_year.setText(time);
            }
        }, str);
        dateDialog.show();
    }

    @Click
    void btn_delay_sure() {
        String year = tv_delay_year.getText().toString();
        String hour = tv_delay_hour.getText().toString();
        String lastTime = year + " " + hour;
        String al_secflag_str = AL_SECFLAG.getText().toString();
        String al_secflag = getXml("AL_SECFLAG", "3");

        showLoading("延期中...");
        WebModel.getInstance().delayApply(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                        LoginOutClass.class);
                LoginOutClass maintFeedback = loginOutClasses.get(0);
                String rt_f = maintFeedback.getRT_F();
                if (rt_f.equals("1")) {
                    showToast("延期成功！");
                    openAct(AlarmActNew.class,true);
                    finish();
                } else {
                    showToast(maintFeedback.getRT_D());
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
        },getXml("OPER_NO", "1"), getXml("AL_ORDER_NO", ""), lastTime,al_secflag,al_secflag_str);
    }

    @Click
    void tv_delay_hour() {

        SelectTimeDialog dateDialog = new SelectTimeDialog(mContext, new SelectTimeDialog
                .ShowDate() {
            @Override
            public void showDate(String time) {
                tv_delay_hour.setText(time);
            }
        }, hour[1]);
        dateDialog.show();

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
        }, "延期日期",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
