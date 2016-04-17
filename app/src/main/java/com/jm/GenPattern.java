package com.jm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jm.gen.MainGen;
import com.jm.utility.AsyncTaskCommand;
import com.jm.utility.BaseActivity;
import com.jm.utility.SpinnersItemSelectedListener;

public class GenPattern extends BaseActivity {
	private int MAX = 35;
	private int position = 2;
	private Button createButton;
	private Button cancelButton;
	private RadioGroup radioGroup;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	private Spinner spinnerBalls;
	private Spinner spinnerHeight;
	private Spinner spinnerPeriod1;
	private Spinner spinnerPeriod2;
	private Spinner spinnerMax;
	private MainGen gen;
	private AsyncTaskCommand command;
	private int mode = MainGen.SPT_NORMAL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.genpattern);

		createButton = (Button) findViewById(R.id.button71);
		createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doCreateButton();
			}
		});

		cancelButton = (Button) findViewById(R.id.button73);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		spinnerBalls = (Spinner) findViewById(R.id.spinner71);
		spinnerHeight = (Spinner) findViewById(R.id.spinner72);
		spinnerPeriod1 = (Spinner) findViewById(R.id.spinner73);
		spinnerPeriod2 = (Spinner) findViewById(R.id.spinner74);
		spinnerMax = (Spinner) findViewById(R.id.spinner75);

		new SpinnersItemSelectedListener(spinnerBalls, spinnerHeight, true);
		new SpinnersItemSelectedListener(spinnerHeight, spinnerBalls, false);

		new SpinnersItemSelectedListener(spinnerPeriod1, spinnerPeriod2, true);
		new SpinnersItemSelectedListener(spinnerPeriod2, spinnerPeriod1, false);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup71);
		radioGroup.check(R.id.radio71);
		radioButton1 = (RadioButton) findViewById(R.id.radio71);
		radioButton2 = (RadioButton) findViewById(R.id.radio72);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (radioButton1.isChecked()) {
							mode = MainGen.SPT_NORMAL;
						} else {
							mode = MainGen.SPT_SYNCHRO;
						}
					}
				});

		String[] item = new String[MAX];
		for (int i = 0; i < MAX; i++) {
			item[i] = "" + (i + 1);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerBalls.setAdapter(adapter);
		spinnerBalls.setSelection(position);

		spinnerHeight.setAdapter(adapter);
		spinnerHeight.setSelection(position);

		spinnerPeriod1.setAdapter(adapter);
		spinnerPeriod1.setSelection(0);

		spinnerPeriod2.setAdapter(adapter);
		spinnerPeriod2.setSelection(position);

		String[] max = { "10", "100", "500", "1000" };
		ArrayAdapter<String> adapterMax = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, max);
		adapterMax
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMax.setAdapter(adapterMax);
		spinnerMax.setSelection(1);
	}

	@Override
	protected void onStart() {
		super.onStart();

		setEnabled(true);
		Menu2.setList(null);
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (command != null) {
			command.stop();
			command = null;
		}
	}

	private void doCreateButton() {
		String ballsStr = spinnerBalls.getSelectedItem().toString();
		String heightStr = spinnerHeight.getSelectedItem().toString();
		String period1Str = spinnerPeriod1.getSelectedItem().toString();
		String period2Str = spinnerPeriod2.getSelectedItem().toString();
		String maxStr = spinnerMax.getSelectedItem().toString();

		int balls = Integer.parseInt(ballsStr);
		int height = Integer.parseInt(heightStr);
		int max = Integer.parseInt(maxStr);

		String periodStr = "" + period1Str + "-" + period2Str;

		gen = new MainGen(this, mode, balls, height, periodStr, max);
		command = new AsyncTaskCommand(this, gen,
				getString(R.string.calculating), true);
		command.execute((String[]) null);
		setEnabled(false);
	}

	public void createDone(boolean isCancel) {
		command = null;
		if (isCancel) {
			setEnabled(true);
			return;
		}
		JmPattern[] list = Menu2.getList();
		int size = list.length;

		// パターンが無い場合
		if (size <= 0) {
			Toast.makeText(this, R.string.message_nopattern, Toast.LENGTH_LONG)
					.show();
			setEnabled(true);
			return;
		}

		Intent intent = new Intent(GenPattern.this, (Class<?>) Menu2.class);
		intent.putExtra("INDEX", Menu1.INDEX7);
		startActivityForResult(intent, -1);
	}

	private void setEnabled(boolean flag) {
		radioGroup.setEnabled(flag);
		radioButton1.setEnabled(flag);
		radioButton2.setEnabled(flag);
		spinnerBalls.setEnabled(flag);
		spinnerHeight.setEnabled(flag);
		spinnerPeriod1.setEnabled(flag);
		spinnerPeriod2.setEnabled(flag);
		spinnerMax.setEnabled(flag);
		createButton.setEnabled(flag);
		cancelButton.setEnabled(flag);
	}
}
