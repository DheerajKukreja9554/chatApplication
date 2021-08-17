package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSide {

    Socket socket;
    DataOutputStream output;
    DataInputStream input;

    ClientSide() {
        try {
            System.out.println("connecting to server....");
            //the ip address 192.168.0.1 was only connecting to local network, have to make it a universal server
            socket = new Socket("127.0.0.1", 1111);
            System.out.println("connected");
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            startService();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
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
                            // output.flush();
                            socket.close();
                            break;
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
                }

            }

        });
        speakThread.start();
        System.out.println("started lspeaking......");
    }

    public void startlistening() {

        Thread listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {

                        String msg = input.readUTF();
                        System.out.println("Server: " + msg);
                        if(msg.equalsIgnoreCase("exit")){
                            System.out.println("exiting");
                            input.close();
                            socket.close();
                            break;
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e);
                }
            }
        });

        listenThread.start();
        System.out.println("Started Listening......");
    }

    public void startService() {
        startSpeaking();
        startlistening();
    }

    public static void main(String[] args) {

        ClientSide client = new ClientSide();

        // try{
        // Socket s=new Socket("localhost",6666);
        // DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        // dout.writeUTF("Hello Server");
        // dout.flush();
        // dout.close();
        // s.close();
        // }catch(Exception e){System.out.println(e);}
        // } }
    }
}

// package client;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.Socket;
// import java.net.UnknownHostException;
// import java.util.Scanner;

// public class ClientSide {
// Socket socket;

// BufferedReader input;
// PrintWriter output;

// public ClientSide() {
// try {
// System.out.println("Client is ready to connect");
// System.out.println("Connecting......");

// socket = new Socket("127.0.0.1", 1110);
// System.out.println("Connected");
// input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// output = new PrintWriter(socket.getOutputStream());
// startService();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// public void startlistening() {

// Thread listenThread = new Thread(new Runnable() {

// @Override
// public void run() {
// try {

// while (true) {

// String content="input.readLine()";
// System.out.println(input.readLine());
// if (content.equalsIgnoreCase("exit")) {
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
// Thread speakThread = new Thread(new Runnable() {

// @Override
// public void run() {
// while (true) {

// try {
// Scanner sc=new Scanner(System.in);
// String content="Client: ";//+sc.nextLine();
// output.println(sc.nextLine());
// if (content.equalsIgnoreCase("exit")) {
// output.println("exit");
// output.flush();
// break;
// }

// } catch (Exception e) {
// //TODO: handle exception
// e.printStackTrace();
// }
// }
// }

// });

// speakThread.start();
// }

// public void startService() {
// startlistening();
// startSpeaking();
// }

// public static void main(String[] args) {
// ClientSide client = new ClientSide();
// // client.startService();
// }

// }
