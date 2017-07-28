package com.app.hubert.library;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class Controller {

    private Activity activity;
    private List<HighLight> list = new ArrayList<>();
    private OnGuideChangedListener onGuideChangedListener;
    private boolean everyWhereCancelable = true;
    private int backgroundColor;
    private String label;
    private boolean alwaysShow;
    private int layoutResId;
    private int[] viewIds;

    private FrameLayout mParentView;
    private GuideLayout guideLayout;
    private SharedPreferences sp;

    public Controller(Builder builder) {
        this.activity = builder.getActivity();
        this.list = builder.getList();
        this.backgroundColor = builder.getBackgroundColor();
        this.onGuideChangedListener = builder.getOnGuideChangedListener();
        this.everyWhereCancelable = builder.isEveryWhereCancelable();
        this.label = builder.getLabel();
        this.alwaysShow = builder.isAlwaysShow();
        this.layoutResId = builder.getLayoutResId();
        this.viewIds = builder.getViewIds();

        mParentView = (FrameLayout) activity.getWindow().getDecorView();
        sp = activity.getSharedPreferences(NewbieGuide.TAG, Activity.MODE_PRIVATE);
    }

    /**
     * 显示指引layout
     *
     * @return {@link NewbieGuide#SUCCESS} 表示成功显示，{@link NewbieGuide#FAILED} 表示已经显示过，不再显示
     */
    public int show() {
        if (!alwaysShow) {
            boolean showed = sp.getBoolean(label, false);
            if (showed) return NewbieGuide.FAILED;
        }

        guideLayout = new GuideLayout(activity);
        guideLayout.setHighLights(list);
        if (backgroundColor > 0)
            guideLayout.setBackgroundColor(backgroundColor);

//        guideLayout.addView(getLeftIndicate(), getLp(0, 100));
//        guideLayout.addView(getMsgAndKnowTv("新手指引~~~~~~~~~"), getLp(0, 0));
        if (layoutResId > 0) {
            View view = LayoutInflater.from(activity).inflate(layoutResId, guideLayout, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = ScreenUtils.getStatusBarHeight(activity);
            params.bottomMargin = ScreenUtils.getNavigationBarHeight(activity);
            if (viewIds != null) {
                for (int viewId : viewIds) {
                    view.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            remove();
                        }
                    });
                }
            }
            guideLayout.addView(view, params);
        }

        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (onGuideChangedListener != null) onGuideChangedListener.onShowed(this);
        sp.edit().putBoolean(label, true).apply();
        if (everyWhereCancelable) {
            guideLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    remove();
                    return false;
                }
            });
        }
        return NewbieGuide.SUCCESS;
    }

    /**
     * 清除"显示过"的标记
     *
     * @param label 引导标示
     */
    public void resetLabel(String label) {
        sp.edit().putBoolean(label, false).apply();
    }

    public void remove() {
        if (guideLayout != null && guideLayout.getParent() != null) {
            ((ViewGroup) guideLayout.getParent()).removeView(guideLayout);
            if (onGuideChangedListener != null) onGuideChangedListener.onRemoved(this);
        }
    }
}