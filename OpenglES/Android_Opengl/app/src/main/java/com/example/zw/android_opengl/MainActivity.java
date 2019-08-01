package com.example.zw.android_opengl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLSurfaceView=(GLSurfaceView)findViewById(R.id.mSurfaceView);
        //这是OpenGL版本 必需
        mGLSurfaceView.setEGLContextClientVersion(2);
        //设置渲染器
        mGLSurfaceView.setRenderer(new MyRenderer());
        //设置渲染模式为连续模式(会以60fps的速度刷新)
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }


}
