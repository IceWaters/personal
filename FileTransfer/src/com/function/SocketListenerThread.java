package com.function;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.UI.MainPage;

public class SocketListenerThread extends Thread{
 	public ServerSocket ss;
 	MainPage mp;
 	JLabel listenerStatus;
 	public Socket fileReceiver,listener,WatchDog;
 	String fileName;
 	long lengthOfFile;
 	public static boolean SocketListenerFinished = false;
 	DropTargetAdapter kgd=new DropTargetAdapter()
	{
		public void drop(DropTargetDropEvent dtde)
		{
			try
	        {
		        Transferable tf=dtde.getTransferable();
		        if(tf.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
		        {
			        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			        @SuppressWarnings("rawtypes")
					List lt=(List)tf.getTransferData(DataFlavor.javaFileListFlavor);
			        @SuppressWarnings("rawtypes")
					Iterator itor=lt.iterator();
			        while(itor.hasNext())
			        {
				        File f=(File)itor.next();
				        System.out.println(f.getAbsolutePath());
						SendFileThread sft = new SendFileThread(mp, f);
						sft.start();
			        }
			        dtde.dropComplete(true);
		         }
		         else
		         {
			        dtde.rejectDrop();
		         }
	         }
	         catch(Exception e)
	         {
		         e.printStackTrace();
	         }
		  }
	};
 	
 	public SocketListenerThread(MainPage mp){
 		this.mp = mp;
 		this.fileReceiver = null;
 		this.listener = null;
 		this.WatchDog = null;
 		
 		//实现聊天输入窗拖入文件后直接发送文件
    	new DropTarget(mp.pMessage.MessageSendField,DnDConstants.ACTION_COPY_OR_MOVE,kgd);
 	}
 	
 	public String fileLength( long length ){
 		double klength = length*1.0/1024;
 		DecimalFormat df = new DecimalFormat("0.00");
 		if( klength<1024 ){
 			return ( df.format(klength) + "K" );	
 		}
 		else{
 			return ( df.format( klength/1024 ) + "M" );	
 		}	
 	}
 	public void run(){
 		try{		
 			ss= new ServerSocket( 9999 );
 			
 			//listenerStatus.setText("监听服务已经启动");
 			while(true && !SocketListenerFinished){	
 				Socket Connected = ss.accept();
 			    InetAddress	address = Connected.getInetAddress();	
 			    mp.IP = address.getHostAddress();
 			   mp.BConnect.setText("断开连接");
 			   mp.isConnected = true;
 			   if(mp.ContactsMap.containsKey(mp.IP)){			   
 				   mp.ConnectState.setText( "<html>连接到"+mp.ContactsMap.get(mp.IP));
 				   mp.pMessage.LReceiver.setText(mp.ContactsMap.get(mp.IP));
 			   }
 			   else{
 				   mp.ConnectState.setText( "<html>连接到"+mp.IP);
 				   mp.pMessage.LReceiver.setText(mp.IP);
 			   }
 				BufferedReader input = new BufferedReader( 
 					          new InputStreamReader( Connected.getInputStream() ) );
 				String request = input.readLine();
 				if( request.startsWith("[FILE]") ){
 					fileReceiver = Connected;
 					fileName = request.substring(6);
			
 					String fLength = input.readLine();
 					lengthOfFile = Long.parseLong( fLength );

 						mp.shake();
 						int i=JOptionPane.showConfirmDialog(mp.MainUI,"是否接收："+fileName,"提示:", JOptionPane.YES_NO_OPTION);
 						if(i==JOptionPane.OK_OPTION){
 							File receiveFile = new File(mp.DefaultFileSavePath+"\\"+fileName);
 							ReceiveFileThread receiver = new ReceiveFileThread(fileReceiver,receiveFile, mp, lengthOfFile);
 							mp.isFileTransferWaitMap.put(fileName, 0);
 							receiver.start();
 						}else{
 							mp.shake();
 							JOptionPane.showMessageDialog(mp.MainUI,
 									"对方拒绝了你的文件"," ",JOptionPane.INFORMATION_MESSAGE);	
 						}
 				}
 				else if( request.equals("[TALK]") )
 				{
 					listener = Connected;
 					new WaitingMsgThread(mp,listener ).start();	
 				}
 				else if(request.startsWith("[WatchDog]"))
 				{
 					WatchDog = Connected; 
 				}
 				else 
 					listenerStatus.setText( "<html>"+address.toString()+"<p>非法格式的请求"); 		
 			}	
 		}
 		catch( Exception e)
 		{
 			System.err.println(e.toString());	
 		}
 	}
}
