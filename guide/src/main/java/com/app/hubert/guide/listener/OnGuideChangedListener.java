package com.app.hubert.guide.listener;

import com.app.hubert.guide.core.Controller;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public interface OnGuideChangedListener {
    void onShowed(Controller controller);

    void onRemoved(Controller controller);
}