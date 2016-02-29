package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public JTextArea ta;
	public JLabel LReceiver;
	public JTextField MessageSendField;
	public JButton MessageSendButton;
	
	public ChatPanel(){
		//this.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
		ta = new JTextArea(10,10);
		ta.setEditable(false);
		ta.setFont(new Font("楷体",Font.PLAIN,16));
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setBackground(Color.WHITE);
		//设置文本区总是显示最底下的  

		JScrollPane p = new JScrollPane(ta);
		p.setBorder(null);
		
		JPanel pL  = new JPanel();
		//pL.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
		LReceiver = new JLabel("  未连接");
		LReceiver.setPreferredSize(new Dimension(300,15));
		LReceiver.setBackground(Color.white);
		pL.add(LReceiver);
		
		JPanel pSend = new JPanel();
		MessageSendField = new JTextField(30);
		MessageSendField.setFont(new Font("楷体",Font.PLAIN,16));
		MessageSendButton = new JButton("发送");
		
		MessageSendButton.setFont(new Font("楷体",Font.PLAIN,11));
		MessageSendButton.setPreferredSize(new Dimension(30,25));
		MessageSendButton.setFocusPainted(false);
		MessageSendButton.setBorderPainted(false);
		pSend.setLayout(new BorderLayout());
		pSend.add(MessageSendField,BorderLayout.WEST);
		pSend.add(MessageSendButton,BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(pL,BorderLayout.NORTH);
		this.add(p,BorderLayout.CENTER);
		this.add(pSend,BorderLayout.SOUTH);
	}
}
