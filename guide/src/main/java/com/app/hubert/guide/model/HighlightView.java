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
            rectF = fetchLocation(target);
        } else if (options != null && options.fetchLocationEveryTime) {
            rectF = fetchLocation(target);
        }
        LogUtil.i(mHole.getClass().getSimpleName() + "'s location:" + rectF);
        return rectF;
    }

    private RectF fetchLocation(View target) {
        RectF location = new RectF();
        Rect locationInView = new Rect();
        try{
            locationInView = ViewUtils.getLocationInView(target, mHole);
        }catch(IllegalArgumentException exception){
            // ViewUtils.getLocationInView(target, mHole); 中包含了一些参数异常的抛出,
            // 假设出现异常,则改为使用获取view可见部分相对于屏幕的坐标
            target.getGlobalVisibleRect(locationInView);
        }
        location.left = locationInView.left - padding;
        location.top = locationInView.top - padding;
        location.right = locationInView.right + padding;
        location.bottom = locationInView.bottom + padding;
        return location;
    }

}
