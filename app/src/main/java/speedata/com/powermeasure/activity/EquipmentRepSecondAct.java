package speedata.com.powermeasure.activity;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

@EActivity(R.layout.activity_equipment_second)
public class EquipmentRepSecondAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_eqSecond_reason;
    @ViewById
    TextView tv_FUNC_CHANGE;
    @ViewById
    TextView tv_VERSION_NO;
    @ViewById
    TextView tv_PER_SYSTEM;
    @ViewById
    TextView tv_ATMT_SN;
    @ViewById
    TextView tv_PLAN_BGN_TIME;
    @ViewById
    TextView tv_PLAN_END_TIME;
    @ViewById
    Button btn_eqSecond_shengji;


    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(EquipmentRepSecondAct.this);
        setSwipeEnable(false);
        initTitlebar();

        tv_eqSecond_reason.setText(getXml("eq_UP_REASON",""));
        tv_FUNC_CHANGE.setText(getXml("eq_FUNC_CHANGE",""));
        tv_VERSION_NO.setText(getXml("eq_VERSION_NO",""));
        tv_PER_SYSTEM.setText(getXml("eq_PER_SYSTEM",""));
        tv_ATMT_SN.setText(getXml("eq_ATMT_SN",""));
        tv_PLAN_BGN_TIME.setText(getXml("eq_PLAN_BGN_TIME",""));
        tv_PLAN_END_TIME.setText(getXml("eq_PLAN_END_TIME",""));

    }

    @Click
    void btn_eqSecond_shengji(){
        showLoading("申请中...");
        WebModel.getInstance().equipUpFeedback(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                        LoginOutClass.class);
                LoginOutClass knottFeedback = loginOutClasses.get(0);
                String rt_f = knottFeedback.getRT_F();
                if (rt_f.equals("1")) {
                    showToast("升级成功!");
                    EquipmentRepSecondAct.this.finish();
                } else {
                    showToast("升级失败，" + knottFeedback.getRT_D());
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
        },getXml("OPER_NO", "1"),getXml("eq_WORK_ORDER_NO",""));
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
        }, "设备升级单",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
