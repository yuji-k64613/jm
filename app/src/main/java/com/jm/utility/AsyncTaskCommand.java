package com.jm.utility;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.KeyEvent;

import com.jm.R;

public class AsyncTaskCommand extends AsyncTask<String, Integer, String> {
	private WeakReference<Activity> activityRef;
	private AsyncTaskIF command;
	private ProgressDialog dialog;
	private boolean isCancel = false;
	
	private String title;
	private boolean isCancelable;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AsyncTaskCommand(Activity activity, AsyncTaskIF command, String title,
			boolean isCancelable) {
		this.activityRef = new WeakReference(activity);
		this.command = command;
		this.title = title;
		this.isCancelable = isCancelable;
	}

	@Override
	protected void onPreExecute() {
		Debug.d(this, "onPreExecute start");

		Activity activity = activityRef.get();
		if (activity == null) {
			return;
		}
		dialog = new ProgressDialog(activity);
		dialog.setIndeterminate(true);
		dialog.setCancelable(isCancelable);
		dialog.setTitle(title);
		if (isCancelable) {
			dialog.setButton(activity.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							AsyncTaskCommand.this.cancel(true);
						}
					});
		}
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_SEARCH:
					return true;
				}
				return false;
			}
		});
		dialog.show();
		Debug.d(this, "onPreExecute end");
	}

	@Override
	protected String doInBackground(String... arg0) {
		Debug.d(this, "doInBackground start");
		command.doExecute();
		Debug.d(this, "doInBackground end");

		return null;
	}

	@Override
	protected void onCancelled() {
		Debug.d(this, "onCancelled start");

		isCancel = true;

		if (command != null) {
			command.cancel();
		}
		onPostExecute(null);
		Debug.d(this, "onCancelled end");
	}

	@Override
	protected void onPostExecute(String result) {
		Debug.d(this, "onPostExecute start");

		try {
			if (command != null) {
				command.done(isCancel);
				command = null;
			}
			if (dialog != null) {
				try {
					dialog.dismiss();
				}
				catch (IllegalArgumentException e){
					;
				}
				dialog = null;
			}
			activityRef = null;
		} catch (RuntimeException e) {
			Debug.d(this, null, e);
			throw e;
		}
		Debug.d(this, "onPostExecute end");
	}

	public void stop() {
		Debug.d(this, "stop start");

		if (command != null) {
			isCancel = true;
			command.cancel();
		}
		// onPostExecute()のdialog.dismiss();でエラーとなるため
		// 本質的な解決策ではない
		// dialog = null;
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}

		Debug.d(this, "stop end");
	}
}
