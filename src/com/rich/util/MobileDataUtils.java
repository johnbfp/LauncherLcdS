package com.rich.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.telephony.TelephonyManager;

public class MobileDataUtils {
	private Context mContext;
	private TelephonyManager mTelMgr;

	public MobileDataUtils(Context context) {
		this.mContext = context;
		mTelMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public boolean GetSimStatus() {
		int simState = mTelMgr.getSimState();
		if (simState == TelephonyManager.SIM_STATE_ABSENT
				|| simState == TelephonyManager.SIM_STATE_UNKNOWN) {
			return true;

		} else {
			return false;
		}
	}

	public int setMobileDataState(boolean enabled) {
		if (GetSimStatus()) {
			return -1;  //no sim 
		}

		try {
			Method setDataEnabled = mTelMgr.getClass().getDeclaredMethod(
					"setDataEnabled", boolean.class);
			if (null != setDataEnabled) {
				setDataEnabled.invoke(mTelMgr, enabled);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	public boolean getMobileDataState() {
		try {
			Method getDataEnabled = mTelMgr.getClass().getDeclaredMethod(
					"getDataEnabled");
			if (null != getDataEnabled) {
				return (Boolean) getDataEnabled.invoke(mTelMgr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
