package com.app.hubert.guide.core;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.lifecycle.FragmentLifecycleAdapter;
import com.app.hubert.guide.lifecycle.ListenerFragment;
import com.app.hubert.guide.lifecycle.V4ListenerFragment;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.util.LogUtil;
import com.app.hubert.guide.util.ScreenUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class Controller {

    private static final String LISTENER_FRAGMENT = "listener_fragment";

    private Fragment fragment;
    private android.support.v4.app.Fragment v4Fragment;
    private Activity activity;

    private OnGuideChangedListener onGuideChangedListener;
    private OnPageChangedListener onPageChangedListener;
    private String label;
    private boolean alwaysShow;
    private List<GuidePage> guidePages;
    private int current;//当前页
    private GuideLayout currentLayout;

    private FrameLayout mParentView;
    private SharedPreferences sp;

    private int topMargin;//statusBar的高度

    public Controller(Builder builder) {
        this.activity = builder.activity;
        this.fragment = builder.fragment;
        this.v4Fragment = builder.v4Fragment;
        this.onGuideChangedListener = builder.onGuideChangedListener;
        this.onPageChangedListener = builder.onPageChangedListener;
        this.label = builder.label;
        this.alwaysShow = builder.alwaysShow;
        this.guidePages = builder.guidePages;

        mParentView = (FrameLayout) activity.getWindow().getDecorView();
        sp = activity.getSharedPreferences(NewbieGuide.TAG, Activity.MODE_PRIVATE);

    }

    /**
     * 显示指引layout
     *
     * @return {@link NewbieGuide#SUCCESS} 表示成功显示，{@link NewbieGuide#FAILED} 表示已经显示过，不再显示
     */
    public void show() {
        if (!alwaysShow) {
            boolean showed = sp.getBoolean(label, false);
            return;
        }
        //fix oppo等部分手机无法关闭硬件加速问题
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mParentView.post(new Runnable() {
            @Override
            public void run() {
                getTopMargin();
                if (guidePages == null || guidePages.size() == 0) {
                    throw new IllegalStateException("there is no guide to show!! Please add alast one Page.");
                }
                current = 0;
                showGuidePage();
                if (onGuideChangedListener != null) {
                    onGuideChangedListener.onShowed(Controller.this);
                }
                addListenerFragment();
                sp.edit().putBoolean(label, true).apply();
            }
        });
    }

    /**
     * 显示current引导页
     */
    private void showGuidePage() {
        GuidePage page = guidePages.get(current);
        GuideLayout guideLayout = new GuideLayout(activity);
        guideLayout.setGuidePage(page);
        addCustomToLayout(guideLayout, page);
        guideLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
            @Override
            public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                if (current < guidePages.size() - 1) {
                    current++;
                    showGuidePage();
                    if (onPageChangedListener != null) {
                        onPageChangedListener.onPageChanged(current);
                    }
                } else {
                    if (onGuideChangedListener != null) {
                        onGuideChangedListener.onRemoved(Controller.this);
                    }
                    removeListenerFragment();
                }
            }
        });
        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        currentLayout = guideLayout;
    }

    /**
     * 将自定义布局填充到guideLayout中
     *
     * @param guideLayout
     */
    private void addCustomToLayout(final GuideLayout guideLayout, GuidePage guidePage) {
        guideLayout.removeAllViews();
        int layoutResId = guidePage.getLayoutResId();
        if (layoutResId != 0) {
            View view = LayoutInflater.from(activity).inflate(layoutResId, guideLayout, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.topMargin = topMargin;
            params.bottomMargin = ScreenUtils.getNavigationBarHeight(activity);
            int[] viewIds = guidePage.getClickToDismissIds();
            if (viewIds != null && viewIds.length > 0) {
                for (int viewId : viewIds) {
                    View click = view.findViewById(viewId);
                    if (click != null) {
                        click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                guideLayout.remove();
                            }
                        });
                    } else {
                        Log.w(NewbieGuide.TAG, "can't find the view by id : " + viewId + " which used to remove guide page");
                    }
                }
            }
            OnLayoutInflatedListener inflatedListener = guidePage.getOnLayoutInflatedListener();
            if (inflatedListener != null) {
                inflatedListener.onLayoutInflated(view);
            }
            guideLayout.addView(view, params);
        }
    }

    private void getTopMargin() {
        final View contentView = activity.findViewById(android.R.id.content);
        int[] location = new int[2];
        contentView.getLocationOnScreen(location);
        topMargin = location[1];
        LogUtil.i("contentView top:" + topMargin);
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

    /**
     * 中断引导层的显示，后续未显示的page将不再显示
     */
    public void remove() {
        if (currentLayout != null && currentLayout.getParent() != null) {
            ((ViewGroup) currentLayout.getParent()).removeView(currentLayout);
            if (onGuideChangedListener != null) {
                onGuideChangedListener.onRemoved(this);
            }
        }
    }

    private void addListenerFragment() {
        //fragment监听销毁界面关闭引导层
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            compatibleFragment(fragment);
            FragmentManager fm = fragment.getChildFragmentManager();
            ListenerFragment listenerFragment = (ListenerFragment) fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (listenerFragment == null) {
                listenerFragment = new ListenerFragment();
                fm.beginTransaction().add(listenerFragment, LISTENER_FRAGMENT).commitAllowingStateLoss();
            }
            listenerFragment.setFragmentLifecycle(new FragmentLifecycleAdapter() {
                @Override
                public void onDestroyView() {
                    LogUtil.i("ListenerFragment.onDestroyView");
                    remove();
                }
            });
        }

        if (v4Fragment != null) {
            android.support.v4.app.FragmentManager v4Fm = v4Fragment.getChildFragmentManager();
            V4ListenerFragment v4ListenerFragment = (V4ListenerFragment) v4Fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (v4ListenerFragment == null) {
                v4ListenerFragment = new V4ListenerFragment();
                v4Fm.beginTransaction().add(v4ListenerFragment, LISTENER_FRAGMENT).commitAllowingStateLoss();
            }
            v4ListenerFragment.setFragmentLifecycle(new FragmentLifecycleAdapter() {
                @Override
                public void onDestroyView() {
                    LogUtil.i("v4ListenerFragment.onDestroyView");
                    remove();
                }
            });
        }
    }

    private void removeListenerFragment() {
        //隐藏引导层时移除监听fragment
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            FragmentManager fm = fragment.getChildFragmentManager();
            ListenerFragment listenerFragment = (ListenerFragment) fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (listenerFragment != null) {
                fm.beginTransaction().remove(listenerFragment).commitAllowingStateLoss();
            }
        }
        if (v4Fragment != null) {
            android.support.v4.app.FragmentManager v4Fm = v4Fragment.getChildFragmentManager();
            V4ListenerFragment v4ListenerFragment = (V4ListenerFragment) v4Fm.findFragmentByTag(LISTENER_FRAGMENT);
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