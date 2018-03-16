import java.io.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.text.*;
import java.util.*;

import javafx.util.*;
import sun.security.rsa.*;

public class Service {

    private BufferedReader commandPromptBufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public KeyPair generateKeyPair() {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, new SecureRandom());
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Pair<String, String> getClientInfo() throws IOException {
        System.out.println("username: ");
        String username = commandPromptBufferedReader.readLine();
        System.out.println("Credit card number: ");
        String creditCardNumber = commandPromptBufferedReader.readLine();
        return new Pair<>(username, creditCardNumber);
    }

    public String getOption() throws IOException {
        String option;
        System.out.print("option: ");
        option = commandPromptBufferedReader.readLine();
        System.out.println();
        return option;
    }

    public boolean verifySignature(byte[] signatureToVerify, byte[] pubKey, String plainText) {
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(getPublicKey(pubKey));
            signature.update(plainText.getBytes());
            return signature.verify(signatureToVerify);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return Boolean.parseBoolean(null);
        }
    }

    public int getClientSecret() {
        Random random = new Random();
        return random.nextInt(100000 - 10000) + 10000;
    }

    private PublicKey getPublicKey(byte[] pubKey) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Integer> convertStringToMap(String string) {
        Map<String, Integer> map = new HashMap<>();
        string = string.replace("{", "").replace("}", "");
        for (String word : string.split(", "))
            map.put(word.split("=")[0], Integer.parseInt(word.split("=")[1]));
        return map;
    }

    public List<String> getHashChainAsStringArray(String string, int steps) {
        List<String> list = new ArrayList<>();
        MessageDigest messageDigest = null;
        byte[] hashString = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        messageDigest.update(string.getBytes());

        for (int i = 0; i < steps; i++) {
            hashString = messageDigest.digest();
            list.add(Arrays.toString(hashString));
            messageDigest.update(hashString);
        }
        return list;
    }

    public String tagString(String tag, String string) {
        return "<" + tag + ">" + string + "</" + tag + ">";
    }

    public String getClientCommit(ClientEntity clientEntity, String hashChainRoot, int hashChainLength) {
        String plainCommit = getPlainCommit(clientEntity.getBankRawResponse(), hashChainRoot, hashChainLength);
        return plainCommit + getSignedCommit(plainCommit, clientEntity.getKeyPair().getPrivate());
    }

    private String getSignedCommit(String message, PrivateKey clientPrivateKey) {
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(clientPrivateKey, new SecureRandom());
            signature.update(message.getBytes());
            return tagString("clientSignature", Arrays.toString(signature.sign()));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPlainCommit(BankRawResponse bankRawResponse, String hashChainRoot, int hashChainLength) {
        return new StringBuilder().append(tagString("idVanzator", "Vanzator"))
                .append(tagString("payword", bankRawResponse.getBankData()))
                .append(tagString("hashChainRoot", hashChainRoot))
                .append(tagString("currentDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date())))
                .append(tagString("hashChainLength", String.valueOf(hashChainLength))).toString();
    }



}

