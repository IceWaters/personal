package com.UI;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
public class SetSystemButton extends JButton{

	/**
	 * 设计系统设置按钮
	 */
	private static final long serialVersionUID = 1L;
	public SetSystemButton(){
		this.setPreferredSize(new Dimension(80,40));
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}
	public void paintComponent(Graphics g){
		g.drawOval(15, 9, 13, 13);
		g.drawOval(19,13,5,5);
		g.drawLine(22,9,22,7);
		g.drawLine(22,23,22,24);
		g.drawLine(15, 16, 13, 16);
		g.drawLine(28,16,30,16);
		g.drawLine(27,11,28,10);
		g.drawLine(27,21,28,22);
		g.drawLine(16,11,15,10);
		g.drawLine(16,21,15,22);
		g.drawString("设置", 40, 20);
	}
}
