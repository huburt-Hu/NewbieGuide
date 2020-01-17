package com.app.hubert.newbieguide;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.NewbieGuide;

/**
 * Created by hubert
 * <p>
 * Created on 2017/9/13.
 */

public class AbcFragment extends Fragment {

    private String text;

    public static AbcFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString("text", text);
        AbcFragment fragment = new AbcFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            text = arguments.getString("text");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_abc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView textView = (TextView) view.findViewById(R.id.tv);
        textView.setText("fragment:" + text);
        if ("2".equals(text)) {
            NewbieGuide.with(this)//传入fragment
                    .setLabel("guide2")//设置引导层标示，必传！否则报错
                    .alwaysShow(true)
                    .setOnGuideChangedListener(new OnGuideChangedListener() {
                        @Override
                        public void onShowed(Controller controller) {

                        }

                        @Override
                        public void onRemoved(Controller controller) {
                            Log.e("tag", "onRemoved");
                        }
                    })
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(textView, HighLight.Shape.CIRCLE)//添加需要高亮的view
                            .setLayoutRes(R.layout.view_guide)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                    )
                    .show();//直接显示引导层
        }
    }
}
