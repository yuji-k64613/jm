package com.jm.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.jm.JmPattern;
import com.jm.R;
import com.jm.common.Constant;
import com.jm.pref.EditPrefUtil;
import com.jm.utility.Debug;
import com.jm.utility.JmException;

public class Dao implements IDao<SQLiteDatabase> {
	private static Dao instance = new Dao();
	private SQLiteStatement insertStmt = null;
	private SQLiteStatement updateStmt = null;
	private SQLiteStatement deleteStmt = null;
	private SQLiteStatement patternFileDeleteStmt = null;

	private Context context;

	public static Dao getInstance() {
		return instance;
	}

	private Dao() {

	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void init(SQLiteDatabase db, Context context) {
		this.context = context;

		db.execSQL("create table pattern (" + "id integer primary key, "
				+ "type integer not null, " + "siteswap text not null, "
				+ "height integer, " + "dwell integer, " + "name integer, "
				+ "motion text," + "lang integer," + "idx integer);");
		init2to3(db);
		start(db);
		initData2to3(db, context);
	}

	public void initData2to3(SQLiteDatabase db, Context context) {
		long id;
		id = initPatternList(db, "pattern.jm", false);
		initPatternList(db, "pattern_ja.jm", false);

		EditPrefUtil pref = new EditPrefUtil(context);
		pref.put(Constant.PREF_SELECTED_PATTERN_INDEX, id);
		pref.update();
	}

	public void init2to3(SQLiteDatabase db) {
		db.execSQL("create table pattern_file ("
				+ android.provider.BaseColumns._ID
				+ " integer primary key autoincrement, "
				+ "name text not null, " + "value blob not null, "
				+ "iswritable integer not null);");
	}

	public void start(SQLiteDatabase db) {
		insertStmt = db.compileStatement("insert into pattern ("
				+ "type, siteswap, height, dwell, name, motion, lang, idx"
				+ ") values (" + "?, ?, ?, ?, ?, ?, ?, ?" + ");");
		updateStmt = db.compileStatement("update pattern set "
				+ "siteswap = ?, height = ?, dwell = ?, name = ?, motion = ? "
				+ "where id = ? and lang = ?;");
		deleteStmt = db
				.compileStatement("delete from pattern where id = ? and lang = ?;");
		patternFileDeleteStmt = db
				.compileStatement("delete from pattern_file where "
						+ android.provider.BaseColumns._ID + " = ?;");
	}

	public void add(JmPattern jp, int index) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		add(jp, helper.getLangId(), index);
	}

	public void addNT(JmPattern jp, int index) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		addNT(jp, helper.getLangId(), index);
	}

	public void add(JmPattern jp, int lang, int index) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			int i = 1;
			SQLiteStatement stmt = insertStmt;
			stmt.bindLong(i++, jp.getType());
			stmt.bindString(i++, jp.getSiteSwap().toString());
			stmt.bindLong(i++, jp.getHeight());
			stmt.bindLong(i++, jp.getDwell());
			stmt.bindString(i++, jp.getName());
			stmt.bindString(i++, jp.motionToString());
			stmt.bindLong(i++, lang);
			stmt.bindLong(i++, index);
			stmt.executeInsert();
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new JmException(e);
		} finally {
			db.endTransaction();
		}
	}

	public void addNT(JmPattern jp, int lang, int index) throws JmException {
		try {
			int i = 1;
			SQLiteStatement stmt = insertStmt;
			stmt.bindLong(i++, jp.getType());
			stmt.bindString(i++, jp.getSiteSwap().toString());
			stmt.bindLong(i++, jp.getHeight());
			stmt.bindLong(i++, jp.getDwell());
			stmt.bindString(i++, jp.getName());
			stmt.bindString(i++, jp.motionToString());
			stmt.bindLong(i++, lang);
			stmt.bindLong(i++, index);
			stmt.executeInsert();
		} catch (SQLException e) {
			throw new JmException(e);
		}
	}

	public void set(JmPattern jp) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			int i = 1;
			SQLiteStatement stmt = updateStmt;
			stmt.bindString(i++, jp.getSiteSwap().toString());
			stmt.bindLong(i++, jp.getHeight());
			stmt.bindLong(i++, jp.getDwell());
			stmt.bindString(i++, jp.getName());
			stmt.bindString(i++, jp.motionToString());
			stmt.bindLong(i++, jp.getId());
			stmt.bindLong(i++, helper.getLangId());
			stmt.execute();
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new JmException(e);
		} finally {
			db.endTransaction();
		}
	}

	public void delete(int id) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			int i = 1;
			SQLiteStatement stmt = deleteStmt;
			stmt.bindLong(i++, id);
			stmt.bindLong(i++, helper.getLangId());
			stmt.execute();
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new JmException(e);
		} finally {
			db.endTransaction();
		}
	}

	public JmPattern[] get(int type) throws JmException {
		JmPattern[] list = null;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			SQLiteDatabase db = helper.getReadableDatabase();
			list = get(db, type);
		} catch (SQLException e) {
			throw new JmException(e);
		}
		return list;
	}

	public JmPattern[] get(SQLiteDatabase db, int type) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		String selection = "lang = " + helper.getLangId() + " and type = "
				+ type;
		String orderBy = "idx";
		return get(db, selection, orderBy);
	}

	public JmPattern[] get(SQLiteDatabase db, String selection, String orderBy)
			throws JmException {
		//DatabaseHelper helper = DatabaseHelper.getInstance();
		JmPattern[] list = null;
		Cursor c = db.query("pattern", new String[] { "id", "type", "siteswap",
				"height", "dwell", "name", "motion" }, selection, null, null,
				null, orderBy);
		c.moveToFirst();
		list = new JmPattern[c.getCount()];
		for (int i = 0; i < list.length; i++) {
			JmPattern jp = new JmPattern(c.getInt(0), // id
					c.getInt(1), // type
					c.getString(5), // name
					c.getString(2), // siteswap
					c.getInt(3), // height
					c.getInt(4), // dwell
					JmPattern.getMotion(c.getString(6))); // motions
			list[i] = jp;
			c.moveToNext();
		}
		c.close();
		return list;
	}

	public JmPattern[] getFromId(long id) throws JmException {
		JmPattern[] list = null;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			SQLiteDatabase db = helper.getReadableDatabase();
			String selection = "id = " + id + " and lang = "
					+ helper.getLangId();
			Cursor c = db.query("pattern", new String[] { "id", "type",
					"siteswap", "height", "dwell", "name", "motion" },
					selection, null, null, null, null);
			c.moveToFirst();
			list = new JmPattern[c.getCount()];
			for (int i = 0; i < list.length; i++) {
				JmPattern jp = new JmPattern(c.getInt(0), // id
						c.getInt(1), // type
						c.getString(5), // name
						c.getString(2), // siteswap
						c.getInt(3), // height
						c.getInt(4), // dwell
						JmPattern.getMotion(c.getString(6))); // motions
				list[i] = jp;
				c.moveToNext();
			}
			c.close();
		} catch (SQLException e) {
			throw new JmException(e);
		}
		return list;
	}

	public int max(int type) throws JmException {
		int m = -1;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			SQLiteDatabase db = helper.getReadableDatabase();
			String sql = "select max(idx) from pattern where type = " + type
					+ " and lang = " + helper.getLangId();
			Cursor c = db.rawQuery(sql, null);
			c.moveToFirst();
			m = c.getInt(0);
			c.close();
		} catch (SQLException e) {
			throw new JmException(e);
		}
		return m;
	}

	public int countAll() throws JmException {
		int count = -1;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			SQLiteDatabase db = helper.getReadableDatabase();
			String sql = "select count(*) from pattern;";
			Cursor c = db.rawQuery(sql, null);
			c.moveToFirst();
			count = c.getInt(0); // count;
			c.close();
		} catch (SQLException e) {
			throw new JmException(e);
		}
		return count;
	}

	public int count() throws JmException {
		int count = -1;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			SQLiteDatabase db = helper.getReadableDatabase();
			String sql = "select count(*) from pattern where lang = "
					+ helper.getLangId() + ";";
			Cursor c = db.rawQuery(sql, null);
			c.moveToFirst();
			count = c.getInt(0); // count;
			c.close();
		} catch (SQLException e) {
			throw new JmException(e);
		}
		return count;
	}

	public int count(int type) throws JmException {
		int count = -1;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			SQLiteDatabase db = helper.getReadableDatabase();
			String sql = "select count(*) from pattern where type = " + type
					+ " and lang = " + helper.getLangId() + ";";
			Cursor c = db.rawQuery(sql, null);
			c.moveToFirst();
			count = c.getInt(0); // count;
			c.close();
		} catch (SQLException e) {
			throw new JmException(e);
		}
		return count;
	}

	public PatternFile getPatternFile(long id) {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getReadableDatabase();

		String selection = android.provider.BaseColumns._ID + " = ?";
		String[] selectionArgs = { String.valueOf(id) };
		String orderBy = null;
		List<PatternFile> list = getPatternFile(db, selection, selectionArgs,
				orderBy, true);
		if (list.size() <= 0) {
			return null;
		}
		return list.get(0);
	}

	public List<PatternFile> getPatternFile() {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getReadableDatabase();
		String orderBy = "name";
		return getPatternFile(db, null, null, orderBy, false);
	}

	public List<PatternFile> getPatternFile(SQLiteDatabase db) {
		return getPatternFile(db, null, null, null, false);
	}

	private List<PatternFile> getPatternFile(SQLiteDatabase db,
			String selection, String[] selectionArgs, String orderBy,
			boolean isBlob) {
		List<PatternFile> list = new ArrayList<PatternFile>();
		Cursor cursor = null;
		try {
			cursor = db.query("pattern_file", new String[] {
					android.provider.BaseColumns._ID, "name", "value",
					"iswritable" }, selection, selectionArgs, null, null,
					orderBy);
			int size = cursor.getCount();
			if (size > 0){
				cursor.moveToFirst();
			}
			for (int i = 0; i < size; i++) {
				PatternFile pf = new PatternFile(cursor.getLong(0),
						cursor.getString(1), (isBlob) ? cursor.getBlob(2)
								: null, cursor.getInt(3) != 0);
				list.add(pf);
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Debug.d(this, null, e);
			list = null;
		} catch (Exception e) {
			Debug.d(this, null, e);
			list = null;
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	}

	// public PatternFile[] getPatternFile(SQLiteDatabase db) throws JmException
	// {
	// String selection = null;
	// String orderBy = "name";
	// return getPatternFile(db, selection, orderBy);
	// }
	//
	// public PatternFile[] getPatternFile(SQLiteDatabase db, String selection,
	// String orderBy) throws JmException {
	// PatternFile[] list = null;
	// Cursor c = db.query("pattern_file", new String[] { "id", "name" },
	// selection, null, null, null, orderBy);
	// c.moveToFirst();
	// list = new PatternFile[c.getCount()];
	// for (int i = 0; i < list.length; i++) {
	// PatternFile pf = new PatternFile(c.getInt(0), // id
	// c.getString(1)); // name
	// list[i] = pf;
	// c.moveToNext();
	// }
	// c.close();
	// return list;
	// }
	//
	// public PatternFile getPatternFile(long id) {
	// String selection = android.provider.BaseColumns._ID + " = ?";
	// String[] selectionArgs = { String.valueOf(id) };
	// String orderBy = null;
	// List<PatternFile> list = getPatternFile(selection, selectionArgs,
	// orderBy);
	// if (list.size() <= 0) {
	// return null;
	// }
	// return list.get(0);
	// }

	public long addPatternFile(String name, byte[] blob, boolean isWritable) {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getWritableDatabase();
		return addPatternFile(db, name, blob, isWritable);
	}

	public long addPatternFile(SQLiteDatabase db, String name, byte[] blob,
			boolean isWritable) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("value", blob);
		values.put("iswritable", isWritable ? 1 : 0);

		return db.insert("pattern_file", null, values);
	}

	public void deletePatternFile(long id) throws JmException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		try {
			int i = 1;
			SQLiteStatement stmt = patternFileDeleteStmt;
			stmt.bindLong(i++, id);
			stmt.execute();
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new JmException(e);
		} finally {
			db.endTransaction();
		}
	}

	public String[] getMenu() {
		String[] list = { context.getString(R.string.list0_1),
				context.getString(R.string.list0_2),
				context.getString(R.string.list0_3),
				context.getString(R.string.list0_4),
				context.getString(R.string.list0_5),
				context.getString(R.string.list0_6),
				context.getString(R.string.list0_7),
				context.getString(R.string.list0_8) };
		return list;

	}

	public long initPatternList(SQLiteDatabase db, String filename,
			boolean isWritable) {
		InputStream is = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream(filename);
			return insertPatternList(db, is, filename, isWritable);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Debug.d(this, e.getMessage());
				}
				is = null;
			}
		}
	}

	public long insertPatternList(InputStream is, String name,
			boolean isWritable) {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		SQLiteDatabase db = helper.getWritableDatabase();
		return insertPatternList(db, is, name, isWritable);
	}

	public long insertPatternList(SQLiteDatabase db, InputStream is,
			String name, boolean isWritable) {
		long id = -1;
		try {
			byte[] b = toByteArray(is);
			if (b == null) {
				return id;
			}
			Dao dao = Dao.getInstance();
			id = dao.addPatternFile(db, name, b, isWritable);
		} finally {
			close(is);
		}
		return id;
	}

	private void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				Debug.d(this, e.getMessage());
			}
		}
	}

	private byte[] toByteArray(InputStream inputStream) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			while (true) {
				int len;
				len = inputStream.read(buffer);
				if (len < 0) {
					break;
				}
				bout.write(buffer, 0, len);
			}
			return bout.toByteArray();
		} catch (IOException e) {
			Debug.d(this, e.getMessage());
			return null;
		}
	}

	@Override
	public boolean isWritable() {
		return true;
	}
}
