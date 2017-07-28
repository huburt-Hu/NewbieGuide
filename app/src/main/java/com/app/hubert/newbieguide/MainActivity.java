package com.app.hubert.newbieguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.hubert.library.Controller;
import com.app.hubert.library.HighLight;
import com.app.hubert.library.NewbieGuide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv);
        Controller controller = NewbieGuide
                .with(this)
                .setLabel("guide1")
                .addHighLight(textView, HighLight.Type.ROUND_RECTANGLE, 10)
                .setLayoutRes(R.layout.view_guide, R.id.textView)
                .build();
        controller.resetLabel("guide1");

        controller.show();
    }
}
