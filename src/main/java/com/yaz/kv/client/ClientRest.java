package main.java.com.yaz.kv.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ClientRest {
    public static void  main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("localhost",8000);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (true) {
            String message = sc.nextLine();
            if(Objects.equals(message, "Bye")) {
                printWriter.println(message);
                break;
            }
            printWriter.println(message);

            String str = bufferedReader.readLine();
            System.out.println(str);
        }

    }
}
