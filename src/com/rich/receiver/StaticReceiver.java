package com.rich.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rich.service.SubLcdService;

public class StaticReceiver extends BroadcastReceiver {
	public static String LOG_TAG = "SUBLCD_staticReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(LOG_TAG, "StaticReceiver onReceive");
		String action = intent.getAction();
		Log.i(LOG_TAG, "action="+action);
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) 
	    { 
	      Log.i(LOG_TAG, "BootCompletedReceiver");
	      Intent newIntent = new Intent(context, SubLcdService.class); 
	      newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	      context.startService(newIntent);       
	    } 
	}

}
