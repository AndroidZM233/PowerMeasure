/*
 *
 * @author Echo
 * @created 2016.6.3
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.view;

import android.content.Context;
import android.os.Build;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import speedata.com.powermeasure.R;


/**
 * * 大白私人助理
 * http://www.lovedabai.com
 * TitleBar
 *
 * @author LIN
 * @version 1.0
 * @created 2015-06-14
 */
public class CustomTitlebar extends RelativeLayout {
    public static final int TITLEBAR_STYLE_NORMAL = 1;//
    public static final int TITLEBAR_STYLE_CUSTOM = 2;//定制中间布局
    public static final int NO_TITLE = 3;
    private TextView mTitlebarName, mTitlebarLeftText, mTitlebarRightText
            ,mTitleMyName,mTitleBM,mTitleRole;
    private ImageView mTitlebarLeftBtn, mTitlebarRightBtn;
    private ViewGroup mCustomLayout, mLeftLayout, mRightLayout;
    private RelativeLayout framelayout;

    public ViewGroup getmRightLayout() {
        return mRightLayout;
    }

    public void setmRightLayout(ViewGroup mRightLayout) {
        this.mRightLayout = mRightLayout;
    }

    public ViewGroup getmLeftLayout() {
        return mLeftLayout;
    }

    public void setmLeftLayout(ViewGroup mLeftLayout) {
        this.mLeftLayout = mLeftLayout;
    }

    public CustomTitlebar(Context context) {
        super(context);
        init();
    }

    public TextView getmTitlebarName() {
        return mTitlebarName;
    }

    public CustomTitlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTitlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.title_bar, this);
//        inflate.setBackgroundColor(getResources().getColor(R.color.light_gree));
        mTitlebarLeftBtn = (ImageView) findViewById(R.id.titlebar_left_btn);
        mTitlebarLeftText = (TextView) findViewById(R.id.titlebar_left_text);
        mTitlebarName = (TextView) findViewById(R.id.titlebar_name);
        mTitleMyName= (TextView) findViewById(R.id.title_name);
        mTitleBM= (TextView) findViewById(R.id.title_BM);
        framelayout= (RelativeLayout) findViewById(R.id.title_fl);
        mTitleRole= (TextView) findViewById(R.id.title_role);
        mTitlebarName.setTextColor(getResources().getColor(R.color.white));
        mTitlebarRightBtn = (ImageView) findViewById(R.id.titlebar_right_btn);
        mTitlebarRightText = (TextView) findViewById(R.id.titlebar_right_text);
        View topban = findViewById(R.id.topban);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //支持
            topban.setVisibility(VISIBLE);
        } else {
            topban.setVisibility(GONE);
        }

        mLeftLayout = (ViewGroup) findViewById(R.id.left_layout);
        mRightLayout = (ViewGroup) findViewById(R.id.right_layout);
        mCustomLayout = (ViewGroup) findViewById(R.id.titlebar_custom_layout);

        //
        mLeftLayout.setVisibility(View.GONE);
        mRightLayout.setVisibility(View.GONE);
        mCustomLayout.setVisibility(View.GONE);
    }


    public void setTitlebarStyle(int style) {
        if (style == TITLEBAR_STYLE_NORMAL) {
            mTitlebarName.setVisibility(View.VISIBLE);
            mRightLayout.setVisibility(View.VISIBLE);
            mCustomLayout.setVisibility(View.GONE);
        } else if (style == TITLEBAR_STYLE_CUSTOM) {
            mTitlebarName.setVisibility(View.GONE);
            mRightLayout.setVisibility(View.VISIBLE);
            mCustomLayout.setVisibility(View.VISIBLE);
        } else if (style==NO_TITLE){
            framelayout.setVisibility(View.GONE);
        }
    }

    public void setCustomView(View v) {
        if (mCustomLayout != null) {
            mCustomLayout.removeAllViews();
            mCustomLayout.addView(v);
        }
    }

    public void setAttrs(OnClickListener leftListener, String name,String myName,String BM,String role,
                         OnClickListener rightListener) {
        mTitlebarLeftBtn.setOnClickListener(leftListener);
        mTitlebarName.setText(name);
        mTitleMyName.setText(myName);
        mTitleBM.setText(BM);
        mTitleRole.setText(role);
        mRightLayout.setOnClickListener(rightListener);
    }

    public void setAttrs(OnClickListener leftListener, int resId,
                         OnClickListener rightListener) {
        mTitlebarLeftBtn.setOnClickListener(leftListener);
        mTitlebarName.setText(resId);
        mRightLayout.setOnClickListener(rightListener);
    }

    public void setAttrs(OnClickListener leftListener,
                         OnClickListener rightListener) {
        mTitlebarLeftBtn.setOnClickListener(leftListener);
        mRightLayout.setOnClickListener(rightListener);
    }

    public void setTitlebarLeftBtnVisbility(int visbility) {
        mLeftLayout.setVisibility(visbility);
    }

    public void setTitlebarLeftBtnText(String text) {
        mTitlebarLeftText.setText(text);
        mTitlebarLeftText.setVisibility(View.VISIBLE);
        mTitlebarLeftBtn.setVisibility(View.GONE);
    }

    public void setTitlebarLeftLabelText(String text) {
        mTitlebarLeftText.setText(text);
        mTitlebarLeftText.setVisibility(View.VISIBLE);
    }

    public void setTitlebarLeftBtnText(Spanned text) {
        mTitlebarLeftText.setText(text);
        mTitlebarLeftText.setVisibility(View.VISIBLE);
        mTitlebarLeftBtn.setVisibility(View.GONE);
    }

    public void setTitlebarLeftBtnTextTextSize(float size) {
        mTitlebarLeftText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitlebarLeftBtnTextTextColor(int color) {
        mTitlebarLeftText.setTextColor(color);
    }

    public void setTitlebarLeftBtnTextBold(boolean isBold) {
        TextPaint tp = mTitlebarLeftText.getPaint();
        tp.setFakeBoldText(isBold);
    }

    public void setTitlebarRightBtnVisbility(int visbility) {
        mRightLayout.setVisibility(visbility);
    }

    public void setTitlebarRightBtnText(String text) {
        mTitlebarRightText.setText(text);
        mTitlebarRightText.setVisibility(View.VISIBLE);
        mTitlebarRightBtn.setVisibility(View.GONE);
    }


    public void setTitlebarRightBtnEnable(boolean enable) {
        mTitlebarRightBtn.setEnabled(enable);
    }


    public void setTitlebarLeftBtnEnable(boolean enable) {
        mTitlebarLeftBtn.setEnabled(enable);
    }


    public void setTitlebarNameVisbility(int visbility) {
        mTitlebarName.setVisibility(visbility);
    }

    public void setTitlebarNameText(String text) {
        mTitlebarName.setText(text);
        setTitlebarNameVisbility(VISIBLE);
    }

    public void setTitlebarNameTextColor(int color) {
        mTitlebarName.setTextColor(color);
        setTitlebarNameVisbility(VISIBLE);
    }

    public void setTitlebarRightBtnBackgroud(int res) {
        mTitlebarRightBtn.setImageResource(res);
        mTitlebarRightBtn.setVisibility(View.VISIBLE);
        mTitlebarRightText.setVisibility(View.GONE);
    }

    public void setTitlebarLeftBtnBackgroud(int res) {
        mTitlebarLeftBtn.setImageResource(res);
        mTitlebarLeftBtn.setVisibility(View.VISIBLE);
        mTitlebarLeftText.setVisibility(View.GONE);
    }

}
