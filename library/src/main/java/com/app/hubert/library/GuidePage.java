package com.app.hubert.library;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/11/16.
 */

public class GuidePage {

    private List<HighLight> highLights = new ArrayList<>();
    private boolean everyWhereCancelable = true;
    private int backgroundColor;
    private boolean fullScreen;
    private int layoutResId;
    private int[] viewIds;

    /**
     * 添加需要高亮的view
     *
     * @param view  需要高亮的view
     * @param type  高亮类型：圆形，椭圆，矩形，圆角矩形
     * @param round 圆角尺寸，单位dp
     * @return builder
     */
    public void addHighLight(View view, HighLight.Type type, int round) {
        HighLight highLight = new HighLight(view, type);
        if (round > 0) highLight.setRound(round);
        highLights.add(highLight);
    }

    public void addHighLights(List<HighLight> list) {
        highLights.addAll(list);
    }

    public List<HighLight> getHighLights() {
        return highLights;
    }

    public boolean isEveryWhereCancelable() {
        return everyWhereCancelable;
    }

    public void setEveryWhereCancelable(boolean everyWhereCancelable) {
        this.everyWhereCancelable = everyWhereCancelable;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public void setLayoutRes(int resId, int... id) {
        this.layoutResId = resId;
        viewIds = id;
    }

    public int[] getViewIds() {
        return viewIds;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isEmpty() {
        return layoutResId == 0 && backgroundColor == 0;
    }
}
