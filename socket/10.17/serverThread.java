import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class serverThread{
    public static void main(String args[]){
        new SocketThread().start();
    }
}

/*This class is to start the thread to start a SocketThread
  and record the uses' name in a map
*/
class SocketThread extends Thread{
    
    public SocketThread(){
        super();
    }

    static Map<String,Socket> users = new HashMap<String,Socket>();
    static Map<String,Socket> usersOn = new HashMap<String,Socket>();
    public static int PORT = 8080;
    ServerSocket s= null;
    Socket socket =null;
    String st;
    public void run(){    
        try{
            s=new ServerSocket(PORT);
            while(true){
                System.out.println("socket= "+s);
                socket =s.accept();
                System.out.println("Connection accept socket:"+socket);

                BufferedReader br=null;                                 //to get the user's name first 
                try{                                          
                    br = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                     
                 //   while(!socket.isClosed()){
                        st = br.readLine();
                        if(users.containsKey(st)){  
                        //if the user has appeared before
                            if(usersOn.containsKey(st))
                            //if the user is already online, then do nothing
                                System.out.println(st+" is already online!!! ");
                            else{
                            //if the user is off line, then change its socket, and start the thread of reading
                                users.remove(st);
                                users.put(st,socket);
                                usersOn.put(st,socket);
                                System.out.println(st+" is online again!!!");
                                new ReadThread(socket, st).start();
                            }
                        }
                        else{ 
                            usersOn.put(st,socket);
                            users.put(st,socket);
                            System.out.println(st+" is a  new user!!!");
                            new ReadThread(socket, st).start();
                        }

                 //   }
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
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

/*The definition of the message : 
 * 1.the first char is to figure out the type of the message:"0" stands for name;"1" stands for contents
 * 2.the rest chars is the main body
 */
class  ReadThread extends Thread{
    //client socketRead;
    Socket strl;
    String destName = null;
    String name;
    public ReadThread(Socket str, String name){
        //super(str);
        this.strl=str;
        this.name=name;
    }

    public void run(){
        BufferedReader br=null;
        SendMessage sendMes = new SendMessage();
        Socket dest = null;
        try{
            br = new BufferedReader(new InputStreamReader(
                    strl.getInputStream()));
                     
            while(!strl.isClosed()){
                String st = br.readLine();
                if(st.charAt(0) == '0'){                   
                //if the first char is '0', it means that the message is the destination user
                    destName = st.substring(1);
                    dest = sendMes.nameTosocket(destName);
                }

                if(dest != null)
                //to send the message wholely to the destination user and let it to analyse the message 
                    sendMes.Send(dest,st);
                
                if (st.charAt(0) == '1')
                    System.out.println("From(" + name+") "+"To("+destName+") : " + st.substring(1));  

            try{
                sleep((int)(Math.random()*1000));
            }catch(InterruptedException e){}
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
            SocketThread.usersOn.remove(name);
        }
    }
}


class  SendMessage{

    public Socket nameTosocket(String name){
    //to get the socket according to the destination name from the map
        Socket socket;
        if(SocketThread.usersOn.containsKey(name)){
        //if the user is online, then return its socket,else return null
            socket = SocketThread.usersOn.get(name);
            System.out.println(name + " is online!!!");
            return socket;
        }
        else{
            if(SocketThread.users.containsKey(name))
                System.out.println(name + " is off line!!!");
            else 
                System.out.println(name + " has not disappeared!!!");
            return null;
        } 
    }

    public void Send(Socket soc, String message){
    //to send the message to the destination user
        PrintWriter pw=null;
        try{
            pw = new PrintWriter(new BufferedWriter( new OutputStreamWriter(
                     soc.getOutputStream())),true);

            pw.println(message); 
            System.out.println(message);
            pw.flush();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}


