package com.jm;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jm.common.Constant;
import com.jm.db.Dao;
import com.jm.db.PatternFile;
import com.jm.pref.EditPrefUtil;
import com.jm.utility.BaseActivity;
import com.jm.utility.Debug;
import com.jm.utility.JmException;

public class PatternManager extends BaseActivity {
	private enum Status {
		NORMAL, SELECTED, INPUT_FILENAME, UPDATE, NONE
	};

	private static int selectedIndex = -1;
	private EditPrefUtil pref = new EditPrefUtil(this);
	private Button patternAddButton;
	private Button patternUpdateButton;
	private Button patternDeleteButton;
	private Button patternCloseButton;
	private LinearLayout patternLayout;
	private TextView patternFileText;
	private ListView listView;
	private PatternFile[] list;
	private Status status = Status.NORMAL;
	private InputStream inputStream;
	private int selectedPatternIndex = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pattern_manager);

		patternAddButton = (Button) findViewById(R.id.patternAddButton);
		patternUpdateButton = (Button) findViewById(R.id.patternUpdateButton);
		patternDeleteButton = (Button) findViewById(R.id.patternDeleteButton);
		patternCloseButton = (Button) findViewById(R.id.patternCloseButton);
		listView = (ListView) findViewById(R.id.patternList);
		patternLayout = (LinearLayout) findViewById(R.id.patternLayout);
		patternFileText = (TextView) findViewById(R.id.patternFileText);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listViewOnItemClick(parent, view, position, id);
			}
		});

		patternAddButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				patternAddButtonOnClick();
			}
		});
		patternUpdateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				patternUpdateButtonOnClick();
			}
		});
		patternDeleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				patternDeleteButtonOnClick();
			}
		});
		patternCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				patternCloseButtonOnClick();
			}
		});
		patternFileText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				setStatus();
			}
		});
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		selectedIndex = pref.getInt(Constant.PREF_PATTERN_FILE_INDEX);
		setStatus((selectedIndex >= 0) ? Status.SELECTED : Status.NORMAL);
		updateList();

		selectedPatternIndex = pref
				.getInt(Constant.PREF_SELECTED_PATTERN_INDEX);
	}

	@Override
	protected void onStart() {
		super.onStart();

		selectedIndex = pref.getInt(Constant.PREF_PATTERN_FILE_INDEX);
	}

	@Override
	protected void onPause() {
		pref.put(Constant.PREF_PATTERN_FILE_INDEX, selectedIndex);
		pref.update();

		super.onPause();
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case 0:
			Uri uri = intent.getData();
			try {
				inputStream = getContentResolver().openInputStream(uri);
				patternFileText.setText("");
				setStatus(Status.INPUT_FILENAME);
			} catch (FileNotFoundException e) {
				Debug.d(this, e.getMessage());
			}
			break;
		}
	}

	private void updateList() {
		Dao dao = Dao.getInstance();
		updateList(dao);
	}

	private void updateList(Dao dao) {
		List<PatternFile> l = dao.getPatternFile();
		if (l == null) {
			return;
		}
		list = l.toArray(new PatternFile[0]);

		ArrayAdapter<PatternFile> adapter = new ArrayAdapter<PatternFile>(this,
				R.layout.simple_list_item_2, R.id.text3, list);
		listView.setAdapter(adapter);

		setStatus();
	}

	private void listViewOnItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		selectedIndex = position;
		setStatus(Status.SELECTED);
	}

	private void patternAddButtonOnClick() {
		switch (status) {
		case NORMAL:
		case SELECTED:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

			intent.setType("text/*");
			try {
				startActivityForResult(intent, 0);
			}
			catch (Exception e){
				// 起動失敗
				Debug.d(this, e.getMessage());
			}
			break;
		case INPUT_FILENAME:
			if (inputStream == null) {
				return;
			}
			String text = patternFileText.getText().toString();
			Dao dao = Dao.getInstance();
			dao.insertPatternList(inputStream, text, true);
			updateList();
			inputStream = null;
			selectedIndex = -1;
			setStatus(Status.NORMAL);
			break;
		default:
			break;
		}
	}

	private void patternUpdateButtonOnClick() {
		if (selectedIndex < 0) {
			return;
		}

		setStatus(Status.UPDATE);

		PatternFile p = list[selectedIndex];
		long pid = p.getId();

		Intent intent = new Intent();
		intent.putExtra(Constant.EXTRAS_ID, pid);
		setResult(RESULT_OK, intent);

		finish();
	}

	private void patternDeleteButtonOnClick() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getString(R.string.title_delete));
		alertDialogBuilder
				.setMessage(getString(R.string.message_delete_patternfile));
		alertDialogBuilder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (selectedIndex < 0) {
							return;
						}

						PatternFile p = list[selectedIndex];
						long pid = p.getId();

						Dao dao = Dao.getInstance();
						try {
							dao.deletePatternFile(pid);
							selectedIndex = -1;
						} catch (JmException e) {
							Debug.d(this, e.getMessage());
							return;
						}

						setStatus(Status.NORMAL);
						updateList();
					}
				});
		alertDialogBuilder.setNeutralButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setStatus(Status.SELECTED);
					}
				});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void patternCloseButtonOnClick() {
		finish();
	}

	private void setStatus() {
		setStatus(Status.NONE);
	}

	private void setStatus(Status s) {
		if (s != Status.NONE) {
			status = s;
		}

		switch (status) {
		case NORMAL:
			patternLayout.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setEnabled(true);
			patternAddButton.setEnabled(true);
			patternUpdateButton.setEnabled(false);
			patternDeleteButton.setEnabled(false);
			patternCloseButton.setEnabled(true);
			break;
		case SELECTED:
			patternLayout.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setEnabled(true);
			patternAddButton.setEnabled(true);
			patternUpdateButton.setEnabled(true);
			boolean b = false;
			if (list != null && selectedIndex >= 0) {
				PatternFile p = list[selectedIndex];
				long pid = p.getId();
				if (pid != selectedPatternIndex && p.isWritable()) {
					b = true;
				}
			}
			patternDeleteButton.setEnabled(b);
			patternCloseButton.setEnabled(true);
			break;
		case INPUT_FILENAME:
			patternLayout.setVisibility(View.VISIBLE);
			listView.setVisibility(View.INVISIBLE);
			listView.setEnabled(false);
			patternAddButton.setEnabled(patternFileText.getText().length() > 0);
			patternUpdateButton.setEnabled(false);
			patternDeleteButton.setEnabled(false);
			patternCloseButton.setEnabled(true);
			break;
		case UPDATE:
			patternLayout.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setEnabled(false);
			patternAddButton.setEnabled(false);
			patternUpdateButton.setEnabled(false);
			patternDeleteButton.setEnabled(false);
			patternCloseButton.setEnabled(false);
			break;
		default:
			break;
		}
	}

	public static void init(Context context) {
		EditPrefUtil pref = new EditPrefUtil(context);
		pref.put(Constant.PREF_PATTERN_FILE_INDEX, -1);
		pref.update();

		selectedIndex = -1;
	}
}
