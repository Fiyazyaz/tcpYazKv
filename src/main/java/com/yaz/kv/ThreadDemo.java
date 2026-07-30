package main.java.com.yaz.kv;

public class ThreadDemo {

    public static void main(String[] args) {

        Thread sender = new Thread(() -> {

            for (int i = 1; i <= 10; i++) {

                System.out.println("Sender : " + i);

            }

        });

        Thread receiver = new Thread(() -> {

            for (int i = 1; i <= 10; i++) {

                System.out.println("Receiver : " + i);

            }

        });

        sender.start();
        receiver.start();
    }
}
