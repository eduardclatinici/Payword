public class ResponseFromVendor {
    private String clientSecret;
    private int steps;

    public ResponseFromVendor(String clientSecret, int steps) {
        this.clientSecret = clientSecret;
        this.steps = steps;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public int getSteps() {
        return steps;
    }
}
