package com.jm.pref;

import android.content.SharedPreferences;

public interface PrefUtil {
	SharedPreferences getSharedPreferences();
	void put(String key, String value);
	void put(String key, int value);
	void put(String key, long value);
	String get(String key);
	int getInt(String key);
	long getLong(String key);
	String get(String key, String initValue);
	int getInt(String key, int initValue);
	long getLong(String key, long initValue);
	void copy(String srcKey, String dstKey);
	void remove(String key);
}
