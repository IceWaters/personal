package com.function;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import com.UI.MainPage;

public class ReceiveFileThread extends Thread{
	Socket socket;
	File localfile;
	long lengthOfFile;
	MainPage mp;
	public boolean isReceiveOver = false;
	long ReceivedLength=0;
	int rowNum;
	
	public ReceiveFileThread(Socket socket,File localfile,MainPage mp,long lengthOfFile){
		this.socket = socket;
		this.localfile = localfile;
		this.lengthOfFile = lengthOfFile;//文件已接收长度
		this.mp = mp;
	}

	public void run()
	{
		try{
			PrintWriter out= new PrintWriter( socket.getOutputStream(),true );
			out.println("OK");
			//查找本地是否有该文件，有则查看文件大小，小于收到
			//的大小则继续发送，否则从头开始收
			File file = new File(mp.DefaultFileSavePath);
			File[] files = file.listFiles();//读取下载目录中的所有文件

			for (int i = 0; i < files.length; i++) {
				if((!files[i].isDirectory())&&(files[i].getAbsolutePath().equals(this.localfile.getAbsolutePath()))){  //文件
					ReceivedLength = files[i].length();  //断点位置
					break;
				}
			  }
			out.println(ReceivedLength);//发送已收到的长度
			
			BufferedInputStream remoteFile = 
			                   new BufferedInputStream( socket.getInputStream());
			RandomAccessFile saveFile = new RandomAccessFile(localfile,"rw");
			byte[] buffer = new byte[1024*128];
			int b;
	 		
			long cur=ReceivedLength;
	 		
	    	rowNum = mp.pDownloadPanel.addRow(localfile.getName(), fileLength(lengthOfFile), 0,0);
	 		
	    	long initialTime = new Date().getTime();
			long preTime = initialTime;
			long curTime = initialTime;
			//将写文件的指针移动到已接收的位置
			saveFile.seek(ReceivedLength);
			//临时文件描述记录对方IP地址和已接收长度
			RandomAccessFile tempFile = new RandomAccessFile(this.localfile.getParentFile()+"\\"+this.localfile.getName()+".receive.cfg","rw");
			tempFile.seek(0l);
			tempFile.writeBytes(this.socket.getInetAddress().toString().replace('/', ' ').trim());
			tempFile.writeBytes("\n\r");
			long saveCur = tempFile.getFilePointer();//保存当前存取位置
			int count = 0;
			while(isReceiveOver == false && mp.isConnected){
				out.println(mp.isFileTransferWaitMap.get(localfile.getName()));//向发送方发送是否暂停标志
				if(mp.isFileTransferWaitMap.get(localfile.getName()) == 0){//没有暂停则接着读
					b=remoteFile.read(buffer);		
					if(b != -1)
					{
						saveFile.write(buffer, 0, b);
						cur+=b;			
						tempFile.writeBytes(String.valueOf(cur).trim());;//将已发送的文件长度写入到文件中
						tempFile.seek(saveCur);
						
						if(count == 19){   
							count = 0;
							curTime = new Date().getTime();
							if(curTime-preTime>0)
								mp.pDownloadPanel.setSpeed(rowNum-1, (int) (b*20000/(curTime-preTime)) );
							preTime = curTime;
						}
						count++;
						mp.pDownloadPanel.setProgress(rowNum-1, (int)( (cur*1.0/lengthOfFile)*100 ) );  	
					}
					else isReceiveOver = true;
				}
			}
			if(isReceiveOver){
				mp.pDownloadPanel.deleteRow(rowNum-1);
	    		mp.pComplishedPanel.addRow(localfile.getName(), fileLength(localfile.length()), 100, 0);
				remoteFile.close();
				saveFile.close();
				socket.close();
			
				tempFile.close();
				File tf = new File(this.localfile.getParentFile()+"\\"+this.localfile.getName()+".receive.cfg");
				tf.delete();//文件传输完成之后删除掉描述文件
				}
	    	}
		 /**发送方将连接断开了
		  * catch里执行断开所有的连接操作，停止收文件线程
		  * 主界面上的按钮编程未连接状态，连接状态为未连接
		  * 弹出警告提示!
		  */
			catch (Exception e) {
				JOptionPane.showMessageDialog(mp.MainUI, "连接中断，文件接收中断");
				mp.pDownloadPanel.table.setValueAt("失败", rowNum, 3);
			}	
	}
	private String fileLength( long length )
 	{
 		double klength = length*1.0/1024;
 		DecimalFormat df = new DecimalFormat("0.00");
 		if( klength<1024 )
 		{
 			return ( df.format(klength) + "K" );	
 		}
 		else if(klength <1024*1024)
 		{
 			return ( df.format( klength/1024 ) + "M" );	
 		}	
 		else
 		{
 			return ( df.format( klength/1024/1024 ) + "G" );	
 		}
 	}	
}
