package com.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class SetMenu extends JDialog{
	private static final long serialVersionUID = 1L;
	private int SetMenuWidth = 80;
	private int SetMenuHeight = 120;
	private static JPanel p;
	SetSystemButton setSystem;
	UpdateButton upDate;
	EchoButton echo;
	AboutButton about;
	
	public  SetMenu(int x, int y){
		this.setSize(SetMenuWidth, SetMenuHeight);
		this.setResizable(false);
		this.setBackground(new Color(1.0f,1.0f,1.0f));
		this.setUndecorated(true);
		this.setLocation(x, y);
		
		setSystem = new SetSystemButton();
		upDate = new UpdateButton();
		echo = new EchoButton();
		about = new AboutButton();
		
		p = new JPanel();
		p.setBackground(Color.white);
		p.setPreferredSize(new Dimension(SetMenuWidth,SetMenuHeight));
		p.setLayout(new GridLayout(4,1));
		p.add(setSystem);
		p.add(upDate);
		p.add(echo);
		p.add(about);
		this.add(p);
	}
}