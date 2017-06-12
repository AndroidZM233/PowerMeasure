package common.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 张明_ on 2016/9/18.
 */
public class FTPUtils {
    private FTPClient ftpClient = null;
    private static FTPUtils ftpUtilsInstance = null;
    private String FTPUrl;
    private int FTPPort;
    private String UserName;
    private String UserPassword;
    private static String encoding = System.getProperty("file.encoding");

    private FTPUtils() {
        ftpClient = new FTPClient();
    }

    /*
     * 得到类对象实例（因为只能有一个这样的类对象，所以用单例模式）
     */
    public static FTPUtils getInstance() {
        if (ftpUtilsInstance == null) {
            ftpUtilsInstance = new FTPUtils();
        }
        return ftpUtilsInstance;
    }

    /**
     * 设置FTP服务器
     *
     * @param FTPUrl       FTP服务器ip地址
     * @param FTPPort      FTP服务器端口号
     * @param UserName     登陆FTP服务器的账号
     * @param UserPassword 登陆FTP服务器的密码
     * @return
     */
    public boolean initFTPSetting(String FTPUrl, int FTPPort, String UserName, String UserPassword) {
        this.FTPUrl = FTPUrl;
        this.FTPPort = FTPPort;
        this.UserName = UserName;
        this.UserPassword = UserPassword;

        int reply;

        try {
            //1.要连接的FTP服务器Url,Port
            ftpClient.connect(FTPUrl, FTPPort);

            //2.登陆FTP服务器
            ftpClient.login(UserName, UserPassword);

            //3.看返回的值是不是230，如果是，表示登陆成功
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                //断开
                ftpClient.disconnect();
                return false;
            }

            return true;

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param FilePath 要上传文件所在SDCard的路径
     * @param FileName 要上传的文件的文件名(如：Sim唯一标识码)
     * @return true为成功，false为失败
     */
    public boolean uploadFile(String FilePath, String FileName, String ToFTPPath) {

        if (!ftpClient.isConnected()) {
            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
                return false;
            }
        }

        try {

            //设置存储路径
            ftpClient.makeDirectory(ToFTPPath);
            ftpClient.changeWorkingDirectory(ToFTPPath);

            //设置上传文件需要的一些基本信息
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //文件上传吧～
            FileInputStream fileInputStream = new FileInputStream(FilePath);
            ftpClient.storeFile(FileName, fileInputStream);

            //关闭文件流
            fileInputStream.close();

            //退出登陆FTP，关闭ftpCLient的连接
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 下载文件
     *
     * @param FilePath    要存放的文件的路径
     * @param FileName    远程FTP服务器上的那个文件的名字
     * @param FTPFilePath 远程FTP服务器上的那个文件的路径
     * @return true为成功，false为失败
     */
    public String downLoadFile(String FilePath, String FileName, String FTPFilePath, String type) {
        String bToUpFTP = null;
        if (!ftpClient.isConnected()) {
            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
                return "false";
            }
        }

        try {
            // 转到指定下载目录
            ftpClient.changeWorkingDirectory(FTPFilePath);

            //解决Host attempting data connection ip address is not same as server
            ftpClient.setRemoteVerificationEnabled(false);
            // 列出该目录下所有文件
            FTPFile[] files = ftpClient.listFiles();

            // 遍历所有文件，找到指定的文件
            for (FTPFile file : files) {
                if (file.getName().equals(FileName)) {
                    //根据绝对路径初始化文件
                    File localFile = new File(FilePath);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new Date());
                    String name = file.getName();
                    String[] strName = name.split("\\.");
                    // 输出流
                    OutputStream outputStream = new FileOutputStream(localFile);

                    // 下载文件
                    bToUpFTP = type + timeStamp + strName[1];
                    ftpClient.retrieveFile(bToUpFTP, outputStream);

                    //关闭流
                    outputStream.close();
                }
            }

            //退出登陆FTP，关闭ftpCLient的连接
            ftpClient.logout();
            ftpClient.disconnect();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bToUpFTP;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @Version1.0
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param remotePath
     *            FTP服务器上的相对路径
     * @param fileName
     *            要下载的文件名
     * @param localPath
     *            下载后保存到本地的路径
     * @return
     */
    public boolean downFile(String url, int port, String username,
                            String password, String remotePath, String fileName,
                            String localPath) {
        boolean result = false;
        try {
            int reply;
            ftpClient.setControlEncoding(encoding);

            /*
             *  为了上传和下载中文文件，有些地方建议使用以下两句代替
             *  new String(remotePath.getBytes(encoding),"iso-8859-1")转码。
             *  经过测试，通不过。
             */
//            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
//            conf.setServerLanguageCode("zh");

            ftpClient.connect(url, port);
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftpClient.login(username, password);// 登录
            // 设置文件传输类型为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取ftp登录应答代码
            reply = ftpClient.getReplyCode();
            // 验证是否登陆成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return result;
            }
            // 转移到FTP服务器目录至指定的目录下
            ftpClient.changeWorkingDirectory(new String(remotePath.getBytes(encoding),"iso-8859-1"));
            // 获取文件列表
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftpClient.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
