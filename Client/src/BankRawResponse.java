public class BankRawResponse {

    private String bankData;
    private String bankIdentity;
    private String clientIdentity;
    private String clientIP;
    private String bankPublicKey;
    private String clientPublicKey;
    private String expiryDate;
    private String additionalInfo;
    private String bankSignature;


    public BankRawResponse(String bankData) {
        this.bankData = bankData;
        extractDataFromResponse(bankData);
    }

    private void extractDataFromResponse(String bankData){
        this.bankIdentity = extractField("bankIdentity");
        this.clientIdentity = extractField("clientIdentity");
        this.clientIP = extractField("clientIP");
        this.bankPublicKey = extractField("bankPublicKey");
        this.clientPublicKey = extractField("clientPublicKey");
        this.expiryDate = extractField("expiryDate");
        this.additionalInfo = extractField("additionalInfo");
        this.bankSignature = extractField("bankSignature");
    }

    private String extractField(String tag){
        String tag1 = "<"+tag+">";
        String tag2 = "</"+tag+">";
        return bankData.substring(this.bankData.indexOf(tag1)+tag1.length(),this.bankData.indexOf(tag2));
    }


    public String getBankDataWithoutSignature(){
        return this.bankData.substring(0,this.bankData.indexOf("<bankSignature>"));
    }

    public String getBankData() {
        return bankData;
    }

    public String getBankPublicKey() {
        return bankPublicKey;
    }

    public String getBankSignature() {
        return bankSignature;
    }

    public String getBankIdentity() {
        return bankIdentity;
    }

    public String getClientIdentity() {
        return clientIdentity;
    }

    public String getClientIP() {
        return clientIP;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
