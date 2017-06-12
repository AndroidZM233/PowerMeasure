package common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import speedata.com.powermeasure.view.RingProgressView;

public class ViewHolder {

    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 得到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public int getPosition() {
        return mPosition;
    }
    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public ViewHolder setRingProgressView(int viewId, int text) {
        RingProgressView mRingProgressView = getView(viewId);
        mRingProgressView.setPercent(text);
        return this;
    }

    public ViewHolder setProgessText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public ViewHolder setTextColor(int viewId, String color) {
        TextView view = getView(viewId);
        view.setTextColor(Color.parseColor(color));
        return this;
    }
    public ViewHolder setLLBackground(int viewId,String color){
        RelativeLayout layout=getView(viewId);
        layout.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    public ViewHolder setLLBackgroundIV(int viewId, int id){
        LinearLayout layout=getView(viewId);
        layout.setBackgroundResource(id);
        return this;
    }
    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, int bm) {
        ImageView view = getView(viewId);
        view.setImageResource(bm);
//        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
//    public ViewHolder setImageURL(int viewId, String url) {
//        ImageView view = getView(viewId);
//        AsyncBitmapLoader abl = new AsyncBitmapLoader();
//        abl.loadBitmap(view, url, new ImageCallBack() {
//
//            @Override
//            public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//        });
//
//        return this;
//    }

}

