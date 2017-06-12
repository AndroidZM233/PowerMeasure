package speedata.com.powermeasure.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import common.adapter.CommonAdapter;
import common.adapter.ViewHolder;
import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.KnottGzQueryClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_trouble)
public class TroubleAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    ListView lv_trouble;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case knottGzQuery_SUCCESS:
                    CommonAdapter commonAdapter=new CommonAdapter(mContext,rt_list,
                            R.layout.adapter_knott) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            helper.setText(R.id.tv_EQUIP_NAME,rt_list.get(position).getEQUIP_NAME());
                            helper.setText(R.id.tv_ALARM_TIME,rt_list.get(position).getALARM_TIME());
                            helper.setText(R.id.tv_APP_TIME,rt_list.get(position).getAPP_TIME());
                            helper.setText(R.id.tv_APPR_TIME,rt_list.get(position).getAPPR_TIME());
                            helper.setText(R.id.tv_WORK_ORDER_NO,rt_list.get(position).getWORK_ORDER_NO());
                        }
                    };
                    lv_trouble.setAdapter(commonAdapter);
                    lv_trouble.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            setXml("knott_ALARM_TIME",rt_list.get(position).getALARM_TIME());
                            setXml("knott_ALARM_CAUSE_NAME",rt_list.get(position).getALARM_CAUSE_NAME());
                            setXml("knott_APP_DESC",rt_list.get(position).getAPP_DESC());
                            setXml("knott_REPAIR_SCHEME",rt_list.get(position).getREPAIR_SCHEME());
                            setXml("knott_WORK_ORDER_NO",rt_list.get(position).getWORK_ORDER_NO());
                            openAct(TroubleSecondAct.class,true);
                        }
                    });
                    break;
            }
        }
    };
    private List<KnottGzQueryClass.RTLISTBean> rt_list;
    private static final int knottGzQuery_SUCCESS=0;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(TroubleAct.this);
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoKnottGzQuery();
    }

    private void DoKnottGzQuery() {
        showLoading("查询中...");
        WebModel.getInstance().knottGzQuery(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<KnottGzQueryClass> knottGzQueryClasses =
                        JSON.parseArray(callWebService, KnottGzQueryClass.class);
                KnottGzQueryClass knottGzQueryClass = knottGzQueryClasses.get(0);
                String rt_f = knottGzQueryClass.getRT_F();
                if (rt_f.equals("1")){
                    rt_list = knottGzQueryClass.getRT_LIST();
                    Message message = new Message();
                    message.what = knottGzQuery_SUCCESS;
                    handler.sendMessage(message);
                }else {
                    showToast(knottGzQueryClass.getRT_D());
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
        },"IOM05",getXml("OPER_NO",""));
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
        }, "疑难故障",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
