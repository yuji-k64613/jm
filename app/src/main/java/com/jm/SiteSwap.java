package com.jm;

import java.io.Serializable;
import java.util.Hashtable;

public class SiteSwap implements Serializable {
	private static final long serialVersionUID = 8397505993446975803L;
	private static final Hashtable<String, SiteSwap> MAP = new Hashtable<String, SiteSwap>();
	private static final int MAX_N_SITESWAP = 255;
	private static final int MAX_N_MULTIPLEX = 11;
	private static final byte CROSS = (byte)'x' - 'a' + 10;
	private static final byte COMMA = -1;  
	private static final byte BRA = -2;
	private static final byte KET = -3;
	private static final byte PAR = -4;
	private static final byte ENTHESIS = -5;
	private static final byte ERROR = Byte.MIN_VALUE;

	/*********************************************************************
	 インスタンスを取得します
	 *********************************************************************/
	protected static SiteSwap getInstance(String siteswap) {
		if (MAP.containsKey(siteswap)) return (SiteSwap)MAP.get(siteswap);
		SiteSwap ss = new SiteSwap();
		if (!ss.setSiteswap(siteswap)) return null;
		MAP.put(siteswap, ss);
		return ss;
	}
	public static void clearCashe() {
		MAP.clear();
	}

	/*********************************************************************
	 *********************************************************************/
	private static String toSiteswapString(byte b) {
		String ret = "";
		if (b < 0) {
			b = (byte)-b;
			ret = "x";
		}
		if (b < 10) return b + ret;
		return (char)('a' + b - 10) + ret;
	}

	/*********************************************************************
	 *********************************************************************/
	private static byte toSiteswapByte(char c) {
		switch(c) {
		case '(' : return PAR;
		case ')' : return ENTHESIS;
		case ',' : return COMMA;
		case '[' : return BRA;
		case ']' : return KET;
		default:
			if ('0' <= c && c <= '9') 		return (byte)(c - '0');
			else if ('a' <= c && c <= 'z')	return (byte)(c - 'a' + 10);
			else if ('A' <= c && c <= 'Z')	return (byte)(c - 'A' + 10);
		}
		return ERROR;
	}


	private byte[][]	siteswap;
	private boolean		isSynchronous;
	private byte		maxValue;
	private int 		number;

	/*********************************************************************
	 インスタンスを生成します
	 *********************************************************************/
	private SiteSwap()
	{
		this.siteswap = null;
		this.isSynchronous = false;
		this.number = 0;
		this.maxValue = 0;
	}
	
	/*********************************************************************
	 i番目のサイトスワップ値を返します
	 i番目がマルチの場合はその j個目を返します。
	 *********************************************************************/
	public byte getValue(int i, int j) {
		i = i % siteswap.length;
		return siteswap[i][j % siteswap[i].length];
	}

	/*********************************************************************
	 サイトスワップ値の最大値を返します
	 *********************************************************************/
	public int getMaxValue() {
		return maxValue;
	}

	/*********************************************************************
	 ボールの個数を返します
	 *********************************************************************/
	public int getNumberOfBalls() {
		return number;
	}
	
	/*********************************************************************
	 シンクロならばtrueを返します
	 *********************************************************************/
	public boolean isSynchronous() {
		return isSynchronous;
	}

	/*********************************************************************
	 サイトスワップの長さを返します
	 *********************************************************************/
	public int size() {
		return siteswap.length;
	}
	
	/*********************************************************************
	 index番目のサイトスワップ値のマルチ数を返します
	 *********************************************************************/
	public int size(int index) {
		return siteswap[index % siteswap.length].length;
	}
	
	/*********************************************************************
	 *********************************************************************/
	public String[] getStrings() {
		if (isSynchronous()) return getSynStrings();		
		String[] ret = new String[siteswap.length];
		for(int i = 0; i < siteswap.length; i++){
			ret[i] = getString(i);
		}
		return ret;
	}
	
	/*********************************************************************
	 *********************************************************************/
	private String[] getSynStrings() {
		String[] ret = new String[siteswap.length/2];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = '(' + getString(i*2) + ',' + getString(i*2+1) + ')';
		}
		return ret;
	}

	/*********************************************************************
	 *********************************************************************/
	private String getString(int i) {
		if (siteswap[i].length == 0) return "0";
		if (siteswap[i].length == 1) return toSiteswapString(siteswap[i][0]);
		StringBuffer p = new StringBuffer("[");
		for (int j = 0; j < siteswap[i].length; j++){
			p.append(toSiteswapString(siteswap[i][j]));
		}
		p.append(']');
		return p.toString();
	}

	/*********************************************************************
	 *********************************************************************/
	public String toString() {
		StringBuffer str = new StringBuffer("");
		String[] s = getStrings();
		for (int i = 0; i < s.length; i++)
			str.append(s[i]);
		return str.toString();
	}
	
	/*********************************************************************
	 サイトスワップ表記法で書かれた文字列を解析してセットします
	 
	 @return	文法エラーならばfalse
	 *********************************************************************/
	private boolean setSiteswap(String s) {
		if (number != 0)					return false;
		if (s == null)						return false;
		s = s.trim();
		if (s.length() < 0)					return false;
		if (s.length() > MAX_N_SITESWAP)	return false;
		if (s.charAt(0) == '(') isSynchronous = true;

		int parenthesisFlag = 0;
		boolean braketFlag = false;
		byte[][] patt = new byte[s.length()][MAX_N_MULTIPLEX];
		int[] patts = new int[s.length()];
		int pattw = 0;
		byte a = 0;

		for (int ch = 0; ch < s.length(); ch++) {
			byte t = toSiteswapByte(s.charAt(ch));
			switch (t) {
			case ERROR : return false;
			case BRA :				// [
				braketFlag = true;
				patts[pattw] = 0;
				continue;
			case KET :				// ]
				if (!braketFlag) 			return false;
				braketFlag = false;
				pattw++;
				continue;
			case PAR :				// (
				if (!isSynchronous)			return false;
				if (parenthesisFlag != 0)	return false;
				parenthesisFlag = 1;
				continue;
			case ENTHESIS :			// )
				if (!isSynchronous)			return false;
				if (parenthesisFlag != 5)	return false;
				parenthesisFlag = 0;
				continue;
			case COMMA :			// ,
				if (!isSynchronous)			return false;
				if (parenthesisFlag != 2)	return false;
				parenthesisFlag = 4;
				continue;
			case CROSS :			// x
				if (!isSynchronous)			break;
				if (parenthesisFlag != 2 && parenthesisFlag != 5) return false;
				if (braketFlag) patt[pattw][patts[pattw] - 1] = (byte)-a;
				else patt[pattw - 1][0] = (byte)-a;
				continue;
			}
			a = t;
			if (isSynchronous) {
				if (a % 2 != 0) return false;
				if (!braketFlag && parenthesisFlag != 1 && parenthesisFlag != 4) return false;
				if (parenthesisFlag == 1) parenthesisFlag = 2;
				else if (parenthesisFlag == 4) parenthesisFlag = 5;
			}
			if (braketFlag) {
				if (a == 0) return false;
				patt[pattw][patts[pattw]++] = a;
				if (patts[pattw] > MAX_N_MULTIPLEX) return false;
			} else {
				patt[pattw][0] = a;
				if (a == 0)	patts[pattw++] = 0;
				else		patts[pattw++] = 1;
			}
		}
		if (parenthesisFlag != 0 || braketFlag || pattw == 0) return false;

		siteswap = new byte[pattw][];
		for (int i = 0; i < siteswap.length; i++) {
			siteswap[i] = new byte[patts[i]];
			for (int j = 0; j < siteswap[i].length; j++) {
				siteswap[i][j] = patt[i][j];
				int b = Math.abs(siteswap[i][j]);
				number += b;
				maxValue = (byte)Math.max(maxValue, b);
			}
		}
		if (number % siteswap.length != 0) return false;
		number /= siteswap.length;
		if (number == 0) return false;
		return true;
	}
}
