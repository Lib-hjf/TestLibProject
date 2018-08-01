package com.hjf.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.hjf.MyApp;

public class NotifyUtil {

	public static void toast(final String msg) {
		if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
			Toast.makeText(MyApp.getContent(), msg, Toast.LENGTH_LONG).show();
		}else {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MyApp.getContent(), msg, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
