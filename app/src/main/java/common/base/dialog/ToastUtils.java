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

package common.base.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import speedata.com.powermeasure.R;


public class ToastUtils {
    private static Toast toast;
    private static Context context;
    private static View toastRoot;
    // 可以使ToastUtils在非UI线程中运行
    private static Handler UIHandler;


    private ToastUtils() {
    }

    public static void init(Context c, Handler handler) {
        toastRoot = LayoutInflater.from(c).inflate(
                R.layout.diloag_flipping_loading, null);
        context = c;
        UIHandler = handler;
        checkToast();

        //  show(boolean) allow duplicates   or showSticky() sticky notification
    }

    public static void changeContext(Context c) {
        if (context != null) {
            context = c;
        }
    }

    @SuppressLint("ShowToast")
    private static void checkToast() {
        if (toast == null) {
            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        }
    }

    private static void setToastDisplayTime(ToastDisplayTime time) {
        if (time == ToastDisplayTime.TOAST_DISPLAY_LONG)
            toast.setDuration(Toast.LENGTH_LONG);
        else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
    }

    private static void show(String msg, ToastDisplayTime time) {
        checkToast();
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(msg);
        // toast.setText(msg);
        setToastDisplayTime(time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastRoot);
        toast.show();
    }

    private static void show(int msg, ToastDisplayTime time) {
        checkToast();
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(msg);
        // toast.setText(msg);
        setToastDisplayTime(time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastRoot);
        toast.show();
    }

    private static void show(String msg, ToastDisplayTime time, int position) {
        checkToast();
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(msg);
        // toast.setText(msg);
        setToastDisplayTime(time);
        toast.setGravity(position, 0, 0);
        toast.setView(toastRoot);
        toast.show();
    }

    private static void show(int msg, ToastDisplayTime time, int position) {
        checkToast();
        ((TextView) toastRoot.findViewById(R.id.toast_text)).setText(msg);
        // toast.setText(msg);
        setToastDisplayTime(time);
        toast.setGravity(position, 0, 0);
        toast.setView(toastRoot);
        toast.show();
    }

    public static void showLong(String msg) {
        show(msg, ToastDisplayTime.TOAST_DISPLAY_LONG);
    }

    public static void showLong(String msg, boolean isCancel) {
        show(msg, ToastDisplayTime.TOAST_DISPLAY_LONG);
    }

    public static void showLong(int msg) {
        show(msg, ToastDisplayTime.TOAST_DISPLAY_LONG);
    }

    public static void showShort(String msg) {
        try {
            show(msg, ToastDisplayTime.TOAST_DISPLAY_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShort(int msg) {
        show(msg, ToastDisplayTime.TOAST_DISPLAY_SHORT);
    }

    public static void showShortWithPositon(String msg, int position) {
        show(msg, ToastDisplayTime.TOAST_DISPLAY_SHORT, position);
    }

    public static void showShortWithPositon(int msg, int position) {
        show(msg, ToastDisplayTime.TOAST_DISPLAY_SHORT, position);
    }

    public static void showInUiThread(final String msg) {

        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                showShort(msg);
            }

        });
    }

    public static void exit() {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
    }

    public enum ToastDisplayTime {
        TOAST_DISPLAY_LONG, TOAST_DISPLAY_SHORT
    }
}
