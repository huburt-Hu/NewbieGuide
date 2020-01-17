package com.app.hubert.newbieguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by hubert
 * <p>
 * Created on 2017/9/13.
 */

public class TestFragmentActivity extends AppCompatActivity {

    Fragment fragment = AbcFragment.newInstance("1");
    Fragment fragment2 = AbcFragment.newInstance("2");

    public static void start(Context context) {
        Intent starter = new Intent(context, TestFragmentActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_fragment);
        Button button = (Button) findViewById(R.id.btn_change);
        Button button1 = (Button) findViewById(R.id.btn_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment)
                        .addToBackStack("fragment1")
                        .commit();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment2)
                        .addToBackStack("fragment2")
                        .commit();
            }
        });

    }
}
