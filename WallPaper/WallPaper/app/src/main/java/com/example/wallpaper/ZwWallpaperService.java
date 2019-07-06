package com.example.wallpaper;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.icu.util.LocaleData;
import android.os.IBinder;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public class ZwWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new ZwWallpaperEngine();
    }
    private class ZwWallpaperEngine extends Engine  {
        private Camera mCamera;
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
           // initCamera();//不能在这里执行，此时无法取得surfaceHolder
        }

        private void initCamera() {
            mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            try {
                mCamera.setPreviewDisplay(getSurfaceHolder());
                mCamera.setDisplayOrientation(90);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initCamera();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(visible){
                mCamera.startPreview();
            }else {
                mCamera.stopPreview();
            }

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }

    }
}
