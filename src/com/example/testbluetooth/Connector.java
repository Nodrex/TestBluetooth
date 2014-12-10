package com.example.testbluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
//TODO should be changed to implement runnable interface and not extend thread class.
public class Connector extends Thread {

	private BluetoothServerSocket mmServerSocket;

	public Connector(BluetoothAdapter mBluetoothAdapter, UUID uuid) throws Exception {
		mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(Constants.CONNECTION_SERVICE_NAME, uuid);
		System.out.println(mmServerSocket);
	}

	public void run() {
		BluetoothSocket socket = null;
		while (true) {
			try {
				socket = mmServerSocket.accept();
			} catch (IOException e) {
				break;
			}
			if (socket != null) {
				// manageConnectedSocket(socket);
				System.out.println(socket);
				try {
					mmServerSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
}
