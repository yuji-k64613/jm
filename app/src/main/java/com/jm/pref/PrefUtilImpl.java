package com.jm.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;

public class PrefUtilImpl implements PrefUtil {
	private static final String MENU1_ACTIVITY = "Menu1";
	private static final String PACKAGE_COM_JM = "com.jm";
	private Context context;

	public PrefUtilImpl(Context context) {
		this.context = context;
	}

	@Override
	public SharedPreferences getSharedPreferences(){
		SharedPreferences pref = null;
		try {
			Context ctxt = context.createPackageContext(PACKAGE_COM_JM,
					Context.CONTEXT_RESTRICTED);
			pref = ctxt.getSharedPreferences(MENU1_ACTIVITY,
					Context.MODE_PRIVATE);
		} catch (NameNotFoundException e) {
			;
		}		
		return pref;
	}
	
	@Override
	public void put(String key, String value) {
		synchronized (this) {
			SharedPreferences pref = getSharedPreferences();
			if (pref == null){
				return;
			}

			Editor e = pref.edit();
			e.putString(key, value);
			e.commit();
		}
	}

	@Override
	public void put(String key, int value) {
		put(key, String.valueOf(value));
	}

	@Override
	public void put(String key, long value) {
		put(key, String.valueOf(value));
	}

	@Override
	public String get(String key) {
		String val;
		
		synchronized (this) {
			SharedPreferences pref = getSharedPreferences();
			if (pref == null){
				return null;
			}

			val = pref.getString(key, null);
		}

		return val;
	}

	@Override
	public int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	@Override
	public long getLong(String key) {
		return Long.parseLong(get(key));
	}

	@Override
	public String get(String key, String initValue) {
		String value = get(key);
		if (value != null) {
			return value;
		}
		put(key, initValue);
		return initValue;
	}

	@Override
	public int getInt(String key, int initValue) {
		return Integer.valueOf(get(key, String.valueOf(initValue)));
	}

	@Override
	public long getLong(String key, long initValue) {
		return Long.valueOf(get(key, String.valueOf(initValue)));
	}
	
	@Override
	public void copy(String srcKey, String dstKey){
		String value = get(srcKey);
		put(dstKey, value);
	}
	
	@Override
	public void remove(String key) {
		put(key, null);
	}
}
