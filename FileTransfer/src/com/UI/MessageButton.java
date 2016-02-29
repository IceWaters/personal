package com.UI;
/**软件底部消息按钮，当有消息是，按钮上显示一个消息数量的
 * *红色小圆圈，点击查看后圆圈消失 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MessageButton extends JButton{

	/**
	 * 设计自定义的消息点击按钮
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon messageIcon = new ImageIcon("Image/Message.png");
	public MessageButton(){
		this.setPreferredSize(new Dimension(75,64));//设置按钮的绝对大小
		this.setBorderPainted(false);	//设置按钮无边界
		this.setContentAreaFilled(false);//设置按钮背景为透明
		this.setFocusPainted(false);
	}
	public void paintComponent(Graphics g){
		g.drawImage(messageIcon.getImage(),0,0,this);
	}
	public void Focused(){
		this.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
	}
}
