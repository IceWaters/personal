package com.UI;
/**����ײ���Ϣ��ť��������Ϣ�ǣ���ť����ʾһ����Ϣ������
 * *��ɫСԲȦ������鿴��ԲȦ��ʧ */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MessageButton extends JButton{

	/**
	 * ����Զ������Ϣ�����ť
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon messageIcon = new ImageIcon("Image/Message.png");
	public MessageButton(){
		this.setPreferredSize(new Dimension(75,64));//���ð�ť�ľ��Դ�С
		this.setBorderPainted(false);	//���ð�ť�ޱ߽�
		this.setContentAreaFilled(false);//���ð�ť����Ϊ͸��
		this.setFocusPainted(false);
	}
	public void paintComponent(Graphics g){
		g.drawImage(messageIcon.getImage(),0,0,this);
	}
	public void Focused(){
		this.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
	}
}
