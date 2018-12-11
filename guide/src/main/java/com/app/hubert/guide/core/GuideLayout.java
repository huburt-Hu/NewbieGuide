package com.app.hubert.guide.core;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.listener.AnimationListenerAdapter;
import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.HighlightOptions;
import com.app.hubert.guide.model.RelativeGuide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class GuideLayout extends FrameLayout {

    public static final int DEFAULT_BACKGROUND_COLOR = 0xb2000000;

    private Controller controller;
    private Paint mPaint;
    public GuidePage guidePage;
    private OnGuideLayoutDismissListener listener;
    private float downX;
    private float downY;
    private int touchSlop;

    public GuideLayout(Context context, GuidePage page, Controller controller) {
        super(context);
        init();
        setGuidePage(page);
        this.controller = controller;
    }

    private GuideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private GuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(xfermode);

        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //ViewGroup默认设定为true，会使onDraw方法不执行，如果复写了onDraw(Canvas)方法，需要清除此标记
        setWillNotDraw(false);

        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private void setGuidePage(GuidePage page) {
        this.guidePage = page;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guidePage.isEverywhereCancelable()) {
                    remove();
                }
            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float upX = event.getX();
                float upY = event.getY();
                if (Math.abs(upX - downX) < touchSlop && Math.abs(upY - downY) < touchSlop) {
                    List<HighLight> highLights = guidePage.getHighLights();
                    for (HighLight highLight : highLights) {
                        RectF rectF = highLight.getRectF((ViewGroup) getParent());
                        if (rectF.contains(upX, upY)) {
                            notifyClickListener(highLight);
                            return true;
                        }
                    }
                    performClick();
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    private void notifyClickListener(HighLight highLight) {
        HighlightOptions options = highLight.getOptions();
        if (options != null) {
            if (options.onClickListener != null) {
                options.onClickListener.onClick(this);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int backgroundColor = guidePage.getBackgroundColor();
        canvas.drawColor(backgroundColor == 0 ? DEFAULT_BACKGROUND_COLOR : backgroundColor);
        drawHighlights(canvas);
    }

    private void drawHighlights(Canvas canvas) {
        List<HighLight> highLights = guidePage.getHighLights();
        if (highLights != null) {
            for (HighLight highLight : highLights) {
                RectF rectF = highLight.getRectF((ViewGroup) getParent());
                switch (highLight.getShape()) {
                    case CIRCLE:
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), highLight.getRadius(), mPaint);
                        break;
                    case OVAL:
                        canvas.drawOval(rectF, mPaint);
                        break;
                    case ROUND_RECTANGLE:
                        canvas.drawRoundRect(rectF, highLight.getRound(), highLight.getRound(), mPaint);
                        break;
                    case RECTANGLE:
                    default:
                        canvas.drawRect(rectF, mPaint);
                        break;
                }
                notifyDrewListener(canvas, highLight, rectF);
            }
        }
    }

    private void notifyDrewListener(Canvas canvas, HighLight highLight, RectF rectF) {
        HighlightOptions options = highLight.getOptions();
        if (options != null) {
            if (options.onHighlightDrewListener != null) {
                options.onHighlightDrewListener.onHighlightDrew(canvas, rectF);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addCustomToLayout(guidePage);
        Animation enterAnimation = guidePage.getEnterAnimation();
        if (enterAnimation != null) {
            startAnimation(enterAnimation);
        }
    }

    /**
     * 将自定义布局填充到guideLayout中
     */
    private void addCustomToLayout(GuidePage guidePage) {
        removeAllViews();
        int layoutResId = guidePage.getLayoutResId();
        if (layoutResId != 0) {
            View view = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int[] viewIds = guidePage.getClickToDismissIds();
            if (viewIds != null && viewIds.length > 0) {
                for (int viewId : viewIds) {
                    View click = view.findViewById(viewId);
                    if (click != null) {
                        click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                remove();
                            }
                        });
                    } else {
                        Log.w(NewbieGuide.TAG, "can't find the view by id : " + viewId + " which used to remove guide page");
                    }
                }
            }
            OnLayoutInflatedListener inflatedListener = guidePage.getOnLayoutInflatedListener();
            if (inflatedListener != null) {
                inflatedListener.onLayoutInflated(view, controller);
            }
            addView(view, params);
        }
        List<RelativeGuide> relativeGuides = guidePage.getRelativeGuides();
        if (relativeGuides.size() > 0) {
            for (RelativeGuide relativeGuide : relativeGuides) {
                addView(relativeGuide.getGuideLayout((ViewGroup) getParent(), controller));
            }
        }
    }

    public void setOnGuideLayoutDismissListener(OnGuideLayoutDismissListener listener) {
        this.listener = listener;
    }

    public void remove() {
        Animation exitAnimation = guidePage.getExitAnimation();
        if (exitAnimation != null) {
            exitAnimation.setAnimationListener(new AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    dismiss();
                }
            });
            startAnimation(exitAnimation);
        } else {
            dismiss();
        }
    }

    private void dismiss() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            if (listener != null) {
                listener.onGuideLayoutDismiss(this);
            }
        }
    }

    public interface OnGuideLayoutDismissListener {
        void onGuideLayoutDismiss(GuideLayout guideLayout);
    }

}
