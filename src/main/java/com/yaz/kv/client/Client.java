package main.java.com.yaz.kv.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 6000);

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

                    System.out.println("\nServer : " + message);

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
    }
}