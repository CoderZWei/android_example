package com.example.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.timer.action")){
            Log.d("zw_debug","receive");
            Toast.makeText(context,"receive msg",Toast.LENGTH_SHORT).show();
        }

    }

}
