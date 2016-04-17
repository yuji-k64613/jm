package com.jm;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jm.common.Constant;
import com.jm.db.DaoFactory;
import com.jm.utility.BaseActivity;
import com.jm.utility.JmException;
import com.jm.utility.ListViewArrayAdapter;
import com.jm.utility.Resource;

public class Menu2 extends BaseActivity {
	private int index = -1;
	private ListView listView = null;
	private JmPattern[] list = null;
	private int topPosition = -1;
	private int topPositionY = 0;
	
	private static JmPattern[] listGen = null;

	public static void setList(JmPattern[] list){
		listGen = list;
	}
	
	public static JmPattern[] getList(){
		return listGen;
	}
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.menu2);

	    Bundle extras = getIntent().getExtras();
	    index = extras.getInt("INDEX");
		DaoFactory f = DaoFactory.getInstance();
	    if (f.getMode() == DaoFactory.Mode.ANDROID && index == Menu1.INDEX7){
	    	list = listGen;
	    }
	    else {
		    try {
		    	list = DaoFactory.getInstance().getDao().get(index);
		    	listGen = list; // 不要では
		    }
		    catch (JmException e){
		    	showDialog(this, e);	    	
		    	return;
		    }
	    }
	    
	    if (list == null){
	    	// 以下の手順でエラー発生
	    	// 1. パターン生成画面でパターンを生成する
			// 2. リスト画面を開く
	    	// 3. プロセスをバックグラウンドにする
	    	// 4. OSが他のアプリにメモリを割り当てるためにプロセスを終了する
	    	// 5. 再起動
	    	finish();
	    	return;
	    }
	    
		listView = (ListView)findViewById(R.id.Menu2);
		ListViewArrayAdapter adapter = new ListViewArrayAdapter(
				this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DaoFactory f = DaoFactory.getInstance();
				
				if (f.getMode() == DaoFactory.Mode.ANDROID && index == Menu1.INDEX6 && position == 0){
					Intent intent = new Intent(Menu2.this, (Class<?>)Edit.class);
					intent.putExtra("MODE", Edit.NEW);
					intent.putExtra(Constant.EXTRAS_ID, list[position].getId());
					startActivityForResult(intent, -1);				
				}
				else if (f.getMode() == DaoFactory.Mode.ANDROID && index == Menu1.INDEX7){
					initJuggleView();
					
					topPosition = listView.getFirstVisiblePosition();
					topPositionY = listView.getChildAt(0).getTop();
					
					Intent intent = new Intent(Menu2.this, (Class<?>)JuggleView.class);
					intent.putExtra("INDEX1", index);
					intent.putExtra("INDEX2", position);
					intent.putExtra(Constant.EXTRAS_ID, -1);
					intent.putExtra("PATTERN", (Serializable)list[position]);
					startActivityForResult(intent, -1);					
				}
				else {
					initJuggleView();
					
					topPosition = listView.getFirstVisiblePosition();
					topPositionY = listView.getChildAt(0).getTop();
					
					Intent intent = new Intent(Menu2.this, (Class<?>)JuggleView.class);
					intent.putExtra("INDEX1", index);
					intent.putExtra("INDEX2", position);
					intent.putExtra(Constant.EXTRAS_ID, list[position].getId());
					startActivityForResult(intent, -1);
				}
			}			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				DaoFactory f = DaoFactory.getInstance();

				if (f.getMode() == DaoFactory.Mode.ANDROID && index == Menu1.INDEX6 && position == 0){
					return false;
				}
				if (f.getMode() == DaoFactory.Mode.ANDROID && index == Menu1.INDEX7){
					return false;
				}
				Intent intent = new Intent(Menu2.this, (Class<?>)Edit.class);
				intent.putExtra("MODE", Edit.EDIT);
				intent.putExtra("INDEX1", index);
				intent.putExtra(Constant.EXTRAS_ID, list[position].getId());
				startActivityForResult(intent, -1);				
				return true;
			}
		});
    }
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		DaoFactory f = DaoFactory.getInstance();
		if (f.getMode() == DaoFactory.Mode.ANDROID && index == Menu1.INDEX7){
			list = listGen;
		}
		else if (index >= 0) {
			try {
				list = DaoFactory.getInstance().getDao().get(index);
			} catch (JmException e) {
				showDialog(this, e);
				return;
			}
		}
		ListViewArrayAdapter adapter = new ListViewArrayAdapter(
				this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		
		if (topPosition >= 0){
			// リスト選択位置復活
			listView.setSelectionFromTop(topPosition, topPositionY);
		}
	}

	private void initJuggleView(){
        Resource.cx = 0.0f;
        Resource.cy = 0.0f;
        Resource.counter = 0;
        
        JuggleView.init();
	}
}
