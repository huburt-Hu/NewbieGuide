package com.app.hubert.library;

import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class HighLight {

    private View mHole;
    private Type mType;
    private int round;

    public HighLight(View hole, Type type) {
        this.mHole = hole;
        this.mType = type;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRadius() {
        return mHole != null ? Math.max(mHole.getWidth() / 2, mHole.getHeight() / 2) : 0;
    }

    public RectF getRectF() {
        RectF rectF = new RectF();
        if (mHole != null) {
            int[] location = new int[2];
            mHole.getLocationOnScreen(location);
            rectF.left = location[0];
            rectF.top = location[1];
            rectF.right = location[0] + mHole.getWidth();
            rectF.bottom = location[1] + mHole.getHeight();
            LogUtil.i(mHole.getClass().getSimpleName() + "'s location:" + rectF);
        }
        return rectF;
    }

    public Type getType() {
        return mType;
    }

    public static enum Type {
        CIRCLE,//圆形
        RECTANGLE, //矩形
        OVAL,//椭圆
        ROUND_RECTANGLE;//圆角矩形
    }

}