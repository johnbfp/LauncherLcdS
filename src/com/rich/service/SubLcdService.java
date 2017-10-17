package com.rich.service;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SublcdManager;

import com.rich.controller.SublcdStateMachine;
import com.rich.lisener.OnSublcdKeyLisener;
import com.rich.lisener.UpdateUIListener;

public class SubLcdService extends Service implements UpdateUIListener {
	private OnSublcdKeyLisener onSublcdKeyLisener;
	private SublcdStateMachine sublcdmachine;
	private SublcdManager mSublcdManager;
	private Handler mHandler = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mHandler = new MyHandler(this);
		sublcdmachine = new SublcdStateMachine(this);
		sublcdmachine.registerListener(this);
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction("com.android.action.KEYCODE_DPAD_UP");
		mFilter.addAction("com.android.action.KEYCODE_DPAD_DOWN");
		mFilter.addAction("com.android.action.KEYCODE_BACK");
		mFilter.addAction("com.android.action.KEYCODE_ENTER");
		registerReceiver(mLcdReceiver, mFilter);
		mSublcdManager = (SublcdManager) getSystemService("sublcd");

		ServiceMonitor.getInstance().startMonitoring(getApplicationContext());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void update(String tip) {
		mHandler.obtainMessage(0, tip).sendToTarget();
	}

	public void registerListener(OnSublcdKeyLisener l) {
		this.onSublcdKeyLisener = l;
	}

	public void unregisterListener() {
		this.onSublcdKeyLisener = null;
	}

	private void updateText(String text) {
		mSublcdManager.Sub_ClearContentArea(true);
		mSublcdManager.Sub_DrawText(20, 20, 100, 20, 16, text, 0xFFFFFF, 1, 0);
	}

	static class MyHandler extends Handler {
		WeakReference<SubLcdService> mActivity = null;

		public MyHandler(SubLcdService service) {
			mActivity = new WeakReference<SubLcdService>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			SubLcdService service = mActivity.get();
			service.updateText((String) msg.obj);
		}

	};

	private SubLcdReceiver mLcdReceiver = new SubLcdReceiver();

	class SubLcdReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if ("com.android.action.KEYCODE_DPAD_UP".equals(arg1.getAction())) {
				onSublcdKeyLisener.up();
			} else if ("com.android.action.KEYCODE_DPAD_DOWN".equals(arg1
					.getAction())) {
				onSublcdKeyLisener.down();
			} else if ("com.android.action.KEYCODE_BACK".equals(arg1
					.getAction())) {
				onSublcdKeyLisener.back();
			} else if ("com.android.action.KEYCODE_ENTER".equals(arg1
					.getAction())) {
				onSublcdKeyLisener.kenter();
			}
		}

	}
}
