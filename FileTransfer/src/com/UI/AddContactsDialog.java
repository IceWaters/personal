package com.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddContactsDialog{
	public JDialog AddContacts;
	public JTextField nickname;//�ǳ������
	public JTextField IPAddress;//IP��ַ�����
	public JButton ensure;//ȷ�ϰ�ť
	private JButton concel;//ȡ����ť
	
	public AddContactsDialog(int x, int y){
		AddContacts = new JDialog();
		AddContacts.setTitle("�����ϵ��");
		AddContacts.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		AddContacts.setSize(260,140);
		AddContacts.setResizable(false);
		AddContacts.setLocation(x, y);
		AddContacts.setAlwaysOnTop(true);

		ensure = new JButton("ȷ��");
		concel = new JButton("ȡ��");
		nickname = new JTextField(5);
		IPAddress = new JTextField(10);

		ensure.setBorderPainted(false);
		ensure.setFocusPainted(false);

		concel.setBorderPainted(false);
		concel.setFocusPainted(false);
		
		JLabel LNickName = new JLabel("   �ǳ�");
		JLabel LIP = new JLabel("  IP��ַ");
		LNickName.setBounds(5,10,60,25);
		LIP.setBounds(5, 40, 60, 25);
		
		AddContacts.setLayout(null);
		AddContacts.add(LNickName);
		AddContacts.add(LIP);
		this.nickname.setBounds(70, 10, 150, 25);
		AddContacts.add(this.nickname);
		this.IPAddress.setBounds(70, 40, 150, 25);
		AddContacts.add(this.IPAddress);
		this.ensure.setBounds(50, 70, 60, 30);
		AddContacts.add(this.ensure);
		this.concel.setBounds(140, 70, 60, 30);
		AddContacts.add(this.concel);
		
		AddContacts.setVisible(true);
		concel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AddContacts.dispose();
			}		
		});
	}
	public void dispose(){
		AddContacts.dispose();
	}
}
