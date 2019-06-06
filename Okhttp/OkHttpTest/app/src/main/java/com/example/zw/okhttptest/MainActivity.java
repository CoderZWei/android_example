package com.example.zw.okhttptest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zw.okhttptest.util.DownloadUtils;
import com.example.zw.okhttptest.util.PermissionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    Button mBtnGet,mBtnPost;
    OkHttpClient client=new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermissionsIfNeed(this);
        mBtnGet=(Button)findViewById(R.id.Btn_get);
        mBtnGet.setOnClickListener(this);
        mBtnPost=(Button)findViewById(R.id.Btn_post);
        mBtnPost.setOnClickListener(this);
        DownloadUtils downloadUtils=new DownloadUtils();
        downloadUtils.setDownloadListener(new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                Log.d("zw:download","success");
            }

            @Override
            public void onDownloadFailed() {
                Log.d("zw:download","failed");
            }

            @Override
            public void onDownloading(int progress) {
                Log.d("zw:progress",String.valueOf(progress));
            }
        });
        downloadUtils.download("https://d.qiezzi.com/qiezi-clinic.apk",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"demo.apk");
    }


    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Btn_get:
                getRequest();
                break;
            case R.id.Btn_post:
                postRequest();
                break;
        }
    }
    private void getRequest() {
        final Request request=new Request.Builder()
                .get()
                .tag(this)
                .url("http://www.baidu.com")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response=null;
                try{
                    response=client.newCall(request).execute();
                    if(response.isSuccessful()){
                        Log.d("zw:",response.body().string());
                    }
                }catch (IOException e){
                    Log.d("zw:","error");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void postRequest() {
        final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json="{'winCondition':'HIGH_SCORE'}";
                RequestBody body=RequestBody.create(JSON,json);
                Request request=new Request.Builder()
                        .url("http://www.baidu.com")
                        .post(body)
                        .build();
                try {
                    Response response=client.newCall(request).execute();
                    if(response.isSuccessful()){
                        Log.d("zw:",response.body().string());
                    }
                }catch (IOException e){
                    Log.d("zw:","error");
                    e.printStackTrace();
                }
            }
        }).start();


       /* RequestBody formBody = new FormEncodingBuilder()
                .add("","")
                .build();
                */
    }


}
