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
import speedata.com.powermeasure.bean.RegularListClass;
import speedata.com.powermeasure.model.WebModel;

@EActivity(R.layout.activity_regularl)
public class RegularlAct extends FragActBase {
    private static final int RE_LIST_SUCCESS = 0;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    ListView lv_re;
    private List<RegularListClass.RTLISTBean> regular_list;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RE_LIST_SUCCESS:
                    CommonAdapter commonAdapter=new CommonAdapter(mContext,regular_list,
                            R.layout.adapter_sp_new) {
                        @Override
                        public void convert(ViewHolder helper, Object item, int position) {
                            helper.setText(R.id.tv_sp_startTime,
                                    regular_list.get(position).getSTART_TIME());
                            helper.setText(R.id.tv_sp_endTime,
                                    regular_list.get(position).getEND_TIME());
                            helper.setText(R.id.tv_sp_method,
                                    regular_list.get(position).getORDER_NO());
                            helper.setText(R.id.tv_ATMT_NAME,
                                    regular_list.get(position).getATMT_NAME());
                            if (position%2!=0){
                                helper.setLLBackgroundIV(R.id.ll_sp,R.drawable.green);
                            }else {
                                helper.setLLBackgroundIV(R.id.ll_sp,R.drawable.blue);
                            }
                        }
                    };
                    lv_re.setAdapter(commonAdapter);
                    lv_re.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showLoading("跳转中...");
                            setXml("RE_ORDER_NO",regular_list.get(position).getORDER_NO());
                            setXml("RE_START_TIME",regular_list.get(position).getSTART_TIME());
                            setXml("RE_END_TIME",regular_list.get(position).getEND_TIME());
                            setXml("RE_FILE_NAME", String.valueOf(regular_list.get(position).getFILE_NAME()));
                            setXml("RE_FILE_PATH", String.valueOf(regular_list.get(position).getFILE_PATH()));
                            openAct(RegularSecondAct.class,true);
                        }
                    });
                    break;
            }
        }
    };
    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(RegularlAct.this);
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoGetReList();
    }

    private void DoGetReList() {
        //定期维保列表查询
        showLoading("查询中...");
        WebModel.getInstance().getRegularlList(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
//                callWebService="[{\"RT_F\":\"1\",\"RT_D\":\"\",\"REGULAR_LIST\":[{\"ORDER_NO\":\"C2016070212334\",\"START_TIME\":\"2016-08-02 8：00\",\n" +
//                        "\"END_TIME\":\"2016-08-04 17：00\",\"FILE_NAME\":\"123456\",\"FILE_PATH\":\"/home/app/\"}]}]";
                List<RegularListClass> regularListClasses = JSON.parseArray(
                        callWebService, RegularListClass.class);
                RegularListClass regularListClass = regularListClasses.get(0);
                String rt_f = regularListClass.getRT_F();
                if (rt_f.equals("1")){
                    regular_list = regularListClass.getRT_LIST();
                    Message message=new Message();
                    message.what=RE_LIST_SUCCESS;
                    handler.sendMessage(message);
                }else {
                    showToast("查询失败，"+regularListClass.getRT_D());
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
                        }else {
                            showToast("出错了！");
                        }
                        hideLoading();
                    }
                });
            }
        },"IOM03",getXml("OPER_NO", ""));
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
        }, "定期维保",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
