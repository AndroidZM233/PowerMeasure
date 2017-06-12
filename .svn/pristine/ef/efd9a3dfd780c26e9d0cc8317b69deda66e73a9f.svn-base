package speedata.com.powermeasure.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.Date;

import common.http.MResponse;
import common.http.MResponseListener;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.LoginOutClass;
import speedata.com.powermeasure.model.HttpModel;

/**
 * Created by 张明_ on 2016/8/16.
 */
public class PostponeDialog extends Dialog {
    private EditText etTime;
    private Button btnOk;
    private Button btnBack;
    private TextView tvNum;
    private Context mContext;

    public PostponeDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public PostponeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PostponeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        setContentView(R.layout.dialog_postpone);
        etTime= (EditText) findViewById(R.id.et_postDialog_time);
        tvNum= (TextView) findViewById(R.id.tv_postDialog_num);
        btnBack= (Button) findViewById(R.id.btn_postDialog_back);
        btnOk= (Button) findViewById(R.id.btn_postDialog_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Date mTime= (Date) etTime.getText();
                        HttpModel.getInstance().delayApply(new MResponseListener() {
                            @Override
                            public void onSuccess(MResponse response) {
                                String callWebService = String.valueOf(response.data);
                                LoginOutClass delayApply= JSON.parseObject(callWebService,
                                        LoginOutClass.class);
                                String rt_f = delayApply.getRT_F();
                                if (rt_f.equals("1")){
                                    Toast.makeText(mContext,"延期成功",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext,delayApply.getRT_D(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(MResponse response) {
                                Toast.makeText(mContext,response.msg.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        },tvNum.getText().toString(),mTime);
                    }
                }).start();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
