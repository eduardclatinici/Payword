import java.io.*;
import java.net.*;

public class Vanzator {
    private static final int port = 8888;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Vendor Server up & ready for connections.......");
        while(true) {
            new ServerThread(serverSocket.accept()).start();
        }
    }

}
