import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverThread{
    public static void main(String args[]){
        new SocketThread().start();
    }
}

//the thread to start a socket
class SocketThread extends Thread{
    
    public SocketThread(){
        super();
    }

    public static int PORT = 8080;
    ServerSocket s= null;
    Socket socket =null;
    public void run(){    
        try{
            s=new ServerSocket(PORT);
            while(true){
                System.out.println("socket= "+s);
                socket =s.accept();
                System.out.println("Connection accept socket:"+socket);

                new ReadThread(socket).start(); 
                new SendThread(socket).start();
                //   System.out.println("Connection Socket Message :" + str);

                try{
                    sleep((int)(Math.random()*1000));
                }catch(InterruptedException e){}
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
                System.out.println("close...");
                socket.close();
                s.close();
            }catch(Exception e2){

            }
        }
    }
}
class  ReadThread extends Thread{
    //client socketRead;
    Socket strl;
    public ReadThread(Socket str){
        //super(str);
        this.strl=str;
    }

    public void run(){
        BufferedReader br=null;
        try{
        br = new BufferedReader(new InputStreamReader(
                    strl.getInputStream()));
                     
      //  String st = br.readLine();

        while(!strl.isClosed()){
            String st = br.readLine();
            if(st.equals("null")){
             //   strl.close();
            //  System.exit(0);
                System.out.println("nihao");
              //  break;
            }
            System.out.println("Receive : " + st);  
            try{
                sleep((int)(Math.random()*1000));
            }catch(InterruptedException e){}
        }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}


class  SendThread extends Thread{//{{{
    Socket strl;
    public SendThread(Socket str){
        //super(str);
        this.strl=str;
    }
    
    public void run(){
        BufferedReader buf=null;
        PrintWriter pw = null;
        try{
        pw = new PrintWriter(new BufferedWriter( new OutputStreamWriter(
                     strl.getOutputStream())),true);

        buf =new BufferedReader(new InputStreamReader(System.in));
        
        while(true){    
            String st = buf.readLine(); 
            pw.println(st);      
            pw.flush();
            try{
                sleep((int)(Math.random()*1000));
            }catch(InterruptedException e){}
        }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}//}}}


