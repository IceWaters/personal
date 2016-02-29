package com.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**设计自定义的系统设置对话框*/
public class SetDialog {
	private int SetDialogWidth = 300;
	private int SetDialogHeight = 200;
	final int Close_MinSize = 0;
	final int Close_Exit = 1;
	private boolean isDraging = false;
	private int xx;
	private int yy;
	public JDialog jd;//对话框容器
	public ButtonGroup bg;
	public JRadioButton b1;
	public JRadioButton b2;
	private JPanel pPath;
	public JTextField tf;
	private JButton viewPath;
	private JButton openPath;
	private String FilePath;//文件保存路径
	public JButton cb;
	
	public SetDialog(int x, int y){
		jd = new JDialog();
		jd.setSize(SetDialogWidth,SetDialogHeight);
		jd.setResizable(false);
		jd.setLocation(x, y);
		jd.setUndecorated(true);
		
		JPanel pSysSet = new JPanel();
		cb = new JButton("×");
		cb.setFont(new Font("楷体",Font.BOLD,16));
		cb.setBorderPainted(false);	//设置按钮无边界
		cb.setContentAreaFilled(false);//设置按钮背景为透明
		cb.setFocusPainted(false);
		
		JLabel l = new JLabel("  设置");
		l.setFont(new Font("黑体",Font.PLAIN,16));
		pSysSet.setPreferredSize(new Dimension(SetDialogWidth,30));
		pSysSet.setLayout(new BorderLayout());
		pSysSet.add(l,BorderLayout.CENTER);
		pSysSet.add(cb,BorderLayout.EAST);
		pSysSet.addMouseListener(new DragMouseListener());
		pSysSet.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				if(isDraging){
					int left = jd.getLocation().x;
					int top = jd.getLocation().y;
					jd.setLocation(left+e.getX()-xx,top+e.getY()-yy);
				}
			}
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
		});
		
		//关闭按钮复选框
		JPanel pCloseSet = new JPanel();
		bg = new ButtonGroup();
		b1 = new JRadioButton("最小化到托盘",false);
		b1.setFocusPainted(true);
		b2 = new JRadioButton("退出程序",true);
		b2.setFocusPainted(false);
		bg.add(b1);
		bg.add(b2);
		pCloseSet.setPreferredSize(new Dimension(SetDialogWidth,40));
		pCloseSet.setLayout(new GridLayout(2,2));
		pCloseSet.add(new JLabel("  点击关闭按钮"));
		pCloseSet.add(new JLabel());
		pCloseSet.add(b1);
		pCloseSet.add(b2);
		//默认保存路径设置	 
		pPath = new JPanel();
		pPath.setPreferredSize(new Dimension(SetDialogWidth,110));
		JLabel FileSavePath = new JLabel("  文件保存位置");
		tf = new JTextField(28);
		viewPath = new JButton("浏览");
		viewPath.setContentAreaFilled(false);
		viewPath.setBorderPainted(false);
		viewPath.setFocusPainted(false);
		viewPath.setFont(new Font("楷体",Font.PLAIN,15));
		viewPath.addActionListener(new ViewActionListener());
		openPath = new JButton("打开");
		openPath.setContentAreaFilled(false);
		openPath.setBorderPainted(false);
		openPath.setFocusPainted(false);
		openPath.setFont(new Font("楷体",Font.PLAIN,15));
		openPath.addActionListener(new OpenPathActionListener());
		JPanel pLabel = new JPanel();//放置文件保存位置标签
		JPanel pPathSet = new JPanel();
		pLabel.setLayout(new GridLayout(1,2));
		pLabel.setPreferredSize(new Dimension(SetDialogWidth,30));
		pLabel.add(FileSavePath);
		pPathSet.setLayout(new BorderLayout());
		pPathSet.setPreferredSize(new Dimension(SetDialogWidth,30));
		
		pPathSet.add(tf,BorderLayout.WEST);
		pPathSet.add(viewPath,BorderLayout.CENTER);
		pPathSet.add(openPath,BorderLayout.EAST);
		pPath.setLayout(new BorderLayout());
		pPath.add(pLabel,BorderLayout.NORTH);
		pPath.add(pPathSet,BorderLayout.CENTER);
		JPanel pNull = new JPanel();
		pNull.setPreferredSize(new Dimension(SetDialogWidth,55));
		pPath.add(pNull,BorderLayout.SOUTH);
		
		jd.add(pSysSet,BorderLayout.NORTH);
		jd.add(pCloseSet,BorderLayout.CENTER);
		jd.add(pPath,BorderLayout.SOUTH);
		jd.setVisible(true);
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
	private class ViewActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFont(new Font("楷体",Font.PLAIN,10));
			jfc.setDialogTitle("选择文件保存的路径");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = jfc.showOpenDialog(jfc);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				FilePath = jfc.getSelectedFile().getAbsolutePath();
			}
			tf.setText(FilePath);
		}
	}
	private class OpenPathActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//执行打开文件位置操作
				Runtime.getRuntime().exec("cmd /c start "+tf.getText().trim());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}	
	}
	public void shake(){
			jd.setAlwaysOnTop(true);
		 	int x = jd.getX();
		    int y = jd.getY();
		    for (int i = 0; i < 10; i++) {
		     if ((i & 1) == 0) {
		      x += 3;
		      y += 3;
		     } else {
		      x -= 3;
		      y -= 3;
		     }
		     jd.setLocation(x, y);
		     try {
		    	 Thread.sleep(50);
		     } catch (InterruptedException e1) {
		    	 	e1.printStackTrace();
		     }
		    }
		    jd.setAlwaysOnTop(false);
	}
}