package com.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
/**����Զ���Ĺرհ�ť*/

public class CloseButton extends JButton{
	private static final long serialVersionUID = 1L;
	private int [] x1Points= {5,3,15,17};
	private int [] y1Points = {3,5,17,15};
	private int [] x2Points= {15,17,5,3};
	private int [] y2Points = {3,5,17,15};
	
	public void paintComponent(Graphics g){
		this.setPreferredSize(new Dimension(20,20));//���ð�ť�ľ��Դ�С
		this.setBorderPainted(false);	//���ð�ť�ޱ߽�
		this.setContentAreaFilled(false);//���ð�ť����Ϊ͸��
		this.setFocusPainted(false);
		
		g.setColor(new Color(1.0f,1.0f,1.0f));
		g.fillPolygon(x1Points, y1Points, y1Points.length);
		g.fillPolygon(x2Points, y2Points, y2Points.length);
	}
}
