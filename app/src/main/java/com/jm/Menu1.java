package com.jm;

import java.io.ByteArrayInputStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jm.common.Constant;
import com.jm.db.Dao;
import com.jm.db.DaoFactory;
import com.jm.db.DatabaseHelper;
import com.jm.db.IDao;
import com.jm.db.InitDB;
import com.jm.db.JmDao;
import com.jm.db.PatternFile;
import com.jm.pref.EditPrefUtil;
import com.jm.utility.AsyncTaskCommand;
import com.jm.utility.BaseActivity;
import com.jm.utility.Debug;
import com.jm.utility.JmException;

public class Menu1 extends BaseActivity {
	public static final int INDEX6 = 6;
	public static final int INDEX7 = 7;
	private static boolean isInit = true;
	private EditPrefUtil pref = new EditPrefUtil(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu1);

		if (isInit) {
			isInit = false;
			if (init()) {
				return;
			}
		}

		updateList();
	}

	private void updateList() {
		IDao<?> dao = DaoFactory.getInstance().getDao();
		updateList(dao);
	}

	private void updateList(IDao<?> dao) {
		CharSequence[] list = dao.getMenu();
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_list_item_1, list);
		ListView listView = (ListView) findViewById(R.id.Menu1);
		listView.setAdapter(adapter);
		listView.setScrollingCacheEnabled(false);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DaoFactory f = DaoFactory.getInstance();

				if (f.getMode() == DaoFactory.Mode.ANDROID
						&& position == INDEX7) {
					Intent intent = new Intent(Menu1.this,
							(Class<?>) GenPattern.class);
					intent.putExtra("INDEX", position);
					startActivityForResult(intent, -1);
				} else {
					Intent intent = new Intent(Menu1.this,
							(Class<?>) Menu2.class);
					intent.putExtra("INDEX", position);
					startActivityForResult(intent, -1);
				}
			}
		});
	}

	private boolean init() {
		boolean ret = false;
		try {
			DatabaseHelper helper = DatabaseHelper.getInstance();
			boolean convFlag = helper.getConvFlag();
			int count;

			Dao dao = Dao.getInstance();
			dao.setContext(this);
			count = dao.countAll();
			if (count <= 0 || convFlag) {
				InitDB idb = new InitDB(this.getApplicationContext());
				AsyncTaskCommand command = new AsyncTaskCommand(this, idb,
						getString(R.string.update), false);
				command.execute((String[]) null);
			}
			else {
				JmDao jdao = JmDao.getInstance();
				jdao.init(null, this);
			}
		} catch (JmException e) {
			Debug.d(this, e.getMessage());
			return true;
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST + 1, Menu.NONE, R.string.orgmode).setIcon(
				android.R.drawable.ic_menu_more);
		menu.add(0, Menu.FIRST + 2, Menu.NONE, R.string.andmode).setIcon(
				android.R.drawable.ic_menu_more);
		menu.add(0, Menu.FIRST + 3, Menu.NONE, R.string.load).setIcon(
				android.R.drawable.ic_menu_manage);
		boolean ret = super.onCreateOptionsMenu(menu);

		return ret;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		DaoFactory f = DaoFactory.getInstance();
		if (f.getMode() == DaoFactory.Mode.ORIGINAL) {
			menu.findItem(Menu.FIRST + 1).setVisible(false);
			menu.findItem(Menu.FIRST + 2).setVisible(true);
			menu.findItem(Menu.FIRST + 3).setVisible(true);
		} else {
			menu.findItem(Menu.FIRST + 1).setVisible(true);
			menu.findItem(Menu.FIRST + 2).setVisible(false);
			menu.findItem(Menu.FIRST + 3).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		Intent intent;

		try {
			switch (item.getItemId()) {
			case Menu.FIRST:
				intent = new Intent(this, (Class<?>) Help.class);
				startActivityForResult(intent, -1);
				break;
			case Menu.FIRST + 1:
			case Menu.FIRST + 2:
				changeMode();
				break;
			case Menu.FIRST + 3:
				PatternManager.init(this.getApplicationContext());
				intent = new Intent(this, (Class<?>) PatternManager.class);
				startActivityForResult(intent, 1);
				break;
			default:
				ret = super.onOptionsItemSelected(item);
				break;
			}
		} catch (Exception e) {
			terminate(e);
		}
		return ret;
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case 1:
			Bundle extras = intent.getExtras();
			long pid = extras.getLong(Constant.EXTRAS_ID);

			Dao dao = Dao.getInstance();
			PatternFile p = dao.getPatternFile(pid);

			ByteArrayInputStream bis = new ByteArrayInputStream(p.getValue());

			JmDao jmdao = JmDao.getInstance();
			jmdao.loadPatternFile(bis);

			pref.put(Constant.PREF_SELECTED_PATTERN_INDEX, pid);
			pref.update();

			updateList();
			break;
		}
	}

	private void changeMode() {
		DaoFactory f = DaoFactory.getInstance();
		f.changeMode();

		updateList();
	}
}
