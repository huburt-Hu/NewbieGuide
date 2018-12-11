package com.app.hubert.newbieguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.util.LogUtil;

/**
 * Created by hubert on 2018/12/11.
 */
public class ScrollViewActivity extends AppCompatActivity {

    private Controller controller;

    public static void start(Context context) {
        Intent starter = new Intent(context, ScrollViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        final View light = findViewById(R.id.v_light);
        controller = NewbieGuide.with(ScrollViewActivity.this)
                .setLabel("scroll1")
                .alwaysShow(true)
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(light))
                .build();
        ObservableScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setScrollViewListener(new ObservableScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (scrollView.getScrollY() + scrollView.getHeight() -
                        scrollView.getPaddingTop() - scrollView.getPaddingBottom()
                        == scrollView.getChildAt(0).getHeight()) {//判断滑动到底部
                    LogUtil.i("call show");
                    controller.show();
                }
            }
        });
    }
}
