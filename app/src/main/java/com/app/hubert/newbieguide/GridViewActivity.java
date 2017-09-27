package com.app.hubert.newbieguide;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends AppCompatActivity {

    private String[] from = {"image", "title"};
    private int[] to = {R.id.image, R.id.title};

    public static void start(Context context) {
        Intent starter = new Intent(context, GridViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        final GridView gridView = (GridView) findViewById(R.id.grid_view);
        final SimpleAdapter pictureAdapter = new SimpleAdapter(this, getList(),
                R.layout.picture_item, from, to);
        gridView.setAdapter(pictureAdapter);
        gridView.post(new Runnable() {
            @Override
            public void run() {
                //高亮gridView的第2个子view
                View childAt = gridView.getChildAt(1);
                NewbieGuide.with(GridViewActivity.this)
                        .setLabel("grid_view_guide")
                        .addHighLight(childAt, HighLight.Type.RECTANGLE)
                        .setLayoutRes(R.layout.view_guide)
                        .alwaysShow(true)
                        .show();
            }
        });
    }

    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] titles = new String[]{"本地音乐", "我的最爱", "我的下载", "我的歌单", "最近播放", "我的最爱", "我的下载", "我的歌单", "最近播放"};
        Integer[] images = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", titles[i]);
            list.add(map);
        }
        return list;
    }
}

