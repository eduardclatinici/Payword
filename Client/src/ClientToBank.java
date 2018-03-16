import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javafx.util.*;

class ClientToBank {

    private KeyPair keyPair;

    private BufferedReader socketBufferedReader;

    private Socket bankSocket;

    private PrintWriter printWriter;

    private Pair<String, String> clientInputInfo;

    private BankRawResponse bankRawResponse;

    private BankResponse bankResponse;

    private Service service = new Service();

    ClientToBank(int bankPort) throws IOException {
        bankSocket = new Socket("localhost", bankPort);
        socketBufferedReader = new BufferedReader(new InputStreamReader(bankSocket.getInputStream()));
        printWriter = new PrintWriter(bankSocket.getOutputStream(), true);
        keyPair = service.generateKeyPair();
    }

    ClientEntity getClientInfoWithBankResponse() throws IOException {
        connectToBankServer();
        return new ClientEntity(clientInputInfo.getKey(), clientInputInfo.getKey(),service.getClientSecret(), this.keyPair, this.bankRawResponse,
                this.bankResponse);
    }

    void connectToBankServer() throws IOException {
        clientInputInfo = service.getClientInfo();
        sendClientInfo(clientInputInfo.getKey(), clientInputInfo.getValue());
        System.out.println("Client data sent to the bank");
        getBankResponse();
        System.out.println("Bank data received");
        System.out.println("Bank responded with: " + bankRawResponse.getBankData());
        System.out.println("Signature verified: " + service.verifySignature(bankResponse.getBankSignature(),
                bankResponse.getBankPublicKey(), bankRawResponse.getBankDataWithoutSignature()));
//        String message;
//        while (true) {
//            System.out.println("message to send: ");
//            message = commandPromptBufferedReader.readLine();
//            if (message.equals("exit")) {
//                bankSocket.close();
//                break;
//            }
//            printWriter.println(message);
//            System.out.print("server replied with: ");
//            System.out.println(socketBufferedReader.readLine());
//        }
    }

    private void sendClientInfo(String username, String creditCardNumber) throws IOException {
        printWriter.println(username);
        printWriter.println(creditCardNumber);
        printWriter.println(Arrays.toString(keyPair.getPublic().getEncoded()));
    }

    private void getBankResponse() throws IOException {
        this.bankRawResponse = new BankRawResponse(socketBufferedReader.readLine()); //collect bank data
        this.bankResponse = new BankResponse(bankRawResponse);
    }

}
