package com.app.hubert.guide.core;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    Activity activity;
    Fragment fragment;
    android.support.v4.app.Fragment v4Fragment;
    String label;
    boolean alwaysShow;//总是显示 default false
    View anchor;//锚点view
    int showCounts = 1;//显示次数 default once
    OnGuideChangedListener onGuideChangedListener;
    OnPageChangedListener onPageChangedListener;
    List<GuidePage> guidePages = new ArrayList<>();

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
     * 引导层显示的锚点，即根布局，不设置的话默认是decorView
     *
     * @param anchor root
     */
    public Builder anchor(View anchor) {
        this.anchor = anchor;
        return this;
    }

    /**
     * 引导层的显示次数，默认是1次。<br>
     * 这里的次数是通过sp控制的，是指同一个label在不清除缓存的情况下可以显示的总次数。
     *
     * @param count 次数
     */
    public Builder setShowCounts(int count) {
        this.showCounts = count;
        return this;
    }

    /**
     * 是否总是显示引导层，即是否无限次的显示。<br>
     * 默认为false，如果设置了true，{@link Builder#setShowCounts} 将无效。
     *
     * @param b
     */
    public Builder alwaysShow(boolean b) {
        this.alwaysShow = b;
        return this;
    }

    /**
     * 添加引导页
     */
    public Builder addGuidePage(GuidePage page) {
        guidePages.add(page);
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
     * 设置引导页切换监听
     */
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
        check();
        return new Controller(this);
    }

    /**
     * 构建引导层controller并直接显示引导层
     *
     * @return controller
     */
    public Controller show() {
        check();
        Controller controller = new Controller(this);
        controller.show();
        return controller;
    }

    private void check() {
        if (TextUtils.isEmpty(label)) {
            throw new IllegalArgumentException("the param 'label' is missing, please call setLabel()");
        }
        if (activity == null && (fragment != null || v4Fragment != null)) {
            throw new IllegalStateException("activity is null, please make sure that fragment is showing when call NewbieGuide");
        }
    }
}