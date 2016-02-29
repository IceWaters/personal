package com.UI;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

public class EchoButton extends JButton{

	/**
	 * 设计反馈按钮图标
	 */
	private static final long serialVersionUID = 1L;
	private int []xPoints = {15,15,28,28,24};
	private int []yPoints = {8,24,24,12,8};
	public EchoButton(){
		this.setPreferredSize(new Dimension(80,40));
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}
	public void paintComponent(Graphics g){
		g.drawPolygon(xPoints, yPoints,xPoints.length);
		g.drawLine(18,12,24,12);
		g.drawLine(18,16,24,16);
		g.drawLine(18,20,24,20);
		g.drawString("反馈", 40, 20);
	}
}
