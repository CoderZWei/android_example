package com.example.zw.photoalbum.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoader {
    private LruCache<String,Bitmap>mMemoryCache;

    public ImageLoader() {
        int maxMemory=(int)Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/8;
        this.mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public Bitmap getImgFromNet(String imageUrl) {
        Bitmap bitmap=null;
        HttpURLConnection con=null;
        try {
            URL url=new URL(imageUrl);
            con=(HttpURLConnection)url.openConnection();
            con.setConnectTimeout(5*1000);
            con.setReadTimeout(10*1000);
            bitmap=BitmapFactory.decodeStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(con!=null){
                con.disconnect();
            }
        }
        return bitmap;
    }
    public Bitmap getImgFromCache(String imageKey){
        return mMemoryCache.get(imageKey);
    }
    public void addImgToCache(String key, Bitmap bitmap) {
        if(getImgFromCache(key)==null){
            mMemoryCache.put(key,bitmap);
        }
    }}
