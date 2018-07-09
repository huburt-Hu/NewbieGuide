package com.app.hubert.guide.model;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.app.hubert.guide.util.LogUtil;
import com.app.hubert.guide.util.ViewUtils;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class HighlightView implements HighLight {

    private View mHole;
    private Shape shape;
    private int round;
    /**
     * 高亮相对view的padding
     */
    private int padding;
    private HighlightOptions options;
    private RectF rectF;

    public HighlightView(View mHole, Shape shape, int round, int padding) {
        this.mHole = mHole;
        this.shape = shape;
        this.round = round;
        this.padding = padding;
    }

    public void setOptions(HighlightOptions options) {
        this.options = options;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public HighlightOptions getOptions() {
        return options;
    }

    @Override
    public float getRadius() {
        if (mHole == null) {
            throw new IllegalArgumentException("the highlight view is null!");
        }
        return Math.max(mHole.getWidth() / 2, mHole.getHeight() / 2) + padding;
    }

    @Override
    public RectF getRectF(View target) {
        if (mHole == null) {
            throw new IllegalArgumentException("the highlight view is null!");
        }
        if (rectF == null) {
            rectF = new RectF();
            Rect locationInView = ViewUtils.getLocationInView(target, mHole);
            rectF.left = locationInView.left - padding;
            rectF.top = locationInView.top - padding;
            rectF.right = locationInView.right + padding;
            rectF.bottom = locationInView.bottom + padding;
            LogUtil.i(mHole.getClass().getSimpleName() + "'s location:" + rectF);
        }
        return rectF;
    }


}