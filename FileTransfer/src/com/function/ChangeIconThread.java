package com.function;

import com.UI.MainPage;

public class ChangeIconThread extends Thread{
	MainPage mp;
	public int ChangeIconTime = 3000;
	public ChangeIconThread(MainPage mp){
		this.mp = mp;
	}
	public void setChangeIconTime(int time){
		this.ChangeIconTime = time;
	}
	public int getChangeIconTime(){
		return this.ChangeIconTime;
	}
	public void run(){
		for(int i=0;i<6;i++){
			mp.setBackImage(i);
			try {
				Thread.sleep(ChangeIconTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(i==5)i=0;
		}
	}
}
