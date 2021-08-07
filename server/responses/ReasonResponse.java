package server.responses;

public class ReasonResponse extends Response{
    private final String reason;

    public ReasonResponse(String response, String reason) {
        super(response);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
