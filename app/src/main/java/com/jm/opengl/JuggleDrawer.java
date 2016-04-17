package com.jm.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.jm.Ball;
import com.jm.JmPattern;
import com.jm.JuggleView;
import com.jm.Juggler;
import com.jm.JugglingBalls;
import com.jm.utility.JmException;
import com.jm.utility.Resource;

// JugglerCanvasの代わり
public class JuggleDrawer {
	private static final int DENOMINATOR = 1000;
	private static final int N = 2;
	
	private JuggleView view;

	private int diameter;
	private int center;
	private int base = 60;
	// private int magnification = 400;
	private int magnification = -1;
	private int ssHeight;

	//private JmPattern jp;
	private Juggler handler;
	private JugglingBalls balls;

	private GL10 currentGL10;
	private PolygonDrawer polygonDrawer = new PolygonDrawer();
	private float cr = -1;
	

	//public JuggleDrawer(JuggleView view, int id) throws JmException {
	public JuggleDrawer(JuggleView view, JmPattern jp) throws JmException {
		this.view = view;

//		JmPattern[] list = Dao.getInstance().getFromId(id);
//		if (list.length <= 0) {
//			throw new JmException();
//		}
//		jp = list[0];
		handler = new Juggler(this);
		handler.start(jp);
	}

	public void clear() {
		magnification = -1;
		currentGL10 = null;
		OpenGLGraphics.dispose();
	}

	public void setBalls(JugglingBalls balls) {
		this.balls = balls;
	}

	protected void onDrawInit() {
		center = getWidth() / 2;
		OpenGLGraphics g = OpenGLGraphics.getInstance();
		g.translate(center, getHeight());
		ssHeight = Resource.charHeight() + 2;

		this.setImageHeight(balls.getYRange());
		this.createBalls(handler.getSiteSwap().getNumberOfBalls());
	}

	void setImageHeight(int yrange) {
		magnification = DENOMINATOR;
		int height = getHeight() - ssHeight;
		if (yrange > 0) {
			magnification = magnification * height / yrange;
		}
		base = (int) (getHeight() - 55 * magnification / DENOMINATOR);
	}

	public void createBalls(int number) {
		diameter = translateR(Ball.RADIUS) * 2;
		if (diameter < 2) {
			diameter = 2;
		}
	}

	public int getWidth() {
		return view.getSurfaceWidth();
	}

	public int getHeight() {
		return view.getSurfaceHeight();
	}

	public void update() {
		handler.updateJuggler();
	}

	public void draw(GL10 gl) {
		if (magnification < 0) {
			onDrawInit();
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		if (cr < 0){
			cr = getHeight() / 2;
		}
		float hm = (int)cr;
		float lm = OpenGLActivityBase.LN * hm / OpenGLActivityBase.HN;
		float r = lm;
		float cx = Resource.cx;
		float cy = Resource.cy;

		GLU.gluLookAt(gl,
				(float) (r * Math.sin(cx) * Math.cos(cy)),
				(float) (r * Math.sin(cy)),
				(float) (r * Math.cos(cx) * Math.cos(cy)),
				0, 0, 0,
				(float) (r * Math.sin(cx) * Math.cos(cy)),
				(float) (r * Math.sin(cy)) + 1,
				(float) (r * Math.cos(cx) * Math.cos(cy)));
		
		currentGL10 = gl;
		OpenGLGraphics g = OpenGLGraphics.getInstance();
		g.begin(gl);
		drawGround(g);
		handler.drawJuggler();
		g.end(gl);
	}

	private void drawGround(OpenGLGraphics g){
		int w = this.getWidth();
		int h = (int)((-this.getHeight() / 2) * 1.05f);
		int z = (int)((h / 10) * 1.5f);
		int ww = w / 10;
		int n;
		int m = 224;
		float dn = 0.9f;
		int size = 1;
		
		n = m;
		g.drawLine(getGL(), -ww * 5, h, z * 3, ww * 5, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * 5, h, z * 1, ww * 5, h, z * 1, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * 5, h, z * -1, ww * 5, h, z * -1, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * 5, h, z * -3, ww * 5, h, z * -3, n, n, n, size);
		n = (int)(n * dn);

		n = m;
		g.drawLine(getGL(), -ww * 5, h, -z * 3, -ww * 5, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * 3, h, -z * 3, -ww * 3, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * 1, h, -z * 3, -ww * 1, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * -1, h, -z * 3, -ww * -1, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * -3, h, -z * 3, -ww * -3, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
		g.drawLine(getGL(), -ww * -5, h, -z * 3, -ww * -5, h, z * 3, n, n, n, size);
		n = (int)(n * dn);
	
		polygonDrawer.drawBoard(getGL(), ww * 5, h, -z * 3, ww * -5, h, z * 3, 160, 160, 160, 0);
	}
	
	public void rotate(float vx, float vy, float vr) {
		float cx = Resource.cx;
		float cy = Resource.cy;
		float dy = 0.001f;
		
		cx -= vx;
		if (cx < -(float)Math.PI / 2){
			cx = -(float)Math.PI / 2;
		}
		else if (cx > (float)Math.PI / 2){
			cx = (float)Math.PI / 2;
		}
		
		cy += vy;
		if (cy < -(float)Math.PI / 2 + dy){
			cy = -(float)Math.PI / 2 + dy;
		}
		else if (cy > (float)Math.PI / 2 - dy){
			cy = (float)Math.PI / 2 - dy;
		}

		if (cr >= 0){
			cr -= vr;
			int h = getHeight();
			if (cr < h / 6){
				cr = h / 6;
			}
			else if (cr > h * 3){
				cr = h * 3;
			}
		}
		
		Resource.cx = cx;
		Resource.cy = cy;
	}

	private GL10 getGL() {
		return currentGL10;
	}

	public void drawBall(int i, int x, int y) {
		OpenGLGraphics g = OpenGLGraphics.getInstance();
		x = translateX(x);
		y = translateY(y);
		g.drawImage(getGL(), i, x, y, diameter / 2, diameter / 2);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		OpenGLGraphics g = OpenGLGraphics.getInstance();
		x1 = translateX(x1);
		y1 = translateY(y1);
		x2 = translateX(x2);
		y2 = translateY(y2);
		g.drawLine(getGL(), x1, y1, x2, y2, N);
	}

	public void drawCircle(int x, int y, int r) {
		OpenGLGraphics g = OpenGLGraphics.getInstance();
		x = translateX(x);
		y = translateY(y);
		r = translateR(r);
		g.drawCircle(getGL(), x, y, r, N);
	}

	private int translateX(int x) {
		if (JmPattern.ifMirror())
			x = -x;
		return translate(x);
	}

	private int translateY(int y) {
		return translate(y) + base;
	}

	private int translateR(int r) {
		return translate(r);
	}

	private int translate(int t) {
		return t * magnification / DENOMINATOR;
	}
}