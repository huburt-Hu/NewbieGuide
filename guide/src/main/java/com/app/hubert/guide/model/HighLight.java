package com.app.hubert.guide.model;

import android.graphics.RectF;
import android.view.View;

import com.app.hubert.guide.util.LogUtil;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class HighLight {

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

    public static HighLight newInstance(View view) {
        return new HighLight(view);
    }

    private HighLight(View hole) {
        this.mHole = hole;
    }

    public HighLight setShape(Shape shape) {
        this.shape = shape;
        return this;
    }

    public HighLight setRound(int round) {
        this.round = round;
        return this;
    }

    public HighLight setPadding(int padding) {
        this.padding = padding;
        return this;
    }

    public int getPadding() {
        return padding;
    }

    public Shape getShape() {
        return shape;
    }

    public int getRound() {
        return round;
    }

    public int getRadius() {
        return mHole != null ? Math.max(mHole.getWidth() / 2, mHole.getHeight() / 2) : 0;
    }

    public RectF getRectF() {
        RectF rectF = new RectF();
        int[] location = new int[2];
        mHole.getLocationOnScreen(location);
        rectF.left = location[0] - padding;
        rectF.top = location[1] - padding;
        rectF.right = location[0] + mHole.getWidth() + padding * 2;
        rectF.bottom = location[1] + mHole.getHeight() + padding * 2;
        LogUtil.i(mHole.getClass().getSimpleName() + "'s location:" + rectF);
        return rectF;
    }

    public enum Shape {
        CIRCLE,//圆形
        RECTANGLE, //矩形
        OVAL,//椭圆
        ROUND_RECTANGLE;//圆角矩形
    }

}