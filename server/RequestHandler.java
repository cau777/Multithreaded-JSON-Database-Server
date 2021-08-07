package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.responses.Response;
import server.commands.Command;
import server.commands.DeleteCommand;
import server.commands.GetCommand;
import server.commands.SetCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final DatabaseController controller;
    private final AtomicBoolean serverStoppingFlag;

    public RequestHandler(Socket socket, DataInputStream inputStream, DataOutputStream outputStream,
                          DatabaseController databaseController, AtomicBoolean serverStoppingFlag) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.controller = databaseController;
        this.serverStoppingFlag = serverStoppingFlag;
    }

    @Override
    public void run() {
        try (socket; inputStream; outputStream) {
            String request = inputStream.readUTF();
            System.out.println("Received: " + request);

            Gson gson = new Gson();
            Map<String, Object> requestMap = gson.fromJson(request, new TypeToken<Map<String, Object>>() {
            }.getType());

            String type = ((String) requestMap.get("type")).toLowerCase();
            Response responseObj;

            if (type.equals("exit")) {
                serverStoppingFlag.set(true);
                responseObj = Response.OK();
            } else {
                Command command;

                switch (type) {
                    case "get":
                        command = new GetCommand();
                        break;
                    case "set":
                        command = new SetCommand(requestMap.get("value"));
                        break;
                    case "delete":
                        command = new DeleteCommand();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + type);
                }

                controller.setCommand(command);
                String[] key;
                Object keyValue = requestMap.get("key");

                if (keyValue instanceof String)
                    key = new String[]{(String) keyValue};
                else
                    key = ((ArrayList<String>) keyValue).toArray(new String[0]);

                responseObj = controller.executeCommand(key);
            }

            String response = gson.toJson(responseObj);
            outputStream.writeUTF(response);
            System.out.println("Sent: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
