/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.base;

import android.app.Activity;
import android.app.Application;
import android.os.Process;


import java.util.ArrayList;
import java.util.List;

import common.utils.ExceptionHandler;
import common.utils.LogUtil;

/**
 * 作者：deqing on 2016/5/29 12:59
 * 邮箱：bairu.xu@speedatagroup.com
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.init(this);
        initCrash();
    }

    /**
     * 异常监听
     */
    public void initCrash() {
        final ExceptionHandler instanceMyExceptionHandler = ExceptionHandler.getInstanceMyExceptionHandler(this);
        instanceMyExceptionHandler.setUnCatchableAcceptListioner();
        new Thread() {
            @Override
            public void run() {
                super.run();
                instanceMyExceptionHandler.sendErrorLogFromSdcard();
            }
        }.start();
    }


    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : activities) {
            activity.finish();
        }
        onAppExceptionDestroy();
        System.exit(0);
        android.os.Process.killProcess(Process.myPid());
    }


    /**
     * 处理程序退出的处理
     */
    protected abstract void onAppExceptionDestroy();
}
