package com.app.hubert.guide.model;

import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.util.LogUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hubert on 2018/6/28.
 */
public class RelativeGuide {

    @IntDef({android.view.Gravity.LEFT, android.view.Gravity.TOP,
            android.view.Gravity.RIGHT, android.view.Gravity.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface LimitGravity {

    }

    public static class MarginInfo {
        public int leftMargin;
        public int topMargin;
        public int rightMargin;
        public int bottomMargin;
        public int gravity;

        @Override
        public String toString() {
            return "MarginInfo{" +
                    "leftMargin=" + leftMargin +
                    ", topMargin=" + topMargin +
                    ", rightMargin=" + rightMargin +
                    ", bottomMargin=" + bottomMargin +
                    ", gravity=" + gravity +
                    '}';
        }
    }

    public HighLight highLight;
    @LayoutRes
    public int layout;
    public int padding;
    public int gravity;

    public RelativeGuide(@LayoutRes int layout, @LimitGravity int gravity) {
        this.layout = layout;
        this.gravity = gravity;
    }

    /**
     * @param layout  相对位置引导布局
     * @param gravity 仅限left top right bottom
     * @param padding 与高亮view的padding，单位px
     */
    public RelativeGuide(@LayoutRes int layout, @LimitGravity int gravity, int padding) {
        this.layout = layout;
        this.gravity = gravity;
        this.padding = padding;
    }

    public final View getGuideLayout(ViewGroup viewGroup, Controller controller) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        onLayoutInflated(view);
        onLayoutInflated(view, controller);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        MarginInfo marginInfo = getMarginInfo(gravity, viewGroup, view);
        LogUtil.e(marginInfo.toString());
        offsetMargin(marginInfo, viewGroup, view);
        layoutParams.gravity = marginInfo.gravity;
        layoutParams.leftMargin += marginInfo.leftMargin;
        layoutParams.topMargin += marginInfo.topMargin;
        layoutParams.rightMargin += marginInfo.rightMargin;
        layoutParams.bottomMargin += marginInfo.bottomMargin;
        view.setLayoutParams(layoutParams);
        return view;
    }

    private MarginInfo getMarginInfo(@LimitGravity int gravity, ViewGroup viewGroup, View view) {
        MarginInfo marginInfo = new MarginInfo();
        RectF rectF = highLight.getRectF(viewGroup);
        switch (gravity) {
            case Gravity.LEFT:
                marginInfo.gravity = Gravity.RIGHT;
                marginInfo.rightMargin = (int) (viewGroup.getWidth() - rectF.left + padding);
                marginInfo.topMargin = (int) rectF.top;
                break;
            case Gravity.TOP:
                marginInfo.gravity = Gravity.BOTTOM;
                marginInfo.bottomMargin = (int) (viewGroup.getHeight() - rectF.top + padding);
                marginInfo.leftMargin = (int) rectF.left;
                break;
            case Gravity.RIGHT:
                marginInfo.leftMargin = (int) (rectF.right + padding);
                marginInfo.topMargin = (int) rectF.top;
                break;
            case Gravity.BOTTOM:
                marginInfo.topMargin = (int) (rectF.bottom + padding);
                marginInfo.leftMargin = (int) rectF.left;
                break;
        }
        return marginInfo;
    }

    protected void offsetMargin(MarginInfo marginInfo, ViewGroup viewGroup, View view) {
        //do nothing
    }

    /**
     * 复写初始化布局
     *
     * @param view inflated from layout
     * @see RelativeGuide#onLayoutInflated(View view, Controller controller)
     */
    @Deprecated
    protected void onLayoutInflated(View view) {
        //do nothing
    }

    /**
     * 复写初始化布局
     *
     * @param view       inflated from layout
     * @param controller controller
     */
    protected void onLayoutInflated(View view, Controller controller) {
        //do nothing
    }
}
