package com.example.zw.myview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CircleProgress mCircleProgress;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionsIfNeed(this);
        mCircleProgress=(CircleProgress)findViewById(R.id.mCircleProgress);
        mCircleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleProgress.startCircleProgressAnim();
                mCircleProgress.changemforceColor();
            }
        });
    }
}
