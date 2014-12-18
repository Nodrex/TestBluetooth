package com.example.testbluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
//TODO should be changed to implement runnable interface and not extend thread class.
public class Connector extends Thread {

	private BluetoothServerSocket mmServerSocket;

	public Connector(BluetoothAdapter mBluetoothAdapter) throws Exception {
		mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
				Constants.CONNECTION_SERVICE_NAME, 
				UUID.fromString(Constants.UUID));
	}

	public void run() {
		BluetoothSocket socket = null;
		while (true) {
			try {
				socket = mmServerSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if (socket != null) {
				manageConnectedSocket(socket);
				try {
					mmServerSocket.close();
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
