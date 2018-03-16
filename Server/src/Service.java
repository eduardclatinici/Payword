import java.security.*;
import java.security.spec.*;

public class Service {
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
