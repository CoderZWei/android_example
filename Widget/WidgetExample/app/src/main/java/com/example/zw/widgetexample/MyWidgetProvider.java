package com.example.zw.widgetexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {
    public static final String WIDGET_ACTION="com.example.zw.widgetexample.widget.action";
    private  ComponentName mComponentName;
    private  RemoteViews mRemoteViews;

    public MyWidgetProvider() {
        super();
    }

    //widget被更新时调用
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        mRemoteViews=new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        mRemoteViews.setTextViewText(R.id.mTextView,"change text");
        Intent intent1=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent1=PendingIntent.getActivity(context,200,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.mBtn1,pendingIntent1);

        Intent intent2=new Intent().setAction(WIDGET_ACTION);
        intent2.setPackage(context.getPackageName());
        PendingIntent pendingIntent2=PendingIntent.getBroadcast(context,200,intent2,PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.mBtn2,pendingIntent2);

        mComponentName=new ComponentName(context,MyWidgetProvider.class);
        appWidgetManager.updateAppWidget(mComponentName,mRemoteViews);
        //appWidgetManager.updateAppWidget(appWidgetIds,mRemoteViews);
//        for (int appWidgetId:appWidgetIds){
//            appWidgetManager.updateAppWidget(appWidgetId,mRemoteViews);
//        }

        //可以用
//        for(int i=0;i<appWidgetIds.length;i++){
//                mRemoteViews=new RemoteViews(context.getPackageName(),R.layout.widget_layout);
//            Intent intent2=new Intent();
//            intent2.setAction(WIDGET_ACTION);
//            //targetSdk25以上对隐式广播加了限制，需要指定包名或使用动态广播，否则会报错：
//            // "BroadcastQueue: Background execution not allowed: receiving Intent"
//            intent2.setPackage(context.getPackageName());
//            PendingIntent pendingIntent2=PendingIntent.getBroadcast(context,0,intent2,0);
//            mRemoteViews.setOnClickPendingIntent(R.id.mBtn2,pendingIntent2);
//            appWidgetManager.updateAppWidget(appWidgetIds[i],mRemoteViews);
//        }


    }
    //接收widget点击时发送的广播
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action=intent.getAction();
        Log.d("zw_debug",action);
        if(action.equals(WIDGET_ACTION)){
            Toast.makeText(context,"widget click",Toast.LENGTH_SHORT).show();
        }
//        AppWidgetManager.getInstance(context).updateAppWidget(mComponentName,mRemoteViews);
    }


    //widget删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
    //当最后一个widget删除时调用
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    //当widget大小改变时调用
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
    //当widget从备份恢复时调用
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
