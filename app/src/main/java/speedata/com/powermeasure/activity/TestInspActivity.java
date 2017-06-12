package speedata.com.powermeasure.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.io.File;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import speedata.com.powermeasure.R;
@EActivity(R.layout.activity_test_insp)
public class TestInspActivity extends FragActBase {

    private Button btntest;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btntest= (Button) findViewById(R.id.btntest);
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("com.olivephone.edit");
                String fileType="application/vnd.openxmlformats-officedocument" +
                        ".wordprocessingml.document"; //docx
//                String fileType="application/vnd.ms-word"; //doc
//                String fileType="text/plain"; //txt
//                String fileType="text/html"; //html
//                String fileType="application/vnd.ms-excel"; //xls
//                String fileType="application/vnd.openxmlformats-officedocument" +
//                        ".spreadsheetml.sheet"; //xlsx
//                String fileType="application/vnd.ms-powerpoint"; //ppt
//                String fileType="application/vnd.openxmlformats-officedocument" +
//                        ".presentationml.presentation"; //pptx
                intent.setDataAndType(Uri.fromFile(
                        new File("/sdcard/2.docx")),fileType);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    showToast("检测到系统尚未安装OliveOffice的apk程序");
                    e.printStackTrace();
                }
            }
        });
    }

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
}
