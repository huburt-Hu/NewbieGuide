package com.app.hubert.guide.model;

import android.graphics.RectF;
import android.view.View;

/**
 * Created by hubert on 2018/6/6.
 */
public class HighlightRectF implements HighLight {

    private RectF rectF;
    private Shape shape;
    private int round;

    public HighlightRectF(RectF rectF, Shape shape, int round) {
        this.rectF = rectF;
        this.shape = shape;
        this.round = round;
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
}
