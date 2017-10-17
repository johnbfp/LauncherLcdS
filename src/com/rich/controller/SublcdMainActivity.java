package com.rich.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SublcdManager;
import android.view.KeyEvent;
import android.widget.TextView;

import com.rich.design.R;
import com.rich.lisener.OnSublcdKeyLisener;
import com.rich.lisener.UpdateUIListener;

public class SublcdMainActivity extends Activity implements UpdateUIListener{
	 private OnSublcdKeyLisener onSublcdKeyLisener;
	 private SublcdStateMachine machine = null;
	 private Handler mHandler = null; 
	 private TextView mTextView;
	 private SublcdManager mSublcdManager;
	 
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		 setContentView(R.layout.activity_main);
		 mHandler = new MyHandler(this);
		 machine = new SublcdStateMachine(this);
		 machine.registerListener(this);
		 mTextView = (TextView) findViewById(R.id.textView1);
		 IntentFilter mFilter =new IntentFilter();
		 mFilter.addAction("com.android.action.KEYCODE_DPAD_UP");
		 mFilter.addAction("com.android.action.KEYCODE_DPAD_DOWN");
		 mFilter.addAction("com.android.action.KEYCODE_BACK");
		 mFilter.addAction("com.android.action.KEYCODE_ENTER");
		 registerReceiver(mLcdReceiver, mFilter);
		 mSublcdManager = (SublcdManager) getSystemService("sublcd");
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
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		if (KeyEvent.KEYCODE_DPAD_UP == arg0) {
			if(onSublcdKeyLisener !=null){
				onSublcdKeyLisener.up();
			}
		}else if(KeyEvent.KEYCODE_DPAD_DOWN == arg0){
			if(onSublcdKeyLisener !=null){
				onSublcdKeyLisener.down();
			}
		}else if(KeyEvent.KEYCODE_DPAD_CENTER == arg0){
			if(onSublcdKeyLisener !=null){
				onSublcdKeyLisener.kenter();
			}
		}else if(KeyEvent.KEYCODE_BACK == arg0){
			if(onSublcdKeyLisener !=null){
				onSublcdKeyLisener.back();
			}
		}else if(KeyEvent.KEYCODE_ENTER == arg0){
			if(onSublcdKeyLisener !=null){
				onSublcdKeyLisener.kenter();
			}
		}
		return true;
	}
	
	private void updateText(String text) {
		mTextView.setText(text);
		mSublcdManager.Sub_ClearContentArea(true);
		mSublcdManager.Sub_DrawText(20, 20, 100, 20, 16, text,  0xFFFFFF, 1, 0);
	}
	
	static class MyHandler extends Handler {
		WeakReference<SublcdMainActivity> mActivity = null;

		public MyHandler(SublcdMainActivity act) {
			mActivity = new WeakReference<SublcdMainActivity>(act);
		}

		@Override
		public void handleMessage(Message msg) {
			SublcdMainActivity activity = mActivity.get();
			activity.updateText((String) msg.obj);
		}

	};
	 private SubLcdReceiver mLcdReceiver =new SubLcdReceiver();
	class SubLcdReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if("com.android.action.KEYCODE_DPAD_UP".equals(arg1.getAction())){
				onSublcdKeyLisener.up();
			}else if("com.android.action.KEYCODE_DPAD_DOWN".equals(arg1.getAction())){
				onSublcdKeyLisener.down();
			}else if("com.android.action.KEYCODE_BACK".equals(arg1.getAction())){
				onSublcdKeyLisener.back();
			}else if("com.android.action.KEYCODE_ENTER".equals(arg1.getAction())){
				onSublcdKeyLisener.kenter();
			}
		}
		
	}
}
