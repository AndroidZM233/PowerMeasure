package speedata.com.powermeasure.bean;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by 张明_ on 2016/8/10.
 */
public class ResultList  {
    public String CONT_NO;
    public String RESULT_CODE;

//    public String getCONT_NO() {
//        return CONT_NO;
//    }

    public void setCONT_NO(String CONT_NO) {
        this.CONT_NO = CONT_NO;
    }

//    public String getRESULT_CODE() {
//        return RESULT_CODE;
//    }

    public void setRESULT_CODE(String RESULT_CODE) {
        this.RESULT_CODE = RESULT_CODE;
    }


}
