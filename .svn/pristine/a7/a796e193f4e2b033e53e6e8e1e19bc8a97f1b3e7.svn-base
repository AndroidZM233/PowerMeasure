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
import speedata.com.powermeasure.bean.EquipUpQueryClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_equipment_replace)
public class EquipmentReplaceAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    ListView lv_EReplace;
    private static final int Get_SPLIST_SUCCESS=0;

    private static final int GET_TROUBLE_LIST_SUCCESS = 0;  //拿list成功
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_TROUBLE_LIST_SUCCESS:
                    CommonAdapter commonAdapter=new CommonAdapter(mContext,rt_list,
                            R.layout.adapter_equipment_replace) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            helper.setText(R.id.tv_WORK_ORDER_NO,
                                    rt_list.get(position).getWORK_ORDER_NO());
                            helper.setText(R.id.tv_EQUIP_NAME,
                                    rt_list.get(position).getEQUIP_NAME());
                            helper.setText(R.id.tv_APP_USR_NAME,
                                    rt_list.get(position).getAPP_USR_NAME());
                            helper.setText(R.id.tv_APP_TIME,
                                    rt_list.get(position).getAPP_TIME());
                        }
                    };
                    lv_EReplace.setAdapter(commonAdapter);
                    lv_EReplace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showLoading("跳转中...");
                            setXml("eq_UP_REASON",rt_list.get(position).getUP_REASON());
                            setXml("eq_FUNC_CHANGE",rt_list.get(position).getFUNC_CHANGE());
                            setXml("eq_VERSION_NO",rt_list.get(position).getVERSION_NO());
                            setXml("eq_PER_SYSTEM",rt_list.get(position).getPER_SYSTEM());
                            setXml("eq_PLAN_BGN_TIME",rt_list.get(position).getPLAN_BGN_TIME());
                            setXml("eq_PLAN_END_TIME",rt_list.get(position).getPLAN_END_TIME());
                            setXml("eq_ATMT_SN",rt_list.get(position).getATMT_SN());
                            setXml("eq_WORK_ORDER_NO",rt_list.get(position).getWORK_ORDER_NO());
                            openAct(EquipmentRepSecondAct.class,true);
                        }
                    });
                    break;
            }
        }
    };
    private List<EquipUpQueryClass.RTLISTBean> rt_list;

    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(EquipmentReplaceAct.this);
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoequipUpQuery();
    }
    private void DoequipUpQuery() {
        showLoading("查询中...");
        WebModel.getInstance().equipUpQuery(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<EquipUpQueryClass> equipUpQueryClasses = JSON.parseArray(callWebService
                        , EquipUpQueryClass.class);
                EquipUpQueryClass equipUpQueryClass = equipUpQueryClasses.get(0);
                String rt_f = equipUpQueryClass.getRT_F();
                if (rt_f.equals("1")){
                    rt_list = equipUpQueryClass.getRT_LIST();
                    Message message=new Message();
                    message.what=Get_SPLIST_SUCCESS;
                    handler.sendMessage(message);
                }else {
                    showToast("查询失败，"+equipUpQueryClass.getRT_D());
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
        },"IOM07",getXml("OPER_NO", ""));
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
        }, "升级管理",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
