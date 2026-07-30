package main.java.com.yaz.kv.server;


import model.ClientHandler;
import model.KeyValueStore;
import model.KeyValueStoreExpiry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerRest {
    public static void  main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8000);

        System.out.println("Server Started at Port 8000....");
        KeyValueStore keyValueStore = new KeyValueStore();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("store.txt"));
        String m;
        while((m = bufferedReader.readLine()) != null) {
            KeyValueStoreExpiry keyValueStoreExpiry = new KeyValueStoreExpiry();
            String[] arr = m.split(" ");

            if(Objects.equals(arr[0].toUpperCase(), "PUT")) {
                keyValueStoreExpiry.setValue(arr[2]);
                keyValueStoreExpiry.setExpiry(Long.valueOf(arr[3]));
                keyValueStore.restore(arr[1],keyValueStoreExpiry);
            } else if (Objects.equals(arr[0].toUpperCase() , "DELETE")) {
                keyValueStore.delete(arr[1]);
            }
        }

        ExecutorService pool = Executors.newFixedThreadPool(10);

        while(true) {
            Socket clientSocket = serverSocket.accept();
            pool.submit(new ClientHandler(clientSocket, keyValueStore));
        }
    }
}
