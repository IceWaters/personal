package com.function;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.UI.MainPage;

public class SendFileThread extends Thread{
		File file;
		String IP;
		MainPage mp;
		public int isWait = 0;
		long SendedLength;//已发送文件长度
		Socket s;
		public long cur;
		
		public SendFileThread(MainPage mp,File file)
		{
			this.mp = mp;
			this.file = file;
			this.IP = mp.IP;
		}
		public void run()
		{
			request();
		}

		private void request()
		{
			int rowNum = 0;
			try
			{
				s = new Socket( IP, 9999);
			    PrintWriter out = new PrintWriter(s.getOutputStream(),true); 
			    BufferedReader in = new BufferedReader(
			    	               new InputStreamReader( s.getInputStream() ) );
			    ReceiveIsWait riw = new ReceiveIsWait(in);
			    out.println( "[FILE]"+file.getName() );//发送文件名
			    out.println( file.length() );//发送文件长度
			    
			    String answer = in.readLine();//等待确认是否接收文件
			    SendedLength = Long.parseLong(in.readLine());
			    riw.start();
			    if ( answer.equals("OK") )
			    {
			    	//在发送任务表中添加一项纪录
			    	rowNum = mp.pUploadPanel.addRow(file.getName(),fileLength(file.length()), 0, 0);	   	
			    	RandomAccessFile localFile = new RandomAccessFile(file,"rw");
	    			BufferedOutputStream sendFile = 
	       		         new BufferedOutputStream( s.getOutputStream());
	    			long fl = file.length();
	    			cur=SendedLength;//一传输长度
	    			
	    			long initialTime = new Date().getTime();
    				long preTime = initialTime;
    				long curTime = initialTime;
    				
	    			byte[] buffer = new byte[1024*128];
	    			int b;
	    			boolean isSendOver = false;
	    			long saveCur;//保存写入文件的位置信息
	    			localFile.seek(this.SendedLength);//将文件指针移动到已发送的位置
	    			/**为每一个发送正在发送的文件建立一个描述文件状态的
	    			*	临时文件，该文件包含：发送对象的IP地址，已发送的长度
	    			*/
	    			RandomAccessFile tempFile = new RandomAccessFile(mp.DefaultFileSavePath+"\\"+file.getName()+".send.cfg","rw");
	    			tempFile.seek(0l);
	    			tempFile.writeBytes(IP);//写入对方IP地址
	    			tempFile.writeBytes("\n\r");
	    			tempFile.writeUTF(file.getAbsolutePath());//写入发送文件的绝对路径
	    			tempFile.writeBytes("\n\r");
	    			saveCur = tempFile.getFilePointer();
	    			int count = 0;
	    			while(isSendOver == false && mp.isConnected){
	    				if(isWait == 0){
	    					b = localFile.read(buffer);
	    					if(b!=-1){
	    						sendFile.write(buffer, 0, b);
	    						cur+=b;
	    						tempFile.seek(saveCur);//光标重定位
	    						tempFile.writeLong(cur);//将已发送的文件长度写入到文件中
	    						if(count == 9){
	    							count = 0;
		    						curTime = new Date().getTime();
		    						if(curTime-preTime>0)
		    							mp.pUploadPanel.setSpeed(rowNum-1, (int) (b*10000/(curTime-preTime)) );
		    						preTime = curTime;
	    						}
	    						count++;
	    						mp.pUploadPanel.setProgress(rowNum-1, (int)( (cur*1.0/fl)*100 ));
	    					}
	    					else isSendOver = true;			
	    				}
	    			}
	    			//传完一个文件之后的操作
	    			if(isSendOver){
	    				riw.finished = true;
	    				tempFile.close();
	    				localFile.close();
	    				sendFile.close();
	    				File tf = new File(mp.DefaultFileSavePath+"\\"+file.getName()+".send.cfg");
	    				tf.delete();//文件传输完成之后删除掉描述文件
	    				mp.pComplishedPanel.addRow(file.getName(),fileLength(file.length()), 100, 0);
	    				mp.pUploadPanel.deleteRow(rowNum-1);
	    			}
			    }
			    else
			    {	
			    	JOptionPane.showMessageDialog(mp.MainUI, "对方同意接收您的文件！！！",
	 			        	  null, JOptionPane.ERROR_MESSAGE);
			    }
			in.close();
			out.close();
			s.close();    	   
			}
			/*发送文件失败之后的操作
			 * 断开连接，连接按钮设置，任务标志位失败
			 * */
			catch( Exception e)
			{
				JOptionPane.showMessageDialog(mp.MainUI, "连接中断，文件发送中断");
				mp.pUploadPanel.table.setValueAt("失败", rowNum, 3);
			}       	
		}
		private class ReceiveIsWait extends Thread{
			BufferedReader in;
			public boolean finished = false;
			public ReceiveIsWait( BufferedReader in){
				this.in = in;
			}
			public void run(){
				try {
					while(!finished){
						isWait = Integer.parseInt(in.readLine());
					}
				} catch (IOException e) {
					finished = true;
				}
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
