package com.example.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private AlarmManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent();
        intent.setAction("com.example.timer.action");
        intent.setPackage(getPackageName());
        final PendingIntent sender=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        mBtn=(Button)findViewById(R.id.mBtn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zw_debug","send");
                //am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+3000,sender);
                //RTC_WAKEUP：在系统精确时间触发，会唤醒cpu
                //RTC：在系统精确时间触发，设备休眠不唤醒不触发
                //ELAPSED_REALTIME_WAKEUP：系统从启动到现在的X毫秒时间（包括深度睡眠的时间）后触发
                //ELAPSED_REALTIME：系统从启动到现在的X毫秒（包括深度睡眠的时间）后触发，系统休眠时不会唤醒cpu，也就不会触发
                am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+3000,sender);
            }
        });
    }
}
