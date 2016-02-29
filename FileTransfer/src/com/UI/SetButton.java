package com.UI;
/**设置自定义设置按钮*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

public class SetButton extends JButton{
	private static final long serialVersionUID = 1L;
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(3, 3, 14, 2);
		g.fillRect(3, 8, 14, 2);
		g.fillRect(3, 13, 14, 2);
		
		this.setPreferredSize(new Dimension(20,20));
		this.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
	}
}
