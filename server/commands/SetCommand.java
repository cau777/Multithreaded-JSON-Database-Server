package server.commands;

import server.databases.Database;
import server.responses.Response;

public class SetCommand implements Command {
    private final Object message;

    public SetCommand(Object message) {
        this.message = message;
    }

    @Override
    public Response execute(Database database, String[] cellPath) {
        database.set(cellPath, message);
        return Response.OK();
    }
}
