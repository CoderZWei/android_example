package com.example.zw.photoalbum.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

//参数：Params，Progress，Result
public class ImageAsyncTask extends AsyncTask<String,Void,Bitmap> {
    private String mImageUrl;
    private ImageView mImageView;
    private ImageLoader mImageLoader;
    public void setImageLoader(ImageLoader imageLoader){
        this.mImageLoader=imageLoader;
    }
    public void setImageView(ImageView mImageView){
        this.mImageView=mImageView;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        mImageUrl=params[0];
        Bitmap mBimtmap=mImageLoader.getImgFromNet(mImageUrl);
        if(mBimtmap!=null){
           mImageLoader.addImgToCache(params[0],mBimtmap);
        }
        return mBimtmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(mImageView!=null && bitmap!=null){
            mImageView.setImageBitmap(bitmap);
        }
    }


}
