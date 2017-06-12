package speedata.com.powermeasure.activity;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

@EActivity(R.layout.activity_trouble_second)
public class TroubleSecondAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_troubleSecond_time;
    @ViewById
    TextView tv_troubleSecond_name;
    @ViewById
    TextView tv_troubleSecond_dec;
    @ViewById
    TextView tv_troubleSecond_method;
    @ViewById
    EditText et_troubleSecond_beizhu;
    @ViewById
    Button btn_troubleSecond_xiufu;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(TroubleSecondAct.this);
        setSwipeEnable(false);
        initTitlebar();
        tv_troubleSecond_time.setText(getXml("knott_ALARM_TIME",""));
        tv_troubleSecond_name.setText(getXml("knott_ALARM_CAUSE_NAME",""));
        tv_troubleSecond_dec.setText(getXml("knott_APP_DESC",""));
        tv_troubleSecond_method.setText(getXml("knott_REPAIR_SCHEME",""));
    }

    @Click
    void btn_troubleSecond_xiufu(){
        showLoading("申请中...");
        WebModel.getInstance().knottFeedback(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                        LoginOutClass.class);
                LoginOutClass knottFeedback = loginOutClasses.get(0);
                String rt_f = knottFeedback.getRT_F();
                if (rt_f.equals("1")) {
                    showToast("修复验证成功!");
                    TroubleSecondAct.this.finish();
                } else {
                    showToast("修复验证失败，" + knottFeedback.getRT_D());
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
        },getXml("OPER_NO", "1"),getXml("knott_WORK_ORDER_NO",""),et_troubleSecond_beizhu.getText().toString());
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
        }, "疑难故障单",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
