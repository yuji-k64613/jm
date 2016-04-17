package com.jm;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jm.db.Dao;
import com.jm.db.DatabaseHelper;
import com.jm.utility.BaseActivity;
import com.jm.utility.JmException;

public class JuggleMaster extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			DatabaseHelper helper = new DatabaseHelper(this);
			SQLiteDatabase db = helper.getWritableDatabase();
			Dao dao = Dao.getInstance();
			int count = dao.count();
			dao.start(db);
			if (count <= 0) {
				new PatternList();
			}
		} catch (JmException e) {
			showDialog(this, e);
			return;
		} catch (Exception e) {
			showDialog(this, e);
			return;
		}

		Button startButton = (Button) findViewById(R.id.StartButton);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(JuggleMaster.this,
						(Class<?>) Menu1.class);
				startActivityForResult(intent, -1);
			}
		});
	}
}