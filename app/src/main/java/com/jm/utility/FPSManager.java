package com.jm.utility;

import java.util.LinkedList;

import android.os.SystemClock;

public class FPSManager {
	private long prevTime;
	private long elapsedTime;
	private float fps;
	private long times;
	private LinkedList<Long> elapsedTimeList;
	private int sampleNum;
	
	public FPSManager(int sampleNum){
		prevTime = 0l;
		elapsedTime = 0l;
		fps = 0.0f;
		times = 0l;
		elapsedTimeList = new LinkedList<Long>();
		for (int i = 0; i < sampleNum; i++){
			elapsedTimeList.add(0l);
		}
		this.sampleNum = sampleNum;
	}
	
	public void clasFPS(){
		long nowTime = SystemClock.uptimeMillis();
		
		elapsedTime = nowTime - prevTime;
		prevTime = nowTime;
		
		times += elapsedTime;
		elapsedTimeList.add(elapsedTime);
		times -= elapsedTimeList.poll();
		
		long tmp = times / sampleNum;
		
		if (tmp != 0l){
			fps = 1000.0f / tmp;
		}
		else {
			fps = 0.0f;
		}
	}
	
	public float getFPS(){
		return fps;
	}
	
	public long getElapsedTime(){
		return elapsedTime;
	}
}
