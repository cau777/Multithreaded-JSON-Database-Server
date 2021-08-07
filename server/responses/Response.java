package server.responses;

public class Response {
    private final String response;

    public Response(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public static Response OK() {
        return new Response("OK");
    }
}
