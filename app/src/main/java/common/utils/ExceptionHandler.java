package common.utils;

import android.content.Context;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExceptionHandler implements UncaughtExceptionHandler {

    protected static final String SAVE_EXCEPTION_FILE_PARENT_PATH = "";
    protected static final String SAVE_EXCEPTION_FILE_NAME = "";
    private static final String TAG = LogUtil.DEGUG_MODE ? "ExceptionHandler"
            : ExceptionHandler.class.getSimpleName();
    private static final boolean debug = true;
    private static Context mContext;

    private static ExceptionHandler exceptionHandler;
    UncaughtExceptionHandler defaultExceptionHandler;


    private ExceptionHandler() {
    }

    public static ExceptionHandler getInstanceMyExceptionHandler(Context context) {
        if (exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler();
            mContext = context;
        }
        return exceptionHandler;
    }

    /**
     * 提供给 log.e调用，初始化完毕后再调用此方法<br/>
     * <b>请确保使用前 应用已调用getErrorReportModel(Context context)方法
     *
     * @return
     */
    public static ExceptionHandler getInstanceMyExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * 初始化方法
     * <p/>
     * 上下文对象
     */
    public void setUnCatchableAcceptListioner() {
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void exitApp() {
        LogUtil.i(debug, TAG, "【ExceptionHandler.exitApp()】【start】");
//        App.getInstance().onTerminate();
        LogUtil.i(debug, TAG, "【ExceptionHandler.exitApp()】【end】");
    }

    /**
     * 异常处理方法
     *
     * @Params Thread对象
     */
    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        if (!handleException(thread, ex) && defaultExceptionHandler != null) {
            //崩溃退出登录信息
            defaultExceptionHandler.uncaughtException(thread, ex);
        }
        exitApp();//退出app
    }

    /**
     * 用于catch中 存储log.e 相关的错误日志
     */
    public void creatCatchReportFile(String e) {
        try {
            String path = FileUtil.write(mContext, "errlog", e);
            uploadLog(new File(path));// 上传文件
        } catch (Exception e1) {
            e1.printStackTrace();
            LogUtil.e(debug, TAG, "【FileUtil.copyFile()】【e=" + e1 + "】");
        }
    }

    // 程序异常处理方法
    private boolean handleException(Thread thread, final Throwable ex) {
        LogUtil.i(debug, TAG, "【ExceptionHandler.le()】【start】");
        new Thread() {
            @Override
            public void run() {
                super.run();
                StringBuilder sb = new StringBuilder();
                Format formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                Date firstDate = new Date(System.currentTimeMillis()); // 第一次创建文件，也就是开始日期
                String str = formatter.format(firstDate);
                sb.append("\n");
                sb.append(str); // 把当前的日期写入到字符串中
                Writer writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                ex.printStackTrace(pw);
                String errorresult = writer.toString();
                sb.append(errorresult);
                sb.append("\n");
                String path = null;
                try {
                    path = FileUtil.writeSdcard(mContext, ex);// 写sdcard
                    File mSourceFile = new File(path);
                    boolean exists = mSourceFile.exists();
                    LogUtil.i(debug, TAG, "【ExceptionHandler.run()】【exists=" + exists + "】");
                    if (!exists) {
                        LogUtil.i(debug, TAG, "【ExceptionHandler.run()】【sd卡创建文件失败】");
                        path = FileUtil.write(mContext, "errlog", sb.toString());// 写应用文件
                        uploadLog(new File(path));// 上传文件
                    } else {
                        uploadLog(mSourceFile);// 上传文件
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (path == null) {
                    LogUtil.e(debug, TAG,
                            "【ErrorReportModel.UploadErrThread.run()】【 info=创建错误日志文件失败...】");
                }
            }
        }.start();
        LogUtil.i(debug, TAG, "【ExceptionHandler.handleException()】【end】");
        return false;
    }

    public String getErrorLogPath() {
        String path = mContext.getFilesDir() + "/errlog.txt";
        File file = new File(path);
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            return FileUtil.getSdcardLogAbsolutePath(mContext);
        }
    }

    /**
     * 从本地直接发送日志文件
     */
    public void sendErrorLogFromSdcard() {
        String errorLogPath = getErrorLogPath();
        LogUtil.i(debug, TAG,
                "【ExceptionHandler.sendErrorLogFromSdcard()】【errorLogPath="
                        + errorLogPath + "】");
        File file = new File(errorLogPath);
        uploadLog(file);
    }

    public void uploadLog(final File mSourceFile) {
        if (mSourceFile.exists()) {
            LogUtil.i(debug, TAG,
                    "【ErrorReportModel.UploadErrThread.run()】【 info=发送错误报告到邮箱...】");
            try {
                SendEmailUtil.sendClientErrorLogEmail(
                        mSourceFile.getAbsolutePath(), mContext);

                boolean delete = FileUtil.deleteFile(mSourceFile.getAbsolutePath());// 删除文件
                LogUtil.i(debug, TAG, "【ExceptionHandler.uploadLog()】【delete=" + delete + "】");
                boolean deleteFile = mContext
                        .deleteFile(mSourceFile.getName());
                LogUtil.i(debug, TAG, "【ExceptionHandler.mSourceFile()】【deleteFile=" + deleteFile + "】");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(debug, TAG,
                        "【ExceptionHandler.uploadLog(...).new Thread() {...}.run()】【e="
                                + e + "】");
            }

        } else {
            LogUtil.e(debug, TAG,
                    "【ErrorReportModel.UploadErrThread.run()】【 info=无错误日志文件...】");
        }
    }

}
