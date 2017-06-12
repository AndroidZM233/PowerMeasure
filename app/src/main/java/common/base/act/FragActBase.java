/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.base.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.uhflibs.r2000_native;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.WindowFeature;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.base.App;
import common.base.StateBarController;
import common.base.dialog.FlippingLoadingDialog;
import common.base.dialog.ToastUtils;
import common.event.EventBusWraper;
import common.event.ViewMessage;
import common.utils.AbInnerUtil;
import common.utils.ExceptionHandler;
import common.utils.PlaySoundPool;
import common.utils.ScanUtil;
import common.utils.SharedXmlUtil;
import common.utils.StringUtil;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import speedata.com.powermeasure.R;
import speedata.com.powermeasure.bean.OperLoginClass;

@WindowFeature(value = Window.FEATURE_NO_TITLE)
@EActivity
public abstract class FragActBase extends SwipeBackActivity{
    protected static final int RIGHT = 0;
    protected static final int LEFT = 1;
    protected static Fragment mContent;
    protected Context mContext;
    public boolean inspWairt;
    int count = 0;
    public int pictureId=0;
    int onBackPressedCount = 0;
    public List<OperLoginClass.RTLISTBean> home_msg=new ArrayList<>();
    private FlippingLoadingDialog mProgressDialog;

    public PlaySoundPool pl;
    public final HashMap<String, Object> params = new HashMap<String, Object>();

    /**
     * 注册百度统计监听
     */
    protected abstract Context regieterBaiduBaseCount();

    protected abstract void initTitlebar();


    public abstract void onEventMainThread(ViewMessage viewMessage);

    /**
     * 注册监听
     *
     * @param o
     */
    protected void registerEventbus(Context o) {
        mContext = o;
        EventBusWraper.getInstance().register(o);
    }

    protected void setXml(String key, String value) {
        SharedXmlUtil.getInstance(mContext).write(key, value);
    }

    protected String getXml(String key, String devalue) {
        String read = SharedXmlUtil.getInstance(mContext).read(key, devalue);
        return read;
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof RelativeLayout.MarginLayoutParams) {
            RelativeLayout.MarginLayoutParams p = (RelativeLayout.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void setEtCoustomLength(int length, EditText etCoustom) {
        if (length > 0) {
            etCoustom.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
    }


    public String replaceStringNR(String str){
        str=str.replace("\n", "");
        str=str.replace("\r", "");
        str=str.replace("\u0000","");
        return str;
    }


    public void setClearBtnListener(final EditText et, final View clearbtn) {
        setClearBtnListener(et, clearbtn, null, null);
    }
//    public void getIP(){
//        ipUrl=getXml("IP","");
//    }

    public void setClearBtnListener(final EditText et, final View clearbtn, final TextWatcher textWatcher, final View.OnFocusChangeListener focusChangeListener) {
        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChange(v, hasFocus);
                }
                if (v == et) {
                    if (hasFocus) {
                        if (!StringUtil.isBlank(et.getText())) {
                            clearbtn.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                    clearbtn.setVisibility(View.GONE);
                }
            }
        });
        et.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!StringUtil.isBlank(s)) {
                            clearbtn.setVisibility(View.VISIBLE);
                        } else {
                            clearbtn.setVisibility(View.GONE);
                        }
                        if (textWatcher != null) {
                            textWatcher.onTextChanged(s, start, before, count);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    hideInputMethod();
                    return true;
                }
                return false;
            }
        });
        clearbtn.setVisibility(View.GONE);
    }

    /**
     * 退出界面
     */
    @Override
    public void finish() {
        hideInputMethod();
        super.finish();
        overridePendingTransition(R.anim.none_anim, R.anim.slide_out_to_right);
    }


    /**
     * 隐藏输入法
     */
    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // 显示或者隐藏输入法
            View currentFocus = this.getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(
                        currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = regieterBaiduBaseCount();
        App application = (App) this.getApplication();
//        PushAgent.getInstance(mContext).onAppStart();
        if (mContext != null && mContext instanceof Activity) {
            application.addActivity((Activity) mContext);
        } else {
            mContext = this;
        }
        ToastUtils.changeContext(mContext);
        setXml("pictureId",pictureId+"");
//        ipUrl=getXml("IP","http://121.69.42.34:12501/ZQTWebService.asmx");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
////             Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        StateBarController.getInstance().setStateGone(this);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        if (swipeBackLayout != null) {
            swipeBackLayout.setEnableGesture(true);
            swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        }
    }

    protected void setSwipeEnable(boolean enable) {
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        if (swipeBackLayout != null) {
            swipeBackLayout.setEnableGesture(enable);
        }
    }

    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new FlippingLoadingDialog(this, "加载中...");
        }
        mProgressDialog.setText("加载中...");
        mProgressDialog.show();
    }

    public void showLoading(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new FlippingLoadingDialog(this, text);
        }
        mProgressDialog.setText(text);
        mProgressDialog.show();
    }

    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    private Toast mToast;

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);// getApplicationContext()
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * @param message
     */
    public void post(Object message) {
        EventBusWraper.getInstance().post(message);
    }

    @Override
    protected void onDestroy() {
        EventBusWraper.getInstance().unregister(mContext);
        super.onDestroy();
    }

    @Receiver(actions = {ConnectivityManager.CONNECTIVITY_ACTION})
    public void receivedConnectivity(Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    ExceptionHandler.getInstanceMyExceptionHandler(getApplicationContext())
                            .sendErrorLogFromSdcard();
                }
            }.start();

        } else {
        }
    }


    /**
     * 打开指定的Activity页面
     *
     * @param intent
     */
    protected void openAct(Intent intent) {
        this.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.none_anim);
    }

    /**
     * 打开指定的Activity页面,并且新页面会回传数据
     *
     * @param intent
     */
    protected void openActForResult(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.none_anim);
    }

    /**
     * @param actClass
     * @param isAnnotationsClass
     * @param requestCode
     */
    protected void openActForResult(Class<?> actClass,
                                    boolean isAnnotationsClass, int requestCode) {
        Intent intent = new Intent();
        if (isAnnotationsClass) {
            intent.setClass(this, AbInnerUtil.parse(actClass));
        } else {
            intent.setClass(this, actClass);
        }
        this.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.none_anim, R.anim.slide_in_from_right);
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param intent             包含数据的intent对象
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    public void openAct(Intent intent, Class<?> actClass,
                        boolean isAnnotationsClass) {
        if (intent == null) {
            intent = new Intent();
        }
        if (isAnnotationsClass) {
            intent.setClass(this, AbInnerUtil.parse(actClass));
        } else {
            intent.setClass(this, actClass);
        }
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param actClass Activity页面类
     */
    public void openAct(String key, Serializable objectValue,
                        Class<?> actClass) {
        Intent intent = new Intent();
        intent.putExtra(key, objectValue);
        intent.setClass(this, actClass);
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     * <p/>
     * 包含数据的intent对象
     *
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    public void openAct(String key, Serializable objectValue,
                        Class<?> actClass, boolean isAnnotationsClass) {
        Intent intent = new Intent();
        intent.putExtra(key, objectValue);
        if (isAnnotationsClass) {
            intent.setClass(this, AbInnerUtil.parse(actClass));
        } else {
            intent.setClass(this, actClass);
        }
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于不需要传递数据的页面跳转
     *
     * @param actClass Activity页面类
     *                 要打开的Activity是否是@EActivity
     */
    public void openAct(Class<?> actClass) {
        Intent intent = new Intent(this, actClass);
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于不需要传递数据的页面跳转
     *
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    public void openAct(Class<?> actClass, boolean isAnnotationsClass) {
        Intent intent;
        if (isAnnotationsClass) {
            intent = new Intent(mContext, AbInnerUtil.parse(actClass));
        } else {
            intent = new Intent(mContext, actClass);
        }
        openAct(intent);
    }


    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param intent   包含数据的intent对象
     * @param actClass Activity页面类
     */
    public void openAct(Intent intent, Class<?> actClass) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.setClass(this, actClass);
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于不需要传递数据的页面跳转
     *
     * @param className Activity页面类
     *                  要打开的Activity是否是@EActivity
     * @throws ClassNotFoundException
     */
    protected Class<?> getSubClass(String className) {
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param intent             包含数据的intent对象
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    public void openActForResult(Intent intent, Class<?> actClass,
                                 int requestCode, boolean isAnnotationsClass) {
        if (intent == null) {
            intent = new Intent();
        }
        if (isAnnotationsClass) {
            intent.setClass(this, AbInnerUtil.parse(actClass));
        } else {
            intent.setClass(this, actClass);
        }
        openActForResult(intent, requestCode);
    }

    /**
     * 打开指定的Activity页面,该方法适用于不需要传递数据的页面跳转
     *
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    protected void openActForResult(Class<?> actClass, int requestCode,
                                    boolean isAnnotationsClass) {
        Intent intent;
        if (isAnnotationsClass) {
            intent = new Intent(this, AbInnerUtil.parse(actClass));
        } else {
            intent = new Intent(this, actClass);
        }
        openActForResult(intent, requestCode);
    }


    /**
     * 模拟按下HOME键
     */
    public void pressHome() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        openAct(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onBackPressedCount = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // @Override
    // public void onBackPressed() {
    // // SjcjActivity.isCollected = false;
    // }

    protected void switchContent(Fragment fragment) {
        if (mContent != fragment) {
            mContent = fragment;
//            getSupportFragmentManager().beginTransaction()
//                    // .setCustomAnimations(android.R.anim.fade_in,
//                    // android.R.anim.fade_out)
//                    .replace(R.id.dabai_fragment_layout, fragment) // 替换Fragment，实现切换
//                    .commitAllowingStateLoss();
        }
    }


    /**
     * 递归查找TextView 替换颜色方法
     *
     * @param malcline
     * @param color
     */
    protected void RePlaceTextViewColor(ViewGroup malcline, String color) {
        int childCount = malcline.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = malcline.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                RePlaceTextViewColor((ViewGroup) childAt, color);
            } else if (childAt instanceof TextView) {
//				ThemeUtil.loadResourceToView(this, color, childAt,
//						ThemeUtil.DEFAULT_THEME);
            }
        }
    }

    public void setResult(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }
}
