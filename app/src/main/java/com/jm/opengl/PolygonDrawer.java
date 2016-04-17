package com.jm.opengl;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class PolygonDrawer {
	private int[] vv = new int[3 * 4];
	private int[] cc = new int[4 * 4];
	private IntBuffer vvb = OpenGLGraphics.getIntBuffer(vv);
	private IntBuffer ccb = OpenGLGraphics.getIntBuffer(cc);

	public void drawBoard(GL10 gl, int x0, int y0, int z0, int x1, int y1, int z1, int red, int green, int blue, int alpha){
		int one = OpenGLGraphics.one;
		
		int i = 0;
		vv[i++] = x0 * one;
		vv[i++] = y0 * one;
		vv[i++] = z0 * one;
		vv[i++] = x1 * one;
		vv[i++] = y0 * one;
		vv[i++] = z0 * one;
		vv[i++] = x0 * one;
		vv[i++] = y1 * one;
		vv[i++] = z1 * one;
		vv[i++] = x1 * one;
		vv[i++] = y1 * one;
		vv[i++] = z1 * one;				
		i = 0;
		cc[i++] = red * one / 256;
		cc[i++] = green * one / 256;
		cc[i++] = blue * one / 256;
		cc[i++] = alpha * one / 256;
		cc[i++] = red * one / 256;
		cc[i++] = green * one / 256;
		cc[i++] = blue * one / 256;
		cc[i++] = alpha * one / 256;
		cc[i++] = red * one / 256;
		cc[i++] = green * one / 256;
		cc[i++] = blue * one / 256;
		cc[i++] = alpha * one / 256;
		cc[i++] = red * one / 256;
		cc[i++] = green * one / 256;
		cc[i++] = blue * one / 256;
		cc[i++] = alpha * one / 256;

		OpenGLGraphics.setIntBuffer(vvb, vv);
		OpenGLGraphics.setIntBuffer(ccb, cc);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vvb);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, ccb);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
}
