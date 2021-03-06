package speedata.com.powermeasure.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import speedata.com.powermeasure.R;

public class SetIPActivity extends FragActBase {

    private EditText editText;
    private Button button;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);
        editText= (EditText) findViewById(R.id.et_ip);
        editText.setText(getXml("new_ip","http://191.168.1.61:8080/iom/ws/services/IOMWS?wsdl"));
        button= (Button) findViewById(R.id.btn_ip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strIP=editText.getText().toString();
                if (TextUtils.isEmpty(strIP)){
                    showToast("请填写ip地址");
                }else {
                    setXml("new_ip",strIP);
                    showToast("设置成功");
                    finish();
                }
            }
        });

    }
}
