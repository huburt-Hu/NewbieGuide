package com.app.hubert.guide.listener;

import com.app.hubert.guide.core.Controller;

/**
 * Created by hubert  on 2017/7/27.
 * <p>
 * 引导层显示和消失的监听
 */
public interface OnGuideChangedListener {
    /**
     * 当引导层显示时回调
     *
     * @param controller
     */
    void onShowed(Controller controller);

    /**
     * 当引导层消失时回调
     *
     * @param controller
     */
    void onRemoved(Controller controller);
}