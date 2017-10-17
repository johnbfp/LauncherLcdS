package com.rich.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.util.Log;

public class WifiUtils {
	private static final int WIFICIPHER_NOPASS = 0;
	private static final int WIFICIPHER_WEP = 1;
	private static final int WIFICIPHER_WPA = 2;

	private WifiManager mWifiManager;

	private WifiInfo mWifiInfo;

	private List<ScanResult> mWifiList; // 扫描的wifi

	private List<WifiConfiguration> mWifiConfiguration; // 配置好的WIFI

	// 构造器
	public WifiUtils(Context context) {
		// 取得WifiManager对象
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	// 打开WIFI
	public void OpenWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭WIFI
	public void CloseWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// 扫描 并且获取结果
	public List<ScanResult> StartScan() {
		mWifiManager.startScan();
		mWifiList = mWifiManager.getScanResults();
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
		Log.d("bfp", "============mWifiList====="+mWifiList.size());
		return mWifiList;
	}

	// 得到网络列表
	public List<ScanResult> GetWifiList() {
		return sortList(mWifiList);
	}

	// 添加一个网络并连接
	public boolean AddNetwork(ScanResult sr) {
		int wcgID = mWifiManager.addNetwork(createWifiConfig(sr.SSID, "",
				WIFICIPHER_NOPASS)); // 连接指定wifi
		boolean reconnect = mWifiManager.enableNetwork(wcgID, true); // 检测是否连接成功

		if (!reconnect) { // 没有连接成功 若失败，则连接之前成功过的网络
			reconnect = mWifiManager.reconnect();

		}
		return reconnect;
	}

	// 断开指定Wifi
	public void DisconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	// 过滤不需要的Wifi   此处过滤有密码的wifi
	private List<ScanResult> sortList(List<ScanResult> list) {	
		List<ScanResult> result=new ArrayList<ScanResult>();
		
		for (ScanResult scanResult : list) {
			String capabilities = scanResult.capabilities;
			if (!TextUtils.isEmpty(capabilities)) {
				if (capabilities.contains("WPA")
						|| capabilities.contains("wpa")) {
					continue;
				} else if (capabilities.contains("WEP")
						|| capabilities.contains("wep")) {
					continue;
				}
			}
			result.add(scanResult);
			Log.d("bfp", "============result="+result.size());
		}
		
		return result;
	}

	// 得到MAC地址
	public String GetMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// 得到接入点的BSSID
	public String GetBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// 得到IP地址
	public int GetIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 得到连接的ID
	public int GetNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// 得到WifiInfo的所有信息包
	public String GetWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	private WifiConfiguration createWifiConfig(String ssid, String password,
			int type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + ssid + "\"";

		WifiConfiguration tempConfig = isExist(ssid);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (type == WIFICIPHER_NOPASS) {
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		} else if (type == WIFICIPHER_WEP) {
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (type == WIFICIPHER_WPA) {
			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}

		return config;
	}

	private WifiConfiguration isExist(String ssid) {
		List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();

		for (WifiConfiguration config : configs) {
			if (config.SSID.equals("\"" + ssid + "\"")) {
				return config;
			}
		}
		return null;
	}

}
