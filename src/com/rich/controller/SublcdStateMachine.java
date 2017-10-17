package com.rich.controller;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Message;
import android.util.Log;

import com.rich.controller.state.State;
import com.rich.controller.state.StateMachine;
import com.rich.data.StackLinkedList;
import com.rich.design.R;
import com.rich.lisener.OnSublcdKeyLisener;
import com.rich.lisener.UpdateUIListener;
import com.rich.util.WifiUtils;

public class SublcdStateMachine extends StateMachine {

	private final static String TAG = SublcdStateMachine.class.getSimpleName();

	private final static int MSG_LUANCHER = 1;
	private final static int MSG_SETTINGS = 2;
	private final static int MSG_MMI = 3;
	private final static int MSG_PTT = 4;
	private final static int MSG_DEFAULT = 0;

	private UpdateUIListener listener = null;
	private Context mContext;

	public SublcdStateMachine(Context context) {
		super(TAG);
		mContext = context;
		mWifiUtils = new WifiUtils(mContext);
		addState(mDefaulteState);
		addState(mLauncherState, mDefaulteState);
		addState(mSettingsState, mDefaulteState);
		   addState(mWifiState, mSettingsState);
		      addState(mWifiSwitchState, mWifiState);
		      addState(mWifiListState, mWifiState);
		addState(mGPRSState, mSettingsState);
		addState(mDateState, mSettingsState);
		addState(mBlueToothState, mSettingsState);
		addState(mFactoryModeState, mDefaulteState);
		   addState(mLCDState, mFactoryModeState);
		      addState(mIsSuccessState, mLCDState);
		      addState(mLCDDetailState, mLCDState);
		addState(mPTTState, mDefaulteState);
		setInitialState(mLauncherState);
		start();
	}

	public void registerListener(UpdateUIListener l) {
		this.listener = l;
	}

	private void notifyUI(String text) {
		if (listener != null) {
			listener.update(text);
		}
	}

	private State mDefaulteState = new DefaultState();

	class DefaultState extends State implements OnSublcdKeyLisener {

		@Override
		public boolean processMessage(Message msg) {
			transitionTo(mPTTState);
			return true;
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI("");
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public void up() {
			// TODO Auto-generated method stub

		}

		@Override
		public void down() {
			// TODO Auto-generated method stub

		}

		@Override
		public void kenter() {
			// TODO Auto-generated method stub

		}

		@Override
		public void back() {
			sendMessage(MSG_PTT);
		}
	}

	private State mLauncherState = new Launcher();

	class Launcher extends State implements OnSublcdKeyLisener {

		public Launcher() {

		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mLauncherState===" + msg.what);
			switch (msg.what) {
			case MSG_PTT:
				transitionTo(mPTTState);
				break;
			case MSG_SETTINGS:
				transitionTo(mSettingsState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			Log.d("bfp", "===up");
			sendMessage(MSG_PTT);
		}

		@Override
		public void down() {
			Log.d("bfp", "===down");
			sendMessage(MSG_SETTINGS);
		}

		@Override
		public void kenter() {
			Log.d("bfp", "===kenter");
			// sendMessage(MSG_LUANCHER);
		}

		@Override
		public void back() {
			Log.d("bfp", "===back");
		}

	}

	public State mSettingsState = new Settings();

	class Settings extends State implements OnSublcdKeyLisener {

		public Settings() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mSettingsState===" + msg.what);
			switch (msg.what) {
			case MSG_LUANCHER:
				transitionTo(mLauncherState);
				break;
			case MSG_MMI:
				transitionTo(mFactoryModeState);
				break;
			case MSG_WIFI:
				transitionTo(mWifiState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_LUANCHER);
		}

		@Override
		public void down() {
			sendMessage(MSG_MMI);
		}

		@Override
		public void kenter() {
			sendMessage(MSG_WIFI);
		}

		@Override
		public void back() {

		}

	}

	// /////===================================================================================///////////
	private final static int MSG_MMI_LCD = 30;
	private final static int MSG_MMI_LCD_DETAIL = 301;
	private final static int MSG_MMI_LCD_ISSUCCESS = 301;
	private final static int MSG_MMI_KEY = 31;
	private final static int MSG_MMI_WIFI = 32;
	private final static int MSG_MMI_BT = 33;
	private State mFactoryModeState = new FactoryMode();

	class FactoryMode extends State implements OnSublcdKeyLisener {
		
		public FactoryMode() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mFactoryModeState===" + msg.what);
			switch (msg.what) {
			case MSG_SETTINGS:
				transitionTo(mSettingsState);
				break;
			case MSG_PTT:
				transitionTo(mPTTState);
				break;
			case MSG_MMI_LCD:
				transitionTo(mLCDState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_SETTINGS);
		}

		@Override
		public void down() {
			sendMessage(MSG_PTT);
		}

		@Override
		public void kenter() {
			sendMessage(MSG_MMI_LCD);
		}

		@Override
		public void back() {
			// TODO Auto-generated method stub

		}

	}
    //1 lcd test
	
	private State mLCDState = new LCD();
	class LCD extends State implements OnSublcdKeyLisener {

		public LCD() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mLCDState===" + msg.what);
			switch (msg.what) {
			case MSG_MMI:
				transitionTo(mFactoryModeState);
				break;
			case MSG_MMI_LCD_DETAIL:
				transitionTo(mLCDDetailState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			
		}

		@Override
		public void down() {
			
		}
		@Override
		public void kenter() {
			sendMessage(MSG_MMI_LCD_DETAIL);
		}

		@Override
		public void back() {
            sendMessage(MSG_MMI);
		}

	}
	//1.1 LCD success or fail
	private State mIsSuccessState = new IsSuccess();
	class IsSuccess extends State implements OnSublcdKeyLisener {
        private  boolean isSuccess = true;
        private  boolean isSwitch = false;
		public IsSuccess() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mIsSuccessState===" + msg.what);
			switch (msg.what) {
			case MSG_MMI_LCD:
				transitionTo(mLCDState);
				break;
			case MSG_MMI_LCD_DETAIL:
				transitionTo(mIsSuccessState);
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			if(isSuccess){
				return mContext.getResources().getString(R.string.success);
			}else{
				return mContext.getResources().getString(R.string.fail);
			}
			
		}

		@Override
		public void up() {
			Log.d("bfp","up==isSuccess"+isSuccess);
			isSuccess = false;
			if(!isSuccess){
				notifyUI(getName());
				isSwitch =false;
			}
		}

		@Override
		public void down() {
			if(isSwitch){
				sendMessage(MSG_MMI_LCD_DETAIL);
				isSwitch = false;
			}else{
				isSuccess = true;
				if(isSuccess){
					notifyUI(getName());
					isSwitch =false;
				}
			}
			
		}

		@Override
		public void kenter() {
			if(isSuccess){
				//TODO ´æ´¢²âÊÔ×´Ì¬
			}
			isSwitch =true;
		}

		@Override
		public void back() {
            sendMessage(MSG_MMI_LCD);
		}

	}
	private State mLCDDetailState = new LCDDetail();
	class LCDDetail extends State implements OnSublcdKeyLisener {
		private  boolean isBlack = true;
		private  boolean isSwitch = false;
		public LCDDetail() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mLCDDetailState===" + msg.what);
			switch (msg.what) {
			case MSG_MMI_LCD:
				transitionTo(mLCDState);
				break;
			case MSG_MMI_LCD_ISSUCCESS:
				transitionTo(mIsSuccessState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			if(isBlack){
				return mContext.getResources().getString(R.string.full_black);
			}else{
				return mContext.getResources().getString(R.string.full_white);
			}
		}

		@Override
		public void up() {
			if(isSwitch){
				sendMessage(MSG_MMI_LCD_ISSUCCESS);
				isSwitch = false;
			}else{
				isBlack = false;
				if(!isBlack){
					notifyUI(getName());
					isSwitch = false;
				}
			}
			
			
		}

		@Override
		public void down() {
			isBlack = true;
			if(isBlack){
				notifyUI(getName());
				isSwitch = false;
			}
		}

		@Override
		public void kenter() {
			if(isBlack){
				//TODO
			}
			isSwitch =true;
		}

		@Override
		public void back() {
            sendMessage(MSG_MMI_LCD);
		}

	}
	//===================================================================
	
	private State mPTTState = new PTT();
	class PTT extends State implements OnSublcdKeyLisener {

		public PTT() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mPTTState===" + msg.what);
			switch (msg.what) {
			case MSG_MMI:
				transitionTo(mFactoryModeState);
				break;
			case MSG_LUANCHER:
				transitionTo(mLauncherState);
				break;
			case MSG_DEFAULT:
				transitionTo(mDefaulteState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_MMI);
		}

		@Override
		public void down() {
			sendMessage(MSG_LUANCHER);
		}

		@Override
		public void kenter() {
			sendMessage(MSG_DEFAULT);
		}

		@Override
		public void back() {

		}

	}

	// ============================factoryMode end ========================
	private final static int MSG_WIFI = 5;
	private final static int MSG_GPS = 6;
	private final static int MSG_DATE = 7;
	private final static int MSG_BLUETOOTH = 8;
	private final static int MSG_WIFILIST = 9;
	private final static int MSG_WIFISWITCH = 10;

	private State mWifiState = new Wifi();

	class Wifi extends State implements OnSublcdKeyLisener {

		public Wifi() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mWifiState===" + msg.what);
			switch (msg.what) {
			case MSG_BLUETOOTH:
				transitionTo(mBlueToothState);
				break;
			case MSG_GPS:
				transitionTo(mGPRSState);
				break;
			case MSG_SETTINGS:
				transitionTo(mSettingsState);
				break;
			case MSG_WIFISWITCH:
				transitionTo(mWifiSwitchState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_BLUETOOTH);
		}

		@Override
		public void down() {
			sendMessage(MSG_GPS);
		}

		@Override
		public void kenter() {
			sendMessage(MSG_WIFISWITCH);
		}

		@Override
		public void back() {
			sendMessage(MSG_SETTINGS);
		}

	}

	private StackLinkedList<ScanResult> mLinkedList;
	private StackLinkedList<ScanResult> mtempLinkedList;
	private List<ScanResult> mWifiList;
	private State mWifiSwitchState = new WifiSwitch();
	private WifiUtils mWifiUtils;

	class WifiSwitch extends State implements OnSublcdKeyLisener {
		private final String isDisable = "OFF===>ON";
		private final String isEnable = "ON===>OFF";
		private boolean isOpen = false;

		public WifiSwitch() {

		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mWifiSwitchState===" + msg.what);
			switch (msg.what) {
			case MSG_WIFI:
				transitionTo(mWifiState);
				break;
			case MSG_WIFILIST:
				transitionTo(mWifiListState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			if (!isOpen) {
				return isDisable;
			}
			return isEnable;
		}

		@Override
		public void up() {

		}

		@Override
		public void down() {

			if ((mLinkedList != null && mLinkedList.size() > 0)) {
				sendMessage(MSG_WIFILIST);
			}
		}

		@Override
		public void kenter() {

			if (!isOpen) {
				mWifiUtils.OpenWifi();
				mLinkedList = new StackLinkedList<ScanResult>();
				mtempLinkedList = new StackLinkedList<ScanResult>();
				new Thread() {
					public void run() {

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mWifiList = mWifiUtils.StartScan();
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// mWifiList = mWifiUtils.GetWifiList();
						for (int i = 0; i < mWifiList.size(); i++) {
							mLinkedList.push(mWifiList.get(i));

						}
						Log.d("bfp", "======get" + mLinkedList.size());
					};
				}.start();
				isOpen = true;
			} else {
				mWifiUtils.CloseWifi();
				isOpen = false;
				mLinkedList = null;
				mtempLinkedList = null;
			}
			notifyUI(getName());
		}

		@Override
		public void back() {
			sendMessage(MSG_WIFI);
		}

	}

	private State mWifiListState = new WifiList();

	class WifiList extends State implements OnSublcdKeyLisener {
		private ScanResult mScanResult;
		private String mSelectWifi;
		private boolean isConnect = false;
		private boolean isUp = false;
		private boolean isDown = false;
		private String isConnectName;

		public WifiList() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			showListDown();
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mWifiListState===" + msg.what);
			switch (msg.what) {
			case MSG_WIFI:
				transitionTo(mWifiState);
				break;
			case MSG_WIFISWITCH:
				transitionTo(mWifiSwitchState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {

			isDown = true;
			if (mtempLinkedList != null) {

				if (isUp) {
					isUp = false;
					Log.d("bfp", "====== isDown" + isDown);
					ScanResult mResult = mtempLinkedList.pop();
					mLinkedList.push(mResult);
				}

				mScanResult = mtempLinkedList.pop();
				if (mScanResult != null) {
					mSelectWifi = mScanResult.SSID;
					if (mSelectWifi != null) {
						Log.d("bfp", "mSelectWifi =" + mSelectWifi);
						if (mSelectWifi.equalsIgnoreCase(isConnectName)) {
							notifyUI("*" + mSelectWifi);
						} else {
							notifyUI(mSelectWifi);
						}
						mLinkedList.push(mScanResult);
					} else {
						sendMessage(MSG_WIFISWITCH);
					}
				} else {
					sendMessage(MSG_WIFISWITCH);
				}

			}

		}

		@Override
		public void down() {
			showListDown();
		}

		public void showListDown() {

			if (mLinkedList != null) {

				if (isDown) {
					isDown = false;
					ScanResult mResult = mLinkedList.pop();
					mtempLinkedList.push(mResult);
					Log.d("bfp", "======isUp " + isUp);
				}
				isUp = true;
				mScanResult = mLinkedList.pop();

				if (mScanResult != null) {
					mSelectWifi = mScanResult.SSID;
					if (mSelectWifi != null) {
						Log.d("bfp", "mSelectWifi =" + mSelectWifi);
						if (mSelectWifi.equalsIgnoreCase(isConnectName)) {
							notifyUI("*" + mSelectWifi);
						} else {
							notifyUI(mSelectWifi);
						}

						mtempLinkedList.push(mScanResult);
					}
				}
			}
		}

		@Override
		public void kenter() {
			if (!isConnect) {
				if (mScanResult != null && !mScanResult.SSID.equals("")) {
					mWifiUtils.AddNetwork(mScanResult);
					isConnectName = mScanResult.SSID;
				}

				isConnect = true;
			} else {

				// mWifiUtils.DisconnectWifi(mScanResult.);
				isConnect = false;
			}

		}

		@Override
		public void back() {
			sendMessage(MSG_WIFI);
		}

	}

	// /==============
	private State mGPRSState = new GPRS();

	class GPRS extends State implements OnSublcdKeyLisener {

		public GPRS() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mGPRSState===" + msg.what);
			switch (msg.what) {
			case MSG_WIFI:
				transitionTo(mWifiState);
				break;
			case MSG_DATE:
				transitionTo(mDateState);
				break;
			case MSG_SETTINGS:
				transitionTo(mSettingsState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_WIFI);
		}

		@Override
		public void down() {
			sendMessage(MSG_DATE);
		}

		@Override
		public void kenter() {

		}

		@Override
		public void back() {
			sendMessage(MSG_SETTINGS);
		}

	}

	// ============================
	private State mDateState = new Date();

	class Date extends State implements OnSublcdKeyLisener {

		public Date() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mDateState===" + msg.what);
			switch (msg.what) {
			case MSG_GPS:
				transitionTo(mGPRSState);
				break;
			case MSG_BLUETOOTH:
				transitionTo(mBlueToothState);
				break;
			case MSG_SETTINGS:
				transitionTo(mSettingsState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_GPS);
		}

		@Override
		public void down() {
			sendMessage(MSG_BLUETOOTH);
		}

		@Override
		public void kenter() {

		}

		@Override
		public void back() {
			sendMessage(MSG_SETTINGS);
		}

	}

	// ===================================
	private State mBlueToothState = new BlueTooth();

	class BlueTooth extends State implements OnSublcdKeyLisener {

		public BlueTooth() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void enter() {
			((SublcdMainActivity) mContext).registerListener(this);
			notifyUI(getName());
			super.enter();
		}

		@Override
		public void exit() {
			((SublcdMainActivity) mContext).unregisterListener();
			super.exit();
		}

		@Override
		public boolean processMessage(Message msg) {
			Log.d("bfp", "mBlueToothState===" + msg.what);
			switch (msg.what) {
			case MSG_DATE:
				transitionTo(mDateState);
				break;
			case MSG_WIFI:
				transitionTo(mWifiState);
				break;
			case MSG_SETTINGS:
				transitionTo(mSettingsState);
				break;
			default:
				return false;
			}
			return true;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return super.getName();
		}

		@Override
		public void up() {
			sendMessage(MSG_DATE);
		}

		@Override
		public void down() {
			sendMessage(MSG_WIFI);
		}

		@Override
		public void kenter() {

		}

		@Override
		public void back() {
			sendMessage(MSG_SETTINGS);
		}

	}

}
