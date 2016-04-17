package com.jm.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class OpenGLGraphics {
	public static float PAI = (float)Math.PI;
	public static int one = 0x10000;
	private OpenGLSphere sphere = null;

	//private int center;
	private int height;
	private static OpenGLGraphics instance = null;

	private int N = 360 / 10;
	private int[] vv = new int[3 * 2];
	private int[] cc = new int[4 * 2];
	private IntBuffer vvb = getIntBuffer(vv);
	private IntBuffer ccb = getIntBuffer(cc);
	int[] ww = new int[N * 3];
	int[] dd = new int[N * 4];
	private IntBuffer wwb = getIntBuffer(ww);
	private IntBuffer ddb = getIntBuffer(dd);	
	
	private OpenGLGraphics() {

	}

	public static void dispose(){
		OpenGLSphere.dispose();
		instance = null;
	}
	
	public void translate(int center, int height) {
		//this.center = center;
		this.height = height;
	}

	public static OpenGLGraphics getInstance() {
		if (instance == null) {
			instance = new OpenGLGraphics();
		}
		return instance;
	}

	public void begin(GL10 gl){
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void end(GL10 gl){
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);		
	}
	
	public void drawLine(GL10 gl, int x0, int y0, int x1, int y1, int size) {
		drawLineY(gl, x0, y0, 0, x1, y1, 0, 0, 0, 0, size);
	}
	
	public void drawLineY(GL10 gl, int x0, int y0, int z0, int x1, int y1, int z1, int red, int green, int blue, int size) {
		y0 = -y0;
		y0 += height / 2;
		y1 = -y1;
		y1 += height / 2;
		
		drawLine(gl, x0, y0, z0, x1, y1, z1, red, green, blue, size);		
	}
	
	public void drawLine(GL10 gl, int x0, int y0, int z0, int x1, int y1, int z1, int red, int green, int blue, int size) {
		int alpha = 256;

		int i = 0;
		vv[i++] = x0 * one;
		vv[i++] = y0 * one;
		vv[i++] = z0 * one;
		vv[i++] = x1 * one;
		vv[i++] = y1 * one;
		vv[i++] = z1 * one;
		i = 0;
		cc[i++] = one * red / 255;
		cc[i++] = one * green / 255;
		cc[i++] = one * blue / 255;
		cc[i++] = one * alpha / 255;
		cc[i++] = one * red / 255;
		cc[i++] = one * green / 255;
		cc[i++] = one * blue / 255;
		cc[i++] = one * alpha / 255;
		drawLine(gl, vvb, vv, ccb, cc, size, GL10.GL_LINES, 2);
	}

	public void drawCircle(GL10 gl, int x, int y, int r, int size) {
		int red = 0;
		int green = 0;
		int blue = 0;
		int alpha = 256;

		int rx;
		int ry;
		int rz = 0;
		
		y = -y;
		y += height / 2;
		
		for (int i = 0; i < N; i++){
			rx = (int)((r * Math.cos(2.0 * PAI * (float)i / (float)N) + x) * one);
			ry = (int)((r * Math.sin(2.0 * PAI * (float)i / (float)N) + y) * one);

			ww[i * 3 + 0] = rx;
			ww[i * 3 + 1] = ry;
			ww[i * 3 + 2] = rz;

			dd[i * 4 + 0] = one * red / 255;
			dd[i * 4 + 1] = one * green / 255;
			dd[i * 4 + 2] = one * blue / 255;
			dd[i * 4 + 3] = one * alpha / 255;
		}
		drawLine(gl, wwb, ww, ddb, dd, size, GL10.GL_LINE_LOOP, N);
	}
	
	public void drawImage(GL10 gl, int i, int x, int y, int z, int size) {
		y = -y;
		y += height / 2;

		if (sphere == null){
			sphere = OpenGLSphere.getInstance();
			sphere.create(one * size, 7, 7);
		}
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		sphere.draw(gl, i);
		gl.glPopMatrix();
	}

	private void drawLine(GL10 gl, IntBuffer vb, int[] vx, IntBuffer cb, int[] cl, int size, int mode, int pointNum) {
		//int size = 1;

		gl.glLineWidth(size);
		setIntBuffer(vb, vx);
		setIntBuffer(cb, cl);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vb);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, cb);
		gl.glDrawArrays(mode, 0, pointNum * 1);
	}

	public static IntBuffer getIntBuffer(int[] table) {
		ByteBuffer bb = ByteBuffer.allocateDirect(table.length * 4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer ib = bb.asIntBuffer();
		return ib;
	}
	
	public static void setIntBuffer(IntBuffer ib, int[] table){
		ib.put(table);
		ib.position(0);
	}
}
