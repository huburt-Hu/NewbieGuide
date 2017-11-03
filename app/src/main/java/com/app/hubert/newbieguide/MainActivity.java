package com.app.hubert.newbieguide;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridViewActivity.start(MainActivity.this);
            }
        });
        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragmentActivity.start(MainActivity.this);
            }
        });

        NewbieGuide.with(this)//传入activity
                .setLabel("guide1")//设置引导层标示，必传！否则报错
                .addHighLight(button)//添加需要高亮的view
                .setLayoutRes(R.layout.view_guide_custom)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                .setBackgroundColor(getResources().getColor(R.color.testColor))
                .alwaysShow(true)//总是显示
                .show();//直接显示引导层


//        Controller controller = NewbieGuide.with(this)
//                .setOnGuideChangedListener(new OnGuideChangedListener() {//设置监听
//                    @Override
//                    public void onShowed(Controller controller) {
//                        //引导层显示
//                    }
//
//                    @Override
//                    public void onRemoved(Controller controller) {
//                        //引导层消失
//                    }
//                })
//                .setBackgroundColor(Color.BLACK)//设置引导层背景色，建议有透明度，默认背景色为：0xb2000000
//                .setEveryWhereCancelable(false)//设置点击任何区域消失，默认为true
//                .setLayoutRes(R.layout.view_guide, R.id.textView)//自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
//                .alwaysShow(true)//是否每次都显示引导层，默认false
//                .build();//构建引导层的控制器
//        controller.resetLabel("guide1");//重置该引导层为未显示过
//        controller.remove();//移除引导层
//        controller.show();//显示引导层
    }
}
