package com.jm;

import java.util.Vector;
import com.jm.db.Dao;
import com.jm.db.DatabaseHelper;
import com.jm.utility.JmException;

public class PatternList {

//	public static JmPattern tempPattern = null;
//	public static final String[] PACKAGE_INDEX = {
//									"[1] 3ボール その1",
//									"[2] 3ボール その2",
//									"[3] 4ボール",
//									"[4] 5ボール",
//									"[5] 6ボール以上",
//									"[6] トレーニング",
//									"[7] マイパターン",
//									"[8] パターン生成"
//									};

// edit {
//	private boolean			overwrited;
//	private boolean			registerable;
//	private Vector			patterns;
//	private List 			patternList;
	private Vector<JmPattern> patterns;
// }
	private int idx = -1;

	public PatternList() {
		
	}
	
	public void create(int langId) throws JmException {
		//言語別設定追加
		
		//DatabaseHelper helper = DatabaseHelper.getInstance();
		//switch (helper.getLangId()){
		switch (langId){
		case DatabaseHelper.JAPANEASE_ID:
			setPackage00();
			setPackage01();
			setPackage02();
			setPackage03();
			setPackage04();
			setPackage05();
			setPackage06();
			break;
		case DatabaseHelper.FRENCH_ID:
			setPackage00Fr();			
			setPackage01Fr();
			setPackage02Fr();
			setPackage03Fr();
			setPackage04Fr();
			setPackage05Fr();
			setPackage06Fr();
			break;
		default:
			setPackage00En();			
			setPackage01En();
			setPackage02En();
			setPackage03En();
			setPackage04En();
			setPackage05En();
			setPackage06En();
			break;
		}
	}
	
	public void update() throws JmException {
//		DatabaseHelper helper = DatabaseHelper.getInstance();
//		switch (helper.getLangId()) {
//		case DatabaseHelper.JAPANEASE_ID:
//			setPackage06();
//			break;
//		default:
//			setPackage06En();
//			setPackage06();
//			break;
//		}
		setPackage06();
	}

	/*
    public PatternList(Runnable parent, int index) throws JmException {
// edit
		//super(parent);
		SiteSwap.clearCashe();
// edit {
//		this.overwrited = false;
//		this.registerable = false;
// }
		this.patterns = new Vector<JmPattern>();
// edit {
//		this.patternList = new List(PACKAGE_INDEX[index], List.IMPLICIT, 
//								new String[]{Resource.MSG_NOTHING}, null);
// }
		switch (index) {
		case 0 : setPackage00(); break;
		case 1 : setPackage01(); break;
		case 2 : setPackage02(); break;
		case 3 : setPackage03(); break;
		case 4 : setPackage04(); break;
		case 5 : setPackage05(); break;
		default : 
// edit {
//			loadPatterns();
//			this.registerable = true;
//			patternList.addCommand(Resource.CMD_MAKE);
//			patternList.addCommand(Resource.CMD_DELETE);
//			patternList.addCommand(Resource.CMD_PASTE);
// }
			break;
		}
// edit {
//		patternList.addCommand(Resource.CMD_PARAM);
//		patternList.addCommand(Resource.CMD_COPY);
//		setDisplayable(patternList);
// }
    }
	 */
	
// edit {
//	public void run() {
//		if (!patterns.isEmpty()) {
//	        int index = patternList.getSelectedIndex();
//	        int index = 0;
//	        patternList.set(index, get(index).getName(), null);
//		}
//		super.run();
//	}
// }
	
// edit {
//	public void commandAction(Command c, Displayable d) {
//        int index = patternList.getSelectedIndex();
//		
//		if (c == List.SELECT_COMMAND) {
//			if (!patterns.isEmpty())
//				new JugglerViewer(this, get(index)).run();
//		} else if (c == Resource.CMD_PARAM) {
//			if (!patterns.isEmpty()) {
//				new JmParameters(this, get(index), false).run();
//				overwrited = true;
//			}
//		} else if (c == Resource.CMD_MAKE) {
//			new JmParameters(this, new JmPattern("", "3"), true).run();
//		} else if (c == Resource.CMD_DELETE) {
//			delete(index);
//		} else if (c == Resource.CMD_COPY) {
//			if (!patterns.isEmpty())
//				tempPattern = get(index);
//		} else if (c == Resource.CMD_PASTE) {
//			if (tempPattern != null)
//				insert(new JmPattern(tempPattern));
//		} else {
//			if (c == BaseDisplay.CMD_BACK && registerable && overwrited)
//				storePatterns();
//			super.commandAction(c, d);
//		}
//    }
// }
	
// edit {
//	public boolean insert(JmPattern jp) {
//		if (jp.getSiteSwap() == null) return false;
//        int index = patternList.getSelectedIndex();
//		if (patterns.isEmpty())	patternList.delete(0);
//		patternList.insert(index, jp.getName(), null);
//		patterns.insertElementAt(jp, index);
//		overwrited = true;
//		return true;
//	}
// }
	
// edit {
//    private JmPattern get(int index) {
	public JmPattern get(int index) {
	// private JmPattern get(int index) {
// }
		return (JmPattern)patterns.elementAt(index);
	}

// edit
	public int size(){
		return patterns.size();
	}
	
	private void clearIndex(){
		idx = 0;
	}
	
	private void add(JmPattern jp) throws JmException {
		if (jp.getSiteSwap() == null) return;
// edit {
//		if (patterns.isEmpty())	patternList.delete(0);
//		patternList.append(jp.getName(), null);
// }
//		patterns.addElement(jp);
		Dao.getInstance().addNT(jp, idx++);
// }
	}

//	private void addJp(JmPattern jp) throws JmException {
//		if (jp.getSiteSwap() == null) return;
//		Dao.getInstance().addNT(jp, DatabaseHelper.JAPANEASE_ID, idx++);
//	}

//	private int count(int type) throws JmException {
//		return Dao.getInstance().count(type);		
//	}
	
// edit {
//	private boolean delete(int index) {
//		if (patterns.isEmpty()) return false;
//		patternList.delete(index);
//		patterns.removeElementAt(index);
//		if (patterns.isEmpty())
//			patternList.append(Resource.MSG_NOTHING, null);
//		overwrited = true;
//		return true;
//	}
// }
	
	/*********************************************************************
	 可能ならば保存した設定を読み込みます。
	 *********************************************************************/
// edit {
//	private void loadPatterns() {
//		try {
//			RecordStore store = RecordStore.openRecordStore(Resource.JMME_RECORD_NAME, false);
//			int num = (int)store.getRecord(2)[0] & 0xff;
//			for (int i = 0; i < num; i++)
//				add(new JmPattern(store.getRecord(i+3)));
//			store.closeRecordStore();
//		} catch (RecordStoreException ex) {
//		}
//	}
// }
	
	/*********************************************************************
	 可能ならば設定を保存します。
	 *********************************************************************/
// edit {
//	private boolean storePatterns() {
//		try {
//			RecordStore store = RecordStore.openRecordStore(Resource.JMME_RECORD_NAME, true);
//			byte[] b_num = new byte[1];
//			b_num[0] = (byte)(patterns.size() & 0xff);
//			try {
//				store.setRecord(2, b_num, 0, 1);
//			} catch (RecordStoreException ex) {
//				store.addRecord(b_num, 0, 1);
//			}
//			int num = 0;
//			for (Enumeration en = patterns.elements(); en.hasMoreElements(); num++) {
//				byte[] bytes = ((JmPattern)en.nextElement()).getBytes();
//				try {
//					store.setRecord(num+3, bytes, 0, bytes.length);
//				} catch (RecordStoreException ex) {
//					store.addRecord(bytes, 0, bytes.length);
//				}
//			}
//			b_num[0] = (byte)(num & 0xff);
//			store.setRecord(2, b_num, 0, 1);
//			store.closeRecordStore();
//		} catch (RecordStoreException ex) {
//			return false;
//		}
//		return true;
//	}
// }
	
	public static final byte[] n = JmPattern.NORMAL;
	public static final byte[] r = JmPattern.reverse(n);
	public static final byte[] a = {0,0,0,0};
	public static final byte[] b = {10,0,10,0};
	public static final byte[] c = {12,0,12,0};
	public static final byte[] d = {6,0,6,0};
	public static final byte[] e = {5,0,5,0};
	public static final byte[] f = {4,0,4,0};
	public static final byte[] g = {13,0,13,0};
	public static final byte[] h = {10,0,3,0};
	public static final byte[] k = {-1,0,-12,0};
	public static final byte[] o = {7,0,7,0};

	public static final byte[][] reverse = new byte[][] {r};
	public static final byte[][] center = new byte[][] {{13,0,0,0}};
	public static final byte[][] shower = new byte[][] {{5,0,10,0},{10,0,5,0}};
	public static final byte[][] millsmess = new byte[][] {k,{0,0,12,0},{1,0,-12,0}};
	public static final byte[][] columns = new byte[][] {c,f,f,c};

	public void setPackage00En() throws JmException {
		clearIndex();
// edit {
		int type = 0;
// }
		add(new JmPattern(type, "Cascade", "3"));
		add(new JmPattern(type, "Flash", "333355500", 30, 50));
		add(new JmPattern(type, "Tennis", "3", new byte[][] {{8,0,4,0},{8,0,4,0},{14,3,14,3}}));
		add(new JmPattern(type, "Half-shower", "3", new byte[][] {{8,0,15,8},{15,8,8,0}}));
		add(new JmPattern(type, "Reverse cascade", "3", reverse));
		add(new JmPattern(type, "Windwill", "3", new byte[][] {{10,0,-10,-2},{-10,4,10,2}}));
		add(new JmPattern(type, "Reach over", "3", 15, 50, new byte[][] {n,n,n,g,n,{10,0,8,-2},{-13,5,0,2},{13,-3,4,0}}));
		add(new JmPattern(type, "Reach under", "3", 15, 50, new byte[][] {n,n,n,g,{13,0,4,-3},{10,4,4,4},{-16,-3,0,0},{13,3,4,0}}));
		add(new JmPattern(type, "Over head", "3", new byte[][] {{12,19,3,19}}));
		add(new JmPattern(type, "Axes", "(2,4x)(4x,2)", 12, 50, new byte[][] {{4,9,-10,-3},{10,6,7,9},{10,6,7,9},{4,9,-10,-3}}));
		add(new JmPattern(type, "Eat the apple", "33333423", new byte[][] {{0,6,-3,4},n,n,n,n,{13,0,2,0},{12,7,0,7},{13,0,-4,0}}));
		add(new JmPattern(type, "1 up 2 up", "(0,4)(4,4)", 14, 50, new byte[][] {a,b,b,b}));
		add(new JmPattern(type, "Yo-yo over", "(4,2)", 13, 50,  new byte[][] {{5,2,5,3},{-5,-1,-5,0},{5,13,5,13},{5,-1,5,0}}));
		add(new JmPattern(type, "Yo-yo under", "(4,2)", 13, 50, new byte[][] {{5,-1,5,0},{-5,3,-5,4},{5,10,5,10},{5,3,5,4}}));
		add(new JmPattern(type, "One level for each", "3", 13, 50, new byte[][] {{20,10,20,10},b,{15,5,15,5},{20,10,20,10},b,{15,5,15,5}}));
		add(new JmPattern(type, "Statue of Liberty", "3", 12, 50, new byte[][] {{3,0,12,4},{10,20,9,19}}));
		add(new JmPattern(type, "Shuffle", "(4x,2x)", 12, 50, new byte[][] {{0,-5,12,10},{10,0,14,0}}));
		add(new JmPattern(type, "Luke Shuffle", "(4,2x)(2x,4)", 15, 50, new byte[][] {{10,0,14,10},{10,-3,6,0},{10,-3,6,0},{10,0,14,10}}));
		add(new JmPattern(type, "Robot 1/2", "242334", 15, 50, new byte[][] {{5,4,10,-3},b,{10,-3,10,13},a,{10,13,-10,13},d}));
		add(new JmPattern(type, "Robot", "3", 10, 50, new byte[][] {{7,12,-7,12},{7,0,-7,0}}));
		add(new JmPattern(type, "Shower", "51", 10, 50, shower));
		add(new JmPattern(type, "High Shower", "51"));
		add(new JmPattern(type, "Box", "(2x,4)(4,2x)", 12, 50, new byte[][] {b,{10,0,2,0},{10,0,2,0},b}));
		add(new JmPattern(type, "Weave", "(2,4)(2,4x)(4,2)(4x,2)", 10, 50, new byte[][] {{10,1,-7,0},{12,0,12,9},e,{-5,9,0,3},{12,0,12,9},{10,1,-7,0},{-5,9,0,3},e}));
		add(new JmPattern(type, "Follow", "423", 10, 50, new byte[][] {{-10,10,-10,4},{10,-3,10,10},{0,4,0,2}}));
		add(new JmPattern(type, "Boston Mess A", "3", new byte[][] {{10,-2,10,-2},{10,3,10,3},{0,-2,0,-2},{-10,3,-10,3},{-10,-2,-10,-2},{0,3,0,3}}));
		add(new JmPattern(type, "Boston Mess B", "3", new byte[][] {{10,3,10,-2},{10,3,10,3},{0,-2,0,-2},{-10,3,-10,3},{-10,-2,-10,-2},{0,3,0,3},{10,-2,10,3},{10,-2,10,-2},{0,3,0,3},{-10,-2,-10,-2},{-10,3,-10,3},{0,-2,0,-2}}));
		add(new JmPattern(type, "Mill's mess", "3", 13, 50, millsmess));
		add(new JmPattern(type, "Reverse Mill's mess", "3", 13, 50, new byte[][] {{-12,0,-1,0},{12,0,0,0},{-12,0,1,0}}));
		add(new JmPattern(type, "Barks", "423", 10, 50, new byte[][] {{12,12,-6,-2},{-6,5,-6,12},{0,5,0,5}}));
		add(new JmPattern(type, "Rubenstein's Revenge", "35223", 15, 50, new byte[][] {{3,1,13,8},{7,-2,-10,-3},{3,6,-12,2},{-1,0,12,2},{-2,2,-10,-2}}));
		add(new JmPattern(type, "One hand", "60"));
		add(new JmPattern(type, "One hand cascade", "60", new byte[][] {{-6,0,2,0},g,{18,0,10,0},g}));
		add(new JmPattern(type, "One hand multiplexe 2b", "[46]06020"));
		add(new JmPattern(type, "One hand two stages", "8040"));
	}
		
	public void setPackage01En() throws JmException {
		clearIndex();
// edit {
		int type = 1;
// }
		add(new JmPattern(type, "531"));
		add(new JmPattern(type, "450"));
		add(new JmPattern(type, "5241"));
		add(new JmPattern(type, "51414"));
		add(new JmPattern(type, "72312"));
		add(new JmPattern(type, "Wide Cascade", "3", 12, 50, new byte[][] {{21,9,18,4}}));
		add(new JmPattern(type, "Cascade colonnes throw", "3", new byte[][] {{-8,0,12,0}}));
		add(new JmPattern(type, "Continuous Reach over", "3", 15, 50, new byte[][] {{10,0,3,-2},{-13,5,13,0},{13,-3,4,0}}));
		add(new JmPattern(type, "Continuous Reach under", "3", 15, 50, new byte[][] {{10,4,3,4},{-13,-5,13,0},{13,3,4,0}}));
		add(new JmPattern(type, "Cross arms", "3", new byte[][] {{-4,0,-12,0}}));
		add(new JmPattern(type, "Eat all the apple", "(2,4x)(4x,2)", 12, 50, new byte[][] {{0,7,-3,-3},{12,10,0,8},{12,10,0,8},{0,7,-3,-3}}));
		
		add(new JmPattern(type, "1-up 2-up B", "(4,4)(0,4x)(4,4)(4x,0)", 14, 50, new byte[][] {o,o,{14,0,0,0},o,o,o,o,{14,0,0,0}}));
		add(new JmPattern(type, "1-up 2-up C", "(4,4)(0,4x)(4,4)(4,0)(4,4)(4x,0)(4,4)(0,4)", 14, 50, new byte[][] {o,o,{14,0,14,0},o,o,o,o,a,o,o,o,{14,0,14,0},o,o,a,o}));
		add(new JmPattern(type, "1-up 2-up D", "(4,4)(0,4x)(4,4)(4x,0)", 14, 50, new byte[][] {{12,0,-6,0},{-6,0,12,0},a,d,{-6,0,12,0},{12,0,-6,0},d,a}));
		add(new JmPattern(type, "1-up 2-up E", "(4,4)(0,4)", 14, 50, new byte[][] {f,f,{13,0,-13,0},f}));
		add(new JmPattern(type, "1-up 2-up F", "(6,6)(0,2x)(4x,0)", 10, 50, new byte[][] {f,f,{13,7,4,0},f,f,{4,0,13,7}}));

		byte[] fake1 = {10,-1,10,0};
		byte[] fake2 = {0,-1,0,0};
		byte[] fake3 = {10,10,10,10};
		add(new JmPattern(type, "Fake A", "(2,4)", 14, 50, new byte[][] {fake1,fake1,fake2,fake3}));
		add(new JmPattern(type, "Fake B", "(2,4)", 14, 50, new byte[][] {fake2,fake1,fake1,fake3}));
		add(new JmPattern(type, "Fake both side", "(4,2)(4x,2)(2,4)(2,4x)", 13, 50, new byte[][] {fake1,fake1,fake3,fake2,fake1,fake1,fake2,fake3}));
		add(new JmPattern(type, "Around the yo-yo", "(4,2)", 13, 50, new byte[][] {{0,2,0,3},fake2,{0,13,0,13},{10,-1,-10,-1}}));
		add(new JmPattern(type, "Yo-yo (Tornado)", "(2,4)", new byte[][] {e,{15,15,-15,14},{-5,0,-5,0},{15,14,-15,15}}));
		add(new JmPattern(type, "Yo-yo (crossing)", "(2,4)", new byte[][] {e,{15,12,0,12},{-5,0,-5,0},{-15,12,0,12}}));
		add(new JmPattern(type, "(4x,2x)(2,4)"));
		add(new JmPattern(type, "(4x,6)(0,2x)"));
		add(new JmPattern(type, "Slow Tennis", "(2,4)(2,4x)(4,2)(4x,2)", 14, 50, new byte[][] {e,e,{15,3,15,3},e,e,e,e,{15,3,15,3}}));
		add(new JmPattern(type, "Cascade Statue of Liberty", "3", 14, 50, new byte[][] {{3,4,12,0},{10,17,9,19}}));
		add(new JmPattern(type, "441", 13, 50));
		add(new JmPattern(type, "Reverse 441", "441", 13, 50, new byte[][] {r,r,{4,0,0,0}}));
		add(new JmPattern(type, "441 Shuffle", "441", 10, 50, new byte[][] {{5,-2,5,-2},d,{15,4,9,10}}));
		add(new JmPattern(type, "Boss Side Slam", "(4x,2x)(2,4x)(2x,4x)(4x,2)", 10, 50, new byte[][] {{6,0,12,10},b,{10,0,4,0},d,b,{6,0,12,10},d,{10,0,4,0}}));
		add(new JmPattern(type, "Robot B", "2334", 13, 50, new byte[][] {{0,11,10,11},{10,0,0,0},{0,12,-10,12},{10,0,0,0}}));

		byte[] eb = {3,0,10,0};
		add(new JmPattern(type, "Slow Half-Shower", "5223", shower));
		add(new JmPattern(type, "Half-Shower under the arm", "(2x,4x)", new byte[][] {{5,0,-10,0},{3,4,-3,4}}));
		add(new JmPattern(type, "Half-Shower under both arm", "(2x,4x)", new byte[][] {eb,{5,4,0,4},{3,0,-10,0},{5,4,0,4}}));
		add(new JmPattern(type, "Synchro shower", "(2x,4x)", shower));
		add(new JmPattern(type, "7131 Shower", "7131", shower));
		add(new JmPattern(type, "(2x,2x) Shower", "(2x,6x)(2x,2x)", 15, 50, new byte[][] {{6,0,12,3},{6,3,6,0},{6,0,6,3},{12,3,6,0}}));
		add(new JmPattern(type, "315171 Shower", "315171", shower));
		add(new JmPattern(type, "Hafumesu", "3", 13, 50, new byte[][] {c,{0,0,-12,-3},{0,2,-12,2},{0,-3,-6,0}}));
		add(new JmPattern(type, "423 Mill's Mess", "423", millsmess));
		add(new JmPattern(type, "414 Mill's Mess", "414", millsmess));
		add(new JmPattern(type, "315 Mill's Mess", "315", millsmess));
		add(new JmPattern(type, "44133 Mill's Mess box", "44133", 13, 50, new byte[][] {{-2,0,-12,5},{2,-2,12,0},{0,2,-3,0},{10,0,10,2},{7,-2,-10,-3}}));
		add(new JmPattern(type, "Mill's Mess box", "612", millsmess));
		add(new JmPattern(type, "126 Box", "126", 15, 50));
		add(new JmPattern(type, "630 Box", "630", 15, 50));
		add(new JmPattern(type, "Extend Box", "(4x,2x)(4,2x)(2x,4x)(2x,4)", 11, 50, new byte[][] {{14,0,7,0},a,{14,0,0,0},{0,0,14,0},a,{14,0,7,0},{0,0,14,0},{14,0,0,0}}));
		add(new JmPattern(type, "2 Floors Box", "(2x,8)(2x,4)(0,2x)(8,2x)(4,2x)(2x,0)", new byte[][] {eb,h,eb,h,h,eb,h,eb,h,eb,eb,h}));
	}
	
	public void setPackage02En() throws JmException {
		clearIndex();
// edit {
		int type = 2;
// }
		add(new JmPattern(type, "Fountaine", "4"));
		add(new JmPattern(type, "Synchro Fountain", "(4,4)"));
		add(new JmPattern(type, "Reverse Fountain", "4", reverse));
		add(new JmPattern(type, "Reverse Synchro Fountain", "(4,4)", reverse));
		add(new JmPattern(type, "4b Columns switch", "(4x,4x)(4,4)", new byte[][] {{12,0,12,2},{12,2,12,0},f,f}));
		add(new JmPattern(type, "Columns 4 piston", "4", columns));
		add(new JmPattern(type, "4b Columns synchro (asymmetric)", "(4,4)", columns));
		add(new JmPattern(type, "4b Columns synchro (symmetric)", "(4,4)", new byte[][] {c,c,f,f}));
		add(new JmPattern(type, "4b Columns synchro (Splits)", "(4,4)", new byte[][] {c,{-4,0,-4,0},{-4,0,-4,0},c}));
		byte[] cross1 = {13,0,7,3};
		byte[] cross2 = {13,3,7,0};
		add(new JmPattern(type, "4 Cross A", "(4x,4x)", new byte[][] {cross1,cross2}));
		add(new JmPattern(type, "4 Cross B", "(4x,4x)", new byte[][] {cross1,cross2,cross2,cross1}));
		add(new JmPattern(type, "4 Tennis", "44453",	 new byte[][] {{9,0,4,0},{9,0,4,0},{0,0,4,0},{15,3,15,3},{9,0,4,0}}));
		add(new JmPattern(type, "444447333", 30, 50));
		add(new JmPattern(type, "53"));
		add(new JmPattern(type, "561"));
		add(new JmPattern(type, "453"));
		add(new JmPattern(type, "741"));
		add(new JmPattern(type, "6424"));
		add(new JmPattern(type, "61616"));
		add(new JmPattern(type, "7161616"));
		add(new JmPattern(type, "7272712"));
		add(new JmPattern(type, "(2x,6x)(6x,2x)"));
		add(new JmPattern(type, "(2,6x)(2x,6)(6x,2)(6,2x)"));
		add(new JmPattern(type, "(2,4)([44x],2x)"));

		add(new JmPattern(type, "4b Half-shower", "53", shower));
		add(new JmPattern(type, "4b shower", "71", shower));
		add(new JmPattern(type, "4b Synchro shower", "(2x,6x)", shower));
		add(new JmPattern(type, "9151 Shower", "9151", shower));

		add(new JmPattern(type, "4b box", "(2x,8)(2x,4)(8,2x)(4,2x)", new byte[][] {b,h,b,h,h,b,h,b}));
		add(new JmPattern(type, "A pseudo four box", "633", new byte[][] {b,{7,0,0,0},{7,0,0,0}}));
		add(new JmPattern(type, "B 4 box mock", "(2x,6)(2x,6)(6,2x)(6,2x)"));

		add(new JmPattern(type, "4 Mill's mess", "4", millsmess));
		add(new JmPattern(type, "Mill's mess 534", "534", millsmess));
		add(new JmPattern(type, "Mill's mess 552", "552", millsmess));
		add(new JmPattern(type, "Mill's mess 642", "642", millsmess));
		add(new JmPattern(type, "4 Barks Barrage", "525", 12, 50, new byte[][] {{14,14,-5,-2},{-9,3,-9,14},{0,0,3,0}}));

		add(new JmPattern(type, "MultiPlex [34]1", "[34]1"));
		add(new JmPattern(type, "MutiPlex 4[43]1", "4[43]1"));
		add(new JmPattern(type, "Mill's mess MultiPlex", "[34]23", millsmess));
		add(new JmPattern(type, "Wind Mill's mess", "3[53]22[32]", 15, 50, new byte[][] {{3,1,13,8},{7,-2,-10,-3},{3,6,-12,2},{-1,0,12,2},{-2,2,-10,-2}}));
	}
	
	public void setPackage03En() throws JmException {
		clearIndex();
// edit {
		int type = 3;
// }
		add(new JmPattern(type, "5 Cascade", "5"));
		add(new JmPattern(type, "5 Reverse cascade", "5", reverse));
		add(new JmPattern(type, "555555744", 30, 50));
		add(new JmPattern(type, "2 Left 3 right", "64"));
		add(new JmPattern(type, "744"));
		add(new JmPattern(type, "753"));
		add(new JmPattern(type, "66661"));
		add(new JmPattern(type, "88333"));
		add(new JmPattern(type, "75751"));
		add(new JmPattern(type, "123456789", center));

		add(new JmPattern(type, "Half Shower 5", "5", new byte[][] {{8,0,15,8},{15,8,8,0}}));
		add(new JmPattern(type, "Half Shower 73", "73", shower));
		add(new JmPattern(type, "Half Shower Synchro", "(4x,6x)", shower));
		add(new JmPattern(type, "5 Shower", "91", shower));
		add(new JmPattern(type, "[97]121 Shower", "[97]121", shower));
		add(new JmPattern(type, "(4x,6)(6,4x)"));

		add(new JmPattern(type, "5 Mill's mess", "5", millsmess));
		add(new JmPattern(type, "5 Barks Barrage", "726", 12, 50, new byte[][] {{16,14,-3,-4},{-9,0,-10,14},{2,-2,4,0}}));
		add(new JmPattern(type, "5 Columns", "(6,6)(6,6)(0,6)", new byte[][] {c,c,d,d,a,d}));
		add(new JmPattern(type, "5 Boston mess A", "5", new byte[][] {{-12,0,-12,0},{-6,0,-6,0},a,d,c,c,d,a,{-6,0,-6,0},{-12,0,-12,0}}));
		add(new JmPattern(type, "5 Boston mess B", "5", new byte[][] {c,d,d,c,a,{-12,0,-12,0},{-6,0,-6,0},{-6,0,-6,0},{-12,0,-12,0},a}));
		add(new JmPattern(type, "5 Boston mess C", "5", new byte[][] {{12,3,12,3},{-6,0,-6,0},{0,3,0,3},d,{-12,3,-12,3},{-12,0,-12,0},{6,3,6,3},a,{-6,3,-6,3},c}));
		add(new JmPattern(type, "5 Multiplex columns", "([46],[46])(0,6)(2,2)", new byte[][] {g,g,a,g,g,g}));

		add(new JmPattern(type, "Martin", "[62]25", new byte[][] {g,n,n}));
		add(new JmPattern(type, "25[75]51"));
		add(new JmPattern(type, "(2,[62])([22],6x)([62],2)(6x,[22])"));
		add(new JmPattern(type, "Multiplex [54][22]2", "[54][22]2"));
		add(new JmPattern(type, "Multiplex 24[54]", "24[54]"));
	}
	
	public void setPackage04En() throws JmException {
		clearIndex();
// edit {
		int type = 4;
// }
		add(new JmPattern(type, "6 Fountain", "6"));
		add(new JmPattern(type, "6 Synchro Fountain", "(6,6)"));
		add(new JmPattern(type, "7 Cascade", "7"));
		add(new JmPattern(type, "8 Fountain", "8"));
		add(new JmPattern(type, "9 Cascade", "9"));

		add(new JmPattern(type, "6 Half-shower", "75", shower));
		add(new JmPattern(type, "6 Shower", "b1", shower));
		add(new JmPattern(type, "7 Shower", "d1", shower));

		add(new JmPattern(type, "6 Columns", "(6,6)",
		 new byte[][] {{15,0,15,0},{15,0,15,0},{9,0,9,0},{9,0,9,0},{3,0,3,0},{3,0,3,0}}));
		add(new JmPattern(type, "Mill's mess 6b", "6", millsmess));
		add(new JmPattern(type, "Mill's mess 864 6b", "864", millsmess));

		add(new JmPattern(type, "Multiplexe 7b [456][22]2", "[456][22]2"));
		add(new JmPattern(type, "Multiplexe 7b fake6", "([66x],2)(2,[66x])", center));
		add(new JmPattern(type, "26[76]"));
		add(new JmPattern(type, "[234]57"));

		add(new JmPattern(type, "9 Multiplexe [54]", "[54]"));
		add(new JmPattern(type, "35 Cascade", "z"));
		add(new JmPattern(type, "18 Shower", "z1", shower));
		add(new JmPattern(type, "35 Multiplexe shower", "[9bdfh][11111]", shower));
		add(new JmPattern(type, "Mill's mess 12b 333666999cccfffiiilll", "333666999cccfffiiilll", millsmess));
		add(new JmPattern(type, "[b9753]0020[22]0[222]0[2222]0"));
		add(new JmPattern(type, "123456789abcdefghijklmnopqrstuvwxyz", center));

		byte[] box1 = {13,0,3,0};
		byte[] box2 = JmPattern.reverse(box1);
		add(new JmPattern(type, "9b Box", "u1q1m1i1e1a1612",
		 new byte[][] {g,box1,box2,box1,box2,box1,box2,box1,box2,box1,box2,box1,box2,box1,g}));
		add(new JmPattern(type, "trpnljhfdb97531"));
		add(new JmPattern(type, "Ken", "ken"));
		add(new JmPattern(type, "MultiPlexe [56789]", "[56789]"));
		add(new JmPattern(type, "20 Columns A", "(k,k)", 10, 50,
		 new byte[][] {{29,3,29,3},{2,3,2,3},{26,3,26,3},{5,3,5,3},{23,3,23,3},{8,3,8,3},{20,3,20,3},{11,3,11,3},{17,3,17,3},{14,3,14,3},{14,3,14,3},{17,3,17,3},{11,3,11,3},{20,3,20,3},{8,3,8,3},{23,3,23,3},{5,3,5,3},{26,3,26,3},{2,3,2,3},{29,3,29,3}}));
		add(new JmPattern(type, "20 Columns B", "k", 10, 50,
		 new byte[][] {{20,3,20,3},{18,3,18,3},{16,3,16,3},{14,3,14,3},{12,3,12,3},{10,3,10,3},{8,3,8,3},{6,3,6,3},{4,3,4,3},{2,3,2,3},{2,3,2,3},{4,3,4,3},{6,3,6,3},{8,3,8,3},{10,3,10,3},{12,3,12,3},{14,3,14,3},{16,3,16,3},{18,3,18,3},{20,3,20,3}}));
	}

	public void setPackage05En() throws JmException {
		clearIndex();
// edit {
		int type = 5;
// }
		add(new JmPattern(type, "Throw one", "300"));
		add(new JmPattern(type, "Throw two", "33022"));
		add(new JmPattern(type, "Continuous throw two", "330"));
		add(new JmPattern(type, "Flash", "[32]3322"));
		add(new JmPattern(type, "Slow cascade", "3", 20, 75));
		add(new JmPattern(type, "Four hand practice", "40"));
		add(new JmPattern(type, "Columns four hand practice", "40", new byte[][] {a,g,b,g}));
		add(new JmPattern(type, "Two of the five practice", "50500"));
		add(new JmPattern(type, "3 Practice five Chase", "50505"));
		add(new JmPattern(type, "3 Flash five practice", "55500"));
		add(new JmPattern(type, "Three of the five practice", "52512"));
		add(new JmPattern(type, "Practice five 4 Flash", "[52][52]55022[22][22]"));
		add(new JmPattern(type, "A gap of five practice", "55550"));
		add(new JmPattern(type, "Practice of five B gap", "552"));
		add(new JmPattern(type, "Four of the five practice", "5551"));
		add(new JmPattern(type, "5 flash", "[522][52][52]5522[22][22]"));
		add(new JmPattern(type, "3 -> 5Cascade", "[32][32][32][32][32][32][52][52][52]555555522"));

		add(new JmPattern(type, "Practice 3 box", "(2x,4)(0,2x)", 12, 50, new byte[][] {b,{10,0,2,0},{10,0,2,0},b}));
		add(new JmPattern(type, "Practice 3 Mill's mess", "330", 13, 50, millsmess));
	}
	
	public void setPackage06En() throws JmException {
		clearIndex();
		int type = 6;
		add(new JmPattern(type, "[New]", "1"));
	}

	public void setPackage00Fr() throws JmException {
		clearIndex();
// edit {
		int type = 0;
// }
		add(new JmPattern(type, "Cascade", "3"));
		add(new JmPattern(type, "Flash", "333355500", 30, 50));
		add(new JmPattern(type, "Tennis", "3", new byte[][] {{8,0,4,0},{8,0,4,0},{14,3,14,3}}));
		add(new JmPattern(type, "Demi-Douche", "3", new byte[][] {{8,0,15,8},{15,8,8,0}}));
		add(new JmPattern(type, "Cascade inversée", "3", reverse));
		add(new JmPattern(type, "Moulin", "3", new byte[][] {{10,0,-10,-2},{-10,4,10,2}}));
		add(new JmPattern(type, "Par-dessus", "3", 15, 50, new byte[][] {n,n,n,g,n,{10,0,8,-2},{-13,5,0,2},{13,-3,4,0}}));
		add(new JmPattern(type, "Par en dessous", "3", 15, 50, new byte[][] {n,n,n,g,{13,0,4,-3},{10,4,4,4},{-16,-3,0,0},{13,3,4,0}}));
		add(new JmPattern(type, "Au dessus de la tête", "3", new byte[][] {{12,19,3,19}}));
		add(new JmPattern(type, "Haches", "(2,4x)(4x,2)", 12, 50, new byte[][] {{4,9,-10,-3},{10,6,7,9},{10,6,7,9},{4,9,-10,-3}}));
		add(new JmPattern(type, "Manger la pomme", "33333423", new byte[][] {{0,6,-3,4},n,n,n,n,{13,0,2,0},{12,7,0,7},{13,0,-4,0}}));
		add(new JmPattern(type, "Collonne", "(0,4)(4,4)", 14, 50, new byte[][] {a,b,b,b}));
		add(new JmPattern(type, "Yo-yo dessus", "(4,2)", 13, 50,  new byte[][] {{5,2,5,3},{-5,-1,-5,0},{5,13,5,13},{5,-1,5,0}}));
		add(new JmPattern(type, "Yo-yo dessous", "(4,2)", 13, 50, new byte[][] {{5,-1,5,0},{-5,3,-5,4},{5,10,5,10},{5,3,5,4}}));
		add(new JmPattern(type, "Un niveau pour chacune", "3", 13, 50, new byte[][] {{20,10,20,10},b,{15,5,15,5},{20,10,20,10},b,{15,5,15,5}}));
		add(new JmPattern(type, "Statue de la liberté", "3", 12, 50, new byte[][] {{3,0,12,4},{10,20,9,19}}));
		add(new JmPattern(type, "Shuffle", "(4x,2x)", 12, 50, new byte[][] {{0,-5,12,10},{10,0,14,0}}));
		add(new JmPattern(type, "Luke Shuffle", "(4,2x)(2x,4)", 15, 50, new byte[][] {{10,0,14,10},{10,-3,6,0},{10,-3,6,0},{10,0,14,10}}));
		add(new JmPattern(type, "Machine 1/2", "242334", 15, 50, new byte[][] {{5,4,10,-3},b,{10,-3,10,13},a,{10,13,-10,13},d}));
		add(new JmPattern(type, "Machine", "3", 10, 50, new byte[][] {{7,12,-7,12},{7,0,-7,0}}));
		add(new JmPattern(type, "Douche", "51", 10, 50, shower));
		add(new JmPattern(type, "Douche haute", "51"));
		add(new JmPattern(type, "Boite", "(2x,4)(4,2x)", 12, 50, new byte[][] {b,{10,0,2,0},{10,0,2,0},b}));
		add(new JmPattern(type, "Vague", "(2,4)(2,4x)(4,2)(4x,2)", 10, 50, new byte[][] {{10,1,-7,0},{12,0,12,9},e,{-5,9,0,3},{12,0,12,9},{10,1,-7,0},{-5,9,0,3},e}));
		add(new JmPattern(type, "Suivre", "423", 10, 50, new byte[][] {{-10,10,-10,4},{10,-3,10,10},{0,4,0,2}}));
		add(new JmPattern(type, "Boston mess A", "3", new byte[][] {{10,-2,10,-2},{10,3,10,3},{0,-2,0,-2},{-10,3,-10,3},{-10,-2,-10,-2},{0,3,0,3}}));
		add(new JmPattern(type, "Boston mess B", "3", new byte[][] {{10,3,10,-2},{10,3,10,3},{0,-2,0,-2},{-10,3,-10,3},{-10,-2,-10,-2},{0,3,0,3},{10,-2,10,3},{10,-2,10,-2},{0,3,0,3},{-10,-2,-10,-2},{-10,3,-10,3},{0,-2,0,-2}}));
		add(new JmPattern(type, "Mill's mess", "3", 13, 50, millsmess));
		add(new JmPattern(type, "Reverse Mill's mess", "3", 13, 50, new byte[][] {{-12,0,-1,0},{12,0,0,0},{-12,0,1,0}}));
		add(new JmPattern(type, "Barrage", "423", 10, 50, new byte[][] {{12,12,-6,-2},{-6,5,-6,12},{0,5,0,5}}));
		add(new JmPattern(type, "Revenge de Rubenstein", "35223", 15, 50, new byte[][] {{3,1,13,8},{7,-2,-10,-3},{3,6,-12,2},{-1,0,12,2},{-2,2,-10,-2}}));
		add(new JmPattern(type, "une main", "60"));
		add(new JmPattern(type, "une main cascade", "60", new byte[][] {{-6,0,2,0},g,{18,0,10,0},g}));
		add(new JmPattern(type, "une main multiplex 2b", "[46]06020"));
		add(new JmPattern(type, "une main deux étages", "8040"));
	}
	
	public void setPackage01Fr() throws JmException {
		clearIndex();
// edit {
		int type = 1;
// }
		add(new JmPattern(type, "531"));
		add(new JmPattern(type, "450"));
		add(new JmPattern(type, "5241"));
		add(new JmPattern(type, "51414"));
		add(new JmPattern(type, "72312"));
		add(new JmPattern(type, "Cascade large", "3", 12, 50, new byte[][] {{21,9,18,4}}));
		add(new JmPattern(type, "Cascade lancée en colonne", "3", new byte[][] {{-8,0,12,0}}));
		add(new JmPattern(type, "Par-dessus en continu", "3", 15, 50, new byte[][] {{10,0,3,-2},{-13,5,13,0},{13,-3,4,0}}));
		add(new JmPattern(type, "Par en dessous en continu", "3", 15, 50, new byte[][] {{10,4,3,4},{-13,-5,13,0},{13,3,4,0}}));
		add(new JmPattern(type, "Mains croisées", "3", new byte[][] {{-4,0,-12,0}}));
		add(new JmPattern(type, "Manger toutes les pommes", "(2,4x)(4x,2)", 12, 50, new byte[][] {{0,7,-3,-3},{12,10,0,8},{12,10,0,8},{0,7,-3,-3}}));
		
		add(new JmPattern(type, "1-up 2-up B", "(4,4)(0,4x)(4,4)(4x,0)", 14, 50, new byte[][] {o,o,{14,0,0,0},o,o,o,o,{14,0,0,0}}));
		add(new JmPattern(type, "1-up 2-up C", "(4,4)(0,4x)(4,4)(4,0)(4,4)(4x,0)(4,4)(0,4)", 14, 50, new byte[][] {o,o,{14,0,14,0},o,o,o,o,a,o,o,o,{14,0,14,0},o,o,a,o}));
		add(new JmPattern(type, "1-up 2-up D", "(4,4)(0,4x)(4,4)(4x,0)", 14, 50, new byte[][] {{12,0,-6,0},{-6,0,12,0},a,d,{-6,0,12,0},{12,0,-6,0},d,a}));
		add(new JmPattern(type, "1-up 2-up E", "(4,4)(0,4)", 14, 50, new byte[][] {f,f,{13,0,-13,0},f}));
		add(new JmPattern(type, "1-up 2-up F", "(6,6)(0,2x)(4x,0)", 10, 50, new byte[][] {f,f,{13,7,4,0},f,f,{4,0,13,7}}));

		byte[] fake1 = {10,-1,10,0};
		byte[] fake2 = {0,-1,0,0};
		byte[] fake3 = {10,10,10,10};
		add(new JmPattern(type, "Fake A", "(2,4)", 14, 50, new byte[][] {fake1,fake1,fake2,fake3}));
		add(new JmPattern(type, "Fake B", "(2,4)", 14, 50, new byte[][] {fake2,fake1,fake1,fake3}));
		add(new JmPattern(type, "Fake 2 côtés", "(4,2)(4x,2)(2,4)(2,4x)", 13, 50, new byte[][] {fake1,fake1,fake3,fake2,fake1,fake1,fake2,fake3}));
		add(new JmPattern(type, "Yo-yo autour", "(4,2)", 13, 50, new byte[][] {{0,2,0,3},fake2,{0,13,0,13},{10,-1,-10,-1}}));
		add(new JmPattern(type, "Yo-yo tornade", "(2,4)", new byte[][] {e,{15,15,-15,14},{-5,0,-5,0},{15,14,-15,15}}));
		add(new JmPattern(type, "Yo-yo à travers", "(2,4)", new byte[][] {e,{15,12,0,12},{-5,0,-5,0},{-15,12,0,12}}));
		add(new JmPattern(type, "(4x,2x)(2,4)"));
		add(new JmPattern(type, "(4x,6)(0,2x)"));
		add(new JmPattern(type, "Tennis lent", "(2,4)(2,4x)(4,2)(4x,2)", 14, 50, new byte[][] {e,e,{15,3,15,3},e,e,e,e,{15,3,15,3}}));
		add(new JmPattern(type, "Statue de la liberté Cascade", "3", 14, 50, new byte[][] {{3,4,12,0},{10,17,9,19}}));
		add(new JmPattern(type, "441", 13, 50));
		add(new JmPattern(type, "441 Inversé", "441", 13, 50, new byte[][] {r,r,{4,0,0,0}}));
		add(new JmPattern(type, "441 Shuffle", "441", 10, 50, new byte[][] {{5,-2,5,-2},d,{15,4,9,10}}));
		add(new JmPattern(type, "Boss Side Slam", "(4x,2x)(2,4x)(2x,4x)(4x,2)", 10, 50, new byte[][] {{6,0,12,10},b,{10,0,4,0},d,b,{6,0,12,10},d,{10,0,4,0}}));
		add(new JmPattern(type, "Machine B", "2334", 13, 50, new byte[][] {{0,11,10,11},{10,0,0,0},{0,12,-10,12},{10,0,0,0}}));

		byte[] eb = {3,0,10,0};
		add(new JmPattern(type, "Demi-douche lente", "5223", shower));
		add(new JmPattern(type, "Demi-douche sous le bras", "(2x,4x)", new byte[][] {{5,0,-10,0},{3,4,-3,4}}));
		add(new JmPattern(type, "Demi-douche sous les bras", "(2x,4x)", new byte[][] {eb,{5,4,0,4},{3,0,-10,0},{5,4,0,4}}));
		add(new JmPattern(type, "Douche", "(2x,4x)", shower));
		add(new JmPattern(type, "Douche 7131", "7131", shower));
		add(new JmPattern(type, "Douche (2x,2x)", "(2x,6x)(2x,2x)", 15, 50, new byte[][] {{6,0,12,3},{6,3,6,0},{6,0,6,3},{12,3,6,0}}));
		add(new JmPattern(type, "Douche 315171", "315171", shower));
		add(new JmPattern(type, "Hafumesu", "3", 13, 50, new byte[][] {c,{0,0,-12,-3},{0,2,-12,2},{0,-3,-6,0}}));
		add(new JmPattern(type, "423 Mill's mess", "423", millsmess));
		add(new JmPattern(type, "414 Mill's mess", "414", millsmess));
		add(new JmPattern(type, "315 Mill's mess", "315", millsmess));
		add(new JmPattern(type, "44133 Mill's mess", "44133", 13, 50, new byte[][] {{-2,0,-12,5},{2,-2,12,0},{0,2,-3,0},{10,0,10,2},{7,-2,-10,-3}}));
		add(new JmPattern(type, "Boite Mill's mess", "612", millsmess));
		add(new JmPattern(type, "Boite 126", "126", 15, 50));
		add(new JmPattern(type, "Boite 630", "630", 15, 50));
		add(new JmPattern(type, "Boite étendue", "(4x,2x)(4,2x)(2x,4x)(2x,4)", 11, 50, new byte[][] {{14,0,7,0},a,{14,0,0,0},{0,0,14,0},a,{14,0,7,0},{0,0,14,0},{14,0,0,0}}));
		add(new JmPattern(type, "Boite 2 étages", "(2x,8)(2x,4)(0,2x)(8,2x)(4,2x)(2x,0)", new byte[][] {eb,h,eb,h,h,eb,h,eb,h,eb,eb,h}));
	}
	
	public void setPackage02Fr() throws JmException {
		clearIndex();
// edit {
		int type = 2;
// }
		add(new JmPattern(type, "Fontaine", "4"));
		add(new JmPattern(type, "Fontaine synchrone", "(4,4)"));
		add(new JmPattern(type, "Fontaine inversée", "4", reverse));
		add(new JmPattern(type, "Fontaine Synchrone inversée", "(4,4)", reverse));
		add(new JmPattern(type, "Colonnes 4b avec un échange", "(4x,4x)(4,4)", new byte[][] {{12,0,12,2},{12,2,12,0},f,f}));
		add(new JmPattern(type, "Colonnes 4b Piston", "4", columns));
		add(new JmPattern(type, "Colonnes 4b synchro (asymétrique)", "(4,4)", columns));
		add(new JmPattern(type, "Colonnes 4b synchro (symétrique)", "(4,4)", new byte[][] {c,c,f,f}));
		add(new JmPattern(type, "Colonnes 4b synchro séparées", "(4,4)", new byte[][] {c,{-4,0,-4,0},{-4,0,-4,0},c}));
		byte[] cross1 = {13,0,7,3};
		byte[] cross2 = {13,3,7,0};
		add(new JmPattern(type, "Croisé A", "(4x,4x)", new byte[][] {cross1,cross2}));
		add(new JmPattern(type, "Croisé B", "(4x,4x)", new byte[][] {cross1,cross2,cross2,cross1}));
		add(new JmPattern(type, "Tennis", "44453",	 new byte[][] {{9,0,4,0},{9,0,4,0},{0,0,4,0},{15,3,15,3},{9,0,4,0}}));
		add(new JmPattern(type, "444447333", 30, 50));
		add(new JmPattern(type, "53"));
		add(new JmPattern(type, "561"));
		add(new JmPattern(type, "453"));
		add(new JmPattern(type, "741"));
		add(new JmPattern(type, "6424"));
		add(new JmPattern(type, "61616"));
		add(new JmPattern(type, "7161616"));
		add(new JmPattern(type, "7272712"));
		add(new JmPattern(type, "(2x,6x)(6x,2x)"));
		add(new JmPattern(type, "(2,6x)(2x,6)(6x,2)(6,2x)"));
		add(new JmPattern(type, "(2,4)([44x],2x)"));

		add(new JmPattern(type, "Demi-douche 4b", "53", shower));
		add(new JmPattern(type, "Douche 4b", "71", shower));
		add(new JmPattern(type, "Douche synchro 4b", "(2x,6x)", shower));
		add(new JmPattern(type, "Douche 9151", "9151", shower));

		add(new JmPattern(type, "Boite", "(2x,8)(2x,4)(8,2x)(4,2x)", new byte[][] {b,h,b,h,h,b,h,b}));
		add(new JmPattern(type, "Pseudo Boite A", "633", new byte[][] {b,{7,0,0,0},{7,0,0,0}}));
		add(new JmPattern(type, "Pseudo Boite B", "(2x,6)(2x,6)(6,2x)(6,2x)"));

		add(new JmPattern(type, "Mill's mess", "4", millsmess));
		add(new JmPattern(type, "Mill's mess 534", "534", millsmess));
		add(new JmPattern(type, "Mill's mess 552", "552", millsmess));
		add(new JmPattern(type, "Mill's mess 642", "642", millsmess));
		add(new JmPattern(type, "Barrage", "525", 12, 50, new byte[][] {{14,14,-5,-2},{-9,3,-9,14},{0,0,3,0}}));

		add(new JmPattern(type, "Multiplex [34]1", "[34]1"));
		add(new JmPattern(type, "Mutiplex 4[43]1", "4[43]1"));
		add(new JmPattern(type, "Mill's mess Multiplex", "[34]23", millsmess));
		add(new JmPattern(type, "Mill's mess moulin", "3[53]22[32]", 15, 50, new byte[][] {{3,1,13,8},{7,-2,-10,-3},{3,6,-12,2},{-1,0,12,2},{-2,2,-10,-2}}));
	}
	
	public void setPackage03Fr() throws JmException {
		clearIndex();
// edit {
		int type = 3;
// }
		add(new JmPattern(type, "Cascade", "5"));
		add(new JmPattern(type, "Cascade inversée", "5", reverse));
		add(new JmPattern(type, "555555744", 30, 50));
		add(new JmPattern(type, "2 gauche et 3 droite", "64"));
		add(new JmPattern(type, "744"));
		add(new JmPattern(type, "753"));
		add(new JmPattern(type, "66661"));
		add(new JmPattern(type, "88333"));
		add(new JmPattern(type, "75751"));
		add(new JmPattern(type, "123456789", center));

		add(new JmPattern(type, "Demi-douche 5", "5", new byte[][] {{8,0,15,8},{15,8,8,0}}));
		add(new JmPattern(type, "Demi-douche 73", "73", shower));
		add(new JmPattern(type, "Demi-douche synchrone", "(4x,6x)", shower));
		add(new JmPattern(type, "Douche", "91", shower));
		add(new JmPattern(type, "[97]121 douche", "[97]121", shower));
		add(new JmPattern(type, "(6,4x)", "(4x,6)(6,4x)"));

		add(new JmPattern(type, "Mill's mess", "5", millsmess));
		add(new JmPattern(type, "Barrage", "726", 12, 50, new byte[][] {{16,14,-3,-4},{-9,0,-10,14},{2,-2,4,0}}));
		add(new JmPattern(type, "Colonnes", "(6,6)(6,6)(0,6)", new byte[][] {c,c,d,d,a,d}));
		add(new JmPattern(type, "Boston mess A", "5", new byte[][] {{-12,0,-12,0},{-6,0,-6,0},a,d,c,c,d,a,{-6,0,-6,0},{-12,0,-12,0}}));
		add(new JmPattern(type, "Boston mess B", "5", new byte[][] {c,d,d,c,a,{-12,0,-12,0},{-6,0,-6,0},{-6,0,-6,0},{-12,0,-12,0},a}));
		add(new JmPattern(type, "Boston mess C", "5", new byte[][] {{12,3,12,3},{-6,0,-6,0},{0,3,0,3},d,{-12,3,-12,3},{-12,0,-12,0},{6,3,6,3},a,{-6,3,-6,3},c}));
		add(new JmPattern(type, "Colonnes multiplex", "([46],[46])(0,6)(2,2)", new byte[][] {g,g,a,g,g,g}));

		add(new JmPattern(type, "Martin", "[62]25", new byte[][] {g,n,n}));
		add(new JmPattern(type, "25[75]51"));
		add(new JmPattern(type, "(2,[62])([22],6x)([62],2)(6x,[22])"));
		add(new JmPattern(type, "Multiplex [54][22]2", "[54][22]2"));
		add(new JmPattern(type, "Multiplex 24[54]", "24[54]"));
	}
	
	public void setPackage04Fr() throws JmException {
		clearIndex();
// edit {
		int type = 4;
// }
		add(new JmPattern(type, "Fontaine 6b", "6"));
		add(new JmPattern(type, "Fontaine Synchro 6b", "(6,6)"));
		add(new JmPattern(type, "Cascade 7b", "7"));
		add(new JmPattern(type, "Fontaine 8b", "8"));
		add(new JmPattern(type, "Cascade 9b", "9"));

		add(new JmPattern(type, "Demi-douche 6b", "75", shower));
		add(new JmPattern(type, "Douche 6b", "b1", shower));
		add(new JmPattern(type, "Douche 7b", "d1", shower));

		add(new JmPattern(type, "Colonnes 6b", "(6,6)",
		 new byte[][] {{15,0,15,0},{15,0,15,0},{9,0,9,0},{9,0,9,0},{3,0,3,0},{3,0,3,0}}));
		add(new JmPattern(type, "Mill's mess 6b", "6", millsmess));
		add(new JmPattern(type, "Mill's mess 864 6b", "864", millsmess));

		add(new JmPattern(type, "Multiplex 7b [456][22]2", "[456][22]2"));
		add(new JmPattern(type, "Multiplex 7b faux6", "([66x],2)(2,[66x])", center));
		add(new JmPattern(type, "26[76]"));
		add(new JmPattern(type, "[234]57"));

		add(new JmPattern(type, "9 multiplex [54]", "[54]"));
		add(new JmPattern(type, "35 Cascade", "z"));
		add(new JmPattern(type, "18 Douche", "z1", shower));
		add(new JmPattern(type, "35 douche multiplex", "[9bdfh][11111]", shower));
		add(new JmPattern(type, "Mill's mess 12b 333666999cccfffiiilll", "333666999cccfffiiilll", millsmess));
		add(new JmPattern(type, "[b9753]0020[22]0[222]0[2222]0"));
		add(new JmPattern(type, "123456789abcdefghijklmnopqrstuvwxyz", center));

		byte[] box1 = {13,0,3,0};
		byte[] box2 = JmPattern.reverse(box1);
		add(new JmPattern(type, "Boite 9b", "u1q1m1i1e1a1612",
		 new byte[][] {g,box1,box2,box1,box2,box1,box2,box1,box2,box1,box2,box1,box2,box1,g}));
		add(new JmPattern(type, "trpnljhfdb97531"));
		add(new JmPattern(type, "Ken", "ken"));
		add(new JmPattern(type, "Multiplex [56789]", "[56789]"));
		add(new JmPattern(type, "20 colonnes A", "(k,k)", 10, 50,
		 new byte[][] {{29,3,29,3},{2,3,2,3},{26,3,26,3},{5,3,5,3},{23,3,23,3},{8,3,8,3},{20,3,20,3},{11,3,11,3},{17,3,17,3},{14,3,14,3},{14,3,14,3},{17,3,17,3},{11,3,11,3},{20,3,20,3},{8,3,8,3},{23,3,23,3},{5,3,5,3},{26,3,26,3},{2,3,2,3},{29,3,29,3}}));
		add(new JmPattern(type, "20 colonnes B", "k", 10, 50,
		 new byte[][] {{20,3,20,3},{18,3,18,3},{16,3,16,3},{14,3,14,3},{12,3,12,3},{10,3,10,3},{8,3,8,3},{6,3,6,3},{4,3,4,3},{2,3,2,3},{2,3,2,3},{4,3,4,3},{6,3,6,3},{8,3,8,3},{10,3,10,3},{12,3,12,3},{14,3,14,3},{16,3,16,3},{18,3,18,3},{20,3,20,3}}));
	}

	public void setPackage05Fr() throws JmException {
		clearIndex();
// edit {
		int type = 5;
// }
		add(new JmPattern(type, "Lancé un", "300"));
		add(new JmPattern(type, "Lancé deux", "33022"));
		add(new JmPattern(type, "Lancé continu deux", "330"));
		add(new JmPattern(type, "Flash", "[32]3322"));
		add(new JmPattern(type, "Cascade lente", "3", 20, 75));
		add(new JmPattern(type, "Fontaine 2b 1 main - éducatif 4b", "40"));
		add(new JmPattern(type, "Colonne 2b 1 main - éducatif 4b", "40", new byte[][] {a,g,b,g}));
		add(new JmPattern(type, "Serpent 2b - éducatif 5b", "50500"));
		add(new JmPattern(type, "Serpent 3b - éducatif 5b", "50505"));
		add(new JmPattern(type, "Flash 3b enchainé - éducatif 5b", "55500"));
		add(new JmPattern(type, "3b - éducatif 5b", "52512"));
		add(new JmPattern(type, "Flash 4b - éducatif 5b", "[52][52]55022[22][22]"));
		add(new JmPattern(type, "Flash 4b enchainé - éducatif 5b", "55550"));
		add(new JmPattern(type, "4b - éducatif 5b", "552"));
		add(new JmPattern(type, "4b - éducatif 5b", "5551"));
		add(new JmPattern(type, "5 flash", "[522][52][52]5522[22][22]"));
		add(new JmPattern(type, "3 -> 5 Cascade", "[32][32][32][32][32][32][52][52][52]555555522"));

		add(new JmPattern(type, "Educatif boite", "(2x,4)(0,2x)", 12, 50, new byte[][] {b,{10,0,2,0},{10,0,2,0},b}));
		add(new JmPattern(type, "Educatif Mill's mess", "330", 13, 50, millsmess));
	}
	
	public void setPackage06Fr() throws JmException {
		clearIndex();
		int type = 6;
		add(new JmPattern(type, "[Nouveau]", "1"));
	}	

	public void setPackage00() throws JmException {
		clearIndex();
// edit {
		int type = 0;
// }
		add(new JmPattern(type, "カスケード", "3"));
		add(new JmPattern(type, "フラッシュ", "333355500", 30, 50));
		add(new JmPattern(type, "テニス", "3", new byte[][] {{8,0,4,0},{8,0,4,0},{14,3,14,3}}));
		add(new JmPattern(type, "ハーフシャワー", "3", new byte[][] {{8,0,15,8},{15,8,8,0}}));
		add(new JmPattern(type, "リバースカスケード", "3", reverse));
		add(new JmPattern(type, "ウインドミル", "3", new byte[][] {{10,0,-10,-2},{-10,4,10,2}}));
		add(new JmPattern(type, "リーチオーバー", "3", 15, 50, new byte[][] {n,n,n,g,n,{10,0,8,-2},{-13,5,0,2},{13,-3,4,0}}));
		add(new JmPattern(type, "リーチアンダー", "3", 15, 50, new byte[][] {n,n,n,g,{13,0,4,-3},{10,4,4,4},{-16,-3,0,0},{13,3,4,0}}));
		add(new JmPattern(type, "オーバーザヘッド", "3", new byte[][] {{12,19,3,19}}));
		add(new JmPattern(type, "チョップ", "(2,4x)(4x,2)", 12, 50, new byte[][] {{4,9,-10,-3},{10,6,7,9},{10,6,7,9},{4,9,-10,-3}}));
		add(new JmPattern(type, "リンゴ食べちゃえ", "33333423", new byte[][] {{0,6,-3,4},n,n,n,n,{13,0,2,0},{12,7,0,7},{13,0,-4,0}}));
		add(new JmPattern(type, "1アップ2アップ", "(0,4)(4,4)", 14, 50, new byte[][] {a,b,b,b}));
		add(new JmPattern(type, "ヨーヨー", "(4,2)", 13, 50,  new byte[][] {{5,2,5,3},{-5,-1,-5,0},{5,13,5,13},{5,-1,5,0}}));
		add(new JmPattern(type, "オイオイ", "(4,2)", 13, 50, new byte[][] {{5,-1,5,0},{-5,3,-5,4},{5,10,5,10},{5,3,5,4}}));
		add(new JmPattern(type, "アーチ", "3", 13, 50, new byte[][] {{20,10,20,10},b,{15,5,15,5},{20,10,20,10},b,{15,5,15,5}}));
		add(new JmPattern(type, "自由の女神", "3", 12, 50, new byte[][] {{3,0,12,4},{10,20,9,19}}));
		add(new JmPattern(type, "シャッフル", "(4x,2x)", 12, 50, new byte[][] {{0,-5,12,10},{10,0,14,0}}));
		add(new JmPattern(type, "ルークスシャッフル", "(4,2x)(2x,4)", 15, 50, new byte[][] {{10,0,14,10},{10,-3,6,0},{10,-3,6,0},{10,0,14,10}}));
		add(new JmPattern(type, "ロボット", "242334", 15, 50, new byte[][] {{5,4,10,-3},b,{10,-3,10,13},a,{10,13,-10,13},d}));
		add(new JmPattern(type, "キャリー", "3", 10, 50, new byte[][] {{7,12,-7,12},{7,0,-7,0}}));
		add(new JmPattern(type, "シャワー", "51", 10, 50, shower));
		add(new JmPattern(type, "日本のお手玉", "51"));
		add(new JmPattern(type, "ボックス", "(2x,4)(4,2x)", 12, 50, new byte[][] {b,{10,0,2,0},{10,0,2,0},b}));
		add(new JmPattern(type, "ウィーブ", "(2,4)(2,4x)(4,2)(4x,2)", 10, 50, new byte[][] {{10,1,-7,0},{12,0,12,9},e,{-5,9,0,3},{12,0,12,9},{10,1,-7,0},{-5,9,0,3},e}));
		add(new JmPattern(type, "フォロー", "423", 10, 50, new byte[][] {{-10,10,-10,4},{10,-3,10,10},{0,4,0,2}}));
		add(new JmPattern(type, "ボストンメス A", "3", new byte[][] {{10,-2,10,-2},{10,3,10,3},{0,-2,0,-2},{-10,3,-10,3},{-10,-2,-10,-2},{0,3,0,3}}));
		add(new JmPattern(type, "ボストンメス B", "3", new byte[][] {{10,3,10,-2},{10,3,10,3},{0,-2,0,-2},{-10,3,-10,3},{-10,-2,-10,-2},{0,3,0,3},{10,-2,10,3},{10,-2,10,-2},{0,3,0,3},{-10,-2,-10,-2},{-10,3,-10,3},{0,-2,0,-2}}));
		add(new JmPattern(type, "ミルズメス", "3", 13, 50, millsmess));
		add(new JmPattern(type, "リバースミルズメス", "3", 13, 50, new byte[][] {{-12,0,-1,0},{12,0,0,0},{-12,0,1,0}}));
		add(new JmPattern(type, "バークスバラージ", "423", 10, 50, new byte[][] {{12,12,-6,-2},{-6,5,-6,12},{0,5,0,5}}));
		add(new JmPattern(type, "ルーベンシュタインズリベンジ", "35223", 15, 50, new byte[][] {{3,1,13,8},{7,-2,-10,-3},{3,6,-12,2},{-1,0,12,2},{-2,2,-10,-2}}));
		add(new JmPattern(type, "片手３個", "60"));
		add(new JmPattern(type, "片手カスケード", "60", new byte[][] {{-6,0,2,0},g,{18,0,10,0},g}));
		add(new JmPattern(type, "片手３個のマルチ", "[46]06020"));
		add(new JmPattern(type, "8040"));
	}
	
	
	public void setPackage01() throws JmException {
		clearIndex();
// edit {
		int type = 1;
// }
		add(new JmPattern(type, "531"));
		add(new JmPattern(type, "450"));
		add(new JmPattern(type, "5241"));
		add(new JmPattern(type, "51414"));
		add(new JmPattern(type, "72312"));
		add(new JmPattern(type, "ワイドカスケード", "3", 12, 50, new byte[][] {{21,9,18,4}}));
		add(new JmPattern(type, "ワイドリバースカスケード", "3", new byte[][] {{-8,0,12,0}}));
		add(new JmPattern(type, "リーチオーバー連続", "3", 15, 50, new byte[][] {{10,0,3,-2},{-13,5,13,0},{13,-3,4,0}}));
		add(new JmPattern(type, "リーチアンダー連続", "3", 15, 50, new byte[][] {{10,4,3,4},{-13,-5,13,0},{13,3,4,0}}));
		add(new JmPattern(type, "クロス アーム", "3", new byte[][] {{-4,0,-12,0}}));
		add(new JmPattern(type, "リンゴ食べ放題", "(2,4x)(4x,2)", 12, 50, new byte[][] {{0,7,-3,-3},{12,10,0,8},{12,10,0,8},{0,7,-3,-3}}));
		
		add(new JmPattern(type, "1アップ2アップ B", "(4,4)(0,4x)(4,4)(4x,0)", 14, 50, new byte[][] {o,o,{14,0,0,0},o,o,o,o,{14,0,0,0}}));
		add(new JmPattern(type, "1アップ2アップ C", "(4,4)(0,4x)(4,4)(4,0)(4,4)(4x,0)(4,4)(0,4)", 14, 50, new byte[][] {o,o,{14,0,14,0},o,o,o,o,a,o,o,o,{14,0,14,0},o,o,a,o}));
		add(new JmPattern(type, "1アップ2アップ D", "(4,4)(0,4x)(4,4)(4x,0)", 14, 50, new byte[][] {{12,0,-6,0},{-6,0,12,0},a,d,{-6,0,12,0},{12,0,-6,0},d,a}));
		add(new JmPattern(type, "1アップ2アップ E", "(4,4)(0,4)", 14, 50, new byte[][] {f,f,{13,0,-13,0},f}));
		add(new JmPattern(type, "1アップ2アップ F", "(6,6)(0,2x)(4x,0)", 10, 50, new byte[][] {f,f,{13,7,4,0},f,f,{4,0,13,7}}));

		byte[] fake1 = {10,-1,10,0};
		byte[] fake2 = {0,-1,0,0};
		byte[] fake3 = {10,10,10,10};
		add(new JmPattern(type, "フェイク A", "(2,4)", 14, 50, new byte[][] {fake1,fake1,fake2,fake3}));
		add(new JmPattern(type, "フェイク B", "(2,4)", 14, 50, new byte[][] {fake2,fake1,fake1,fake3}));
		add(new JmPattern(type, "フェイク C", "(4,2)(4x,2)(2,4)(2,4x)", 13, 50, new byte[][] {fake1,fake1,fake3,fake2,fake1,fake1,fake2,fake3}));
		add(new JmPattern(type, "ヨーヨーを一周", "(4,2)", 13, 50, new byte[][] {{0,2,0,3},fake2,{0,13,0,13},{10,-1,-10,-1}}));
		add(new JmPattern(type, "ヨーヨー(トルネード)", "(2,4)", new byte[][] {e,{15,15,-15,14},{-5,0,-5,0},{15,14,-15,15}}));
		add(new JmPattern(type, "ヨーヨー(横断)", "(2,4)", new byte[][] {e,{15,12,0,12},{-5,0,-5,0},{-15,12,0,12}}));
		add(new JmPattern(type, "(4x,2x)(2,4)"));
		add(new JmPattern(type, "(4x,6)(0,2x)"));
		add(new JmPattern(type, "テニス B", "(2,4)(2,4x)(4,2)(4x,2)", 14, 50, new byte[][] {e,e,{15,3,15,3},e,e,e,e,{15,3,15,3}}));
		add(new JmPattern(type, "自由の女神 B", "3", 14, 50, new byte[][] {{3,4,12,0},{10,17,9,19}}));
		add(new JmPattern(type, "441", 13, 50));
		add(new JmPattern(type, "441 外回り", "441", 13, 50, new byte[][] {r,r,{4,0,0,0}}));
		add(new JmPattern(type, "441 シャッフル", "441", 10, 50, new byte[][] {{5,-2,5,-2},d,{15,4,9,10}}));
		add(new JmPattern(type, "ボスサイドスラム", "(4x,2x)(2,4x)(2x,4x)(4x,2)", 10, 50, new byte[][] {{6,0,12,10},b,{10,0,4,0},d,b,{6,0,12,10},d,{10,0,4,0}}));
		add(new JmPattern(type, "イクスチェンジ", "2334", 13, 50, new byte[][] {{0,11,10,11},{10,0,0,0},{0,12,-10,12},{10,0,0,0}}));

		byte[] eb = {3,0,10,0};
		add(new JmPattern(type, "ハーフシャワー B", "5223", shower));
		add(new JmPattern(type, "腕の下シャワー", "(2x,4x)", new byte[][] {{5,0,-10,0},{3,4,-3,4}}));
		add(new JmPattern(type, "腕の下シャワー(交互)", "(2x,4x)", new byte[][] {eb,{5,4,0,4},{3,0,-10,0},{5,4,0,4}}));
		add(new JmPattern(type, "シンクロシャワー", "(2x,4x)", shower));
		add(new JmPattern(type, "ハイローシャワー A", "7131", shower));
		add(new JmPattern(type, "ハイローシャワー B", "(2x,6x)(2x,2x)", 15, 50, new byte[][] {{6,0,12,3},{6,3,6,0},{6,0,6,3},{12,3,6,0}}));
		add(new JmPattern(type, "ハイローシャワー C", "315171", shower));
		add(new JmPattern(type, "ハーフメス", "3", 13, 50, new byte[][] {c,{0,0,-12,-3},{0,2,-12,2},{0,-3,-6,0}}));
		add(new JmPattern(type, "ミルズメス 423", "423", millsmess));
		add(new JmPattern(type, "ミルズメス 414", "414", millsmess));
		add(new JmPattern(type, "ミルズメス 315", "315", millsmess));
		add(new JmPattern(type, "ミルズメス 44133", "44133", 13, 50, new byte[][] {{-2,0,-12,5},{2,-2,12,0},{0,2,-3,0},{10,0,10,2},{7,-2,-10,-3}}));
		add(new JmPattern(type, "ミルズメス ボックス", "612", millsmess));
		add(new JmPattern(type, "ボックスもどき A", "126", 15, 50));
		add(new JmPattern(type, "ボックスもどき B", "630", 15, 50));
		add(new JmPattern(type, "ダブルボックス", "(4x,2x)(4,2x)(2x,4x)(2x,4)", 11, 50, new byte[][] {{14,0,7,0},a,{14,0,0,0},{0,0,14,0},a,{14,0,7,0},{0,0,14,0},{14,0,0,0}}));
		add(new JmPattern(type, "拡張ボックス", "(2x,8)(2x,4)(0,2x)(8,2x)(4,2x)(2x,0)", new byte[][] {eb,h,eb,h,h,eb,h,eb,h,eb,eb,h}));
	}
	
	public void setPackage02() throws JmException {
		clearIndex();
// edit {
		int type = 2;
// }
		add(new JmPattern(type, "ファウンテン", "4"));
		add(new JmPattern(type, "シンクロファウンテン", "(4,4)"));
		add(new JmPattern(type, "リバースファウンテン", "4", reverse));
		add(new JmPattern(type, "リバースシンクロファウンテン", "(4,4)", reverse));
		add(new JmPattern(type, "4コラムス スイッチ", "(4x,4x)(4,4)", new byte[][] {{12,0,12,2},{12,2,12,0},f,f}));
		add(new JmPattern(type, "4コラムス ピストン", "4", columns));
		add(new JmPattern(type, "4コラムス シンクロ(非対称)", "(4,4)", columns));
		add(new JmPattern(type, "4コラムス シンクロ(対称)", "(4,4)", new byte[][] {c,c,f,f}));
		add(new JmPattern(type, "4コラムス シンクロ(Splits)", "(4,4)", new byte[][] {c,{-4,0,-4,0},{-4,0,-4,0},c}));
		byte[] cross1 = {13,0,7,3};
		byte[] cross2 = {13,3,7,0};
		add(new JmPattern(type, "4クロス A", "(4x,4x)", new byte[][] {cross1,cross2}));
		add(new JmPattern(type, "4クロス B", "(4x,4x)", new byte[][] {cross1,cross2,cross2,cross1}));
		add(new JmPattern(type, "4テニス", "44453",	 new byte[][] {{9,0,4,0},{9,0,4,0},{0,0,4,0},{15,3,15,3},{9,0,4,0}}));
		add(new JmPattern(type, "444447333", 30, 50));
		add(new JmPattern(type, "53"));
		add(new JmPattern(type, "561"));
		add(new JmPattern(type, "453"));
		add(new JmPattern(type, "741"));
		add(new JmPattern(type, "6424"));
		add(new JmPattern(type, "61616"));
		add(new JmPattern(type, "7161616"));
		add(new JmPattern(type, "7272712"));
		add(new JmPattern(type, "(2x,6x)(6x,2x)"));
		add(new JmPattern(type, "(2,6x)(2x,6)(6x,2)(6,2x)"));
		add(new JmPattern(type, "(2,4)([44x],2x)"));

		add(new JmPattern(type, "4ハーフシャワー", "53", shower));
		add(new JmPattern(type, "4シャワー", "71", shower));
		add(new JmPattern(type, "4シンクロシャワー", "(2x,6x)", shower));
		add(new JmPattern(type, "4ハイローシャワー", "9151", shower));

		add(new JmPattern(type, "4ボックス", "(2x,8)(2x,4)(8,2x)(4,2x)", new byte[][] {b,h,b,h,h,b,h,b}));
		add(new JmPattern(type, "4ボックスもどき A", "633", new byte[][] {b,{7,0,0,0},{7,0,0,0}}));
		add(new JmPattern(type, "4ボックスもどき B", "(2x,6)(2x,6)(6,2x)(6,2x)"));

		add(new JmPattern(type, "4ミルズメス", "4", millsmess));
		add(new JmPattern(type, "4ミルズメス 534", "534", millsmess));
		add(new JmPattern(type, "4ミルズメス 552", "552", millsmess));
		add(new JmPattern(type, "4ミルズメス 642", "642", millsmess));
		add(new JmPattern(type, "4バークスバラージ", "525", 12, 50, new byte[][] {{14,14,-5,-2},{-9,3,-9,14},{0,0,3,0}}));

		add(new JmPattern(type, "4マルチ [34]1", "[34]1"));
		add(new JmPattern(type, "4マルチ 4[43]1", "4[43]1"));
		add(new JmPattern(type, "4マルチ ミルズメス", "[34]23", millsmess));
		add(new JmPattern(type, "ダンシーズデビルメント", "3[53]22[32]", 15, 50, new byte[][] {{3,1,13,8},{7,-2,-10,-3},{3,6,-12,2},{-1,0,12,2},{-2,2,-10,-2}}));
	}
	
	public void setPackage03() throws JmException {
		clearIndex();
// edit {
		int type = 3;
// }
		add(new JmPattern(type, "5カスケード", "5"));
		add(new JmPattern(type, "5リバースカスケード", "5", reverse));
		add(new JmPattern(type, "555555744", 30, 50));
		add(new JmPattern(type, "右３個 左２個", "64"));
		add(new JmPattern(type, "744"));
		add(new JmPattern(type, "753"));
		add(new JmPattern(type, "66661"));
		add(new JmPattern(type, "88333"));
		add(new JmPattern(type, "75751"));
		add(new JmPattern(type, "123456789", center));

		add(new JmPattern(type, "5ハーフシャワー A", "5", new byte[][] {{8,0,15,8},{15,8,8,0}}));
		add(new JmPattern(type, "5ハーフシャワー B", "73", shower));
		add(new JmPattern(type, "5ハーフシャワー C", "(4x,6x)", shower));
		add(new JmPattern(type, "5シャワー", "91", shower));
		add(new JmPattern(type, "5マルチシャワー", "[97]121", shower));
		add(new JmPattern(type, "5ボックス", "(4x,6)(6,4x)"));

		add(new JmPattern(type, "5ミルズメス", "5", millsmess));
		add(new JmPattern(type, "5バークスバラージ", "726", 12, 50, new byte[][] {{16,14,-3,-4},{-9,0,-10,14},{2,-2,4,0}}));
		add(new JmPattern(type, "5コラムス", "(6,6)(6,6)(0,6)", new byte[][] {c,c,d,d,a,d}));
		add(new JmPattern(type, "5ミルズメス コラムス A", "5", new byte[][] {{-12,0,-12,0},{-6,0,-6,0},a,d,c,c,d,a,{-6,0,-6,0},{-12,0,-12,0}}));
		add(new JmPattern(type, "5ミルズメス コラムス B", "5", new byte[][] {c,d,d,c,a,{-12,0,-12,0},{-6,0,-6,0},{-6,0,-6,0},{-12,0,-12,0},a}));
		add(new JmPattern(type, "5ミルズメス コラムス C", "5", new byte[][] {{12,3,12,3},{-6,0,-6,0},{0,3,0,3},d,{-12,3,-12,3},{-12,0,-12,0},{6,3,6,3},a,{-6,3,-6,3},c}));
		add(new JmPattern(type, "5マルチ コラムス", "([46],[46])(0,6)(2,2)", new byte[][] {g,g,a,g,g,g}));

		add(new JmPattern(type, "Martin", "[62]25", new byte[][] {g,n,n}));
		add(new JmPattern(type, "25[75]51"));
		add(new JmPattern(type, "(2,[62])([22],6x)([62],2)(6x,[22])"));
		add(new JmPattern(type, "5マルチ A", "[54][22]2"));
		add(new JmPattern(type, "5マルチ B", "24[54]"));
	}
	
	public void setPackage04() throws JmException {
		clearIndex();
// edit {
		int type = 4;
// }
		add(new JmPattern(type, "6ファウンテン", "6"));
		add(new JmPattern(type, "6シンクロファウンテン", "(6,6)"));
		add(new JmPattern(type, "7カスケード", "7"));
		add(new JmPattern(type, "8ファウンテン", "8"));
		add(new JmPattern(type, "9カスケード", "9"));

		add(new JmPattern(type, "6ハーフシャワー", "75", shower));
		add(new JmPattern(type, "6シャワー", "b1", shower));
		add(new JmPattern(type, "7シャワー", "d1", shower));

		add(new JmPattern(type, "6コラムス", "(6,6)",
		 new byte[][] {{15,0,15,0},{15,0,15,0},{9,0,9,0},{9,0,9,0},{3,0,3,0},{3,0,3,0}}));
		add(new JmPattern(type, "6ミルズメス", "6", millsmess));
		add(new JmPattern(type, "6ミルズメス 864", "864", millsmess));

		add(new JmPattern(type, "7スプリット A", "[456][22]2"));
		add(new JmPattern(type, "7スプリット B", "([66x],2)(2,[66x])", center));
		add(new JmPattern(type, "26[76]"));
		add(new JmPattern(type, "[234]57"));

		add(new JmPattern(type, "9マルチ", "[54]"));
		add(new JmPattern(type, "35カスケード", "z"));
		add(new JmPattern(type, "18シャワー", "z1", shower));
		add(new JmPattern(type, "35マルチ シャワー", "[9bdfh][11111]", shower));
		add(new JmPattern(type, "12ミルズメス 333666999cccfffiiilll", "333666999cccfffiiilll", millsmess));
		add(new JmPattern(type, "[b9753]0020[22]0[222]0[2222]0"));
		add(new JmPattern(type, "123456789abcdefghijklmnopqrstuvwxyz", center));

		byte[] box1 = {13,0,3,0};
		byte[] box2 = JmPattern.reverse(box1);
		add(new JmPattern(type, "9ボックス", "u1q1m1i1e1a1612",
		 new byte[][] {g,box1,box2,box1,box2,box1,box2,box1,box2,box1,box2,box1,box2,box1,g}));
		add(new JmPattern(type, "trpnljhfdb97531"));
		add(new JmPattern(type, "Ken", "ken"));
		add(new JmPattern(type, "ペンタマルチ", "[56789]"));
		add(new JmPattern(type, "20コラムス A", "(k,k)", 10, 50,
		 new byte[][] {{29,3,29,3},{2,3,2,3},{26,3,26,3},{5,3,5,3},{23,3,23,3},{8,3,8,3},{20,3,20,3},{11,3,11,3},{17,3,17,3},{14,3,14,3},{14,3,14,3},{17,3,17,3},{11,3,11,3},{20,3,20,3},{8,3,8,3},{23,3,23,3},{5,3,5,3},{26,3,26,3},{2,3,2,3},{29,3,29,3}}));
		add(new JmPattern(type, "20コラムス B", "k", 10, 50,
		 new byte[][] {{20,3,20,3},{18,3,18,3},{16,3,16,3},{14,3,14,3},{12,3,12,3},{10,3,10,3},{8,3,8,3},{6,3,6,3},{4,3,4,3},{2,3,2,3},{2,3,2,3},{4,3,4,3},{6,3,6,3},{8,3,8,3},{10,3,10,3},{12,3,12,3},{14,3,14,3},{16,3,16,3},{18,3,18,3},{20,3,20,3}}));
	}

	public void setPackage05() throws JmException {
		clearIndex();
// edit {
		int type = 5;
// }
		add(new JmPattern(type, "1個の投げ方", "300"));
		add(new JmPattern(type, "2個の投げ方", "33022"));
		add(new JmPattern(type, "2個の連続", "330"));
		add(new JmPattern(type, "3個の練習 まず３回投げよう", "[32]3322"));
		add(new JmPattern(type, "3個の練習 3スローカスケード", "3", 20, 75));
		add(new JmPattern(type, "4個の練習 片手", "40"));
		add(new JmPattern(type, "4個の練習 片手コラムス", "40", new byte[][] {a,g,b,g}));
		add(new JmPattern(type, "5個の練習 2個", "50500"));
		add(new JmPattern(type, "5個の練習 3チェイス", "50505"));
		add(new JmPattern(type, "5個の練習 3フラッシュ", "55500"));
		add(new JmPattern(type, "5個の練習 3個", "52512"));
		add(new JmPattern(type, "5個の練習 4フラッシュ", "[52][52]55022[22][22]"));
		add(new JmPattern(type, "5個の練習 ギャップ A", "55550"));
		add(new JmPattern(type, "5個の練習 ギャップ B", "552"));
		add(new JmPattern(type, "5個の練習 4個", "5551"));
		add(new JmPattern(type, "5フラッシュ", "[522][52][52]5522[22][22]"));
		add(new JmPattern(type, "3 -> 5カスケード", "[32][32][32][32][32][32][52][52][52]555555522"));

		add(new JmPattern(type, "3ボックスの練習", "(2x,4)(0,2x)", 12, 50, new byte[][] {b,{10,0,2,0},{10,0,2,0},b}));
		add(new JmPattern(type, "3ミルズメスの練習", "330", 13, 50, millsmess));
	}
	
	public void setPackage06() throws JmException {
		clearIndex();
		int type = 6;
		add(new JmPattern(type, "[新規作成]", "1"));
	}	
	
}
