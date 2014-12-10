package com.example.testbluetooth;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class Util {

	public static void toast(Activity activity, String text) {
		Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
	}

	public static void messagBox(Activity activity, List<String> l) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setTitle("Paired Devices");
		
		String text= "";
		for (String string : l) {
			text+=string + "\n";
		}
		
		alertDialogBuilder.setMessage(text);
		
		

		// set positive button: Yes message
		/*
		 * alertDialogBuilder.setPositiveButton("Yes",new
		 * DialogInterface.OnClickListener() {
		 * 
		 * public void onClick(DialogInterface dialog,int id) {
		 * 
		 * }
		 * 
		 * });
		 */

		// set negative button: No message
		/*alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				//alertDialog.dismiss();

			}

		});*/

		// set neutral button: Exit the app message
		
		alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			@Override		
			public void onClick(DialogInterface dialog, int id) {
			}

		});
		

		AlertDialog alertDialog = alertDialogBuilder.create();
		
		alertDialog.show();
	}

}
