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
		this.lengthOfFile = lengthOfFile;//�ļ��ѽ��ճ���
		this.mp = mp;
	}

	public void run()
	{
		try{
			PrintWriter out= new PrintWriter( socket.getOutputStream(),true );
			out.println("OK");
			//���ұ����Ƿ��и��ļ�������鿴�ļ���С��С���յ�
			//�Ĵ�С��������ͣ������ͷ��ʼ��
			File file = new File(mp.DefaultFileSavePath);
			File[] files = file.listFiles();//��ȡ����Ŀ¼�е������ļ�

			for (int i = 0; i < files.length; i++) {
				if((!files[i].isDirectory())&&(files[i].getAbsolutePath().equals(this.localfile.getAbsolutePath()))){  //�ļ�
					ReceivedLength = files[i].length();  //�ϵ�λ��
					break;
				}
			  }
			out.println(ReceivedLength);//�������յ��ĳ���
			
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
			//��д�ļ���ָ���ƶ����ѽ��յ�λ��
			saveFile.seek(ReceivedLength);
			//��ʱ�ļ�������¼�Է�IP��ַ���ѽ��ճ���
			RandomAccessFile tempFile = new RandomAccessFile(this.localfile.getParentFile()+"\\"+this.localfile.getName()+".receive.cfg","rw");
			tempFile.seek(0l);
			tempFile.writeBytes(this.socket.getInetAddress().toString().replace('/', ' ').trim());
			tempFile.writeBytes("\n\r");
			long saveCur = tempFile.getFilePointer();//���浱ǰ��ȡλ��
			int count = 0;
			while(isReceiveOver == false && mp.isConnected){
				out.println(mp.isFileTransferWaitMap.get(localfile.getName()));//���ͷ������Ƿ���ͣ��־
				if(mp.isFileTransferWaitMap.get(localfile.getName()) == 0){//û����ͣ����Ŷ�
					b=remoteFile.read(buffer);		
					if(b != -1)
					{
						saveFile.write(buffer, 0, b);
						cur+=b;			
						tempFile.writeBytes(String.valueOf(cur).trim());;//���ѷ��͵��ļ�����д�뵽�ļ���
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
				tf.delete();//�ļ��������֮��ɾ���������ļ�
				}
	    	}
		 /**���ͷ������ӶϿ���
		  * catch��ִ�жϿ����е����Ӳ�����ֹͣ���ļ��߳�
		  * �������ϵİ�ť���δ����״̬������״̬Ϊδ����
		  * ����������ʾ!
		  */
			catch (Exception e) {
				JOptionPane.showMessageDialog(mp.MainUI, "�����жϣ��ļ������ж�");
				mp.pDownloadPanel.table.setValueAt("ʧ��", rowNum, 3);
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
