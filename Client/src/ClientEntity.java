import java.security.*;

public class ClientEntity {
    private String username;
    private String cardNumber;
    private int secret;
    private KeyPair keyPair;
    private BankRawResponse bankRawResponse;
    private BankResponse bankResponse;

    private String commit;

    public ClientEntity(String username, String cardNumber,int secret, KeyPair keyPair, BankRawResponse bankRawResponse, BankResponse bankResponse) {
        this.username = username;
        this.cardNumber = cardNumber;
        this.keyPair = keyPair;
        this.bankRawResponse = bankRawResponse;
        this.bankResponse = bankResponse;
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public BankRawResponse getBankRawResponse() {
        return bankRawResponse;
    }

    public int getSecret() {
        return secret;
    }

    public void setSecret(int secret) {
        this.secret = secret;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}
