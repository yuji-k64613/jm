package com.jm.utility;

import android.graphics.Paint;

public class Resource {
//	public static final String		MSG_INIT_SUCCESS = "設定を初期化しました。";
//	public static final String		MSG_NOTHING = "登録されていません";
//	public static final String		ERROR_TITLE = "エラー";
//	public static final String		ERRMSG_DELETE = "最低１つは登録されている必要があります。";
//	public static final String		ERRMSG_SITESWAP = " は正しいサイトスワップ表現ではありません。";
//	public static final String		ERRMSG_MOTION = " は正しくありません。";
//	public static final String		ERRMSG_JVIEWER = "パラメータが不正です。パラメータを変更してもう一度実行してみてください。";
//
//	public static final String		HELP_TITLE = "ヘルプ";
//	public static final String		HELPMSG_INDIVIDUAL = "技ごとに固有の設定です。マイリストでのみ変更が保存されます。それ以外で行われた変更はメインメニューに戻るまで有効です。";
//	public static final String		HELPMSG_NAME = "パターンの名前を決定します。";
//	public static final String		HELPMSG_SITESWAP = "サイトスワップ表現を使って技を変更することができます。入力決定後に文法チェックが行われて、正しければ変更が適用されます。文法はJuggleMasterJavaに準じます。";
//	public static final String		HELPMSG_MOTION = "ジャグラーの手の動きを指定できます（玄人向け）。{x1,y1}{x2,y2}と指定します。ここで、x1,y1 は、ボールを取る x/y 座標、x2,y2 は投げる x/y 座標を示します。JuggleMasterJavaに準じます。";
//	public static final String		HELPMSG_HEIGHT = "ボールを投げ上げる高さを調節します。大きいほど高く投げ上げます。";
//	public static final String		HELPMSG_DWELLRATE = "ボールを持っている割合を調節します。小さいほどキャッチしたあとすぐに投げ上げます。";
//	public static final String		HELPMSG_COMMON = "すべての技に共通の設定です。変更はプログラムが終了するまで有効です。";
//	public static final String		HELPMSG_SPEED = "ジャグリングの速さを調節します。大きいほど早くなります。";
//	public static final String		HELPMSG_MIRROR = "ONにすると左右を反転します。";
//	public static final String		HELPMSG_SHOW_BODY = "OFFにするとジャグラーを表示しません。";
//	public static final String		HELPMSG_SHOW_SITESWAP = "OFFにするとサイトスワップを表示しません。";
//	public static final String		HELPMSG_SETENV = "この設定は保存されます。";
//	public static final String		HELPMSG_REDRAWRATE = "描画間隔を調節します。大きいほど動きはスムーズになりますが、処理が重くなります。";
//	public static final String		HELPMSG_BGCOLOR = "背景色を決定します。";
//	public static final String		HELPMSG_FGCOLOR = "前景色を決定します。";
//	public static final String		HELPMSG_EMCOLOR = "サイトスワップ表記の強調色を決定します。";
//	public static final String		HELPMSG_INITIALIZE = "上記の設定を初期状態に戻します。";

	public static int speed = 6;
	private static byte redrawrate = 30;

	public static float cx = 0.0f;
	public static float cy = 0.0f;
	public static long counter = 0;
	
	public static int charWidth() {
		Paint p = new Paint();
		return (int)(p.measureText("a") + 0.5);
	}

	public static int charHeight() {
		Paint p = new Paint();
		return (int)(p.getTextSize() + 0.5);
	}

	public static long getRedrawrate() {
		return redrawrate;
	}
}
