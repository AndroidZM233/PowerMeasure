package common.utils;

import android.content.Context;
import android.util.Xml;

import org.textmining.text.extraction.WordExtractor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by 张明_ on 2016/8/26.
 */
public class FileUtils {
    private Context context;

    public FileUtils(Context context) {
        super();
        this.context = context;
    }

    /**
     * 保存文件
     * @param fileName 文件名
     * @param fileContent 文件内容
     */
    public void save(String fileName, String fileContent) throws Exception {
        // IO将文件保存至手机自带空间
        // 私有操作模式：创建出来的文件只能被本应用访问，其他应用无法访问该文件，
        // 另采用私有操作模式创建的文件，写入文件中的内容会覆盖原有内容
        FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outStream.write(fileContent.getBytes());//将字符串转成二进制数据
        outStream.close();
    }

    public String read(String fileName) throws Exception {
        FileInputStream inStream = context.openFileInput(fileName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);  ///获取buffer数组中从0-len范围的数据，将数据读入内存
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }


    public  String readDOCX(String path) {
        String river = "";
        try {
            ZipFile xlsxFile = new ZipFile(new File(path));
            ZipEntry sharedStringXML = xlsxFile.getEntry("word/document.xml");
            InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        System.out.println(tag);
                        if (tag.equalsIgnoreCase("t")) {
                            river += xmlParser.nextText() + "\n";
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                evtType = xmlParser.next();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (river == null) {
            river = "解析文件出现问题";
        }

        return river;
    }


    public static String readDOC(String path) {
        // 创建输入流读取doc文件
        FileInputStream in;
        String text = null;
//                Environment.getExternalStorageDirectory().getAbsolutePath()+ "/aa.doc")
        try {
            in = new FileInputStream(new File(path));
            int a= in.available();
            WordExtractor extractor = null;
            // 创建WordExtractor
            extractor = new WordExtractor();
            // 对doc文件进行提取
            text = extractor.extractText(in);
            System.out.println("解析得到的东西"+text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (text == null) {
            text = "解析文件出现问题";
        }
        return text;
    }
}
