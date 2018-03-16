import java.io.*;
import java.net.*;

public class VendorToBank {
    private int bankPort = 7777;

    private Socket socket;

    private BufferedReader socketBufferedReader;

    private PrintWriter printWriter;

    VendorToBank() throws IOException {
        this.socket = new Socket("localhost", bankPort);
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    void connectToBankServer(String clientCommit, String payment, int steps) throws IOException {
        sendClientData(clientCommit,payment,steps);
        if(!socketBufferedReader.readLine().equals("OK"))
            System.out.println("payment not received");
        else{
            System.out.println("payment received");
        }
        System.out.println();
    }

    void sendClientData(String clientCommit, String payment, int steps) throws IOException {
        printWriter.println(clientCommit);
        printWriter.println(payment);
        printWriter.println(steps);
        System.out.println("client data sent to bank server");

    }

}
