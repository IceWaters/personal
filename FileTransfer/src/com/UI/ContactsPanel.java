package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ContactsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String [] headers = {"      Í«≥∆","             IPµÿ÷∑"};
	public JTable table;
	public DefaultTableModel model;
	public Object [][] cellData;
	public JButton addContact;
	public JButton deleteContact;
	
	public ContactsPanel(){
		model = new DefaultTableModel(cellData,headers){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		
		table = new JTable(model){
			private static final long serialVersionUID = 1L;
			public TableCellRenderer getCellRenderer(int row, int column){
				TableCellRenderer renderer = super.getCellRenderer(row, column);
				if(renderer instanceof JLabel){
					((JLabel)renderer).setHorizontalAlignment(JLabel.CENTER);
				}
				return renderer;
			}
		};
		table.setFont(new Font("ø¨ÃÂ",Font.PLAIN,16));
		table.setFocusable(false);
		table.setGridColor(new Color(189, 252, 201));
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setFont(new Font("ø¨ÃÂ",Font.PLAIN,16));
		TableColumn firstColumn = table.getColumnModel().getColumn(0);
		firstColumn.setPreferredWidth(100);
		firstColumn.setMaxWidth(100);
		firstColumn.setMinWidth(100);
//		model.addRow(new Object[]{"LocalHost","127.0.0.1"});
//		model.addRow(new Object[]{"–€∏Á","222.20.35.70"});
//		model.addRow(new Object[]{"…µ√±¿Ÿ∏Á","222.20.10.177"});
//		model.addRow(new Object[]{"¿œÀŒ","222.20.36.178"});
		
		JScrollPane p = new JScrollPane(table);
		p.setBorder(null);
		
		JPanel pAdd = new JPanel();
		addContact = new JButton("+");
		addContact.setBorderPainted(false);
		addContact.setFocusPainted(false);
		addContact.setFont(new Font("ø¨ÃÂ",Font.BOLD,16));
		addContact.setPreferredSize(new Dimension(150,20));
		addContact.setContentAreaFilled(false);
		
		deleteContact = new JButton("del");
		deleteContact.setBorderPainted(false);
		deleteContact.setFocusPainted(false);
		deleteContact.setFont(new Font("ø¨ÃÂ",Font.BOLD,16));
		deleteContact.setPreferredSize(new Dimension(150,20));
		deleteContact.setContentAreaFilled(false);
		
		pAdd.setLayout(new BorderLayout());
		pAdd.add(addContact,BorderLayout.CENTER);
		pAdd.add(deleteContact,BorderLayout.EAST);
		
		this.setLayout(new BorderLayout());
		this.add(p,BorderLayout.CENTER);
		this.add(pAdd,BorderLayout.SOUTH);
	}
	public void deleteConatacts(int n){
		this.model.removeRow(n);
	}
	public void addContacts(String nickname, String IP){
		this.model.addRow(new Object[]{nickname,IP});
	}
}
