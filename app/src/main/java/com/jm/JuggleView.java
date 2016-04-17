package com.jm;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.view.MotionEvent;

import com.jm.common.Constant;
import com.jm.db.DaoFactory;
import com.jm.db.IDao;
import com.jm.opengl.JuggleDrawer;
import com.jm.opengl.OpenGLActivityBase;
import com.jm.utility.Debug;
import com.jm.utility.JmException;
import com.jm.utility.Resource;

public class JuggleView extends OpenGLActivityBase {
    private static final float FPS_NUM = 0.0f;
	private static JuggleDrawer drawer = null;
	
	private int tx = -1;
	private int ty = -1;
	private int rr = -1;
	
	public static void init(){
		drawer = null;
	}
	
	public JuggleView() {
        super(FPS_NUM + Resource.speed * 3, true);        
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.juggleview);        
    }
    
	protected void update(){
		if (drawer == null){
			return;
		}
		drawer.update();
	}
	
	protected void draw(GL10 gl){
		if (drawer == null){
			return;
		}
		drawer.draw(gl);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		
		if (drawer != null){
			return;
		}
		
	    Bundle extras = getIntent().getExtras();
		try {
			JmPattern jp = null;
			int id = extras.getInt(Constant.EXTRAS_ID);
			if (id >= 0) {
				IDao<?> dao = DaoFactory.getInstance().getDao();
				JmPattern[] list = dao.getFromId(id);
				if (list == null){
			    	finish();
					return;
				}
				if (list.length <= 0) {
					throw new JmException();
				}
				jp = list[0];
			} else {
				jp = (JmPattern) extras.getSerializable("PATTERN");
			}
			
			drawer = new JuggleDrawer(this, jp);
			drawer.clear();
		} catch (JmException e) {
			Debug.d(this, e.getMessage());
			finish();
		} catch (Exception e) {
			Debug.d(this, e.getMessage());
			finish();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (drawer == null){
			return true;
		}
		int type = event.getAction();
		int count = event.getPointerCount();
		
		switch (type){
		case MotionEvent.ACTION_DOWN:
			tx = -1;
			ty = -1;
			rr = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			int history = event.getHistorySize() / count;
			
			for (int i = 0; i < history; i++){
				int index1 = event.findPointerIndex(0);
				int index2 = event.findPointerIndex(1);

				if (index1 >= 0 && index2 >= 0){
					int ux1 = (int) event.getHistoricalX(index1, i);
					int uy1 = (int) event.getHistoricalY(index1, i);
					int ux2 = (int) event.getHistoricalX(index2, i);
					int uy2 = (int) event.getHistoricalY(index2, i);
					int ss = (ux1 - ux2) * (ux1 - ux2) + (uy1 - uy2) * (uy1 - uy2);
					if (rr >= 0){
						float drr = rotateR(rr, ss);
						if (drr != 0){
							drawer.rotate(0, 0, drr);						
						}
					}
					rr = ss;					
				}
				else if (index1 >= 0 || index2 >= 0){
					int index;
					if (index1 >= 0){
						index = index1;
					}
					else {
						index = index2;
					}
					
					int ux = (int) event.getHistoricalX(index, i);
					int uy = (int) event.getHistoricalY(index, i);
					if (tx >= 0) {
						float rx = rotateXY(tx, ux);
						float ry = rotateXY(ty, uy);
						if (rx != 0 || ry != 0) {
							drawer.rotate(rx, ry, 0);
						}
					}
					tx = ux;
					ty = uy;
					rr = -1;
				}
			}
			int index1 = event.findPointerIndex(0);
			int index2 = event.findPointerIndex(1);
			if (index1 >= 0 && index2 >= 0){
				int ux1 = (int) event.getX(index1);
				int uy1 = (int) event.getY(index1);
				int ux2 = (int) event.getX(index2);
				int uy2 = (int) event.getY(index2);
				int ss = (ux1 - ux2) * (ux1 - ux2) + (uy1 - uy2) * (uy1 - uy2);
				if (rr >= 0){
					float drr = rotateR(rr, ss);
					if (drr != 0){
						drawer.rotate(0, 0, drr);						
					}
				}
				rr = ss;
			}
			else if (index1 >= 0 || index2 >= 0){
				int index;
				if (index1 >= 0){
					index = index1;
				}
				else {
					index = index2;
				}
				
				int ux = (int) event.getX(index);
				int uy = (int) event.getY(index);
				if (tx >= 0) {
					float rx = rotateXY(tx, ux);
					float ry = rotateXY(ty, uy);
					if (rx != 0 || ry != 0) {
						drawer.rotate(rx, ry, 0);
					}
				}
				tx = ux;
				ty = uy;
				rr = -1;
			}
			break;
		}
		
		return true;
	}
	
	private float rotateXY(int src, int dst){
		if (src > dst){
			return -0.02f;
		}
		else if (src < dst){
			return 0.02f;			
		}
		else {
			return 0.0f;
		}
	}

	private float rotateR(int src, int dst){
		if (src > dst){
			return -10.0f;
		}
		else if (src < dst){
			return 10.0f;			
		}
		else {
			return 0.0f;
		}
	}
}
