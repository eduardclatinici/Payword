import java.io.*;

public class Client {

    private static final int bankPort = 9997;

    private static final int vendorPort = 8888;

    private static ClientEntity clientEntity;


    public static void main(String[] args) {
        try {
            clientEntity = new ClientToBank(bankPort).getClientInfoWithBankResponse();
            new ClientToVendor(vendorPort, clientEntity).connectToVendorServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
