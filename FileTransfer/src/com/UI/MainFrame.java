package com.UI;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	ImageIcon img = new ImageIcon("Image\\BackGround\\pugongying.png");
	public MainFrame(){
		
	}
	public void paint(Graphics g){   
	      g.drawImage(img.getImage(), 0, 0, this);//ÏÔÊ¾Í¼Ïñ   
	}   
}
