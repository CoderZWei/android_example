package com.example.mybubbleview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CircleProgress mCircleProgress;
    private BubbleView mBubbleView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBubbleView=(BubbleView)findViewById(R.id.bubbleView);
        ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(mBubbleView,"alpha",0,1);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mBubbleView,"translationY", 0,150);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mBubbleView,"scaleX", 0.9f,1f);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mBubbleView,"scaleY", 0.9f,1f);
        AnimatorSet set=new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(objectAnimator1,objectAnimator2,objectAnimator3,objectAnimator4);
        set.setStartDelay(500);
        set.start();
        mBubbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
