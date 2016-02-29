package com.UI;

import java.awt.MenuItem;
import java.awt.PopupMenu;

public class TaskPopMenu extends PopupMenu{
	private static final long serialVersionUID = 2L;
	public MenuItem openFile;//���ļ�λ��
	public MenuItem waitTask;//��ͣ����
	public MenuItem continueTask;//��������
	public MenuItem deleteTask;//ɾ������
	
	public TaskPopMenu(){
		openFile = new MenuItem("���ļ�λ��");
		waitTask = new MenuItem("��ͣ");
		continueTask = new MenuItem("����");
		deleteTask = new MenuItem("ɾ������");
		this.add(openFile);
		this.add(waitTask);
		this.add(continueTask);
		this.add(deleteTask);
	}
}
