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

	private List<ScanResult> mWifiList; // ɨ���wifi

	private List<WifiConfiguration> mWifiConfiguration; // ���úõ�WIFI

	// ������
	public WifiUtils(Context context) {
		// ȡ��WifiManager����
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// ȡ��WifiInfo����
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	// ��WIFI
	public void OpenWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// �ر�WIFI
	public void CloseWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// ɨ�� ���һ�ȡ���
	public List<ScanResult> StartScan() {
		mWifiManager.startScan();
		mWifiList = mWifiManager.getScanResults();
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
		Log.d("bfp", "============mWifiList====="+mWifiList.size());
		return mWifiList;
	}

	// �õ������б�
	public List<ScanResult> GetWifiList() {
		return sortList(mWifiList);
	}

	// ���һ�����粢����
	public boolean AddNetwork(ScanResult sr) {
		int wcgID = mWifiManager.addNetwork(createWifiConfig(sr.SSID, "",
				WIFICIPHER_NOPASS)); // ����ָ��wifi
		boolean reconnect = mWifiManager.enableNetwork(wcgID, true); // ����Ƿ����ӳɹ�

		if (!reconnect) { // û�����ӳɹ� ��ʧ�ܣ�������֮ǰ�ɹ���������
			reconnect = mWifiManager.reconnect();

		}
		return reconnect;
	}

	// �Ͽ�ָ��Wifi
	public void DisconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	// ���˲���Ҫ��Wifi   �˴������������wifi
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

	// �õ�MAC��ַ
	public String GetMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// �õ�������BSSID
	public String GetBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// �õ�IP��ַ
	public int GetIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// �õ����ӵ�ID
	public int GetNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// �õ�WifiInfo��������Ϣ��
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
