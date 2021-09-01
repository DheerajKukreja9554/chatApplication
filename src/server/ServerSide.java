package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServerSide {
    
    ServerSocket server;
    Socket socket;
    List<DataOutputStream> clients;
    List<DataInputStream> input;
    
    ServerSide() {
        try {
            // creating server
            System.out.println("Creating Server");
            server=new ServerSocket(1111);
            clients=new LinkedList<>();
            input=new LinkedList<>();
            System.out.println("waiting to connect.....\n");
        }
        catch (Exception e) {
            
            System.out.println(e);
        }
    }
    public void startService() {
        while (true) {
                // accepting connections and for each connection starting to llisten and speak by listen this
                try {
                    socket=server.accept();
                    new Thread(new ListenThis(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
    
    public class ListenThis implements Runnable {
        
        Socket sock;
        String name;
        DataInputStream inputStream;
        DataOutputStream outputStream;
        ListenThis(Socket sock){
            try {
                this.sock=sock;
                inputStream=new DataInputStream(sock.getInputStream());
                outputStream=new DataOutputStream(sock.getOutputStream());
                clients.add(outputStream);
                input.add(inputStream);
                name=inputStream.readUTF();
                //for this connecton, telling group that it has connected
                speakToAll("\nnew connection["+name+"]\n");
                // System.out.println(input.size());
                // System.out.println(clients.size());
            } catch (Exception e) {
                System.out.println(e+"ex");
            }
        }


        @Override
        public void run() {
            try {
                while (true) {
                    String msg=inputStream.readUTF();
                    System.out.println(name+": " +msg);
                    speakToAll(name+": " +msg);
                    if (msg.equalsIgnoreCase("bye")) {
                        input.remove(inputStream);
                        clients.remove(outputStream);
                        sock.close();
                    }
                }
                
            } catch (Exception e) {
                
                speakToAll("\nconnection lost to ["+name+"]\n");
                System.out.println("\nconnection lost to ["+name+"]\n");
            }
            
        }
        public void speakToAll(String msg) {
            try {
                for (DataOutputStream dataOutputStream : clients) {
                    dataOutputStream.writeUTF(msg);
                }
            } catch (Exception e) {
                // doing this will give error while writing to all discinnneccted sockets
                // e.printStackTrace();
            }
        }
        
    }
    
    public static void main(String[] args) {
        ServerSide server=new ServerSide();
       
        server.startService();
    }
}

