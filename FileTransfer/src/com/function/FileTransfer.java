package com.function;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.UI.MainPage;

public class FileTransfer{
	static MainPage mp;
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {e.printStackTrace();
		} catch (InstantiationException e) {e.printStackTrace();
		} catch (IllegalAccessException e) {e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {e.printStackTrace();}
		mp = new MainPage();
		mp.Init();
		ChangeIconThread cit = new ChangeIconThread(mp);
		cit.start();
	}
}
