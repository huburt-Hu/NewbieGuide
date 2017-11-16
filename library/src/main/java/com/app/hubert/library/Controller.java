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

    private OnGuideChangedListener onGuideChangedListener;
    private OnPageChangedListener onPageChangedListener;
    private String label;
    private boolean alwaysShow;
    private List<GuidePage> guidePages;
    private int current;

    private FrameLayout mParentView;
    private GuideLayout guideLayout;
    private SharedPreferences sp;

    public Controller(Builder builder) {
        this.activity = builder.getActivity();
        this.fragment = builder.getFragment();
        this.v4Fragment = builder.getV4Fragment();
        this.onGuideChangedListener = builder.getOnGuideChangedListener();
        this.onPageChangedListener = builder.getOnPageChangedListener();
        this.label = builder.getLabel();
        this.alwaysShow = builder.isAlwaysShow();
        this.guidePages = builder.getGuidePages();

        mParentView = (FrameLayout) activity.getWindow().getDecorView();
        guideLayout = new GuideLayout(activity);
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
        //fix oppo等部分手机无法关闭硬件加速问题
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        if (guidePages != null && guidePages.size() > 0) {
            current = 0;
            GuidePage page = guidePages.get(0);
            updatePage(page);
            mParentView.addView(guideLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (onGuideChangedListener != null) onGuideChangedListener.onShowed(this);
            if (onPageChangedListener != null) onPageChangedListener.onPageChanged(current);
            sp.edit().putBoolean(label, true).apply();
            guideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (guidePages.get(current).isEveryWhereCancelable()) {
                        nextOrRemove();
                    }
                }
            });
            addListenerFragment();
        } else {
            throw new IllegalStateException();
        }
        return NewbieGuide.SUCCESS;
    }

    private void nextOrRemove() {
        if (current < guidePages.size() - 1) {
            updatePage(guidePages.get(++current));
            if (onPageChangedListener != null) onPageChangedListener.onPageChanged(current);
        } else {
            remove();
        }
    }

    private void addListenerFragment() {
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
                    Log.i("NewbieGuide", "ListenerFragment.onDestroyView");
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
                    Log.i("NewbieGuide", "v4ListenerFragment.onDestroyView");
                    remove();
                }
            });
        }
    }

    private void updatePage(GuidePage page) {
        guideLayout.setHighLights(page.getHighLights());
        guideLayout.setBackgroundColor(page.getBackgroundColor());
        guideLayout.removeAllViews();
        if (page.getLayoutResId() != 0) {
            View view = LayoutInflater.from(activity).inflate(page.getLayoutResId(), guideLayout, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (!page.isFullScreen()) {
                params.topMargin = ScreenUtils.getStatusBarHeight(activity);
            }
            params.bottomMargin = ScreenUtils.getNavigationBarHeight(activity);
            int[] viewIds = page.getViewIds();
            if (viewIds != null) {
                for (int viewId : viewIds) {
                    View click = view.findViewById(viewId);
                    if (click != null) {
                        click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextOrRemove();
                            }
                        });
                    } else {
                        Log.e("NewbieGuide", "can't find the view by id : " + viewId + " which used to remove guide layout");
                    }
                }
            }
            guideLayout.addView(view, params);
        }
        guideLayout.invalidate();
    }

    public void resetLabel() {
        resetLabel(label);
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
        removeListenerFragment();
    }

    private void removeListenerFragment() {
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