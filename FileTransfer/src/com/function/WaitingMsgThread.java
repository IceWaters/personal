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
					if(str.contains("\\����"))	mp.shake();
					else{
						Msg.append( "\n"+ip+"\n  "+str+"\n"); 
						Msg.setCaretPosition(Msg.getText().length());//�Զ���ʾ�����һ��					
					}
				}
			    Thread.sleep(300);
			}
		}catch( IOException ioe ){//�����ӶϿ�ʱ�׳����쳣
			System.out.println("�����ж�");
			mp.IP = null;
			mp.isConnected = false;
			mp.BConnect.setText("���ӿͻ���");
			mp.ConnectState.setText("�����ļ�");
			mp.pMessage.LReceiver.setText("δ����");
		}catch(InterruptedException ex){
			System.err.println( ex.toString() );	
		}
	}
}
