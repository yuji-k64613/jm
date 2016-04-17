package com.jm.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

import com.jm.utility.BaseActivity;
import com.jm.utility.FPSManager;

public abstract class OpenGLActivityBase extends BaseActivity implements Renderer {
	public final static int LN = 10;
	public final static int HN = 1;
	
	private GLSurfaceView glSurfaceView;
	private int surfaceWidth;
	private int surfaceHeight;
	private FPSManager fpsManager;
	private long frameTime;
	private long sleepTime;
	private boolean frameSkipEnable;
	private boolean frameSkipState;
	
	public OpenGLActivityBase(float fps, boolean frameSkipEnable){
		this.frameSkipEnable = frameSkipEnable;
		frameSkipState = false;
		fpsManager = new FPSManager(10);
		sleepTime = 0l;
		frameTime = (long)(1000.0f / fps);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			glSurfaceView = createGlSurfaceView();
			glSurfaceView.setRenderer(this);
			
			setContentView(glSurfaceView);
		}
		catch (Exception e){
			showDialog(this, e);
		}
	}

	protected GLSurfaceView createGlSurfaceView() {
		GLSurfaceView glSurfaceView = new GLSurfaceView(this);
		return glSurfaceView;
	}

	protected GLSurfaceView getGlSurfaceView() {
		return glSurfaceView;
	}

	abstract protected void update();
	
	abstract protected void draw(GL10 gl);
	
	@Override
	public void onDrawFrame(GL10 gl) {
		fpsManager.clasFPS();
		
		if (frameSkipState){
			long elapsedTime = fpsManager.getElapsedTime();
			elapsedTime -= sleepTime;
			
			if (elapsedTime < frameTime && elapsedTime > 0l){
				sleepTime = frameTime - elapsedTime;
				try {
					Thread.sleep(sleepTime);
				}
				catch (InterruptedException e){
					;
				}
				elapsedTime = 0;
			}
			else {
				sleepTime = 0;
				elapsedTime -= frameTime;
			}
			
			if (elapsedTime >= frameTime){
				if (frameSkipEnable){
					for (; elapsedTime >= frameTime; elapsedTime -= frameTime){
						update();
					}
				}
			}
		}
		else {
			frameSkipState = true;
		}
		update();
		draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		frameSkipState = false;
		
		surfaceWidth = width;
		surfaceHeight = height;
		
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = (float)width / height;
		gl.glFrustumf(-ratio, ratio, -HN, HN, LN, 100000.0f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		frameSkipState = false;
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_FASTEST);
		
//		gl.glClearColor(0, 0, 0, 1);
		gl.glClearColor(1, 1, 1, 1);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		
		//gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		gl.glShadeModel(GL10.GL_FLAT);
		
	}
	
	@Override
	protected void onResume() {
		frameSkipState = false;
		
		super.onResume();
		glSurfaceView.onResume();
	}
	
	@Override
	protected void onPause() {
		frameSkipState = false;
		
		super.onPause();
		glSurfaceView.onPause();
	}

	public int getSurfaceWidth() {
		return surfaceWidth;
	}

	public int getSurfaceHeight() {
		return surfaceHeight;
	}

	public FPSManager getFtpManager() {
		return fpsManager;
	}
}
