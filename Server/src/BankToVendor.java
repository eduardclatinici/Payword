import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javafx.util.*;

public class BankToVendor {

    private Socket socket;

    private PrintWriter printWriter;

    private BufferedReader bufferedReader;

    private int port = 7777;

    private ResponseFromVendor responseFromVendor;

    private ClientRawCommit clientRawCommit;

    private ClientCommit clientCommit;

    private Service service = new Service();

    private List<String> payments;


    public BankToVendor(List<String> payments) throws IOException {
        this.socket = new ServerSocket(port).accept();
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.payments = payments;
    }

    String connectToVendor() throws IOException {
        clientRawCommit = new ClientRawCommit(bufferedReader.readLine());
        clientCommit = new ClientCommit(clientRawCommit);
        if(verifySignatures()) {
            responseFromVendor = new ResponseFromVendor(bufferedReader.readLine(),
                    Integer.parseInt(bufferedReader.readLine()));
            System.out.println("data from vendor received");
        }
            if(checkChain() && !this.payments.contains(responseFromVendor.getClientSecret())) {
                printWriter.println("OK");
                System.out.println("payment sent");
            }
        System.out.println();
        socket.close();
        return responseFromVendor.getClientSecret();
    }

    private boolean checkChain(){
        byte[] currentHash = responseFromVendor.getClientSecret().getBytes();
        for(int i=0; i<responseFromVendor.getSteps();i++)
            currentHash = service.applyHash(currentHash);
        return Arrays.equals(currentHash, clientCommit.getHashChainRoot());

    }

    private boolean verifySignatures() {
        boolean isClientSignatureVerified, isBankSignatureVerified;
        isBankSignatureVerified = service.verifySignature(clientCommit.getBankSignature(),
                clientCommit.getBankPublicKey(), clientCommit.getPaywordWithoutSignature());
        isClientSignatureVerified = service.verifySignature(clientCommit.getClientSignature(),
                clientCommit.getClientPublicKey(), clientCommit.getClientCommitWithoutSignature());

        showSignaturesStatus(isBankSignatureVerified, isClientSignatureVerified);

        return isBankSignatureVerified && isClientSignatureVerified;
    }

    private void showSignaturesStatus(boolean bank, boolean client) {
        System.out.println("check client signature: " + client);
        System.out.println("check bank signature: " + bank);

    }
}
