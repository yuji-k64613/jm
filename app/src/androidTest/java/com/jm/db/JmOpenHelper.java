package com.jm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JmOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "jm";

	private static final int LATEST_VERSION = 1;

	private Context context;

	public JmOpenHelper(Context context) {
		this(context, LATEST_VERSION);
		this.context = context;
	}

	public JmOpenHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Dao dao = Dao.getInstance();
		dao.init(db, context);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
