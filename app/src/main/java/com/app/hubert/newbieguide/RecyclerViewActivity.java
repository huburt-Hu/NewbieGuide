package com.app.hubert.newbieguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("item " + i);
        }
        recyclerView.setAdapter(new Adapter(data));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                recyclerView.getLayoutManager()
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                NewbieGuide.with(RecyclerViewActivity.this)
                        .setLabel("grid_view_guide")
                        .alwaysShow(true)
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(recyclerView.getChildAt(0))
                                .setEverywhereCancelable(false)
                                .setLayoutRes(R.layout.view_guide_anchor))
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
