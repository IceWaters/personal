package com.UI;

import java.awt.MenuItem;
import java.awt.PopupMenu;

public class TaskPopMenu extends PopupMenu{
	private static final long serialVersionUID = 2L;
	public MenuItem openFile;//打开文件位置
	public MenuItem waitTask;//暂停任务
	public MenuItem continueTask;//继续任务
	public MenuItem deleteTask;//删除任务
	
	public TaskPopMenu(){
		openFile = new MenuItem("打开文件位置");
		waitTask = new MenuItem("暂停");
		continueTask = new MenuItem("继续");
		deleteTask = new MenuItem("删除任务");
		this.add(openFile);
		this.add(waitTask);
		this.add(continueTask);
		this.add(deleteTask);
	}
}
