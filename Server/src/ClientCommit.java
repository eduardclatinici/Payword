public class ClientCommit {

    private String clientCommit;
    private String clientCommitWithoutSignature;
    private String payword;
    private String paywordWithoutSignature;
    private byte[] bankPublicKey;
    private byte[] clientPublicKey;
    private byte[] bankSignature;
    private byte[] clientSignature;
    private byte[] hashChainRoot;

    ClientCommit(ClientRawCommit clientRawCommit){
        this.clientCommit = clientRawCommit.getClientCommit();
        this.payword = clientRawCommit.getPayword();
        this.bankPublicKey = getByteArrayFromString(clientRawCommit.getBankPublicKey());
        this.clientPublicKey = getByteArrayFromString(clientRawCommit.getClientPublicKey());
        this.bankSignature = getByteArrayFromString(clientRawCommit.getBankSignature());
        this.clientSignature = getByteArrayFromString(clientRawCommit.getClientSignature());
        this.hashChainRoot = getByteArrayFromString(clientRawCommit.getHashChainRoot());

        this.clientCommitWithoutSignature = getStringWithoutSignature("clientSignature",clientCommit);
        this.paywordWithoutSignature = getStringWithoutSignature("bankSignature", payword);
    }



    public String getClientCommit() {
        return clientCommit;
    }

    public String getPayword() {
        return payword;
    }

    public byte[] getBankPublicKey() {
        return bankPublicKey;
    }

    public byte[] getClientPublicKey() {
        return clientPublicKey;
    }

    public byte[] getBankSignature() {
        return bankSignature;
    }

    public byte[] getClientSignature() {
        return clientSignature;
    }

    public String getClientCommitWithoutSignature() {
        return clientCommitWithoutSignature;
    }

    public String getPaywordWithoutSignature() {
        return paywordWithoutSignature;
    }

    public byte[] getHashChainRoot() {
        return hashChainRoot;
    }

    private String getStringWithoutSignature(String tag, String data){
        return data.substring(0,data.indexOf(tag)-1);
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
}
