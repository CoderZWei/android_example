package com.example.zw.android_opengl_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zw.android_opengl_image.util.PermissionUtils;

import java.io.FileInputStream;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private Bitmap mBitmap;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionsIfNeed(this);
        /*
        try {
            FileInputStream fis=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/output.mp4");
           // mBitmap=BitmapFactory.decodeStream(fis);
        }catch (Exception e){
            Log.e("FILE ERROR",e.getMessage());
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }*/
        final MyGender mGender=new MyGender("/pic.jpg");
        mGender.start();
        mSurfaceView=(SurfaceView)findViewById(R.id.mSurfaceView);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
               mGender.adjustView(width,height);
                mGender.render(holder.getSurface(),width,height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }


}
