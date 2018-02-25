package com.app.hubert.guide.core;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;

import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    Activity activity;
    Fragment fragment;
    android.support.v4.app.Fragment v4Fragment;

    String label;
    boolean alwaysShow;
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
     * 是否总是显示引导层
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
    }
}