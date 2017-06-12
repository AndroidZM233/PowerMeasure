package speedata.com.powermeasure.activity;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.App;
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.view.CustomTitlebar;
import speedata.com.powermeasure.R;

@EActivity(R.layout.activity_special_third)
public class RegularThirdAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @AfterViews
    protected void main() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        App.getInstance().addActivity(RegularThirdAct.this);
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "",
                getXml("OPER_NAME",""),getXml("DEPT_NAME",""),getXml("ROLE_NAME","运维"),null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }
}
