package com.jm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jm.PatternList;
import com.jm.utility.AsyncTaskIF;
import com.jm.utility.Debug;
import com.jm.utility.JmException;

public class InitDB implements AsyncTaskIF {
	private Context context;

	public InitDB(Context context) {
		this.context = context;
	}

	@Override
	public void doExecute() {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		PatternList p = new PatternList();

		boolean convFlag1to2 = helper.getConvFlag1to2();
		//boolean convFlag2to3 = helper.getConvFlag2to3();

		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			helper.setLangId(DatabaseHelper.NONE_ID);
			int langId = helper.getLangId();
	
			db.beginTransaction();
			
			Dao dao = Dao.getInstance();
			int count = dao.countAll();
			
//			if (helper.getConvFlag()){
//				// 1to2 or 2to3
//				helper.convert(db, langId);
//				Dao.getInstance().start(db);
//
//				// 1to2
//				helper.setLangId(DatabaseHelper.JAPANEASE_ID);
//				p.update();			
//			}
//			else {
//				// 0件 and 2
//				helper.setLangId(DatabaseHelper.JAPANEASE_ID);
//				p.create(DatabaseHelper.JAPANEASE_ID);				
//			}

			if (helper.getConvFlag()){
				// 1to2 or 2to3
				helper.convert(db, langId);
				Dao.getInstance().start(db);
			}
			if (convFlag1to2){
				// 1to2
				helper.setLangId(DatabaseHelper.JAPANEASE_ID);
				p.update();							
			}
			
			if (count <= 0 || convFlag1to2){
				if (count <= 0){
					// 0件 and 2
					helper.setLangId(DatabaseHelper.JAPANEASE_ID);
					p.create(DatabaseHelper.JAPANEASE_ID);				
				}
				
				// (0件 and 2) or 1to2
				helper.setLangId(DatabaseHelper.ENGLISH_ID);
				p.create(DatabaseHelper.ENGLISH_ID);
				
				helper.setLangId(DatabaseHelper.FRENCH_ID);
				p.create(DatabaseHelper.FRENCH_ID);
			}
			
			db.setTransactionSuccessful();
		} catch (JmException e) {
			// 失敗
			Debug.d(this, null, e);
			return;
		} finally {
			db.endTransaction();
		}
		helper.setLangId(DatabaseHelper.NONE_ID);		

		JmDao jdao = JmDao.getInstance();
		jdao.init(null, context);
	}

	@Override
	public void cancel() {

	}

	@Override
	public void done(boolean isCancel) {

	}

}
