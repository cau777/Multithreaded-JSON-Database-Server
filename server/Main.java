package server;

import server.databases.JsonDatabase;
import shared.ConnectionInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server started!");
        DatabaseController controller = new DatabaseController(new JsonDatabase());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        AtomicBoolean stopping = new AtomicBoolean(false);

        try (ServerSocket serverSocket = new ServerSocket(ConnectionInfo.PORT)) {
            serverSocket.setSoTimeout(5);

            while (!stopping.get()) {
                Socket socket;

                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                }

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                executor.submit(new RequestHandler(socket, inputStream, outputStream, controller, stopping));
            }

            executor.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
