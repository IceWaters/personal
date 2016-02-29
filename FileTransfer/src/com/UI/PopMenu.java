package com.UI;

import java.awt.MenuItem;
import java.awt.PopupMenu;

public class PopMenu extends PopupMenu{
	private static final long serialVersionUID = 1L;
	public MenuItem addItem;
	public MenuItem deleteItem;

	public PopMenu(){
		addItem = new MenuItem("add");
		deleteItem = new MenuItem("delete");
		this.add(addItem);
		this.add(deleteItem);
	}
}
