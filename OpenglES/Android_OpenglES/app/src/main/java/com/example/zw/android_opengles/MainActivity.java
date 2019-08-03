package com.example.zw.android_opengles;

import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    String TAG="here";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
        final MyGender mGender = new MyGender("mGender");

        Log.e(TAG,"hah");
        mGender.start();
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //计算宽高比
                float ratio=(float)width/height;
                //设置透视投影
                Matrix.frustumM(mGender.mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
                //设置相机位置
                Matrix.setLookAtM(mGender.mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
                //计算变换矩阵 将mProjectionMatrix和mViewMatrix矩阵相乘并赋给mMVPMatrix mMVPMatrix就是变换矩阵
                Matrix.multiplyMM(mGender.mMVPMatrix,0,mGender.mProjectMatrix,0,mGender.mViewMatrix,0);

                //mGender.drawFrame();
                mGender.render(holder.getSurface(),width,height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mGender.release();
            }
        });

    }
}
