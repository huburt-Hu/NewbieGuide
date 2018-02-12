package com.app.hubert.guide.lifecycle;

public interface FragmentLifecycle {
    void onStart();

    void onStop();

    void onDestroyView();

    void onDestroy();
}