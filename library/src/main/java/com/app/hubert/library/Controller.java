package com.app.hubert.library;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class Controller {

    public static final String TAG = "listener_fragment";

    private Fragment fragment;
    private android.support.v4.app.Fragment v4Fragment;
    private Activity activity;
    private List<HighLight> list = new ArrayList<>();
    private OnGuideChangedListener onGuideChangedListener;
    private boolean everyWhereCancelable = true;
    private int backgroundColor;
    private String label;
    private boolean alwaysShow;
    private int layoutResId;
    private int[] viewIds;

    private FrameLayout mParentView;
    private GuideLayout guideLayout;
    private SharedPreferences sp;

    public Controller(Builder builder) {
        this.activity = builder.getActivity();
        this.fragment = builder.getFragment();
        this.v4Fragment = builder.getV4Fragment();
        this.list = builder.getList();
        this.backgroundColor = builder.getBackgroundColor();
        this.onGuideChangedListener = builder.getOnGuideChangedListener();
        this.everyWhereCancelable = builder.isEveryWhereCancelable();
        this.label = builder.getLabel();
        this.alwaysShow = builder.isAlwaysShow();
        this.layoutResId = builder.getLayoutResId();
        this.viewIds = builder.getViewIds();

        mParentView = (FrameLayout) activity.getWindow().getDecorView();
        sp = activity.getSharedPreferences(NewbieGuide.TAG, Activity.MODE_PRIVATE);
    }

    /**
     * 显示指引layout
     *
     * @return {@link NewbieGuide#SUCCESS} 表示成功显示，{@link NewbieGuide#FAILED} 表示已经显示过，不再显示
     */
    public int show() {
        if (!alwaysShow) {
            boolean showed = sp.getBoolean(label, false);
            if (showed) return NewbieGuide.FAILED;
        }
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        guideLayout = new GuideLayout(activity);
        guideLayout.setHighLights(list);
        if (backgroundColor != 0)
            guideLayout.setBackgroundColor(backgroundColor);

        if (layoutResId != 0) {
            View view = LayoutInflater.from(activity).inflate(layoutResId, guideLayout, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = ScreenUtils.getStatusBarHeight(activity);
            params.bottomMargin = ScreenUtils.getNavigationBarHeight(activity);
            if (viewIds != null) {
                for (int viewId : viewIds) {
                    view.findViewById(viewId).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            remove();
                        }
                    });
                }
            }
            guideLayout.addView(view, params);
        }

        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (onGuideChangedListener != null) onGuideChangedListener.onShowed(this);
        sp.edit().putBoolean(label, true).apply();
        if (everyWhereCancelable) {
            guideLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    remove();
                    return true;
                }
            });
        }
        //fragment监听销毁界面关闭引导层
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            compatibleFragment(fragment);
            FragmentManager fm = fragment.getChildFragmentManager();
            ListenerFragment listenerFragment = (ListenerFragment) fm.findFragmentByTag(TAG);
            if (listenerFragment == null) {
                listenerFragment = new ListenerFragment();
                fm.beginTransaction().add(listenerFragment, TAG).commitAllowingStateLoss();
            }
            listenerFragment.setFragmentLifecycle(new FragmentLifecycleAdapter() {
                @Override
                public void onDestroyView() {
                    remove();
                }
            });
        }

        if (v4Fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            android.support.v4.app.FragmentManager v4Fm = v4Fragment.getChildFragmentManager();
            V4ListenerFragment v4ListenerFragment = (V4ListenerFragment) v4Fm.findFragmentByTag(TAG);
            if (v4ListenerFragment == null) {
                v4ListenerFragment = new V4ListenerFragment();
                v4Fm.beginTransaction().add(v4ListenerFragment, TAG).commitAllowingStateLoss();
            }
            v4ListenerFragment.setFragmentLifecycle(new FragmentLifecycleAdapter() {
                @Override
                public void onDestroyView() {
                    Log.e("NewbieGuide", "onDestroyView");
                    remove();
                }
            });
        }
        return NewbieGuide.SUCCESS;
    }

    /**
     * 清除"显示过"的标记
     *
     * @param label 引导标示
     */
    public void resetLabel(String label) {
        sp.edit().putBoolean(label, false).apply();
    }

    public void remove() {
        if (guideLayout != null && guideLayout.getParent() != null) {
            ((ViewGroup) guideLayout.getParent()).removeView(guideLayout);
            if (onGuideChangedListener != null) onGuideChangedListener.onRemoved(this);
        }
        //隐藏引导层时移除监听fragment
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            FragmentManager fm = fragment.getChildFragmentManager();
            ListenerFragment listenerFragment = (ListenerFragment) fm.findFragmentByTag(TAG);
            if (listenerFragment != null) {
                fm.beginTransaction().remove(listenerFragment).commitAllowingStateLoss();
            }
        }
        if (v4Fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            android.support.v4.app.FragmentManager v4Fm = v4Fragment.getChildFragmentManager();
            V4ListenerFragment v4ListenerFragment = (V4ListenerFragment) v4Fm.findFragmentByTag(TAG);
            if (v4ListenerFragment != null) {
                v4Fm.beginTransaction().remove(v4ListenerFragment).commitAllowingStateLoss();
            }
        }
    }

    /**
     * For bug of Fragment in Android
     * https://issuetracker.google.com/issues/36963722
     *
     * @param fragment
     */
    private void compatibleFragment(Fragment fragment) {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(fragment, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}