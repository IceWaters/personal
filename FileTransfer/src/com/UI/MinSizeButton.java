package com.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;



/**设计自己的最小化按钮*/
import javax.swing.JButton;

public class MinSizeButton extends JButton{
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(4, 8, 12, 2);
		
		this.setPreferredSize(new Dimension(20,20));
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}
}
