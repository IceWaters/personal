package com.UI;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;

public class UpdateButton extends JButton{

	/**
	 * 设计自定义软件更新按钮
	 */
	private static final long serialVersionUID = 1L;
	public UpdateButton(){
		this.setPreferredSize(new Dimension(80,40));
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}
	public void paintComponent(Graphics g){
		g.drawOval(14, 8, 15, 15);
		g.drawLine(21,11,21,20);
		g.drawLine(21,11,18,14);
		g.drawLine(21,11,24,14);
		g.drawString("更新", 40, 20);
	}
}
