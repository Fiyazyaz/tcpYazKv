package model;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final KeyValueStore keyValueStore;

    public ClientHandler(Socket socket, KeyValueStore keyValueStore) {
        this.socket = socket;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public void run() {

        try {

            InputStream inputStream = socket.getInputStream();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            byte[] buffer = new byte[8];
            StringBuilder sb = new StringBuilder();

            while (true) {

                int n = inputStream.read(buffer);

                if (n == -1) {
                    System.out.println("Client Disconnected");
                    break;
                }

                // Append newly received bytes
                sb.append(new String(buffer, 0, n));

                // Process every complete command
                while (sb.indexOf("\n") != -1) {

                    String message =
                            sb.substring(0, sb.indexOf("\n")).trim();

                    sb.delete(0, sb.indexOf("\n") + 1);

                    System.out.println("Received : " + message);

                    if (message.equalsIgnoreCase("bye")) {
                        return;
                    }

                    String[] arr = message.split(" ");

                    //---------------------------------------------------
                    // Save command to file
                    //---------------------------------------------------

                    if (arr[0].equalsIgnoreCase("PUT") ||
                            arr[0].equalsIgnoreCase("DELETE")) {

                        try (PrintWriter writer =
                                     new PrintWriter(
                                             new BufferedWriter(
                                                     new FileWriter("store.txt", true)))) {

                            writer.println(message);

                        }
                    }

                    //---------------------------------------------------
                    // Execute command
                    //---------------------------------------------------

                    if (arr[0].equalsIgnoreCase("PUT")) {

                        KeyValueStoreExpiry expiry =
                                new KeyValueStoreExpiry();

                        expiry.setValue(arr[2]);
                        expiry.setExpiry(Long.parseLong(arr[3]));

                        out.println(
                                keyValueStore.put(arr[1], expiry));

                    }

                    else if (arr[0].equalsIgnoreCase("GET")) {

                        out.println(keyValueStore.get(arr[1]));

                    }

                    else if (arr[0].equalsIgnoreCase("DELETE")) {

                        out.println(keyValueStore.delete(arr[1]));

                    }

                    else {

                        out.println("INVALID COMMAND");

                    }

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}