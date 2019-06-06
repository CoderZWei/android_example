package com.example.zw.okhttptest.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtils {
    private OkHttpClient client;
    private OnDownloadListener downloadListener;
    public DownloadUtils(){
        client=new OkHttpClient();
    }

    public void download(String url, final String savePath){
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadListener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream=null;
                byte[]buf=new byte[1024];
                int len=0;
                FileOutputStream fos=null;
                try {
                    inputStream=response.body().byteStream();
                    long totalLength=response.body().contentLength();
                    Log.d("zw:totalLength",String.valueOf(totalLength));
                    File file=new File(savePath);
                    fos=new FileOutputStream(file);
                    long sum=0;
                    while ((len=inputStream.read(buf))!=-1){
                        fos.write(buf,0,len);
                        Log.d("zw:len",String.valueOf(len));
                        sum+=len;
                        int progress=(int)(sum*1.0f/totalLength *100 );
                        downloadListener.onDownloading(progress);
                    }
                    fos.flush();
                    downloadListener.onDownloadSuccess();

                }catch (IOException e){
                    Log.d("zw:download","error");
                }finally {
                    if(inputStream!=null){
                        inputStream.close();
                    }
                    if(fos!=null){
                        fos.close();
                    }
                }

            }
        });

    }

    public void setDownloadListener(OnDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public interface OnDownloadListener{
        void onDownloadSuccess();
        void onDownloadFailed();
        void onDownloading(int progress);
    }
}
