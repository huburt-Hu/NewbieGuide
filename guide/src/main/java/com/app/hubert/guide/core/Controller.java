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
import com.app.hubert.guide.model.RelativeGuide;
import com.app.hubert.guide.util.LogUtil;
import com.app.hubert.guide.util.ScreenUtils;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 * <p>
 * guide的控制器，可以通过该类控制引导层的显示与回退，或者重置label
 */
public class Controller {

    private static final String LISTENER_FRAGMENT = "listener_fragment";

    private Activity activity;
    private Fragment fragment;
    private android.support.v4.app.Fragment v4Fragment;
    private OnGuideChangedListener onGuideChangedListener;
    private OnPageChangedListener onPageChangedListener;
    private String label;
    private boolean alwaysShow;
    private int showCounts;//显示次数
    private List<GuidePage> guidePages;
    private int current;//当前页
    private GuideLayout currentLayout;
    private FrameLayout mParentView;
    private SharedPreferences sp;
    private int indexOfChild = -1;//使用anchor时记录的在父布局的位置
    private boolean isShowing;

    public Controller(Builder builder) {
        this.activity = builder.activity;
        this.fragment = builder.fragment;
        this.v4Fragment = builder.v4Fragment;
        this.onGuideChangedListener = builder.onGuideChangedListener;
        this.onPageChangedListener = builder.onPageChangedListener;
        this.label = builder.label;
        this.alwaysShow = builder.alwaysShow;
        this.guidePages = builder.guidePages;
        showCounts = builder.showCounts;

        View anchor = builder.anchor;
        if (anchor == null) {
            anchor = activity.findViewById(android.R.id.content);
        }
        if (anchor instanceof FrameLayout) {
            mParentView = (FrameLayout) anchor;
        } else {
            FrameLayout frameLayout = new FrameLayout(activity);
            ViewGroup parent = (ViewGroup) anchor.getParent();
            indexOfChild = parent.indexOfChild(anchor);
            parent.removeView(anchor);
            if (indexOfChild >= 0) {
                parent.addView(frameLayout, indexOfChild, anchor.getLayoutParams());
            } else {
                parent.addView(frameLayout, anchor.getLayoutParams());
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(anchor, lp);
            mParentView = frameLayout;
        }

        sp = activity.getSharedPreferences(NewbieGuide.TAG, Activity.MODE_PRIVATE);

    }

    /**
     * 显示指引layout
     */
    public void show() {
        final int showed = sp.getInt(label, 0);
        if (!alwaysShow) {
            if (showed >= showCounts) {
                return;
            }
        }

        if (isShowing) {
            return;
        }
        isShowing = true;
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                if (guidePages == null || guidePages.size() == 0) {
                    throw new IllegalStateException("there is no guide to show!! Please add at least one Page.");
                }
                current = 0;
                showGuidePage();
                if (onGuideChangedListener != null) {
                    onGuideChangedListener.onShowed(Controller.this);
                }
                addListenerFragment();
                sp.edit().putInt(label, showed + 1).apply();
            }
        });
    }

    /**
     * 显示相应position的引导页
     *
     * @param position from 0 to (pageSize - 1)
     */
    public void showPage(int position) {
        if (position < 0 || position > guidePages.size() - 1) {
            throw new InvalidParameterException("The Guide page position is out of range. current:"
                    + position + ", range: [ 0, " + guidePages.size() + " )");
        }
        if (current == position) {
            return;
        }
        current = position;
        //fix #59 GuideLayout.setOnGuideLayoutDismissListener() on a null object reference
        if (currentLayout != null) {
            currentLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
                @Override
                public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                    showGuidePage();
                }
            });
            currentLayout.remove();
        } else {
            showGuidePage();
        }
    }

    /**
     * 显示当前引导页的前一页
     */
    public void showPreviewPage() {
        showPage(--current);
    }

    /**
     * 显示current引导页
     */
    private void showGuidePage() {
        GuidePage page = guidePages.get(current);
        GuideLayout guideLayout = new GuideLayout(activity, page, this);
        guideLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
            @Override
            public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                showNextOrRemove();
            }
        });
        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        currentLayout = guideLayout;
        if (onPageChangedListener != null) {
            onPageChangedListener.onPageChanged(current);
        }
        isShowing = true;
    }

    private void showNextOrRemove() {
        if (current < guidePages.size() - 1) {
            current++;
            showGuidePage();
        } else {
            if (onGuideChangedListener != null) {
                onGuideChangedListener.onRemoved(Controller.this);
            }
            removeListenerFragment();
            isShowing = false;
        }
    }

    /**
     * 清楚当前Controller的label记录
     */
    public void resetLabel() {
        resetLabel(label);
    }

    /**
     * 清除"显示过"的标记
     *
     * @param label 引导标示
     */
    public void resetLabel(String label) {
        sp.edit().putInt(label, 0).apply();
    }

    /**
     * 中断引导层的显示，后续未显示的page将不再显示
     */
    public void remove() {
        if (currentLayout != null && currentLayout.getParent() != null) {
            ViewGroup parent = (ViewGroup) currentLayout.getParent();
            parent.removeView(currentLayout);
            //移除anchor添加的frameLayout
            if (!(parent instanceof FrameLayout)) {
                ViewGroup original = (ViewGroup) parent.getParent();
                View anchor = parent.getChildAt(0);
                parent.removeAllViews();
                if (anchor != null) {
                    if (indexOfChild > 0) {
                        original.addView(anchor, indexOfChild, parent.getLayoutParams());
                    } else {
                        original.addView(anchor, parent.getLayoutParams());
                    }
                }
            }
            if (onGuideChangedListener != null) {
                onGuideChangedListener.onRemoved(this);
            }
            currentLayout = null;
        }
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
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
