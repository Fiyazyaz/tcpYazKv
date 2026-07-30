package main.java.com.yaz.kv.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP {
    public static void main(String[] args) throws Exception {

        DatagramSocket socket = new DatagramSocket(9000);

        socket.setSoTimeout(3000);

        byte[] buffer = new byte[1024];

        System.out.println("Server Started");

        while (true) {

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            String message =
                    new String(
                            packet.getData(),
                            0,
                            packet.getLength());

            System.out.println("Client : " + message);

            byte[] reply = "PONG".getBytes();

            DatagramPacket response =
                    new DatagramPacket(
                            reply,
                            reply.length,
                            packet.getAddress(),
                            packet.getPort());

            socket.send(response);
        }
    }
}
