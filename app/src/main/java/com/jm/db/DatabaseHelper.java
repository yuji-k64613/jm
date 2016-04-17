package com.jm.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jm.JmPattern;
import com.jm.utility.Debug;
import com.jm.utility.JmException;

public class DatabaseHelper extends SQLiteOpenHelper {
	public final static int NONE_ID = -1;
	public final static int ENGLISH_ID = 1;
	public final static int JAPANEASE_ID = 2;
	public final static int FRENCH_ID = 3;
	private static int langId = NONE_ID;
	private Context context;
	private static boolean convFlag1to2 = false;
	private static boolean convFlag2to3 = false;

	private static DatabaseHelper instance = null;

	public static DatabaseHelper getInstance() {
		return instance;
	}

	public static void init(Context context) throws JmException {
		DaoFactory f = DaoFactory.getInstance();
		f.setContext(context.getApplicationContext());

		if (instance != null) {
			return;
		}

		DatabaseHelper helper = new DatabaseHelper(context);

		// onCreate, onUpgradeを呼び出す
		SQLiteDatabase db = helper.getWritableDatabase();

		// アップグレードで無ければ、SQL分を定義
		// アップデートの場合は、後で実行する
		if (!helper.getConvFlag()) {
			Dao.getInstance().start(db);
		}
	}

	public DatabaseHelper(Context context) {
		// super(context, "JuggleMaster.db", null, 2);
		super(context, "JuggleMaster.db", null, 3);
		instance = this;

		// setLangId(context);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try {
			Dao dao = Dao.getInstance();
			dao.init(db, context);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Debug.d(this, null, e);
			throw e;
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int v;

		v = 1;
		if (oldVersion == v && newVersion > v) {
			convFlag1to2 = true;
		}

		v = 2;
		if (oldVersion <= v && newVersion > v) {
			convFlag2to3 = true;
		}
	}

	public void convert(SQLiteDatabase db, int langId) throws JmException {
		if (convFlag1to2) {
			convert1to2(db, langId);
		}
		if (convFlag2to3) {
			convert2to3(db);
		}

		convFlag1to2 = false;
		convFlag2to3 = false;
	}

	private void convert1to2(SQLiteDatabase db, int langId) throws JmException {
		db.execSQL("ALTER TABLE pattern ADD COLUMN lang integer;");
		db.execSQL("ALTER TABLE pattern ADD COLUMN idx integer;");

		Dao dao = Dao.getInstance();
		for (int i = 0; i < 7; i++) {
			JmPattern[] list;
			list = dao.get(db, "type = " + i, null);
			int size = list.length;
			for (int j = 0; j < size; j++) {
				JmPattern item = list[j];
				int id = item.getId();
				db.execSQL("UPDATE pattern set idx = " + j + " WHERE id = "
						+ id + ";");
			}
		}

		// 既存のデータは、日本語のデータに変換
		db.execSQL("UPDATE pattern set LANG = " + JAPANEASE_ID
				+ " WHERE TYPE <> 6;");
		db.execSQL("DELETE FROM pattern WHERE NAME = '[新規作成]';");
		// ただし、マイパターンは、システムの言語に
		db.execSQL("UPDATE pattern set LANG = " + langId + " WHERE TYPE = 6;");
		db.execSQL("create index langindex on pattern(lang);");
		db.execSQL("create index idxindex on pattern(idx);");
	}

	private void convert2to3(SQLiteDatabase db) {
		Dao dao = Dao.getInstance();
		
		dao.init2to3(db);
		dao.start(db);		
		dao.initData2to3(db, context);
		
		JmDao jdao = JmDao.getInstance();
		jdao.init(null, context);
	}

	public boolean getConvFlag() {
		return convFlag1to2 || convFlag2to3;
	}

	public boolean getConvFlag1to2() {
		return convFlag1to2;
	}

	public boolean getConvFlag2to3() {
		return convFlag2to3;
	}

	public void setLangId(int id) {
		langId = id;
	}

	public int getLangId() {
		if (langId != NONE_ID) {
			return langId;
		}
		String id = context.getString(com.jm.R.string.lang);
		return Integer.valueOf(id).intValue();
	}
}
