package com.app.hubert.newbieguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.HighLight;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setTranslucent(this, 0);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv);
        TextView tvBottom = (TextView) findViewById(R.id.tv_bottom);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridViewActivity.start(MainActivity.this);
            }
        });
        final Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragmentActivity.start(MainActivity.this);
            }
        });

        //简单使用，只显示一页引导页
//        NewbieGuide.with(this)//传入activity
//                .setLabel("guide1")//设置引导层标示，必传！否则报错
//                .addHighLight(button)//添加需要高亮的view
//                .setLayoutRes(R.layout.view_guide_custom)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
//                .setBackgroundColor(getResources().getColor(R.color.testColor))
//                .alwaysShow(true)//总是显示
//                .show();//直接显示引导层

        //v1.1.1之前版本使用这种方式连续显示多页引导页
//        Controller controller = NewbieGuide.with(this)
//                .setLabel("test")
//                .setOnGuideChangedListener(new OnGuideChangedListener() {//设置监听
//                    @Override
//                    public void onShowed(Controller controller) {
//                        //引导层显示
//                    }
//
//                    @Override
//                    public void onRemoved(Controller controller) {
//                        //引导层消失
//                        NewbieGuide.with(MainActivity.this)//传入activity
//                                .setLabel("guide1")//设置引导层标示，必传！否则报错
//                                .addHighLight(button)//添加需要高亮的view
//                                .setLayoutRes(R.layout.view_guide_custom)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
//                                .setBackgroundColor(getResources().getColor(R.color.testColor))
//                                .alwaysShow(true)//总是显示
//                                .show();//直接显示引导层
//                    }
//                })
//                .addHighLight(textView)
//                .setEverywhereCancelable(false)//设置点击任何区域消失，默认为true
//                .setLayoutRes(R.layout.view_guide, R.id.iv)//自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
//                .alwaysShow(true)//是否每次都显示引导层，默认false
//                .build();
//
//        controller.resetLabel();
//        controller.remove();
//        controller.show();

        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        //新增多页模式，即一个引导层显示多页引导内容
        NewbieGuide.with(this)
                .setLabel("page")//设置引导层标示区分不同引导层，必传！否则报错
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {
                        Log.e(TAG, "NewbieGuide  onPageChanged: " + page);
                        //引导页切换，page为当前页位置，从0开始
                    }
                })
                .alwaysShow(true)//是否每次都显示引导层，默认false，只显示一次
                /*-------------以上元素为引导层属性--------------*/

                .addHighLight(textView)//设置高亮的view
                .addHighLight(tvBottom, HighLight.Shape.RECTANGLE, 0, 10)
                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                .setEnterAnimation(enterAnimation)
                .setExitAnimation(exitAnimation)
                .asPage()//保存参数为第一页
                /*------------- 第一页引导页的属性 --------------*/

                .addHighLight(button)//从新设置第二页的参数
                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                    @Override
                    public void onLayoutInflated(View view) {
                        TextView tv = view.findViewById(R.id.textView2);
                        tv.setText("onLayoutInflated");
                    }
                })
                .setLayoutRes(R.layout.view_guide)
                .addHighLight(tvBottom)
                .asPage()
                /*------------- 第二页引导页的属性 --------------*/

                .addHighLight(textView)
                .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的空间id
                .setEveryWhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                .setBackgroundColor(getResources().getColor(R.color.testColor))//设置背景色，建议使用有透明度的颜色
//                .asPage()//只有一页或者最后一页可以省略
                /*------------- 第三页引导页的属性 --------------*/


                .show();//显示引导层
    }
}
