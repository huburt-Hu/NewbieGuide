package com.app.hubert.guide.model;

import android.graphics.RectF;
import android.view.View;

/**
 * Created by hubert on 2018/6/6.
 */
public interface HighLight {

    Shape getShape();

    RectF getRectF(View view);

    float getRadius();

    int getRound();

    public enum Shape {
        CIRCLE,//圆形
        RECTANGLE, //矩形
        OVAL,//椭圆
        ROUND_RECTANGLE;//圆角矩形
    }
}
