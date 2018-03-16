import java.io.*;
import java.net.*;
import java.util.*;

import javafx.util.*;

public class ClientToVendor {

    private Socket vendorSocket;

    private BufferedReader socketBufferedReader;

    private PrintWriter printWriter;

    private ClientEntity clientEntity;

    private Service service = new Service();

    private Map<String, Integer> products = new HashMap<>();

    private Pair<String, Integer> chosenProduct;

    private List<String> hashChain;

    private String commit;

    ClientToVendor(int vendorPort, ClientEntity clientEntity) throws IOException {
        this.vendorSocket = new Socket("localhost", vendorPort);
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(vendorSocket.getInputStream()));
        this.printWriter = new PrintWriter(vendorSocket.getOutputStream(), true);
        this.clientEntity = clientEntity;
    }

    void connectToVendorServer() throws IOException {
        printWriter.println(this.clientEntity.getUsername());
        products = service.convertStringToMap(socketBufferedReader.readLine());
            this.clientEntity.setSecret(service.getClientSecret());
            if(getProductAndPrice()) {
                getHashChain();
                this.clientEntity.setCommit(
                        service.getClientCommit(this.clientEntity, this.hashChain.get(0), this.hashChain.size()));
                printWriter.println(this.clientEntity.getCommit());
                if (socketBufferedReader.readLine().equals("OK"))
                    printWriter.println(chosenProduct.getKey());
                for (int i = 1; i < chosenProduct.getValue(); i++)
                    printWriter.println(this.hashChain.get(i));
                printWriter.println(this.clientEntity.getSecret());
                String response = socketBufferedReader.readLine();
                System.out.print("vendor replied with: " + response);

                if (response.equals("true"))
                    System.out.println("Done");
                System.out.println();
            }
        }

    private boolean getProductAndPrice() throws IOException {
        int ok = 0;
        String chosenProduct = null;
        while (ok == 0) {
            System.out.println(products);
            chosenProduct = service.getOption();
            if (products.keySet().contains(chosenProduct))
                ok = 1;
            else if(chosenProduct.equals("exit"))
                return false;
        }
        this.chosenProduct = new Pair<>(chosenProduct, products.get(chosenProduct));
        return true;
    }

    private void getHashChain() {
        hashChain = new ArrayList<>();
        hashChain.add(String.valueOf(this.clientEntity.getSecret()));
        hashChain.addAll(service.getHashChainAsStringArray(String.valueOf(this.clientEntity.getSecret()),
                this.chosenProduct.getValue()));
        Collections.reverse(hashChain);
    }
}
