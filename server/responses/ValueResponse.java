package server.responses;

public class ValueResponse extends Response{
    private final Object value;

    public ValueResponse(String response, Object value) {
        super(response);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
