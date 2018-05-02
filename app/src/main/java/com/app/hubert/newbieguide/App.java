package com.app.hubert.newbieguide;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by hubert on 2018/5/2.
 */
public class App extends Application {

    private RefWatcher refWatcher;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
        // Normal app init code...
    }

    public static App getInstance() {
        return instance;
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
