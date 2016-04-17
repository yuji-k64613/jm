package com.jm.db;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.jm.JmPattern;
import com.jm.common.Constant;
import com.jm.pattern.PatternAnalyzer;
import com.jm.pref.EditPrefUtil;
import com.jm.utility.Debug;
import com.jm.utility.JmException;
import com.jm.utility.JmUnsupportedOperationException;

public class JmDao implements IDao<String> {
	private static JmDao instance = new JmDao();
	private List<String> menu = null;
	private List<JmPattern> list = null;
	private EditPrefUtil pref;

	public static JmDao getInstance() {
		return instance;
	}

	private JmDao() {

	}

	@Override
	public void init(String db, Context context) {
		pref = new EditPrefUtil(context);
		long id = pref.getLong(Constant.PREF_SELECTED_PATTERN_INDEX);
		if (id < 0) {
			return;
		}
		Dao dao = Dao.getInstance();
		PatternFile p = dao.getPatternFile(id);
		byte[] b = p.getValue();
		ByteArrayInputStream is = new ByteArrayInputStream(b);
		loadPatternFile(is);
	}

	public void loadPatternFile(InputStream is) {
		try {
			PatternAnalyzer pa = new PatternAnalyzer();
			pa.analyzer(is);
			list = pa.getPattern();
			menu = pa.getTitle();

			int size = menu.size();
			for (int i = size - 1; i >= 0; i--) {
				// count()の代わり
				JmPattern[] l = this.get(i);
				if (l.length <= 0) {
					menu.remove(i);
				}
			}

		} catch (FileNotFoundException e) {
			Debug.d(this, e.getMessage());
		} catch (IOException e) {
			Debug.d(this, e.getMessage());
		} catch (JmException e) {
			Debug.d(this, e.getMessage());
		}
	}

	@Override
	public JmPattern[] get(int type) throws JmException {
		if (list == null) {
			return null;
		}
		List<JmPattern> l = new ArrayList<JmPattern>();
		for (JmPattern p : list) {
			if (p.getType() == type) {
				l.add(p);
			}
		}
		return l.toArray(new JmPattern[0]);
	}

	@Override
	public void start(String db) {

	}

	@Override
	public void add(JmPattern jp, int index) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public void addNT(JmPattern jp, int index) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public void add(JmPattern jp, int lang, int index) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public void addNT(JmPattern jp, int lang, int index) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public void set(JmPattern jp) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public void delete(int id) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public JmPattern[] get(String db, int type) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public JmPattern[] get(String db, String selection, String orderBy)
			throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public JmPattern[] getFromId(long id) throws JmException {
		if (list == null) {
			return null;
		}
		for (JmPattern p : list) {
			if (p.getId() == id) {
				return new JmPattern[] { p };
			}
		}
		return null;
	}

	@Override
	public int max(int type) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public int countAll() throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public int count() throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public int count(int type) throws JmException {
		throw new JmUnsupportedOperationException();
	}

	@Override
	public String[] getMenu() {
		return menu.toArray(new String[0]);
	}

	@Override
	public boolean isWritable() {
		return false;
	}
}
