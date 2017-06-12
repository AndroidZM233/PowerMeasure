package speedata.com.powermeasure.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import speedata.com.powermeasure.R;

/**
 * Created by 张明_ on 2016/10/8.
 */
public class HorizontalItemView extends LinearLayout {
    private Context mContext;
    private TextView tvDY_name;
    public HorizontalItemView(Context context) {
        super(context);
    }

    public HorizontalItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View v = LayoutInflater.from(context).inflate(
                R.layout.view_item_main, this, true);
        tvDY_name= (TextView) v.findViewById(R.id.tv_name);
    }

    //设置单元名称
    public void SetDYName(String name){
        tvDY_name.setText(name);
    }

}
