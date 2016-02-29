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
		long SendedLength;//�ѷ����ļ�����
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
			    out.println( "[FILE]"+file.getName() );//�����ļ���
			    out.println( file.length() );//�����ļ�����
			    
			    String answer = in.readLine();//�ȴ�ȷ���Ƿ�����ļ�
			    SendedLength = Long.parseLong(in.readLine());
			    riw.start();
			    if ( answer.equals("OK") )
			    {
			    	//�ڷ�������������һ���¼
			    	rowNum = mp.pUploadPanel.addRow(file.getName(),fileLength(file.length()), 0, 0);	   	
			    	RandomAccessFile localFile = new RandomAccessFile(file,"rw");
	    			BufferedOutputStream sendFile = 
	       		         new BufferedOutputStream( s.getOutputStream());
	    			long fl = file.length();
	    			cur=SendedLength;//һ���䳤��
	    			
	    			long initialTime = new Date().getTime();
    				long preTime = initialTime;
    				long curTime = initialTime;
    				
	    			byte[] buffer = new byte[1024*128];
	    			int b;
	    			boolean isSendOver = false;
	    			long saveCur;//����д���ļ���λ����Ϣ
	    			localFile.seek(this.SendedLength);//���ļ�ָ���ƶ����ѷ��͵�λ��
	    			/**Ϊÿһ���������ڷ��͵��ļ�����һ�������ļ�״̬��
	    			*	��ʱ�ļ������ļ����������Ͷ����IP��ַ���ѷ��͵ĳ���
	    			*/
	    			RandomAccessFile tempFile = new RandomAccessFile(mp.DefaultFileSavePath+"\\"+file.getName()+".send.cfg","rw");
	    			tempFile.seek(0l);
	    			tempFile.writeBytes(IP);//д��Է�IP��ַ
	    			tempFile.writeBytes("\n\r");
	    			tempFile.writeUTF(file.getAbsolutePath());//д�뷢���ļ��ľ���·��
	    			tempFile.writeBytes("\n\r");
	    			saveCur = tempFile.getFilePointer();
	    			int count = 0;
	    			while(isSendOver == false && mp.isConnected){
	    				if(isWait == 0){
	    					b = localFile.read(buffer);
	    					if(b!=-1){
	    						sendFile.write(buffer, 0, b);
	    						cur+=b;
	    						tempFile.seek(saveCur);//����ض�λ
	    						tempFile.writeLong(cur);//���ѷ��͵��ļ�����д�뵽�ļ���
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
	    			//����һ���ļ�֮��Ĳ���
	    			if(isSendOver){
	    				riw.finished = true;
	    				tempFile.close();
	    				localFile.close();
	    				sendFile.close();
	    				File tf = new File(mp.DefaultFileSavePath+"\\"+file.getName()+".send.cfg");
	    				tf.delete();//�ļ��������֮��ɾ���������ļ�
	    				mp.pComplishedPanel.addRow(file.getName(),fileLength(file.length()), 100, 0);
	    				mp.pUploadPanel.deleteRow(rowNum-1);
	    			}
			    }
			    else
			    {	
			    	JOptionPane.showMessageDialog(mp.MainUI, "�Է�ͬ����������ļ�������",
	 			        	  null, JOptionPane.ERROR_MESSAGE);
			    }
			in.close();
			out.close();
			s.close();    	   
			}
			/*�����ļ�ʧ��֮��Ĳ���
			 * �Ͽ����ӣ����Ӱ�ť���ã������־λʧ��
			 * */
			catch( Exception e)
			{
				JOptionPane.showMessageDialog(mp.MainUI, "�����жϣ��ļ������ж�");
				mp.pUploadPanel.table.setValueAt("ʧ��", rowNum, 3);
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
