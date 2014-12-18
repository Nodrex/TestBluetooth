package com.example.testbluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver receiver;
	private BluetoothDevice device;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);

		// Detect if device have bt
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Util.toast(this, "bluetoothAdapter is not supported");
		}

		// request to turn on bt
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, Constants.PROCESS_BLUETOOTH_ON_Id);
		} else {
			requestVisibility();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) return;// it means bt or visibility activation was canceled.
		if (requestCode == Constants.PROCESS_BLUETOOTH_ON_Id) requestVisibility();
		else if(requestCode == Constants.PROCESS_BLUETOOTH_VISIBLE_Id) scanPairedDevices();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiver == null) return;
		unregisterReceiver(receiver);
	}
	
	/**
	 * Asks user if he wants to turn on visibility that other devices will be available to see this device via bluetooth.
	 */
	private void requestVisibility(){
		Intent discoverableIntent = new
		Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,0);//second parameter 0 means it is visible always
		startActivityForResult(discoverableIntent,Constants.PROCESS_BLUETOOTH_VISIBLE_Id);
	}
	

	/**
	 * Scans paired devices.
	 */
	private void scanPairedDevices() {
		List<String> l = new ArrayList<>();
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				l.add(device.getName() + "\n" + device.getAddress());
			}
		}
		//Util.messagBox(this, l);
		scanNewDevices();
	}
	
	/**
	 * Search for new devices.
	 */
	private void scanNewDevices() {
		final List<String> l = new ArrayList<>();
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					l.add(device.getName() + "\n" + device.getAddress());
				}
				if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
					try {
						new Connector(bluetoothAdapter).start();
					} catch (Exception e) {
						l.clear();
						l.add("Could not connect to device:");
						l.add(device.getName());
						l.add(e.toString());
						Util.messagBox(MainActivity.this, l);
					}
				}
				if(!l.isEmpty()){
					//Util.messagBox(MainActivity.this, l);
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy
		bluetoothAdapter.startDiscovery();//starts bt device discovering
	}
	
}
