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
		this.setPreferredSize(new Dimension(75,64));//���ð�ť�ľ��Դ�С
		this.setBorderPainted(false);	//���ð�ť�ޱ߽�
		this.setContentAreaFilled(false);//���ð�ť����Ϊ͸��
		this.setFocusPainted(false);		
	}
	public void paintComponent(Graphics g){
		g.drawImage(TaskIcon.getImage(),0,0,this);
	}
}
