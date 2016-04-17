package com.jm;

import java.io.Serializable;
import java.util.Vector;

//public class JmPattern implements CharSequence, Serializable {
public class JmPattern implements Serializable {
	private static final long serialVersionUID = 1107291329482703361L;
	
	public static final byte[]		NORMAL = {13, 0, 4, 0};
	private static final byte[][]	NORMAL_PATTERN = new byte[][] {NORMAL};
	private static byte				speed = 10;
	private static boolean			mirror = false;
	private static boolean			showBody = true;
	private static boolean			showSiteswap = true;

	public static byte[] reverse(byte[] pattern) {
		return new byte[] {pattern[2], pattern[1], pattern[0], pattern[3]};
	}
	public static int getSpeed() {return speed;}
	public static boolean ifMirror(){return mirror;}
	public static boolean ifShowBody() {return showBody;}
	public static boolean ifShowSiteSwap() {return showSiteswap;}
	protected static void setSpeed(int s) {speed = (byte)s;}
	protected static void setIfMirror(boolean b){mirror = b;}
	protected static void setIfShowBody(boolean b) {showBody = b;}
	protected static void setIfShowSiteSwap(boolean b) {showSiteswap = b;}

	private int id;
	private int type;
	public int getId() {
		return id;
	}
	public int getType() {
		return type;
	}

	private SiteSwap		siteswap;
	private byte			height;
	private byte			dwell;
	private String			name;
	private byte[][]		motion;

//// edit {
//	private String siteswapString;
//	private String motionString;
//	
//	public String getSiteswapString() {
//		return siteswapString;
//	}
//	public String getMotionString() {
//		return motionString;
//	}
////}
	
	public JmPattern(JmPattern that) {
		this.id = that.id;
		this.name = that.name;
		this.siteswap = that.siteswap;
		this.motion = that.motion;
		this.height = that.height;
		this.dwell = that.dwell;
	}

	public JmPattern(int type, String name, String siteswap) {
		this(type, name, siteswap, 20, 50);
	}

	public JmPattern(int type, String name, String siteswap, byte[][] motion) {
		this(type, name, siteswap, 20, 50, motion);
	}

	public JmPattern(int type, String name, String siteswap, int height, int dwell) {
		this(type, name, siteswap, height, dwell, NORMAL_PATTERN);
	}

	public JmPattern(int type, String siteswap) {
		this(type, siteswap, NORMAL_PATTERN);
	}

	public JmPattern(int type, String siteswap, byte[][] motion) {
		this(type, siteswap, siteswap, 20, 50, motion);
	}

	public JmPattern(int type, String siteswap, int height, int dwell) {
		this(type, siteswap, siteswap, height, dwell);
	}

	public JmPattern(int type, String siteswap, int height, int dwell, byte[][] motion) {
		this(type, siteswap, siteswap, height, dwell, motion);
	}

//// edit {
//	public JmPattern(int type, String name, String siteswap, int height, int dwell, String motion) {
//		this(type, name, siteswap, height, dwell, convStringToByte(motion));
//	}
//
	public JmPattern(int id, int type, String name, String siteswap, int height, int dwell, byte[][] motion) {
		this(type, name, siteswap, height, dwell, (motion == null || motion.length <= 0)? NORMAL_PATTERN : motion);
		this.id = id;
	}
//// }

	public JmPattern(int type, String name, String siteswap, int height, int dwell, byte[][] motion) {
		this.id = -1;
		this.type = type;
		this.name = name;
		this.siteswap = SiteSwap.getInstance(siteswap);
		this.motion = motion;
		this.height = (byte)height;
		this.dwell = (byte)dwell;
//// edit {
//		this.siteswapString = siteswap;
//		this.motionString = convByteToString(motion);
//// }
	}

// edit {
//	public JmPattern(byte[] bytes) {
//		int i = 0;
//		this.height = bytes[i++];
//		this.dwell = bytes[i++];
//		int name_len = (int)bytes[i++] & 0xff;
//		int siteswap_len = (int)bytes[i++] & 0xff;
//		int motion_len = (bytes.length - 4 - name_len - siteswap_len) / 4;
//		byte[] b_name = new byte[name_len];
//		byte[] b_siteswap = new byte[siteswap_len];
//		this.motion = new byte[motion_len][4];
//		
//		for (int j = 0; j < b_name.length; j++)		b_name[j] = bytes[i++];
//		for (int j = 0; j < b_siteswap.length; j++)	b_siteswap[j] = bytes[i++];
//		for (int j = 0; j < motion.length; j++)	{	this.motion[j][0] = bytes[i++];
//													this.motion[j][1] = bytes[i++];
//													this.motion[j][2] = bytes[i++];
//													this.motion[j][3] = bytes[i++];}
//		this.name = new String(b_name);
//		this.siteswap = SiteSwap.getInstance(new String(b_siteswap));
//	}
// }
	
	public int motionSize()					{return motion.length;}
	public String getName()					{return name;}
	public byte getHeight()					{return height;}
	public byte getDwell()					{return dwell;}
	public SiteSwap getSiteSwap()			{return siteswap;}
	public byte getThrowPositionX(int index){return motion[index % motion.length][0];}
	public byte getThrowPositionY(int index){return motion[index % motion.length][1];}
	public byte getCatchPositionX(int index){return motion[index % motion.length][2];}
	public byte getCatchPositionY(int index){return motion[index % motion.length][3];}
	public String motionToString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < motion.length; i++) {
			buf.append('{')
			.append(motion[i][0]).append(',')
			.append(motion[i][1]).append("}{")
			.append(motion[i][2]).append(',')
			.append(motion[i][3]).append('}');
		}
		return buf.toString();
	}


	protected void setName(String name)		{this.name = name;}
	protected void setHeight(int height)	{this.height = (byte)height;}
	protected void setDwell(int dwell) 		{this.dwell = (byte)dwell;}

	// edit
	//protected boolean setSiteSwap(String siteswap) {
	public static boolean setSiteSwap(String siteswap) {
		SiteSwap s = SiteSwap.getInstance(siteswap);
		if (s == null) return false;
		// edit
		//this.siteswap = s;
		return true;
	}

	// edit
	//protected boolean setMotion(String motionString) {
	public static byte[][] getMotion(String motionString) {
		byte[][] motion = null;
		
		Vector<byte[]> motionV = new Vector<byte[]>();
		int index = 0;
		try {
			while (true) {
				byte[] oneMotion = new byte[4];
				index = motionString.indexOf('{', index);
				if (index < 0) break;
				int index1 = motionString.indexOf(',', index);

				if (index1 < 0)	return null;
				oneMotion[0] = Byte.parseByte(motionString.substring(index + 1, index1).trim());
				index = motionString.indexOf('}', index1);
				if (index < 0)	return null;
				oneMotion[1] = Byte.parseByte(motionString.substring(index1 + 1, index).trim());

				index = motionString.indexOf('{', index);
				if (index < 0)	return null;
				index1 = motionString.indexOf(',', index);
				if (index1 < 0)	return null;
				oneMotion[2] = Byte.parseByte(motionString.substring(index + 1, index1).trim());

				index = motionString.indexOf('}', index1);
				if (index < 0)	return null;
				oneMotion[3] = Byte.parseByte(motionString.substring(index1 + 1, index).trim());

				motionV.addElement(oneMotion);
			}
		} catch (NumberFormatException ex0) {
			return null;
		} catch (IndexOutOfBoundsException ex1) {
			return null;
		}
		motion = new byte[motionV.size()][4];
		for (int i = 0; i < motionV.size(); i++) {
			byte[] oneMotion = (byte[])motionV.elementAt(i);
			motion[i][0] = oneMotion[0];
			motion[i][1] = oneMotion[1];
			motion[i][2] = oneMotion[2];
			motion[i][3] = oneMotion[3];
		}
		return motion;
	}
	
	public byte[] getBytes() {
		byte[] b_name = name.getBytes();
		byte[] b_siteswap = siteswap.toString().getBytes();
		int len = 4 + b_name.length + b_siteswap.length + motion.length*4;
		byte[] bytes = new byte[len];
		
		int i = 0;
		bytes[i++] = height;
		bytes[i++] = dwell;
		bytes[i++] = (byte)(b_name.length & 0xff);
		bytes[i++] = (byte)(b_siteswap.length & 0xff);
		for (int j = 0; j < b_name.length; j++)		bytes[i++] = b_name[j];
		for (int j = 0; j < b_siteswap.length; j++)	bytes[i++] = b_siteswap[j];
		for (int j = 0; j < motion.length; j++)	{	bytes[i++] = motion[j][0];
													bytes[i++] = motion[j][1];
													bytes[i++] = motion[j][2];
													bytes[i++] = motion[j][3];}
		return bytes;
	}
	
	public String toString() {
		return name;
	}
		
//	@Override
//	public char charAt(int index) {
//		return name.charAt(index);
//	}
//	@Override
//	public int length() {
//		return name.length();
//	}
//	@Override
//	public CharSequence subSequence(int start, int end) {
//		return name.subSequence(start, end);
//	}
}