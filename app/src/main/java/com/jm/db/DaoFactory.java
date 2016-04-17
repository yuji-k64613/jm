package com.jm.db;

import android.content.Context;

import com.jm.pref.EditPrefUtil;

public class DaoFactory {
	public enum Mode {
		ORIGINAL,
		ANDROID
	};
	
	private final static String PREF_MODE = "MODE";
	private static DaoFactory instance = new DaoFactory();
	private Mode mode = null;
	private Context context = null;

	public static DaoFactory getInstance() {
		return instance ;
	}

	private DaoFactory(){
		
	}
	
	public void setContext(Context context){
		this.context  = context;
	}
	
	public IDao<?> getDao(){
		if (getMode() == Mode.ORIGINAL){
			return JmDao.getInstance();
		}
		else {
			return Dao.getInstance();
		}
	}
	
	public Mode getMode(){
		if (mode == null){
			EditPrefUtil pref = new EditPrefUtil(context);
			int m = pref.getInt(PREF_MODE, 1);
			pref.update();
			mode = (m == 0)? Mode.ORIGINAL : Mode.ANDROID;
		}
		return mode;
	}
	
	public void setMode(Mode mode){
		this.mode = mode;

		int m = (mode == Mode.ORIGINAL)? 0 : 1;
		EditPrefUtil pref = new EditPrefUtil(context);
		pref.put(PREF_MODE, m);
		pref.update();
	}
	
	public void changeMode(){
		setMode((getMode() == Mode.ORIGINAL)? Mode.ANDROID : Mode.ORIGINAL);
	}
}
