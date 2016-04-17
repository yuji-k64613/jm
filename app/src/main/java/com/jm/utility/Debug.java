package com.jm.utility;

import android.util.Log;

public class Debug {
	public static final String ON_CREATE = "onCreate";
	public static final String ON_DESTROY = "onDestroy";
	public static final String ON_START = "onStart";
	public static final String ON_STOP = "onStop";
	public static final String ON_RESTART = "onRestart";
	public static final String ON_RESUME = "onResume";

	private static final String FILTER = "com.jm";
	
	public static void d(Object target, String msg){
		d(target, msg, null);
	}

	public static void d(Object target, String msg, Throwable t){
		if (!Log.isLoggable(FILTER, Log.DEBUG)){
			return;
	 	}
	
		String text = target.getClass().getName();
		if (msg != null){
			 text += " " + msg;			
		}
		if (t == null){
			Log.d(FILTER, text);
		}
		else {
			Log.d(FILTER, text, t);			
		}
	}
}
