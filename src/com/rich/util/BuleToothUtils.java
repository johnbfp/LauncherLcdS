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
	private BluetoothAdapter mBluetoothAdapter; // ����������
	private Set<BluetoothDevice> bondedDevices; // ��ȡ�Ѿ���Ե�����
	private BluetoothManager bluetoothManager;
	private Boolean scaleIng; // �Ƿ�����ɨ��
	private static final int REQUEST_ENABLE_BLE = 1; // ��������
	private static final long SCALE_PERIOD = 10 * 1000; // ɨ��ʱ�� 10��
	private static List<BuleToothInfo> resultList;

	public BuleToothUtils(Context context) {
		this.mContext = context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		bondedDevices = mBluetoothAdapter.getBondedDevices();

		// ���ù㲥��Ϣ����
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);// ÿ������һ���豸�ͻᷢ��һ���ù㲥
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);// ��ȫ����������͸ù㲥
		filter.setPriority(Integer.MAX_VALUE);// �������ȼ�
		// ע�����������㲥�����ߣ����ղ������������
		//context.registerReceiver(receiver, filter);

	}

	// ���������򿪹ر�
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
		if (mBluetoothAdapter.isDiscovering()) { // ����Ѿ������У�����ȡ������
			mBluetoothAdapter.cancelDiscovery();
		}
		mBluetoothAdapter.startDiscovery();
	}
	
	

}
