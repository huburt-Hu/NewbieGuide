package com.app.hubert.guide.model;

import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by hubert on 2018/6/6.
 */
public class HighlightRectF implements HighLight {

    private RectF rectF;
    private Shape shape;
    private int round;
    private View.OnClickListener onClickListener;

    public HighlightRectF(RectF rectF, Shape shape, int round,@Nullable View.OnClickListener onClickListener) {
        this.rectF = rectF;
        this.shape = shape;
        this.round = round;
        this.onClickListener = onClickListener;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public RectF getRectF(View view) {
        return rectF;
    }

    @Override
    public float getRadius() {
        return Math.min(rectF.width() / 2, rectF.height() / 2);
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
