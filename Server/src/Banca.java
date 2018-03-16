import java.io.*;
import java.net.*;
import java.util.*;

import javafx.util.*;

public class Banca {

    private static final int CLIENT_PORT = 9997;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);
        List<String> payments = new ArrayList<>();
        String payment;
        System.out.println("Bank Server up & ready for connections.......");
        while(true) {
            new ServerThread(serverSocket.accept()).start();
            payment = new BankToVendor(payments).connectToVendor();
            payments.add(payment);
        }
    }
}
