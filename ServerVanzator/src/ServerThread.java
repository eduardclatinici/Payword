import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {

    private Socket socket;

    private PrintWriter printWriter;

    private BufferedReader bufferedReader;

    private Map<String, Integer> products = new HashMap<>();

    private Service service = new Service();

    private ClientRawCommit clientRawCommit;

    private ClientCommit clientCommit;

    private List<byte[]> hashChain;

    private String payment;

    ServerThread(Socket serverSocket) throws IOException {
        this.socket = serverSocket;
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.products = service.getInitializedMap();
    }

    @Override
    public void run() {
        try {
            System.out.println("user " + bufferedReader.readLine() + " is now connected to the  vendor server...");
            printWriter.println(this.products);
            String chosenProduct;
            int i;
            getClientCommit();
            if (verifySignatures()) {
                printWriter.println("OK");
                chosenProduct = bufferedReader.readLine();
                hashChain = new ArrayList<>();
                hashChain.add(clientCommit.getHashChainRoot());
                printWriter.println(checkHashChain(chosenProduct));
                new VendorToBank().connectToBankServer(this.clientCommit.getClientCommit(),payment,hashChain.size()-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getClientCommit() throws IOException {
        this.clientRawCommit = new ClientRawCommit(bufferedReader.readLine());
        this.clientCommit = new ClientCommit(clientRawCommit);
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

    private boolean checkHashChain(String chosenProduct) throws IOException {
        int i;
        for (i = 1; i < products.get(chosenProduct); i++) {
            hashChain.add(service.getByteArrayFromString(bufferedReader.readLine()));
            if (!Arrays.equals(service.applyHash(hashChain.get(i)), hashChain.get(i - 1)))
                return false;
        }
        payment = bufferedReader.readLine();
        hashChain.add(payment.getBytes());
        if (!Arrays.equals(service.applyHash(hashChain.get(i)), hashChain.get(i - 1)))
            return false;
        return true;
    }

}
