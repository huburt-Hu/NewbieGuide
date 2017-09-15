package com.app.hubert.library;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class NewbieGuide {

    public static final String TAG = "NewbieGuide";

    /**
     * 成功显示标示
     */
    public static final int SUCCESS = 1;

    /**
     * 显示失败标示，即已经显示过一次
     */
    public static final int FAILED = -1;

    /**
     * 新手指引入口
     *
     * @param activity activity
     * @return builder对象，设置参数
     */
    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static Builder with(Fragment fragment) {
        return new Builder(fragment);
    }

    public static Builder with(android.support.v4.app.Fragment v4Fragment) {
        return new Builder(v4Fragment);
    }

}

