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
    private Shape shape = Shape.RECTANGLE;
    /**
     * 圆角，仅当shape = Shape.ROUND_RECTANGLE才生效
     */
    private int round;
    /**
     * 高亮相对view的padding
     */
    private int padding;

    public static HighlightView newInstance(View view) {
        return new HighlightView(view);
    }

    private HighlightView(View hole) {
        this.mHole = hole;
    }

    public HighlightView setShape(Shape shape) {
        this.shape = shape;
        return this;
    }

    public HighlightView setRound(int round) {
        this.round = round;
        return this;
    }

    public HighlightView setPadding(int padding) {
        this.padding = padding;
        return this;
    }

    public int getPadding() {
        return padding;
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
        RectF rectF = new RectF();
        Rect locationInView = ViewUtils.getLocationInView(target, mHole);
        rectF.left = locationInView.left - padding;
        rectF.top = locationInView.top - padding;
        rectF.right = locationInView.right + padding;
        rectF.bottom = locationInView.bottom + padding;
        LogUtil.i(mHole.getClass().getSimpleName() + "'s location:" + rectF);
        return rectF;
    }


}