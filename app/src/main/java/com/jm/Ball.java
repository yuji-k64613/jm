package com.jm;

public class Ball {
	public static final int RADIUS = 10;
	static final int LMAX = 200;
	static final int OBJECT_UNDER = 0x02;
	static final int OBJECT_MOVE = 0x04;
	static final int OBJECT_MOVE2 = 0x08;

	static JugglingBalls balls;
	private int initBh;
	private int initC;
	private boolean initHand;

	private int bh;
	private int c;
	private int c0;
	private int chand;
	private int thand;
	private int gx, gy;
	private boolean isHand = false;
	private int status;

	public Ball(boolean isHand) {
		this.isHand = isHand;
	}
	
	public int getX() {
		return gx;
	}
	public int getY() {
		return gy;
	}

	protected void init(int bh, int c, boolean hand) {
		this.initBh = bh;
		this.initC = c;
		this.initHand = hand;
		reset();
	}
	
	protected void reset() {
		this.status = 0;
		this.bh = initBh;
		this.c = initC;
		if (initHand) {
			this.thand = 1;
			this.chand = 1;
		} else {
			this.thand = 0;
			this.chand = 0;
		}
		this.gx = 0;
		this.gy = 0;
	}
	
	private void setEnable(int st) {
		this.status |= st;
	}
	private void setDisable(int st) {
		this.status &= ~st;
	}
	private boolean isEnable(int st) {
		return ((this.status & st) != 0);
	}
	public boolean juggle(SiteSwap siteswap, long timeCount) {
		boolean ret = false;
		int tp;

		if (c < 0 && timeCount >= -c * balls.getTW()) {
			c = -c;
		}
		while (true) {
			tp = (int) (timeCount - balls.getTW() * Math.abs(c));
			if (tp < balls.getAW()) break;
			setDisable(OBJECT_UNDER);
			c0 = c;
			if (isHand) {
				c += 2;
				ret = true;
			} else {
				int t = c;
				if (siteswap.isSynchronous() && chand != 0) t++;
				int multi = balls.getNowNumberOfMultiplex(t);
				bh = siteswap.getValue(t, multi);
				c += Math.abs(bh);
				thand = chand;
				if (((bh & 1) != 0) || bh < 0) {
					chand = 1 - chand;
				}
				ret = true;
			}
		}

		if (c >= 0 && tp >= 0 && !isEnable(OBJECT_UNDER)) {
			setEnable(OBJECT_UNDER);

			if (isHand) {
				if (isEnable(OBJECT_MOVE2)) {
					setEnable(OBJECT_MOVE);
					setDisable(OBJECT_MOVE2);
				} else {
					setDisable(OBJECT_MOVE);
				}
			} else {
				int t = c;
				if (siteswap.isSynchronous() && chand != 0) t++;
				t %= siteswap.size();
				if (bh == 1) {
					setEnable(OBJECT_MOVE);
				} else {
					setDisable(OBJECT_MOVE);
				}
				for (int i = 0; i < siteswap.size(t); i++) {
					int h = siteswap.getValue(t, i);
					if (h == 1) {
						if (chand != 0) {
							balls.lhand.setEnable(OBJECT_MOVE2);
						} else {
							balls.rhand.setEnable(OBJECT_MOVE2);
						}
					}
					if (h != 2) {
						if (chand != 0) {
							balls.rhand.setEnable(OBJECT_MOVE2);
						} else {
							balls.lhand.setEnable(OBJECT_MOVE2);
						}
						setEnable(OBJECT_MOVE);
					}
				}
			}
		}
		int[] tpo = null, rpo = null;
		if (!isEnable(OBJECT_MOVE)) {
			if (c < 0) {
				tpo = balls.handPosition(this, -c, chand);
				rpo = tpo;
			} else if (isEnable(OBJECT_UNDER)) {
				tpo = balls.handPosition(this, c, chand);
				rpo = balls.handPosition(this, c + 2, chand);
				if (tpo[0] != rpo[0] || tpo[1] != rpo[1]) {
					rpo = balls.handPosition(this, c + 1, chand);
					if (tpo[0] != rpo[0] || tpo[1] != rpo[1]) {
						setEnable(OBJECT_MOVE);
					}
				}
			} else {
				tpo = balls.handPosition(this, c - 2, chand);
				rpo = balls.handPosition(this, c, chand);
				if (tpo[0] != rpo[0] || tpo[1] != rpo[1]) {
					tpo = balls.handPosition(this, c - 1, chand);
					if (tpo[0] != rpo[0] || tpo[1] != rpo[1]) {
						setEnable(OBJECT_MOVE);
					}
				}
			}
		}
		if (isEnable(OBJECT_MOVE)) {
			if (bh == 1) {
				tpo = balls.handPosition(this, c0 + 1, thand);
				rpo = balls.handPosition(this, c + 1, chand);
			} else if (isEnable(OBJECT_UNDER)) {
				tpo = balls.handPosition(this, c, chand);
				rpo = balls.handPosition(this, c + 1, chand);
			} else {
				tpo = balls.handPosition(this, c0 + 1, thand);
				rpo = balls.handPosition(this, c, chand);
			}
		}
		int D = 6000;

		if (!isHand && c < 0) {
			gy = D * tpo[1] * 12;
			if (tpo[0] == 0) {									// コメントアウトしても変わりなし？
				gx = 0;
				gy -= (D * tp * 20 / balls.getTW());
			} else if (tpo[0] > 0) {
				gx = (D * tpo[0] / 10) - (D * tp / 6 / balls.getTW());
			} else {
				gx = (D * tpo[0] / 10) + (D * tp / 6 / balls.getTW());
			}
		} else if (!isEnable(OBJECT_MOVE)) {					//動き始める前の手の位置？
			gx = D * tpo[0] / 10;
			gy = D * tpo[1] * 12;
		} else {												// 投げた後の位置？
			if (bh == 1) {
				gx = D * 2 * (tp - balls.getAW()) / balls.getTW() + D;
				gy = balls.getHigh(1);
			} else if (isEnable(OBJECT_UNDER)) {
				gx = D * 2 * tp / balls.getAW() - D;
				gy = balls.getHigh(0);
			} else {
				gx = D * 2 * tp / (balls.getTW() * Math.abs(bh) - balls.getAW()) + D;
				gy = balls.getHigh(Math.abs(bh));
			}
			gy *= (D - gx * gx / D);
			gy += (gx * (rpo[1] - tpo[1]) + D * (rpo[1] + tpo[1])) * 6;
			gx = (gx * (rpo[0] - tpo[0]) + D * (rpo[0] + tpo[0])) / 20;
		}
		gx = gx * 60 / D;
		gy = -gy / D;
		if (isHand) {
			if (chand != 0)	gx += RADIUS;
			else			gx -= RADIUS;
		} else {
			gy -= RADIUS;
		}
		return ret;
	}
}
