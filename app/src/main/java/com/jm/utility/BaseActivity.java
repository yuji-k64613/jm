package com.jm.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.jm.Help;
import com.jm.R;
import com.jm.db.DatabaseHelper;

public class BaseActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.d(this, Debug.ON_CREATE);

		try {
			DatabaseHelper.init(getApplicationContext());
		} catch (JmException e) {
			terminate(e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Debug.d(this, Debug.ON_RESTART);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Debug.d(this, Debug.ON_RESUME);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Debug.d(this, Debug.ON_START);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Debug.d(this, Debug.ON_STOP);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Debug.d(this, Debug.ON_DESTROY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		menu.add(0, Menu.FIRST, Menu.NONE, R.string.help).setIcon(
				android.R.drawable.ic_menu_help);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			Intent intent = new Intent(this, (Class<?>) Help.class);
			startActivity(intent);
		} catch (Exception e) {
			terminate(e);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_SEARCH:
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	protected void setFullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	protected void showDialog(String title, String msg, final boolean isFinish) {
		new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(msg)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (isFinish) {
									finish();
								}
							}
						}).show();
	}

	protected void showDialog(String title, String msg) {
		showDialog(title, msg, false);
	}

	protected void showDialog(Object target, Throwable t) {
		if (t != null) {
			Debug.d(target, null, t);
		}
		showDialog(getString(R.string.title_system_error),
				getString(R.string.error_unexpected));
	}

	public void terminate(Throwable t) {
		if (t != null) {
			Debug.d(this, null, t);
		}
		moveTaskToBack(true);
	}
}
