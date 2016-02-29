package com.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
/**设计自定义的关闭按钮*/

public class CloseButton extends JButton{
	private static final long serialVersionUID = 1L;
	private int [] x1Points= {5,3,15,17};
	private int [] y1Points = {3,5,17,15};
	private int [] x2Points= {15,17,5,3};
	private int [] y2Points = {3,5,17,15};
	
	public void paintComponent(Graphics g){
		this.setPreferredSize(new Dimension(20,20));//设置按钮的绝对大小
		this.setBorderPainted(false);	//设置按钮无边界
		this.setContentAreaFilled(false);//设置按钮背景为透明
		this.setFocusPainted(false);
		
		g.setColor(new Color(1.0f,1.0f,1.0f));
		g.fillPolygon(x1Points, y1Points, y1Points.length);
		g.fillPolygon(x2Points, y2Points, y2Points.length);
	}
}
