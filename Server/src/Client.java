public class Client {


    private String username;

    private String cardNumber;

    private String publicKey;

    Client(String username, String cardNumber, String publicKey) {
        this.username = username;
        this.cardNumber = cardNumber;
        this.publicKey = publicKey;
    }


    public String getUsername() {
        return username;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
