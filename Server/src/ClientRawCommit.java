public class ClientRawCommit {
    private String clientCommit;
    private String payword;
    private String bankPublicKey;
    private String clientPublicKey;
    private String bankSignature;
    private String clientSignature;
    private String hashChainRoot;

    public ClientRawCommit(String clientCommit) {
        this.clientCommit = clientCommit;
        this.payword = extractField("payword");
        this.bankPublicKey = extractField("bankPublicKey",payword);
        this.clientPublicKey = extractField("clientPublicKey", payword);
        this.bankSignature = extractField("bankSignature", payword);
        this.clientSignature = extractField("clientSignature");
        this.hashChainRoot = extractField("hashChainRoot");

    }


    private String extractField(String tag,String from){
        String tag1 = "<"+tag+">";
        String tag2 = "</"+tag+">";
        return from.substring(from.indexOf(tag1)+tag1.length(),from.indexOf(tag2));
    }

    private String extractField(String tag){
        String tag1 = "<"+tag+">";
        String tag2 = "</"+tag+">";
        return this.clientCommit.substring(clientCommit.indexOf(tag1)+tag1.length(),clientCommit.indexOf(tag2));
    }

    public String getClientCommit() {
        return clientCommit;
    }

    public String getPayword() {
        return payword;
    }

    public String getBankPublicKey() {
        return bankPublicKey;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    public String getBankSignature() {
        return bankSignature;
    }

    public String getClientSignature() {
        return clientSignature;
    }

    public String getHashChainRoot() {
        return hashChainRoot;
    }
}
