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
import common.utils.FTPUtils;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.LoginOutClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_specond_second_new)
public class RegularSecondAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btn_spSecond_fankui;
    @ViewById
    EditText et_spSecond_trouble;
    @ViewById
    TextView tv_spSecond_time;
    @ViewById
    TextView tv_spSecond_start_time;
    @ViewById
    TextView tv_spSecond_fujian;
    private String b_fileName;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(RegularSecondAct.this);
        initTitlebar();
        setSwipeEnable(false);
        tv_spSecond_time.setText(getXml("RE_END_TIME", ""));
        tv_spSecond_start_time.setText(getXml("RE_START_TIME", ""));

        String file_name = getXml("RE_FILE_NAME", "test.txt");
        String file_path = getXml("RE_FILE_PATH", "/data");
        DownFromFTP(file_name, file_path);
        tv_spSecond_fujian.setText(file_name);
    }

    @Click
    void tv_spSecond_fujian() {
        openAct(AttachmentActSecond.class, true);
    }

    //从FTP下载
    private void DownFromFTP(final String file_name, final String file_path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FTPUtils ftpUtils = FTPUtils.getInstance();
                boolean initFTPSetting = ftpUtils
                        .initFTPSetting("191.168.1.61", 21, "ftpiom", "ftpiom");
                if (initFTPSetting) {
                    b_fileName = ftpUtils.downLoadFile("/data/data/speedata.com.powermeasure/files/" + file_name
                            , file_name, file_path, "C_");
                    setXml("b_fileName",b_fileName);
                }

            }
        }).start();

    }

    @Click
    void btn_spSecond_fankui() {
        showLoading("反馈中...");
        WebModel.getInstance().specialFeedback(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<LoginOutClass> loginOutClasses = JSON.parseArray(callWebService,
                        LoginOutClass.class);
                LoginOutClass spBack = loginOutClasses.get(0);
                String rt_f = spBack.getRT_F();
                if (rt_f.equals("1")) {
                    showToast("反馈成功");
                    finish();
                } else {
                    showToast("反馈失败，" + spBack.getRT_D());
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
        },getXml("OPER_NO", "1"), getXml("RE_ORDER_NO", "1"), et_spSecond_trouble.getText().toString());

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
        }, "定期维保反馈",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
