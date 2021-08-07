package server;

import server.databases.Database;
import server.responses.ReasonResponse;
import server.responses.Response;
import server.commands.Command;

public class DatabaseController {
    private final Database database;
    private Command command;

    public DatabaseController(Database database) {
        this.database = database;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Response executeCommand(String[] cellPath) {
        //System.out.println(Arrays.toString(cellPath));
        try {
            return command.execute(database, cellPath);
        } catch (Database.KeyNotFoundException e) {
            return new ReasonResponse("ERROR", "No such key");
        } catch (Exception e) {
            e.printStackTrace();
            return new ReasonResponse("ERROR", e.getClass().getName());
        }
    }
}
