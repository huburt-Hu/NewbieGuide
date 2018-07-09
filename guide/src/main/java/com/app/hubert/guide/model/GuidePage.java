package com.app.hubert.guide.model;

import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;

import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/11/16.
 */

public class GuidePage {

    private List<HighLight> highLights = new ArrayList<>();
    private boolean everywhereCancelable = true;
    private int backgroundColor;

    private int layoutResId;
    private int[] clickToDismissIds;
    private OnLayoutInflatedListener onLayoutInflatedListener;
    private OnHighlightDrewListener onHighlightDrewListener;
    private Animation enterAnimation, exitAnimation;

    public static GuidePage newInstance() {
        return new GuidePage();
    }

    public GuidePage addHighLight(View view) {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0, null);
    }

    public GuidePage addHighLight(View view, RelativeGuide relativeGuide) {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0, relativeGuide);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape) {
        return addHighLight(view, shape, 0, 0, null);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, RelativeGuide relativeGuide) {
        return addHighLight(view, shape, 0, 0, relativeGuide);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, int padding) {
        return addHighLight(view, shape, 0, padding, null);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, int padding, RelativeGuide relativeGuide) {
        return addHighLight(view, shape, 0, padding, relativeGuide);
    }

    /**
     * 添加需要高亮的view
     *
     * @param view          需要高亮的view
     * @param shape         高亮形状{@link com.app.hubert.guide.model.HighLight.Shape}
     * @param round         圆角尺寸，单位dp，仅{@link com.app.hubert.guide.model.HighLight.Shape#ROUND_RECTANGLE}有效
     * @param padding       高亮相对view的padding,单位px
     * @param relativeGuide 相对于高亮的引导布局
     */
    public GuidePage addHighLight(View view, HighLight.Shape shape, int round, int padding,
                                  @Nullable RelativeGuide relativeGuide) {
        HighlightView highlight = new HighlightView(view, shape, round, padding);
        if (relativeGuide != null) {
            relativeGuide.highLight = highlight;
            highlight.setOptions(new HighlightOptions.Builder().setRelativeGuide(relativeGuide).build());
        }
        highLights.add(highlight);
        return this;
    }

    public GuidePage addHighLight(RectF rectF) {
        return addHighLight(rectF, HighLight.Shape.RECTANGLE, 0, null);
    }

    public GuidePage addHighLight(RectF rectF, RelativeGuide relativeGuide) {
        return addHighLight(rectF, HighLight.Shape.RECTANGLE, 0, relativeGuide);
    }

    public GuidePage addHighLight(RectF rectF, HighLight.Shape shape) {
        return addHighLight(rectF, shape, 0, null);
    }

    public GuidePage addHighLight(RectF rectF, HighLight.Shape shape, RelativeGuide relativeGuide) {
        return addHighLight(rectF, shape, 0, relativeGuide);
    }

    public GuidePage addHighLight(RectF rectF, HighLight.Shape shape, int round) {
        return addHighLight(rectF, shape, round, null);
    }

    /**
     * 添加高亮区域
     *
     * @param rectF         高亮区域，相对与anchor view（默认是decorView）
     * @param shape         高亮形状{@link com.app.hubert.guide.model.HighLight.Shape}
     * @param round         圆角尺寸，单位dp，仅{@link com.app.hubert.guide.model.HighLight.Shape#ROUND_RECTANGLE}有效
     * @param relativeGuide 相对于高亮的引导布局
     */
    public GuidePage addHighLight(RectF rectF, HighLight.Shape shape, int round, RelativeGuide relativeGuide) {
        HighlightRectF highlight = new HighlightRectF(rectF, shape, round);
        if (relativeGuide != null) {
            relativeGuide.highLight = highlight;
            highlight.setOptions(new HighlightOptions.Builder().setRelativeGuide(relativeGuide).build());
        }
        highLights.add(highlight);
        return this;
    }

    public GuidePage addHighLightWithOptions(View view, HighlightOptions options) {
        return addHighLightWithOptions(view, HighLight.Shape.RECTANGLE, 0, 0, options);
    }

    public GuidePage addHighLightWithOptions(View view, HighLight.Shape shape, HighlightOptions options) {
        return addHighLightWithOptions(view, shape, 0, 0, options);
    }

    public GuidePage addHighLightWithOptions(View view, HighLight.Shape shape, int round, int padding, HighlightOptions options) {
        HighlightView highlight = new HighlightView(view, shape, round, padding);
        if (options != null) {
            if (options.relativeGuide != null) {
                options.relativeGuide.highLight = highlight;
            }
        }
        highlight.setOptions(options);
        highLights.add(highlight);
        return this;
    }

    public GuidePage addHighLightWithOptions(RectF rectF, HighlightOptions options) {
        return addHighLightWithOptions(rectF, HighLight.Shape.RECTANGLE, 0, options);
    }

    public GuidePage addHighLightWithOptions(RectF rectF, HighLight.Shape shape, HighlightOptions options) {
        return addHighLightWithOptions(rectF, shape, 0, options);
    }

    public GuidePage addHighLightWithOptions(RectF rectF, HighLight.Shape shape, int round, HighlightOptions options) {
        HighlightRectF highlight = new HighlightRectF(rectF, shape, round);
        if (options != null) {
            if (options.relativeGuide != null) {
                options.relativeGuide.highLight = highlight;
            }
        }
        highlight.setOptions(options);
        highLights.add(highlight);
        return this;
    }

    /**
     * 添加引导层布局
     *
     * @param resId 布局id
     * @param id    布局中点击消失引导页的控件id
     */
    public GuidePage setLayoutRes(@LayoutRes int resId, int... id) {
        this.layoutResId = resId;
        clickToDismissIds = id;
        return this;
    }

    public GuidePage setEverywhereCancelable(boolean everywhereCancelable) {
        this.everywhereCancelable = everywhereCancelable;
        return this;
    }

    /**
     * 设置背景色
     */
    public GuidePage setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * 设置自定义layout填充监听，用于自定义layout初始化
     *
     * @param onLayoutInflatedListener listener
     */
    public GuidePage setOnLayoutInflatedListener(OnLayoutInflatedListener onLayoutInflatedListener) {
        this.onLayoutInflatedListener = onLayoutInflatedListener;
        return this;
    }

    /**
     * 设置进入动画
     */
    public GuidePage setEnterAnimation(Animation enterAnimation) {
        this.enterAnimation = enterAnimation;
        return this;
    }

    /**
     * 设置退出动画
     */
    public GuidePage setExitAnimation(Animation exitAnimation) {
        this.exitAnimation = exitAnimation;
        return this;
    }

    public boolean isEverywhereCancelable() {
        return everywhereCancelable;
    }

    public boolean isEmpty() {
        return layoutResId == 0 && highLights.size() == 0;
    }

    public List<HighLight> getHighLights() {
        return highLights;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public int[] getClickToDismissIds() {
        return clickToDismissIds;
    }

    public OnLayoutInflatedListener getOnLayoutInflatedListener() {
        return onLayoutInflatedListener;
    }

    public Animation getEnterAnimation() {
        return enterAnimation;
    }

    public Animation getExitAnimation() {
        return exitAnimation;
    }

    public List<RelativeGuide> getRelativeGuides() {
        List<RelativeGuide> relativeGuides = new ArrayList<>();
        for (HighLight highLight : highLights) {
            HighlightOptions options = highLight.getOptions();
            if (options != null) {
                if (options.relativeGuide != null) {
                    relativeGuides.add(options.relativeGuide);
                }
            }
        }
        return relativeGuides;
    }
}
