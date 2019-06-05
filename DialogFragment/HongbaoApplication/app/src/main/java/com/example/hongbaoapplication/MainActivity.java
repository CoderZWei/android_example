package com.example.hongbaoapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    MyDialog myDialog;
    private float mScale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn=(Button)findViewById(R.id.mBtn);
        //mScale=this.getResources().getDisplayMetrics().density;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;         // 屏幕宽度（像素）
        final int height = dm.heightPixels;
        mScale=dm.density;
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog=new MyDialog(MainActivity.this);
                myDialog.setScale(mScale);
                myDialog.setDialogTheme("1");
                if(myDialog.getDialog()!=null){
                    myDialog.getDialog().setCanceledOnTouchOutside(true);
                }

                myDialog.show(getSupportFragmentManager(),"ad");
            }
        });

    }


}
