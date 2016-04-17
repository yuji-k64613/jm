package com.jm.db;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.jm.utility.JmException;

public class DaoTest extends AndroidTestCase {

	private DatabaseHelper helper;

	public void setUp() throws JmException {
		DatabaseHelper
				.init(new RenamingDelegatingContext(getContext(), "test_"));
		helper = DatabaseHelper.getInstance();

		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from pattern_file");
	}

	public void tearDown() {
		helper.close();
	}

	public void testGetPatternFile() throws JmException {
		Dao dao = Dao.getInstance();
		List<PatternFile> list = dao.getPatternFile();
		int size = list.size();

		assertEquals(0, size);
	}

	public void testAddPatternFile() throws JmException {
		String name = "foo";
		byte[] blob = { 1, 2, 3, 4, 5 };

		Dao dao = Dao.getInstance();
		long id = dao.addPatternFile(name, blob, false);

		List<PatternFile> list = dao.getPatternFile();
		int size = list.size();
		assertEquals(1, size);

		PatternFile p = dao.getPatternFile(id);
		assertEquals("foo", p.getName());
		assertArrayEquals(blob, p.getValue());
	}

	private void assertArrayEquals(byte[] a, byte[] b) {
		assertEquals(a.length, b.length);

		for (int i = 0; i < a.length; i++) {
			assertEquals(a[i], b[i]);
		}
	}
}
