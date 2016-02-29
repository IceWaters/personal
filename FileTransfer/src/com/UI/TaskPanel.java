package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TaskPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	String [] headers = {"      文件名","  大小","    进度","  状态"};
	public JTable table;
	public DefaultTableModel model;
	public Object [][] cellData;
	
	public TaskPanel(){
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
		
		table.setFont(new Font("楷体",Font.PLAIN,16));
		table.setFocusable(false);//设置不可聚焦
		table.setGridColor(Color.yellow);
		table.setRowHeight(20);
		TableColumn firstColumn = table.getColumnModel().getColumn(0);
		firstColumn.setPreferredWidth(120);
		TableColumn secondColumn = table.getColumnModel().getColumn(1);
		secondColumn.setPreferredWidth(70);
		TableColumn progressColumn = table.getColumnModel().getColumn(2);
		progressColumn.setCellRenderer(new ProgressRenderer());
		TableColumn ThirdColumn = table.getColumnModel().getColumn(3);
		ThirdColumn.setPreferredWidth(80);
		
		JScrollPane p = new JScrollPane(table);
		
		this.setLayout(new BorderLayout());
		this.add(p,BorderLayout.CENTER);
	}
	/*添加一行数据
	 * 当表中已存在此项时删除原来的记录
	*/
	public int addRow(String fileName, String fileByts, int progress,int speed){
			if(progress<100)
				model.addRow(new Object[]{fileName, fileByts,progress,String.valueOf(speed)+"Kb/s"} );
			else model.addRow(new Object[]{fileName, fileByts,progress,"完成"} );
			return model.getRowCount();
	}
	//删除最后一行数据
	public void deleteRow(int column){
		model.removeRow(column);
	}
	public void removeAll(){
		int rownum = model.getRowCount();
		for (int i = 0;i<rownum;i++)
			model.removeRow(0);
	}
	//设置指定的行数据的进度条
	public void setProgress(int row,int progress){
		if(progress<100)
			model.setValueAt(progress, row, 2);
		else{
			model.setValueAt(progress, row, 2);
			model.setValueAt("完成", row, 3);
		}
	}
	//设置某一行暂停显示效果
	public void setStop(int row){
		model.setValueAt("暂停", row, 3);
	}
	public void setSpeed(int row,int speed){
		model.setValueAt(Speed(speed ), row, 3);
	}
	//将输入的单位时间传输字节数转换为速度
		private String Speed( int speed )
			{
				double kspeed = speed*1.0/1000;
				DecimalFormat df = new DecimalFormat("0.0");
				if( kspeed<1024 )
				{
					return ( df.format(kspeed) + "K/s" );	
				}
				else
				{
					return ( df.format( kspeed/1000 ) + "M/s" );	
				}	
			}
}

class ProgressRenderer extends JProgressBar implements  TableCellRenderer{
	private static final long serialVersionUID = 1L;
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
		Integer v = (Integer)value;//这一列必须都是integer类型(0-100)
        setStringPainted(true);
        setValue(v);
        return this;
	}		
}
