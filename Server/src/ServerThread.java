import java.io.*;
import java.net.*;
import java.security.*;
import java.text.*;
import java.util.*;

public class ServerThread extends Thread {

    private Socket socket;

    private PrintWriter printWriter;

    private BufferedReader bufferedReader;

    private KeyPair keyPair;

    ServerThread(Socket serverSocket) throws IOException {
        this.socket = serverSocket;
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        generateKeyPair();
    }

    @Override
    public void run() {
            Client client = collectClientData();
            System.out.println("user " + client.getUsername() + " is now connected to the server...");
            printWriter.println(getMessage(client));
            System.out.println("bank sent message to client with username: " + client.getUsername());
            //while (true)printWriter.println(bufferedReader.readLine() + " echo");
    }

    private Client collectClientData() {
        try {
            return new Client(bufferedReader.readLine(), bufferedReader.readLine(), bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to collect client data");
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private String getMessage(Client client){
        String message = getMessageForClient(client);
        return message + getSignature(message);
    }

    private String getMessageForClient(Client client) {
        return new StringBuilder().append(tagString("bankIdentity","banca"))
                .append(tagString("clientIdentity",client.getUsername()))
                .append(tagString("clientIP",socket.getRemoteSocketAddress().toString()))
                .append(tagString("bankPublicKey", Arrays.toString(this.keyPair.getPublic().getEncoded())))
                        //.toString().replace("Sun RSA public key, 2048 bits\n","").replace("modulus: ","")))
                .append(tagString("clientPublicKey",client.getPublicKey()))
                .append(tagString("expiryDate",new SimpleDateFormat("dd.MM.yyyy").format(new Date())))
                .append(tagString("additionalInfo","info")).toString().replace("\n","");
    }

    private String tagString(String tag, String string){
        return "<"+tag+">"+string+"</"+tag+">";
    }

    private String getSignature(String message){
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate(), new SecureRandom());
            signature.update(message.getBytes());
            return tagString("bankSignature",Arrays.toString(signature.sign()));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateKeyPair() {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, new SecureRandom());
            this.keyPair = kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
