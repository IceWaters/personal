package com.UI;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TaskButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon TaskIcon = new ImageIcon("Image/Task.png");
	public TaskButton(){
		this.setPreferredSize(new Dimension(75,64));//设置按钮的绝对大小
		this.setBorderPainted(false);	//设置按钮无边界
		this.setContentAreaFilled(false);//设置按钮背景为透明
		this.setFocusPainted(false);		
	}
	public void paintComponent(Graphics g){
		g.drawImage(TaskIcon.getImage(),0,0,this);
	}
}
