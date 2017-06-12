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

package common.base.frag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.Serializable;
import common.base.StateBarController;
import common.base.act.FragActBase;
import common.event.EventBusWraper;
import common.utils.AbInnerUtil;
import speedata.com.powermeasure.R;

/**
 * * 大白私人助理
 * http://www.lovedabai.com
 * baesFragment
 *
 * @author LIN
 * @version 1.0
 * @created 2015-06-11
 */
public abstract class BaseFragment extends Fragment {
    // 避免fragment重复加载
    protected View mRootView;

    protected Object mContext;

    protected boolean mHasLoad;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param intent             包含数据的intent对象
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    protected void openAct(Intent intent, Class<?> actClass,
                           boolean isAnnotationsClass) {
        if (intent == null) {
            intent = new Intent();
        }
        if (isAnnotationsClass) {
            intent.setClass(getActivity(), AbInnerUtil.parse(actClass));
        } else {
            intent.setClass(getActivity(), actClass);
        }
        openAct(intent);
    }

    protected abstract Object regieterBaiduBaseCount();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = regieterBaiduBaseCount();
        StateBarController.getInstance().setStateGone(getActivity());
    }

    /**
     * 注册监听
     *
     * @param o
     */
    protected void registerEventbus(Object o) {
        mContext = o;
        EventBusWraper.getInstance().register(o);
    }

    /**
     * @param message
     */
    protected void post(Object message) {
        EventBusWraper.getInstance().post(message);
    }

    public Context getContext() {
        return mRootView.getContext();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            // fragment可见时加载数据 onResume
        } else {
            // 不可见时不执行操作 onPause
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (mHasLoad) {
            return;
        }
        initFragment();
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
        intent.setClass(getActivity(), actClass);
        openAct(intent);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        EventBusWraper.getInstance().unregister(mContext);
        super.onDestroy();
        // unRegister();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected View createView(LayoutInflater inflater, ViewGroup container,
                              int layout) {
        if (mRootView == null && getActivity() != null) {
            mRootView = inflater.inflate(layout, container, false);
            mHasLoad = false;
        } else if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            mHasLoad = true;
        }

        return mRootView;
    }

    public abstract void initFragment();

    public void showLoading() {
        Activity a = getActivity();
        if (a instanceof FragActBase) {
            ((FragActBase) a).showLoading();
        }
    }

    public void showLoading(String text) {
        Activity a = getActivity();
        if (a instanceof FragActBase) {
            ((FragActBase) a).showLoading(text);
        }
    }

    public void hideLoading() {
        Activity a = getActivity();
        if (a instanceof FragActBase) {
            ((FragActBase) a).hideLoading();
        }
    }

    /**
     * 打开指定的Activity页面
     *
     * @param intent
     */
    protected void openAct(Intent intent) {
        this.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.none_anim);
    }

    /**
     * 打开指定的Activity页面,并且新页面会回传数据
     *
     * @param intent
     */
    protected void openActForResult(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.none_anim);
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param intent   包含数据的intent对象
     * @param actClass Activity页面类
     */
    protected void openAct(Intent intent, Class<?> actClass) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.setClass(getActivity(), actClass);
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于不需要传递数据的页面跳转
     *
     * @param actClass           Activity页面类
     * @param isAnnotationsClass 要打开的Activity是否是@EActivity
     */
    protected void openAct(Class<?> actClass, boolean isAnnotationsClass) {
        Intent intent;
        if (isAnnotationsClass) {
            intent = new Intent(getActivity(), AbInnerUtil.parse(actClass));
        } else {
            intent = new Intent(getActivity(), actClass);
        }
        openAct(intent);
    }

    /**
     * 打开指定的Activity页面,该方法适用于需要传递数据的页面跳转
     *
     * @param actClass Activity页面类
     */
    protected void openAct(String key, Serializable objectValue,
                           Class<?> actClass, boolean isAnnotationsClass) {
        Intent intent = new Intent();
        intent.putExtra(key, objectValue);
        if (isAnnotationsClass) {
            intent.setClass(getActivity(), AbInnerUtil.parse(actClass));
        } else {
            intent.setClass(getActivity(), actClass);
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
        Intent intent = new Intent(getActivity(), actClass);
        openAct(intent);
    }

    public void setText(TextView textView, String string) {
        textView.setText(string);
    }
}
