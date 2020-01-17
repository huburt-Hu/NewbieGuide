package com.app.hubert.newbieguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/9/23.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("item " + i);
        }
        recyclerView.setAdapter(new Adapter(data));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int targetPosition = 20;
                    if (firstVisibleItemPosition <= targetPosition
                            && targetPosition < lastVisibleItemPosition) {//指定位置滚动到屏幕中
                        NewbieGuide.with(RecyclerViewActivity.this)
                                .setLabel("grid_view_guide")
                                .alwaysShow(true)
                                .addGuidePage(GuidePage.newInstance()
                                        //注意获取position位置view的方法，不要使用getChildAt
                                        .addHighLight(layoutManager.findViewByPosition(targetPosition))
                                        .setLayoutRes(R.layout.view_guide_rv1)
                                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                            @Override
                                            public void onLayoutInflated(View view, Controller controller) {
                                                TextView tv = view.findViewById(R.id.tv);
                                                tv.setText("滚动后才能可见的item这样使用");
                                            }
                                        })
                                )
                                .show();
                    }
                }
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                NewbieGuide.with(RecyclerViewActivity.this)
                        .setLabel("grid_view_guide")
                        .alwaysShow(true)
                        .addGuidePage(GuidePage.newInstance()
                                //getChildAt获取的是屏幕中可见的第一个，并不是数据中的position
                                .addHighLight(recyclerView.getChildAt(0))
                                .setLayoutRes(R.layout.view_guide_rv1)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.tv);
                                        tv.setText("第一页可见的item这样使用");
                                    }
                                })
                        )
                        .show();
            }
        });
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, RecyclerViewActivity.class));
    }

    public static class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public Adapter(@Nullable List<String> data) {
            super(R.layout.item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv, item);
        }
    }

}
