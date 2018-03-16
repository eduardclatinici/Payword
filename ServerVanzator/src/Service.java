import java.security.*;
import java.security.spec.*;
import java.util.*;

public class Service {

    public Map<String, Integer> getInitializedMap(){
        Map<String, Integer> map = new HashMap<>();
        map.put("obiect1",5);
        map.put("obiect2",10);
        map.put("obiect3",15);
        return map;
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

    private PublicKey getPublicKey(byte[] pubKey) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getByteArrayFromString(String stringToConvert) {
        String[] parts = stringToConvert.replace("[", "").replace("]", "").split(", ");
        byte[] byteArray = new byte[parts.length];
        int i = 0;
        for (String part : parts) {
            byteArray[i++] = Byte.valueOf(part);
        }
        return byteArray;
    }

    public byte[] applyHash(byte[] bytes){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

}
