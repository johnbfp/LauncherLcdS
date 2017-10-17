package com.rich.util;

import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BuleToothUtils {
	private Context mContext;
	private BluetoothAdapter mBluetoothAdapter; // 蓝牙适配器
	private Set<BluetoothDevice> bondedDevices; // 获取已经配对的蓝牙
	private BluetoothManager bluetoothManager;
	private Boolean scaleIng; // 是否正在扫描
	private static final int REQUEST_ENABLE_BLE = 1; // 蓝牙请求
	private static final long SCALE_PERIOD = 10 * 1000; // 扫描时长 10秒
	private static List<BuleToothInfo> resultList;

	public BuleToothUtils(Context context) {
		this.mContext = context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		bondedDevices = mBluetoothAdapter.getBondedDevices();

		// 设置广播信息过滤
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);// 每搜索到一个设备就会发送一个该广播
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);// 当全部搜索完后发送该广播
		filter.setPriority(Integer.MAX_VALUE);// 设置优先级
		// 注册蓝牙搜索广播接收者，接收并处理搜索结果
		//context.registerReceiver(receiver, filter);

	}

	// 设置蓝牙打开关闭
	public void setBuleToothEnable(boolean enable) {
		if (mBluetoothAdapter != null) {
			if (enable) {
				if (!mBluetoothAdapter.isEnabled()) {
					mBluetoothAdapter.enable();
				}
			} else {
				mBluetoothAdapter.cancelDiscovery();
			}
		}

	}

	public void startDiscovery() {
		if (mBluetoothAdapter.isDiscovering()) { // 如果已经搜索中，就先取消搜索
			mBluetoothAdapter.cancelDiscovery();
		}
		mBluetoothAdapter.startDiscovery();
	}
	
	

}
