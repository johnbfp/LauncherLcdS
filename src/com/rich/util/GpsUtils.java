package com.rich.util;

import android.content.Context;
import android.location.LocationManager;

public class GpsUtils {
	private Context mContext;

	private GpsUtils(Context context) {
		this.mContext = context;
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
	}

	// ����Ƿ��gps
	public boolean isOPen() {
		/*
		 * 
		 * 
		 * boolean gps = locationManager
		 * .isProviderEnabled(LocationManager.GPS_PROVIDER);
		 * 
		 * boolean network = locationManager
		 * .isProviderEnabled(LocationManager.NETWORK_PROVIDER); if (gps ||
		 * network) { return true; } return false;
		 */

		// boolean gpsEnabled =
		// Settings.Secure.isLocationProviderEnabled(context.getContentResolver(),
		// LocationManager.GPS_PROVIDER );
		// return gpsEnabled;
		return false;
	}

	// ����gps����

	public void setGpsEnable() {
		// Settings.Secure.setLocationProviderEnabled(
		// mContext.getContentResolver(), LocationManager.GPS_PROVIDER, false );
	}
}
