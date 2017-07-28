package com.app.hubert.library;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    private Activity activity;
    private List<HighLight> list = new ArrayList<>();
    private OnGuideChangedListener onGuideChangedListener;
    private boolean everyWhereCancelable = true;
    private int backgroundColor;
    private String label;
    private boolean alwaysShow;
    private int layoutResId;
    private int[] viewIds;

    public Builder(Activity activity) {
        this.activity = activity;
    }

    /**
     * 添加需要高亮的view
     *
     * @param view 需要高亮的view
     * @param type 高亮类型：圆形，椭圆，矩形，圆角矩形
     * @return builder
     */
    public Builder addHighLight(View view, HighLight.Type type) {
        return addHighLight(view, type, 0);
    }

    /**
     * 添加需要高亮的view
     *
     * @param view  需要高亮的view
     * @param type  高亮类型：圆形，椭圆，矩形，圆角矩形
     * @param round 圆角尺寸，单位dp
     * @return builder
     */
    public Builder addHighLight(View view, HighLight.Type type, int round) {
        HighLight highLight = new HighLight(view, type);
        if (round > 0)
            highLight.setRound(round);
        list.add(highLight);
        return this;
    }

    public Builder addHighLight(List<HighLight> list) {
        this.list.addAll(list);
        return this;
    }

    /**
     * 引导层背景色
     */
    public Builder setBackgroundColor(int color) {
        backgroundColor = color;
        return this;
    }

    /**
     * 点击任意区域是否隐藏引导层，默认true
     */
    public Builder setEveryWhereCancelable(boolean cancelable) {
        everyWhereCancelable = cancelable;
        return this;
    }

    /**
     * 设置引导层隐藏，显示监听
     */
    public Builder setOnGuideChangedListener(OnGuideChangedListener listener) {
        onGuideChangedListener = listener;
        return this;
    }

    /**
     * 设置引导层的辨识名，必须设置项，否则报错
     */
    public Builder setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * 是否总是显示引导层
     */
    public Builder alwaysShow(boolean b) {
        this.alwaysShow = b;
        return this;
    }

    /**
     * 设置引导层控件布局
     *
     * @param resId 布局 id
     * @param id    需要设置点击隐藏引导层的view id
     * @return builder
     */
    public Builder setLayoutRes(int resId, int... id) {
        this.layoutResId = resId;
        viewIds = id;
        return this;
    }

    /**
     * 构建引导层controller
     *
     * @return controller
     */
    public Controller build() {
        if (TextUtils.isEmpty(label)) {
            throw new IllegalArgumentException("缺少必要参数：label,通过setLabel()方法设置");
        }
        return new Controller(this);
    }

    /**
     * 构建引导层controller并直接显示引导层
     *
     * @return controller
     */
    public Controller show() {
        if (TextUtils.isEmpty(label)) {
            throw new IllegalArgumentException("缺少必要参数：label,通过setLabel()方法设置");
        }
        Controller controller = new Controller(this);
        controller.show();
        return controller;
    }

    int getLayoutResId() {
        return layoutResId;
    }

    int[] getViewIds() {
        return viewIds;
    }

    boolean isAlwaysShow() {
        return alwaysShow;
    }

    String getLabel() {
        return label;
    }

    Activity getActivity() {
        return activity;
    }

    List<HighLight> getList() {
        return list;
    }

    OnGuideChangedListener getOnGuideChangedListener() {
        return onGuideChangedListener;
    }

    boolean isEveryWhereCancelable() {
        return everyWhereCancelable;
    }

    int getBackgroundColor() {
        return backgroundColor;
    }
}