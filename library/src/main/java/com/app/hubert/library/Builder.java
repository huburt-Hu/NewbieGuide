package com.app.hubert.library;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    private Activity activity;
    private Fragment fragment;
    private android.support.v4.app.Fragment v4Fragment;

    private String label;
    private boolean alwaysShow;
    private OnGuideChangedListener onGuideChangedListener;
    private OnPageChangedListener onPageChangedListener;

    private List<GuidePage> guidePages = new ArrayList<>();
    private GuidePage currentPage = new GuidePage();

    public Builder(Activity activity) {
        this.activity = activity;
    }

    public Builder(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public Builder(android.support.v4.app.Fragment v4Fragment) {
        this.v4Fragment = v4Fragment;
        this.activity = v4Fragment.getActivity();
    }

    /**
     * 添加需要高亮的view,默认高亮类型为矩形
     */
    public Builder addHighLight(View view) {
        return addHighLight(view, HighLight.Type.RECTANGLE, 0);
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
        currentPage.addHighLight(view, type, round);
        return this;
    }

    public Builder addHighLights(List<HighLight> list) {
        currentPage.addHighLights(list);
        return this;
    }

    /**
     * 引导层背景色
     */
    public Builder setBackgroundColor(int color) {
        currentPage.setBackgroundColor(color);
        return this;
    }

    /**
     * 点击任意区域是否隐藏引导层，默认true
     */
    public Builder setEveryWhereCancelable(boolean cancelable) {
        currentPage.setEveryWhereCancelable(cancelable);
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
        currentPage.setLayoutRes(resId, id);
        return this;
    }

    /**
     * 是否全屏显示，即是否包含状态栏
     */
    public Builder fullScreen(boolean isFullScreen) {
        currentPage.setFullScreen(isFullScreen);
        return this;
    }

    /**
     * 将之上参数保存为一页，并创建新页
     *
     * @return
     */
    public Builder asPage() {
        guidePages.add(currentPage);
        currentPage = new GuidePage();
        return this;
    }

    /**
     * 设置引导层隐藏，显示监听
     */
    public Builder setOnGuideChangedListener(OnGuideChangedListener listener) {
        onGuideChangedListener = listener;
        return this;
    }

    public Builder setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
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
     * 构建引导层controller
     *
     * @return controller
     */
    public Controller build() {
        checkAndSaveAsPage();
        return new Controller(this);
    }

    /**
     * 构建引导层controller并直接显示引导层
     *
     * @return controller
     */
    public Controller show() {
        checkAndSaveAsPage();
        Controller controller = new Controller(this);
        controller.show();
        return controller;
    }

    private void checkAndSaveAsPage() {
        if (TextUtils.isEmpty(label)) {
            throw new IllegalArgumentException("the param 'label' is missing, please call setLabel()");
        }
        if (activity == null && (fragment != null || v4Fragment != null)) {
            throw new IllegalStateException("activity is null, please make sure that fragment is showing when call NewbieGuide");
        }
        if (!guidePages.contains(currentPage) && !currentPage.isEmpty()) {
            guidePages.add(currentPage);
        }
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

    OnGuideChangedListener getOnGuideChangedListener() {
        return onGuideChangedListener;
    }

    OnPageChangedListener getOnPageChangedListener() {
        return onPageChangedListener;
    }

    Fragment getFragment() {
        return fragment;
    }

    android.support.v4.app.Fragment getV4Fragment() {
        return v4Fragment;
    }

    public List<GuidePage> getGuidePages() {
        return guidePages;
    }
}