package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {

    String clientName;
    Socket socket;
    DataOutputStream output;
    DataInputStream input;

    ClientSide(String name) {
        try {
            clientName=name;
            System.out.println("\nconnecting to server....");
            //the ip address 192.168.0.1 was only connecting to local network, have to make it a universal server
            socket = new Socket("127.0.0.1", 1111);
            System.out.println("CONNECTED\n");
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            output.writeUTF(clientName);
            // startService();
        } catch (Exception e) {
            System.out.println("Lost connection to server");
                    
            System.exit(0);
        }
    }

    public void startSpeaking() {
        Thread speakThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        Scanner sc = new Scanner(System.in);
                        String msg = sc.nextLine();
                        output.writeUTF(msg);
                        if(msg.equalsIgnoreCase("exit")){
                            System.out.println("exiting");
                            socket.close();
                            break;
                        }
                    }
                    // sc.close();
                } catch (Exception e) {
                    System.out.println("Lost connection to server");
                    
                    System.exit(0);
                }

            }

        });
        speakThread.start();
    }

    public void startlistening() {

        Thread listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {

                        String msg = input.readUTF();
                        if(!msg.startsWith(clientName)&&!msg.equals("\nnew connection["+clientName+"]\n"))
                            System.out.println(msg);
                        if(msg.equalsIgnoreCase("bye")){
                            System.out.println("exiting");
                            input.close();
                            socket.close();
                            break;
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Lost connection to server");
                    
                    System.exit(0);
                }
            }
        });

        listenThread.start();
    }

    public void startService() {
        startSpeaking();
        startlistening();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name:");
        ClientSide client = new ClientSide(sc.nextLine());
        client.startService();

        
    }
}

