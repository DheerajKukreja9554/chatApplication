package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSide {
    
    ServerSocket server;
    Socket socket;
    DataOutputStream output;
    DataInputStream input;


    public ServerSide() {
        try {
            System.out.println("Creating Server");
            server=new ServerSocket(1111);
            System.out.println("waiting to connect.....");
            socket=server.accept();
            System.out.println("Connected");
            output = new DataOutputStream(socket.getOutputStream());
            input=new DataInputStream(socket.getInputStream());
            startService();

        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }
    }

    public void startSpeaking() {
        Thread speakThread=new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    while (true) {
                        Scanner sc=new Scanner(System.in);
                        String msg=sc.nextLine();
                        output.writeUTF(msg);
                        if(msg.equalsIgnoreCase("exit")){
                            System.out.println("exiting");
                            // output.flush();
                            socket.close();
                            sc.close();
                            break;
                        }
                    }
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println(e);
                }
                
            }
            
            
        });

        speakThread.start();
        System.out.println("started speaking");
    }

    public void startlistening() {

        Thread listenThread=new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    while(true){
                        String msg=input.readUTF();
                        System.out.println("Client: "+msg);
                        if(msg.equalsIgnoreCase("exit")){
                            System.out.println("exiting");
                            input.close();
                            socket.close();
                            break;
                        }
                    }
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println(e);
                }
            }
        });

        listenThread.start();
        System.out.println("Started Listening.........");
    }

    public void startService() {
        startSpeaking();
        startlistening();
    }
    public static void main(String[] args) {
        ServerSide server=new ServerSide();
    }
}

// package server;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.Scanner;

// // import jdk.internal.org.jline.utils.InputStreamReader;

// public class ServerSide {

// ServerSocket server;
// Socket socket;
// Scanner sc=new Scanner(System.in);
// BufferedReader input;
// PrintWriter output;
// ServerSide(){
// try {
// server=new ServerSocket(1110);
// System.out.println("Server is ready to connect");
// System.out.println("waiting");
// socket=server.accept();
// System.out.println("connected");
// input=new BufferedReader(new
// java.io.InputStreamReader(socket.getInputStream()));
// output=new PrintWriter(socket.getOutputStream());
// startServer();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// public void startlistening() {

// Thread listenThread=new Thread(new Runnable(){

// @Override
// public void run() {
// try {
// while(true){

// // System.out.print("Client: ");
// String content="input.readLine();";
// System.out.println("heard.....");
// System.out.println(input.readLine());
// if(content.equalsIgnoreCase("exit")){
// System.out.println("exiting.....");
// break;
// }
// }

// } catch (Exception e) {
// //TODO: handle exception
// e.printStackTrace();
// }

// }

// });
// listenThread.start();
// }

// public void startSpeaking() {
// Thread speakThread=new Thread(new Runnable(){

// @Override
// public void run() {
// Scanner sc=new Scanner(System.in);
// try {
// while(true){
// // String content="Server: "+sc.nextLine();
// output.println(sc.nextLine());
// System.out.println("sent......");
// // if(content.equalsIgnoreCase("Server: exit")){
// // System.out.println("");
// // }
// }

// } catch (Exception e) {
// //TODO: handle exception
// e.printStackTrace();
// }
// // sc.close();

// }

// });

// speakThread.start();
// }

// public void startServer() {
// startlistening();
// startSpeaking();
// }
// public static void main(String[] args) {
// ServerSide server=new ServerSide();
// // server.startServer();
// }
// }
