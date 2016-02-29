package com.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

import com.UI.MainPage;

public class WaitingMsgThread extends Thread{
	MainPage mp;
	JTextArea Msg;
	Socket listener;
	public WaitingMsgThread( MainPage mp,Socket listener ){
		this.mp = mp;
		this.Msg = mp.pMessage.ta;
		this.listener = listener;	
	}	
	public void run()
	{
		try{
			BufferedReader input = new BufferedReader( 
			   new InputStreamReader( listener.getInputStream() ) );
			PrintWriter out = new PrintWriter( listener.getOutputStream(), true );
			String ip =listener.getInetAddress().getHostAddress();
			if(mp.ContactsMap.containsKey(ip))
				ip = mp.ContactsMap.get(ip);
		    out.println("OK");
			while(true){		
				String str=input.readLine();
				if( str!=null  ){		
					if(str.contains("\\抖动"))	mp.shake();
					else{
						Msg.append( "\n"+ip+"\n  "+str+"\n"); 
						Msg.setCaretPosition(Msg.getText().length());//自动显示到最后一行					
					}
				}
			    Thread.sleep(300);
			}
		}catch( IOException ioe ){//当连接断开时抛出的异常
			System.out.println("连接中断");
			mp.IP = null;
			mp.isConnected = false;
			mp.BConnect.setText("连接客户端");
			mp.ConnectState.setText("传输文件");
			mp.pMessage.LReceiver.setText("未连接");
		}catch(InterruptedException ex){
			System.err.println( ex.toString() );	
		}
	}
}
