package com.app.hubert.newbieguide;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

    private OnScrollChangeListener scrollChangeListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(OnScrollChangeListener onScrollChangeListener) {
        this.scrollChangeListener = onScrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollChangeListener != null) {
            scrollChangeListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
