package com.jm;

import com.jm.opengl.JuggleDrawer;
import com.jm.utility.Resource;

public class JugglingBalls {

	private static final int GRAVITYx10 = 98;

	protected Ball		 	rhand;
	protected Ball		 	lhand;
	private Ball[]			ball;
	private int 			tw;
	private int 			aw;
	private int[]			high;
	private int[]			multiplex;
	private JmPattern		jp;
	private SiteSwap		siteswap;

	public JugglingBalls(Ball rhand, Ball lhand) {
		Ball.balls = this;
		this.rhand = rhand;
		this.lhand = lhand;
	}
	
	public boolean initialize(JmPattern jp) {
		this.jp = jp;
		this.siteswap = jp.getSiteSwap();
		if (!newBallInstance()){
			return false;
		}
		if (!setTW(jp)){
			return false;
		}
		multiplex = new int[siteswap.size()];
		for (int i = 0; i < multiplex.length; i++){
			multiplex[i] = 0;
		}
		return true;
	}

	public boolean juggle(long counter) {
		for(int i = 0; i < ball.length; i++)
			ball[i].juggle(siteswap, counter);
		boolean ret = rhand.juggle(siteswap, counter);
		ret |= lhand.juggle(siteswap, counter);
		return ret;
	}

	public int getYRange() {
		int gy_max = 80;
		int gy_min = -200;
		int tmax = siteswap.getMaxValue();
		if (tmax < 3) tmax = 3;
		tmax = (tmax + siteswap.size() + jp.motionSize()) * tw;
		for(int t = 0; t < tmax; t++){
			juggle(t);
			for(int i = 0; i < ball.length; i++) {
				gy_max = Math.max(gy_max, ball[i].getY());
				gy_min = Math.min(gy_min, ball[i].getY());
			}
		}
		reset();
		return gy_max - gy_min;
	}

	public void drawBalls(JuggleDrawer drawer) {
		for(int i = 0; i < ball.length; i++)
			drawer.drawBall(i, ball[i].getX(), ball[i].getY());
	}

	public void reset() {
		for (int i = 0; i < ball.length; i++)
			ball[i].reset();
		rhand.reset();
		lhand.reset();
	}
	
	protected int[] handPosition(Ball b, int c, int h) {
		int[] pos = new int[2];

		if (!siteswap.isSynchronous() && h == 0) {
			c--;
		}
		if ((c & 1) != 0) {
			pos[0] = jp.getCatchPositionX(c - h);
			pos[1] = jp.getCatchPositionY(c - h);
		} else {
			pos[0] = jp.getThrowPositionX(c + 1 - h);
			pos[1] = jp.getThrowPositionY(c + 1 - h);
		}
		if (h == 0) {
			pos[0] = -pos[0];
		}
		return pos;
	}

	protected int getTW() {
		return tw;
	}

	protected int getAW() {
		return aw;
	}
	
	protected int getHigh(int i) {
		return high[i];
	}

	private boolean newBallInstance() {
		this.rhand.init(2, 0, true);
		if (!siteswap.isSynchronous())	this.lhand.init(2, -1, false);
		else							this.lhand.init(2, 0, false);
		this.ball = new Ball[siteswap.getNumberOfBalls()];

		int r[] = new int[siteswap.size() + siteswap.getMaxValue()];
		for (int i = 0; i < ball.length; i++){
			int j = 0;
			while (j < r.length && r[j] == siteswap.size(j)) j++;
			if (i == ball.length){
				if (j == r.length){
					break;
				}
				else {
					return false;
				}
			}
			int ballStatusC = -j;
			if (siteswap.isSynchronous()) ballStatusC = -((int)(j / 2)) * 2;
			boolean ballStatusHand = false;
			if (siteswap.isSynchronous() == (j % 2 == 1)) ballStatusHand = true;
			ball[i] = new Ball(false);
			ball[i].init(0, ballStatusC, ballStatusHand);

			while (j < r.length) {
				if (r[j] == siteswap.size(j)){
					return false;
				}
				r[j]++;
				int k = siteswap.getValue(j, siteswap.size(j) - r[j]);
				// シンクロパターンでクロスの場合（Siteswapで　x がつく部分）
				if (siteswap.isSynchronous() && k < 0) {
					if (j % 2 == 0) {
						j += -k + 1;
					} else {
						j += -k - 1;
					}
				} else {
					j += k;
				}
			}
		}
		reset();
		return true;
	}

	private boolean setTW(JmPattern jp) {
		// ga: x10, height: x100, dwell: x100, speed: x10
		int pmax = siteswap.getMaxValue();
		if (pmax < 3) pmax = 3;
		int dwell = jp.getDwell() * 2;

		long tw1 = 24000 * pmax * jp.getHeight();
		tw1 /= square(pmax * 100 - dwell);
		high = new int[pmax+1];
		high[0] = -48;
		high[1] = (int)(tw1);
		for (int i = 2; i <= pmax; i++){
			high[i] = (int)(tw1 * square(100 * i - dwell) / 10000);
		}
		tw1 = tw1 * 100 * square(Resource.getRedrawrate()) 
				/ (3 * GRAVITYx10 * square(JmPattern.getSpeed()));
		// edit {
		if (tw1 == 0){
			tw1 = 1;
		}
		// }
		tw = (int)squareRoot(tw1);
		if (tw == 0) return false;
		aw = tw * dwell / 100;
		if (aw < 1)			aw = 1;
		if (aw > tw * 2 - 1)aw = tw * 2 - 1;
		return true;
	}

	protected int getNowNumberOfMultiplex(int t) {
		t %= siteswap.size();
		int ret = multiplex[t];
		if (++multiplex[t] >= siteswap.size(t)) multiplex[t] = 0;
		return ret;
	}

	public static final long square(long x){
		return x * x;
	}

	public static final long squareRoot(long x) {
		if (x <= 0) return 0;
		
		long s = 1;
		long t = x;
		while (s < t) {
			s <<= 1;
			t >>= 1;
		}
		do {
			t = s;
			s = (x / s + s) >> 1;
		} while (s < t);
		return t;
	}
}
