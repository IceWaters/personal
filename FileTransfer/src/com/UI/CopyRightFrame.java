package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CopyRightFrame{
	/**
	 * 显示软件设计人员及软件的基本信息
	 */
	private int width = 300;
	private int height = 300;
	private boolean isDraging = false;
	private int xx;
	private int yy;
	private static JDialog d;
	
	public CopyRightFrame(int x, int y){
		d = new JDialog();
		d.setSize(width, height);
		d.setResizable(false);
		d.setLocation(x, y);
		d.setTitle("TCP/IP断点续传");
		d.setUndecorated(true);
		d.setOpacity(0.9f);
		Container contentPane = d.getContentPane();
		JTextArea ta = new JTextArea(10,20);
		ta.setEditable(false);
		ta.setFont(new Font("楷体",Font.PLAIN,16));
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		//ta.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
		ta.setBackground(Color.CYAN);
		ta.append("CopyRight:郭雄  宋鹏飞  涂方蕾\n"+"Version:1.0\n"+
						"Date:2014-12\n"+"School:华中科技大学\n");
		JScrollPane p = new JScrollPane(ta);
		p.setBorder(null);
		contentPane.setLayout(new BorderLayout());
		
		JPanel pSysSet = new JPanel();
		pSysSet.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
		pSysSet.setLayout(new BorderLayout());
		CloseButton cb = new CloseButton();
		cb.addActionListener(new CloseActionListener());
		pSysSet.add(new JLabel("关于软件"),BorderLayout.WEST);
		pSysSet.add(cb,BorderLayout.EAST);
		
		contentPane.add(p,BorderLayout.CENTER);
		contentPane.add(pSysSet,BorderLayout.NORTH);
		contentPane.addMouseListener(new DragMouseListener());
		contentPane.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				if(isDraging){
					int left = d.getLocation().x;
					int top = d.getLocation().y;
					d.setLocation(left+e.getX()-xx,top+e.getY()-yy);
				}
			}
			@Override
			public void mouseMoved(MouseEvent arg0) {
				
			}
		});
	}
	public void setVisible(boolean isVisible){
		d.setVisible(isVisible);
	}
	private class CloseActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			d.dispose();
		}	
	}
	private class DragMouseListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent e) {
			isDraging = true;
			xx = e.getX();
			yy = e.getY();
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			isDraging = false;
		}
	}
}
