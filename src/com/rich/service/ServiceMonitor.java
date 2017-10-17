package com.rich.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class ServiceMonitor {
    
    private final static String TAG = "ServiceMonitor";
    
    private static ServiceMonitor instance;
    private AlarmManager mAm;
    private Intent intent;
    private PendingIntent mSender;
    private long intervar = 60000;
    
    public static ServiceMonitor getInstance(){
        if(instance == null){
            instance = new ServiceMonitor();
        }
        return instance;
    }
    
    public static class MonitorBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i(TAG, "#####Check SubLcd Service is running");
            if(!isServiceRunning(context)){
                Log.i(TAG, "#####restarting");
                Intent newIntent = new Intent(context, SubLcdService.class); 
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(newIntent);   
            }
        }
    }
    
    public void setInterval(long interval){
        this.intervar = interval;
    }
    
    public void startMonitoring(Context context) {
        Log.i(TAG, "#####startMonitoring");
        mAm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, MonitorBroadcast.class);
        mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), intervar, mSender);
    }
 
    public void stopMonitoring(Context context) {
        Log.i(TAG, "#####stopMonitoring");
        mAm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, MonitorBroadcast.class);
        mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAm.cancel(mSender);
        mAm = null;
        mSender = null;
    }
    
    
    private static boolean isServiceRunning(Context context){
        ActivityManager mAm = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        String mServiceName = SubLcdService.class.getName();
        for(RunningServiceInfo runningService : 
                mAm.getRunningServices(Integer.MAX_VALUE)){
            if(mServiceName.equals(runningService.service.getClassName())){
                Log.i(TAG, "#####Service is Running");
                return true;
            }
        }
        Log.i(TAG, "#####Service is not Running");
        return false;
    }
}
