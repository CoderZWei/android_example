package com.example.zw.widgetexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {
    public static final String WIDGET_ACTION="com.example.zw.widgetexample.widget.action";
    //widget被更新时调用
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.mTextView,"change text");
        Intent intent=new Intent();
        intent.setAction(WIDGET_ACTION);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.mBtn,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
        for (int appWidgetId:appWidgetIds){
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }
    }
    //接收widget点击时发送的广播
    @Override
    public void onReceive(Context context, Intent intent) {
        //super.onReceive(context, intent);
        String action=intent.getAction();
        //if(action.equals(WIDGET_ACTION)){
            Toast.makeText(context,"widget click",Toast.LENGTH_SHORT).show();
        //}
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
