package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import shared.ConnectionInfo;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        System.out.println("Client started!");

        try (Socket socket = new Socket(ConnectionInfo.ADDRESS, ConnectionInfo.PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            Gson gson = new Gson();
            String request;

            if ("-in".equalsIgnoreCase(args[0])) {
                request = Files.readString(Path.of(ConnectionInfo.CLIENT_PATH + args[1]));
            } else {
                Args parsedArgs = new Args();
                JCommander.newBuilder().addObject(parsedArgs).build().parse(args);
                request = gson.toJson(parsedArgs.getMap());
            }

            outputStream.writeUTF(request);
            System.out.println("Sent: " + request);

            String response = inputStream.readUTF();
            System.out.println("Received: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
