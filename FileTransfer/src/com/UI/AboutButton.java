package com.UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;

public class AboutButton extends JButton{

	/**
	 * 设计自定义的“关于”按钮
	 */
	private static final long serialVersionUID = 1L;
	public AboutButton(){
		this.setPreferredSize(new Dimension(80,30));
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}
	public void paintComponent(Graphics g){
		g.drawOval(14, 8, 15, 15);
		g.drawString("关于", 40, 20);
		g.setFont(new Font("楷体",Font.ITALIC,10));
		g.drawString("i", 19, 20);
	}
}
