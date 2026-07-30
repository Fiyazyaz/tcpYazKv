package main.java.com.yaz.kv.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6000);

        System.out.println("Waiting for client...");

        Socket socket = serverSocket.accept();

        System.out.println("Client Connected!");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));

        PrintWriter writer =
                new PrintWriter(socket.getOutputStream(), true);

        Scanner sc = new Scanner(System.in);

        // Receive Thread
        Thread receiveThread = new Thread(() -> {

            try {

                String message;

                while ((message = reader.readLine()) != null) {

                    System.out.println("\nClient : " + message);

                    if (message.equalsIgnoreCase("bye")) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        // Send Thread
        Thread sendThread = new Thread(() -> {

            try {

                while (true) {

                    String message = sc.nextLine();

                    writer.println(message);

                    if (message.equalsIgnoreCase("bye")) {
                        break;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        receiveThread.start();
        sendThread.start();

        receiveThread.join();
        sendThread.join();

        socket.close();
        serverSocket.close();
    }
}