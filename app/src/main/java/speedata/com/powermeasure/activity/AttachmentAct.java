package speedata.com.powermeasure.activity;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.FTPUtils;
import common.utils.FileUtils;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;

@EActivity(R.layout.activity_attachment)
public class AttachmentAct extends FragActBase {
    @ViewById
    Button btn_att_save;
    @ViewById
    EditText et_att_method;
    @ViewById
    CustomTitlebar titlebar;
    private FileUtils fileUtils;
    private String file_name;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(AttachmentAct.this);
        setSwipeEnable(false);
        initTitlebar();
        fileUtils = new FileUtils(mContext);
        file_name = getXml("b_fileName", "test.txt");
        try {
            String read = fileUtils.readDOCX("/data/data/speedata.com.powermeasure/files/"+file_name);
            et_att_method.setText(read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Click
    void btn_att_save(){
        try {
            fileUtils.save(file_name,et_att_method.getText().toString());
            showToast("保存成功");
            CommitToFTP();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传FTP
    private void CommitToFTP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FTPUtils ftpUtils=FTPUtils.getInstance();
                boolean initFTPSetting = ftpUtils
                        .initFTPSetting("191.168.1.61", 21, "ftpiom", "ftpiom");
                if (initFTPSetting){
                    String iom_path = getXml("IOM_PATH", "");
                    ftpUtils.uploadFile("/data/data/speedata.com.powermeasure/files/"+file_name
                            ,file_name, iom_path+"/IOM02");
                }

            }
        }).start();

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
        }, "附件查看",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
