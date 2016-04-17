package com.jm.pref;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class EditPrefUtil implements PrefUtil {
	private static final String DELM = "\t";
	private Map<String, String> hash = new HashMap<String, String>();
	private PrefUtil pref;
	
	public EditPrefUtil(Context context){
		pref = new PrefUtilImpl(context);
	}
	
	@Override
	public SharedPreferences getSharedPreferences() {
		return pref.getSharedPreferences();
	}

	@Override
	public void put(String key, String value) {
		hash.put(key, value);
	}

	@Override
	public void put(String key, int value) {
		hash.put(key, String.valueOf(value));
	}

	@Override
	public void put(String key, long value) {
		hash.put(key, String.valueOf(value));
	}

	@Override
	public String get(String key) {
		String value = hash.get(key);
		if (value != null){
			return value;
		}
		value = pref.get(key);
		
		return value;
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
		if (value != null){
			return value;
		}
		hash.put(key, initValue);
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

	public void update(){
		Set<Entry<String, String>> set = hash.entrySet();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext(); ){
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			pref.put(key, value);
		}
		hash.clear();
	}

	@Override
	public void copy(String srcKey, String dstKey){
		String value = get(srcKey);
		put(dstKey, value);
	}
	
	@Override
	public void remove(String key) {
		hash.put(key, null);
	}
	
	public void save(String key){
		StringBuffer sb = new StringBuffer();
		
		Set<Entry<String, String>> set = hash.entrySet();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext(); ){
			Entry<String, String> entry = it.next();
			String prefKey = entry.getKey();
			String prefValue = entry.getValue();
			
			sb.append(prefKey);
			sb.append(DELM);
			sb.append(prefValue);
			sb.append(DELM);
		}
		pref.put(key, sb.toString());
	}
	
	public void restore(String key){
		hash.clear();

		String data = pref.get(key);
		String[] str = data.split(DELM);
		for (int i = 0; i < str.length / 2; i++){
			String prefKey = str[i * 2];
			String prefValue = str[i * 2 + 1];
			put(prefKey, prefValue);
		}
	}
}
