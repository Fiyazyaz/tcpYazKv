package main.java.com.yaz.kv.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ServerSelector {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel server = ServerSocketChannel.open();

        server.bind(new InetSocketAddress(8000));

        server.configureBlocking(false);

        Selector selector = Selector.open();

        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server Started...");

        while (true) {

            selector.select();

            Iterator<SelectionKey> iterator =
                    selector.selectedKeys().iterator();

            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();

                iterator.remove();

                if (key.isAcceptable()) {

                    ServerSocketChannel serverChannel =
                            (ServerSocketChannel) key.channel();

                    SocketChannel client = serverChannel.accept();

                    client.configureBlocking(false);

                    client.register(selector, SelectionKey.OP_READ);

                    System.out.println("Client Connected");
                }

                else if (key.isReadable()) {

                    SocketChannel client =
                            (SocketChannel) key.channel();

                    ByteBuffer buffer =
                            ByteBuffer.allocate(1024);

                    int bytesRead = client.read(buffer);

                    if (bytesRead == -1) {

                        client.close();
                        continue;
                    }

                    String message =
                            new String(buffer.array(), 0, bytesRead);

                    System.out.println("Received : " + message);

                    ByteBuffer reply =
                            ByteBuffer.wrap(("Echo : " + message).getBytes());

                    client.write(reply);
                }
            }
        }
    }
}