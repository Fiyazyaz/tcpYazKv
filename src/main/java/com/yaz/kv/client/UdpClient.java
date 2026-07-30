package main.java.com.yaz.kv.client;
import java.net.*;
import java.util.Scanner;

public class UdpClient {

    public static void main(String[] args) throws Exception {

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(10000);

//        Scanner sc = new Scanner(System.in);

        while (true) {

//            String message = sc.nextLine();

//            if(message.equalsIgnoreCase("bye"))
//                break;

            byte[] data = "PING".getBytes();

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            data,
                            data.length,
                            InetAddress.getByName("localhost"),
                            9000);

            socket.send(sendPacket);

            byte[] receiveBuffer = new byte[1024];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveBuffer,
                            receiveBuffer.length);

            socket.receive(receivePacket);

            String reply =
                    new String(
                            receivePacket.getData(),
                            0,
                            receivePacket.getLength());

            System.out.println("Server : " + reply);
            Thread.sleep(1000);
        }
    }
}
