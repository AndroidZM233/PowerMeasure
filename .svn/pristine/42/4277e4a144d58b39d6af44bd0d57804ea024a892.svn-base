/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version v1.0
 *
 */

package speedata.com.powermeasure.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.http.MResponse;
import common.http.MResponseListener;
import common.view.BadgeView;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.SysCountClass;
import speedata.com.powermeasure.model.WebModel;

import static common.utils.LogUtil.DEGUG_MODE;

@EActivity(R.layout.act_main_new)
public class MainAct extends FragActBase {
    private static final String TAG = DEGUG_MODE ? "MainAct" : MainAct.class.getSimpleName();
    private static final boolean debug = true;
    @ViewById
    ImageView num1;
    @ViewById
    ImageView num2;
    @ViewById
    ImageView num3;
    @ViewById
    ImageView num4;
    @ViewById
    ImageView num5;
    @ViewById
    ImageView num6;
    @ViewById
    ImageView num7;
    private List<ImageView> imageViewList;
    private List<SysCountClass.RTLISTBean> rt_list;
    private List<BadgeView> badgeViewList;
    private int rtlist_size;


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        PushAgent mPushAgent = PushAgent.getInstance(mContext);
////        mPushAgent.enable();
//        //开启推送并设置注册的回调处理
//        mPushAgent.enable(new IUmengRegisterCallback() {
//
//            @Override
//            public void onRegistered(final String registrationId) {
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //onRegistered方法的参数registrationId即是device_token
//                        showToast(registrationId);
//                    }
//                });
//            }
//        });
//    }

    @AfterViews
    protected void main() {
        App.getInstance().addActivity(MainAct.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        initTitlebar();
        setSwipeEnable(false);
        badgeViewList = new ArrayList<>();
        addImageViewLists();
        setItemCount();

    }


    private void GetSysCount() {
        showLoading("RFID关闭中...");
        WebModel.getInstance().sysLoginCountPage(new MResponseListener() {
            @Override
            public void onSuccess(MResponse response) {
                String callWebService = String.valueOf(response.data);
                List<SysCountClass> sysCountClasses = JSON.parseArray(callWebService,
                        SysCountClass.class);
                SysCountClass sysCountClass = sysCountClasses.get(0);
                String rt_f = sysCountClass.getRT_F();

                if (rt_f.equals("1")) {
                    rt_list = sysCountClass.getRT_LIST();
                    for (int i = 0; i < rt_list.size(); i++) {
                        int count = i + 1;
                        String IOM = "IOM0" + count;
                        for (int j = 0; j < rt_list.size(); j++) {
                            if (rt_list.get(j).getPROC_CODE().equals(IOM)) {
                                int count1 = rt_list.get(j).getCOUNT();
                                setXml(i + "COUNT", count1 + "");
                                break;
                            }
                        }
                    }
                    setXml("RTLIST_SIZE", rt_list.size() + "");
                    initItemCount();
                } else {
                    showToast(sysCountClass.getRT_D());
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
        }, getXml("OPER_NO", "1"));
    }



    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent=new Intent();Item
//        intent.setAction("com.test");
//        sendBroadcast(intent);
        GetSysCount();


    }

    private void initItemCount() {

        for (int i = 0; i < badgeViewList.size(); i++) {
            String xml = getXml(i + "COUNT", "");
            int count = Integer.parseInt(xml);
            BadgeView badgeView = badgeViewList.get(i);
            badgeView.setBVText(badgeView, count);
        }
    }

    //初始化Item未完成数量
    private void setItemCount() {
        rtlist_size = Integer.parseInt(getXml("RTLIST_SIZE", ""));
        for (int i = 0; i < rtlist_size; i++) {
            String xml = getXml(i + "COUNT", "");
            if (TextUtils.isEmpty(xml)) {
                xml = "0";
            }
            int count = Integer.parseInt(xml);
            BadgeView badgeView = new BadgeView(this, imageViewList.get(i));
            badgeView.setBVText(badgeView, count);
            badgeViewList.add(badgeView);

        }
    }

    //把各个模块图片加到list里
    private void addImageViewLists() {
        imageViewList = new ArrayList<ImageView>();
        imageViewList.add(num1);
        imageViewList.add(num2);
        imageViewList.add(num3);
        imageViewList.add(num4);
        imageViewList.add(num5);
        imageViewList.add(num6);
        imageViewList.add(num7);
    }

    @Click
    void num1() {
//        openAct(InspectAct.class, true);
        openAct(TestActivity.class, true);
    }

    @Click
    void num2() {
        openAct(SpecialAct.class, true);
    }

    @Click
    void num3() {
        openAct(RegularlAct.class, true);
    }

    @Click
    void num4() {
//        openAct(AlarmAct.class, true);
        openAct(AlarmActNew.class, true);
    }

    @Click
    void num5() {
        openAct(TroubleAct.class,true);
    }

    @Click
    void num7() {
        openAct(EquipmentReplaceAct.class,true);
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

