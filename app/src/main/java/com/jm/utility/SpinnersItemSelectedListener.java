package com.jm.utility;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class SpinnersItemSelectedListener implements OnItemSelectedListener {
	private Spinner spinner1;
	private Spinner spinner2;
	private boolean flag;

	public SpinnersItemSelectedListener(Spinner spinner1, Spinner spinner2, boolean flag) {
		this.spinner1 = spinner1;
		this.spinner2 = spinner2;
		this.flag = flag;

		spinner1.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int arg2,
			long arg3) {
		int index1 = spinner1.getSelectedItemPosition();
		int index2 = spinner2.getSelectedItemPosition();

		if (flag && index1 > index2) {
			spinner2.setSelection(index1);
		}
		else if (!flag && index1 < index2) {
			spinner2.setSelection(index1);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}
