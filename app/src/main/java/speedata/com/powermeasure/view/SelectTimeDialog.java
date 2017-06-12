package speedata.com.powermeasure.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import speedata.com.powermeasure.R;

public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    private Button sure;
    private DatePicker m_DatePickerStart;
    private Calendar c;
    private String startTime = "";
    private String endTime = "";
    private int getyear;
    private int getmonth;
    private int getday;
    private ShowDate showDate;
    private String initialDate;
    private TimePicker timePicker;
    private StringBuffer delayTime;

    public SelectTimeDialog(Context context, ShowDate showDate, String initialDate) {
        super(context, R.style.dialog);
        // TODO Auto-generated constructor stub
        this.showDate = showDate;
        this.initialDate = initialDate;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == sure) {
            if (startTime.equals("")) {
                startTime = initialDate;
            }
            showDate.showDate(String.valueOf(startTime));
            dismiss();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_date);
        sure = (Button) findViewById(R.id.date_button);
        sure.setOnClickListener(this);
        timePicker= (TimePicker) findViewById(R.id.Picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (minute<=9){
                    startTime=hourOfDay+":0"+minute;
                }else {
                    startTime=hourOfDay+":"+minute;
                }

            }
        });

    }
    public interface ShowDate {
        public abstract void showDate(String time);
    }

}
