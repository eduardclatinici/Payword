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
    private Service service = new Service();

    ClientCommit(ClientRawCommit clientRawCommit){
        this.clientCommit = clientRawCommit.getClientCommit();
        this.payword = clientRawCommit.getPayword();
        this.bankPublicKey = service.getByteArrayFromString(clientRawCommit.getBankPublicKey());
        this.clientPublicKey = service.getByteArrayFromString(clientRawCommit.getClientPublicKey());
        this.bankSignature = service.getByteArrayFromString(clientRawCommit.getBankSignature());
        this.clientSignature = service.getByteArrayFromString(clientRawCommit.getClientSignature());
        this.hashChainRoot = service.getByteArrayFromString(clientRawCommit.getHashChainRoot());

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
}
