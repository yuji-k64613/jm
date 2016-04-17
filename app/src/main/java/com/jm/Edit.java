package com.jm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jm.common.Constant;
import com.jm.db.DaoFactory;
import com.jm.db.IDao;
import com.jm.utility.BaseActivity;
import com.jm.utility.JmException;
import com.jm.utility.Resource;

public class Edit extends BaseActivity {
	public final static int NEW = 1;
	public final static int EDIT = 2;

	private EditText patternEdit = null;
	private EditText siteswapEdit = null;
	private EditText motionEdit = null;
	private TextView heightTextView = null;
	private SeekBar heightSeekBar = null;
	private TextView dwellTextView = null;
	private SeekBar dwellSeekBar = null;
	private TextView speedTextView = null;
	private SeekBar speedSeekBar = null;
	private Button okButton = null;
	private Button deleteButton = null;
	private Button cancelButton = null;

	private int id;
	private int index;
	private int mode;
	private int type = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.edit);
		
		patternEdit = (EditText) findViewById(R.id.PatternEdit);
		siteswapEdit = (EditText) findViewById(R.id.SiteswapEdit);
		motionEdit = (EditText) findViewById(R.id.MotionEdit);
		heightTextView = (TextView) findViewById(R.id.HeightLabel);
		heightSeekBar = (SeekBar) findViewById(R.id.HeightValue);
		dwellTextView = (TextView) findViewById(R.id.DwellLabel);
		dwellSeekBar = (SeekBar) findViewById(R.id.DwellValue);
		speedTextView = (TextView) findViewById(R.id.SpeedLabel);
		speedSeekBar = (SeekBar) findViewById(R.id.SpeedValue);
		okButton = (Button) findViewById(R.id.OkButton);
		deleteButton = (Button) findViewById(R.id.DeleteButton);
		cancelButton = (Button) findViewById(R.id.CancelButton);

		Bundle extras = getIntent().getExtras();
		mode = extras.getInt("MODE");
		if (mode == NEW) {
			type = 6;
			patternEdit.setText("");
			siteswapEdit.setText("");
			motionEdit.setText("");
			heightSeekBar.setProgress(getProgressValue(50));
			dwellSeekBar.setProgress(getProgressValue(50));

			deleteButton.setEnabled(false);
		} else {
			try {
				id = extras.getInt(Constant.EXTRAS_ID);
				JmPattern[] list = DaoFactory.getInstance().getDao().getFromId(id);
				if (list.length <= 0) {
					return;
				}
				JmPattern jp = list[0];
				patternEdit.setText(jp.getName());
				siteswapEdit.setText(jp.getSiteSwap().toString());
				motionEdit.setText(jp.motionToString());
				heightSeekBar.setProgress(getProgressValue(jp.getHeight()));
				dwellSeekBar.setProgress(getProgressValue(jp.getDwell()));

				DaoFactory f = DaoFactory.getInstance();
				index = extras.getInt("INDEX1");
				if (f.getMode() != DaoFactory.Mode.ANDROID || index != Menu1.INDEX6) {
					deleteButton.setEnabled(false);
				}
			} catch (JmException e) {
				showDialog(this, e);
				return;
			}

		}
		heightTextView.setText(String.valueOf(getTextValue(heightSeekBar
				.getProgress())));
		dwellTextView.setText(String.valueOf(getTextValue(dwellSeekBar
				.getProgress())));
		speedSeekBar.setProgress(getProgressValue(Resource.speed));
		speedTextView.setText(String.valueOf(getTextValue(speedSeekBar
				.getProgress())));

		setStatus();
		setListener();
	}

	private void setListener() {
		patternEdit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				return true;
			}
		});

		heightSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				heightTextView.setText(String.valueOf(getTextValue(arg1)));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		dwellSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				dwellTextView.setText(String.valueOf(getTextValue(arg1)));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		speedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				speedTextView.setText(String.valueOf(getTextValue(progress)));

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String siteswapString = siteswapEdit.getText().toString();
				if (siteswapString == null || siteswapString.length() <= 0) {
					showDialog(getString(R.string.siteswap_error),
							getString(R.string.siteswap) + "[" + siteswapString
									+ "]");
					return;
				}
				if (!JmPattern.setSiteSwap(siteswapString)) {
					showDialog(getString(R.string.siteswap_error),
							getString(R.string.siteswap) + "[" + siteswapString
									+ "]");
					return;
				}

				String patternString = patternEdit.getText().toString();
				if (patternString == null || patternString.length() <= 0) {
					patternString = siteswapString;
				}

				String motionString = motionEdit.getText().toString();
				if (motionString == null || motionString.length() <= 0) {
					motionString = "{13,0}{4,0}";
				}
				byte[][] motion = JmPattern.getMotion(motionString);
				if (motion == null || motion.length == 0
						&& motionString.length() > 0) {
					showDialog(getString(R.string.siteswap_error),
							getString(R.string.handling) + "[" + motionString
									+ "]");
					return;
				}

				JmPattern jp = new JmPattern(id, type, patternString,
						siteswapString, getTextValue(heightSeekBar
								.getProgress()), getTextValue(dwellSeekBar
								.getProgress()), motion);
				try {
					IDao<?> dao = DaoFactory.getInstance().getDao();
					if (mode == NEW) {
						int m = dao.max(type);
						dao.add(jp, m + 1);
					} else {
						if (dao.isWritable()){
							dao.set(jp);
						}
					}
					Resource.speed = getTextValue(speedSeekBar.getProgress());
				} catch (JmException e) {
					showDialog(this, e);
					return;
				}
				finish();
			}
		});

		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Edit.this);
				alertDialogBuilder.setTitle(getString(R.string.title_delete));
				alertDialogBuilder
						.setMessage(getString(R.string.message_delete_pattern));
				alertDialogBuilder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									DaoFactory.getInstance().getDao().delete(id);
									finish();
								} catch (JmException e) {
									showDialog(this, e);
									return;
								}
							}
						});
				alertDialogBuilder.setNeutralButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialogBuilder.setCancelable(true);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private int getTextValue(int val) {
		return val + 1;
	}

	private int getProgressValue(int val) {
		return val - 1;
	}
	
	private void setStatus() {
		IDao<?> dao = DaoFactory.getInstance().getDao();
		
		if (!dao.isWritable()){
			boolean b = false;
			
			patternEdit.setEnabled(b);
			siteswapEdit.setEnabled(b);
			motionEdit.setEnabled(b);
			heightSeekBar.setEnabled(b);
			dwellSeekBar.setEnabled(b);
		}
	}
}
