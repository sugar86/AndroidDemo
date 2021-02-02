package com.sugar.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LinearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_linear);
        //setContentView(R.layout.activity_linear_vertical);
        setContentView(R.layout.activity_linear_weight);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TextView tv = findViewById(R.id.Hiiii);
        if (tv == null)
            return;
        Log.e("ttit", "tv.with:" + tv.getWidth());
        Log.e("ttit", "tv.left:" + tv.getLeft());
        Log.e("text", "tv.height:" + tv.getHeight());
    }
}