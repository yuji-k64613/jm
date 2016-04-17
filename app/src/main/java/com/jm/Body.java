package com.jm;

import com.jm.opengl.JuggleDrawer;

public class Body {
	// 手
	private static final int[] HAND_X = {18, 6};
	private static final int[] HAND_Y = {-1,-1};

	// 頭
	private int hx, hy, hr;
	// 腕
	private int[] rx, ry, lx, ly;
	// 手
	private Ball rhand;
	private Ball lhand;

	public Body() {
		this.rhand = new Ball(true);
		this.lhand = new Ball(true);
		rx = new int[6+HAND_X.length];
		ry = new int[6+HAND_Y.length];
		lx = new int[6+HAND_X.length];
		ly = new int[6+HAND_Y.length];
	}
	public Ball getRightHand() {
		return rhand;
	}

	public Ball getLeftHand() {
		return lhand;
	}

	public void drawBody(JuggleDrawer drawer)
	{
		for(int i = 1; i < rx.length; i++) {
			drawer.drawLine(rx[i-1], ry[i-1], rx[i], ry[i]);
			drawer.drawLine(lx[i-1], ly[i-1], lx[i], ly[i]);
		}
		drawer.drawCircle(hx, hy, hr);
	}
	
	public void move() {		
		int i = HAND_X.length;
		rx[i] = rhand.getX();
		ry[i] = rhand.getY();
		lx[i] = lhand.getX();
		ly[i] = lhand.getY();

		for (int j = 0; j < HAND_X.length; j++) {
			rx[j] = rx[i] - HAND_X[j];
			ry[j] = ry[i] - HAND_Y[j];
			lx[j] = lx[i] + HAND_X[j];
			ly[j] = ly[i] - HAND_Y[j];
		}
		i++;

		rx[i] = rx[i-1] / 3 + 60;
		lx[i] = lx[i-1] / 3 - 60;
		ry[i] = ry[i-1] / 2 - 10;
		ly[i] = ly[i-1] / 2 - 10;
		i++;
		rx[i] = rx[i-1] / 4 + 45;
		lx[i] = lx[i-1] / 4 - 45;
		ry[i] = ry[i-1] / 3 - 63;
		ly[i] = ly[i-1] / 3 - 63;
		i++;
		rx[i] = rx[i-1] / 3 + 20;
		lx[i] = lx[i-1] / 3 - 20;
		ry[i] = ry[i-1] / 3 - 59;
		ly[i] = ly[i-1] / 3 - 59;
		int mx = rx[i] + lx[i];
		int my = ry[i] + ly[i];
		i++;
		rx[i] = (mx + rx[i-1]) / 3;
		lx[i] = (mx + lx[i-1]) / 3;
		ry[i] = (my + ry[i-1]) / 3;
		ly[i] = (my + ly[i-1]) / 3;
		i++;
		hx = mx / 2;
		hy = my / 3 - 52;
		hr = 20;
		rx[i] = hx + 12;
		lx[i] = hx - 12;
		ry[i] = hy + 20;
		ly[i] = ry[i];
	}
}
