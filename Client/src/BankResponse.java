import java.net.*;
import java.text.*;
import java.util.*;

public class BankResponse {

    private String bankIdentity;

    private String clientIdentity;

    private SocketAddress clientIP;

    private byte[] bankPublicKey;

    private byte[] clientPublicKey;

    private Date expiryDate;

    private String additionalInfo;

    private byte[] bankSignature;

    public BankResponse(BankRawResponse bankRawResponse) {
        this.bankIdentity = bankRawResponse.getBankIdentity();
        this.clientIdentity = bankRawResponse.getClientIdentity();
        this.clientIP = parseClientIP(bankRawResponse.getClientIP());
        this.bankPublicKey = getByteArrayFromString(bankRawResponse.getBankPublicKey());
        this.clientPublicKey = getByteArrayFromString(bankRawResponse.getClientPublicKey());
        try {
            this.expiryDate = new SimpleDateFormat("dd.MM.yyyy").parse(bankRawResponse.getExpiryDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.additionalInfo = bankRawResponse.getAdditionalInfo();
        this.bankSignature = getByteArrayFromString(bankRawResponse.getBankSignature());
    }

    public SocketAddress parseClientIP(String hostAndPort) {
        String host = hostAndPort.substring(0, hostAndPort.indexOf(':'));
        int port = Integer.parseInt(hostAndPort.substring(hostAndPort.indexOf(':') + 1));
        return new InetSocketAddress(host, port);
    }

    private byte[] getByteArrayFromString(String stringToConvert) {
        String[] parts = stringToConvert.replace("[", "").replace("]", "").split(", ");
        byte[] byteArray = new byte[parts.length];
        int i = 0;
        for (String part : parts) {
            byteArray[i++] = Byte.valueOf(part);
        }
        return byteArray;
    }

    public String getBankIdentity() {
        return bankIdentity;
    }

    public String getClientIdentity() {
        return clientIdentity;
    }

    public SocketAddress getClientIP() {
        return clientIP;
    }

    public byte[] getBankPublicKey() {
        return bankPublicKey;
    }

    public byte[] getClientPublicKey() {
        return clientPublicKey;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public byte[] getBankSignature() {
        return bankSignature;
    }
}
