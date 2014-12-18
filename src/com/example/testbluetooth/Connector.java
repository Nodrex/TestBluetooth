package com.example.testbluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
//TODO should be changed to implement runnable interface and not extend thread class.
public class Connector extends Thread {

	private BluetoothServerSocket serverSocket;

	public Connector(BluetoothAdapter bluetoothAdapter) throws Exception {
		serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
				Constants.CONNECTION_SERVICE_NAME, 
				UUID.fromString(Constants.UUID));
	}

	public void run() {
		BluetoothSocket socket = null;
		while (true) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if (socket != null) {
				manageConnectedSocket(socket);
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private void manageConnectedSocket(BluetoothSocket socket) {
		try {
			byte[] buffer = new byte[1024];
			socket.getInputStream().read(buffer);
			new String(buffer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
