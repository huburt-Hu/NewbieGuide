package com.app.hubert.newbieguide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.HighlightOptions;
import com.app.hubert.guide.model.RelativeGuide;
import com.app.hubert.guide.util.ViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //简单使用
        final Button btnSimple = (Button) findViewById(R.id.btn_simple);
        btnSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("guide1")
//                        .setShowCounts(3)//控制次数
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnSimple)
                                .addHighLight(new RectF(0, 800, 200, 1200))
                                .setLayoutRes(R.layout.view_guide_simple))
                        .show();
            }
        });
        //对话框形式
        final Button btnDialog = (Button) findViewById(R.id.btn_dialog);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("guide2")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnDialog)
                                .setEverywhereCancelable(false)//是否点击任意位置消失引导页
                                .setLayoutRes(R.layout.view_guide_dialog, R.id.btn_ok)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {

                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        TextView tv = view.findViewById(R.id.tv_text);
                                        tv.setText("this like dialog");
                                    }
                                }))
                        .show();
            }
        });
        //设置anchor 及 自定义绘制图形
        final View anchorView = findViewById(R.id.ll_anchor);
        final Button btnAnchor = (Button) findViewById(R.id.btn_anchor);
        btnAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options = new HighlightOptions.Builder()
                        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                            @Override
                            public void onHighlightDrew(Canvas canvas, RectF rectF) {
                                Paint paint = new Paint();
                                paint.setColor(Color.WHITE);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(10);
                                paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                            }
                        })
                        .build();
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("anchor")
                        .anchor(anchorView)
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLightWithOptions(btnAnchor, HighLight.Shape.CIRCLE, options)
                                .setLayoutRes(R.layout.view_guide_anchor))
                        .show();
            }
        });
        //监听
        final Button btnListener = findViewById(R.id.btn_listener);
        btnListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("listener")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .setOnGuideChangedListener(new OnGuideChangedListener() {
                            @Override
                            public void onShowed(Controller controller) {
                                Toast.makeText(FirstActivity.this, "引导层显示", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onRemoved(Controller controller) {
                                Toast.makeText(FirstActivity.this, "引导层消失", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(btnListener))
                        .show();
            }
        });

        findViewById(R.id.btn_multi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(FirstActivity.this);
            }
        });

        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewActivity.start(FirstActivity.this);
            }
        });

        final View btnRelative = findViewById(R.id.btn_relative);
        btnRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options = new HighlightOptions.Builder()
                        .setRelativeGuide(new RelativeGuide(R.layout.view_relative_guide, Gravity.LEFT, 100) {
                            @Override
                            protected void onLayoutInflated(View view, Controller controller) {
                                TextView textView = view.findViewById(R.id.tv);
                                textView.setText("inflated");
                            }
                        })
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(FirstActivity.this, "highlight click", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
                GuidePage page = GuidePage.newInstance()
                        .addHighLightWithOptions(btnRelative, options);
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("relative")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(page)
                        .show();
            }
        });

        findViewById(R.id.btn_rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(FirstActivity.this)
                        .setLabel("rect")
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(new RectF(0, 800, 500, 1000))
                        )
                        .show();
            }
        });
    }
}
