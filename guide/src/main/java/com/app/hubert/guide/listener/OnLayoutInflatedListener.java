package com.app.hubert.guide.listener;

import android.view.View;

/**
 * Created by hubert on 2018/2/12.
 *
 * 用于引导层布局初始化
 */

public interface OnLayoutInflatedListener {
    /**
     *
     * @param view {@link com.app.hubert.guide.core.Builder#setLayoutRes}方法传入的layoutRes填充后的view
     */
    void onLayoutInflated(View view);
}
