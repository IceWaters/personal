
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;



class  ReadThreadclient extends Thread{
    //client socketRead;
    Socket strl;
    public ReadThreadclient(Socket str){
        //super(str);
        this.strl=str;
    }

    public void run(){

        BufferedReader br=null;
        String sender = null;
        try{
            br = new BufferedReader(new InputStreamReader(
                        strl.getInputStream()));

            while(!strl.isClosed()){
                String st = br.readLine();
                if(st.charAt(0) == '0' && st.length() >= 1)
                    sender = st.substring(1);
                else{
                    System.out.println("Receive("+sender+"): "+st.substring(1));  
                }
                try{
                    sleep((int)(Math.random()*1000));
                }catch(InterruptedException e){}
            }
        }catch(IOException e){
            System.out.println("nihap");
            System.out.println(e.getMessage());
        }
    }
}


class  SendThreadclient extends Thread{

    Socket strl;
    public SendThreadclient(Socket str){
        //super(str);
        this.strl=str;
    }

    public void run(){

        BufferedReader buf=null;
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new BufferedWriter( new OutputStreamWriter(
                            strl.getOutputStream())),true);

            buf =new BufferedReader(new InputStreamReader(System.in)); //to read from the console

            System.out.println("Please input who you are:");
            String st = buf.readLine(); 
            pw.println(st);      
            pw.flush();

            System.out.println("Please input who you want to talk(enter to change again):");
            st = buf.readLine(); 
            st = "0" + st;
            pw.println(st);      
            pw.flush();


            while(!strl.isClosed()){
               // String 
                st = buf.readLine(); 
                if(st.length() == 0){
                //when input the enter key, then to change the destination user
                    System.out.println("Do you want to change the destination user?\n(y)es  (n)o");
                    st = buf.readLine();
                    System.out.println("("+st+")");
                    if(st.charAt(0) == 'y'){
                        System.out.println("Please input who you want to talk(enter to change again):");
                        st = buf.readLine(); 
                        st = "0" + st;
                        pw.println(st);      
                        pw.flush();
                    }
                    else
                        System.out.println("The destination user has not been changed.\n"
                               +"If you want to change it, please try it again!");
                }
                else{
                    st = "1" + st;
                    pw.println(st);      
                    pw.flush();
                }
            //    System.out.println("("+st+")");
            //    st = st.concat("1").concat(st);

                try{
                    sleep((int)(Math.random()*1000));
                }catch(InterruptedException e){}
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}

public class client{
    //ublic static int POPRT = 8080;
    public static void main(String args[]){
        Socket socket =null;
        try{
            socket=new Socket("127.0.0.1", 8080);
            System.out.println("socket= "+socket);
            new ReadThreadclient(socket).start();
            new SendThreadclient(socket).start();

            //	pw.println("END");
            //	pw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }/*ally{
        try{
        System.out.println("close...");
        //	br.close();
        //	pw.close();
        socket.close();
        }catch(IOException e){
        e.printStackTrace();
        }
        } */
    }
}
