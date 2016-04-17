package com.jm.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;

public class OpenGLSphere {
	private static final int color[] = { 0x0000ff, 0xff0000, 0xffff00,
			0x00ff00, 0xffa000, 0xa0522d, 0xff7f2b, 0x87ceeb, 0xb03060,
			0xffc0cb, 0xff00ff, 0xa020f0, 0x00ffff };
	private static OpenGLSphere instance = null;

	private IntBuffer mVertexBuffer;
	private IntBuffer[] mColorBuffers;
	//private IntBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;
//	private int x;
//	private int y;
//	private int z;
//	private int mRadius;
	private int indicesNum;

	public static OpenGLSphere getInstance() {
		if (instance == null) {
			instance = new OpenGLSphere();
		}
		return instance;
	}

	public static void dispose() {
		instance = null;
	}

	private OpenGLSphere() {

	}

	protected void create(int mRadius, int segX, int segY) {
		int gridU = segX;
		int gridV = segY;
		//int gridU1 = gridU + 1;
		int gridV1 = gridV + 1;
		int incU = 360 / gridU;
		int incV = 2 * mRadius / gridV;
		int cnt;

		//this.mRadius = mRadius;

		// vertices
		// vertices配列を宣言
		int[] vertices = new int[(2 + (gridV1 - 2) * gridU) * 3];
		cnt = 0;
		// 一つのtopポイントの設置

		vertices[cnt++] = 0;
		vertices[cnt++] = -mRadius;
		vertices[cnt++] = 0;

		// 切断面の設置
		double d = mRadius;
		double y, t, r;
		for (int iv = 1; iv < gridV1 - 1; ++iv) {
			y = iv * incV - d;
			r = Math.sqrt(d * d - y * y);
			for (int iu = 0; iu < gridU; ++iu) {
				t = iu * incU * Math.PI / 180;
				vertices[cnt++] = (int) (r * Math.cos(t));
				vertices[cnt++] = (int) y;
				vertices[cnt++] = (int) (r * Math.sin(t));
			}
		}

		// もう一つtopポイントの設置
		vertices[cnt++] = 0;
		vertices[cnt++] = mRadius;
		vertices[cnt++] = 0;

		// indices
		byte[] indices = new byte[((gridV - 1) * gridU * 2) * 3];
		cnt = 0;
		for (int iu = 0; iu < gridU; ++iu) {
			indices[cnt++] = 0;
			indices[cnt++] = (byte) ((iu + 1) % gridU + 1);
			indices[cnt++] = (byte) (iu + 1);
		}
		for (int iv = 1; iv < gridV1 - 2; ++iv) {
			for (int iu = 0; iu < gridU; ++iu) {
				int m = (iv - 1) * gridU;
				// Triangle A
				indices[cnt++] = (byte) (iu + 1 + m);
				indices[cnt++] = (byte) ((iu + 1) % gridU + 1 + m);
				indices[cnt++] = (byte) (iu + 1 + gridU + m);

				// Triangle B
				indices[cnt++] = (byte) ((iu + 1) % gridU + 1 + gridU + m);
				indices[cnt++] = (byte) (iu + 1 + gridU + m);
				indices[cnt++] = (byte) ((iu + 1) % gridU + 1 + m);
			}
		}

		int n = (2 + (gridV1 - 2) * gridU) - 1;
		for (int iu = n - gridU; iu < n; ++iu) {
			indices[cnt++] = (byte) iu;
			indices[cnt++] = (byte) (iu % gridU + n - gridU);
			indices[cnt++] = (byte) n;
		}
		indicesNum = indices.length;

        int cr;
        int cg;
        int cb;
		mColorBuffers = new IntBuffer[color.length];
		for (int j = 0; j < color.length; j++){
			int[] colors = new int[(2 + (gridV1 - 2) * gridU) * 4];
			cnt = 0;
	        cr = Color.red(color[j]);
	        cg = Color.green(color[j]);
	        cb = Color.blue(color[j]);
			for (int i = 0; i < colors.length; i += 4) {
	            colors[cnt++] = cr * 0x10000 / 255;
	            colors[cnt++] = cg * 0x10000 / 255;
	            colors[cnt++] = cb * 0x10000 / 255;
				colors[cnt++] = 0x10000;
			}
			mColorBuffers[j] = createIntBuffer(colors);
		}
		
		mVertexBuffer = createIntBuffer(vertices);
		mIndexBuffer = createByteBuffer(indices);
	}

	public void draw(GL10 gl, int index) {
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffers[index
				% color.length]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indicesNum, GL10.GL_UNSIGNED_BYTE,
				mIndexBuffer);
	}

	/**
	 * IntBufferの作成
	 * 
	 * @param intArray
	 * @return
	 */
	public IntBuffer createIntBuffer(int[] intArray) {
		IntBuffer tmpIb;
		/* 色の値 */
		ByteBuffer tmpBb = ByteBuffer.allocateDirect(intArray.length * 4);
		tmpBb.order(ByteOrder.nativeOrder());
		tmpIb = tmpBb.asIntBuffer();
		tmpIb.put(intArray);
		tmpIb.position(0);

		return tmpIb;
	}

	/**
	 * ByteBufferの作成
	 * 
	 * @param byteArray
	 * @return
	 */
	public ByteBuffer createByteBuffer(byte[] byteArray) {

		/* 色の値 */
		ByteBuffer tmpBb = ByteBuffer.allocateDirect(byteArray.length);
		tmpBb.put(byteArray);
		tmpBb.position(0);

		return tmpBb;
	}
}
