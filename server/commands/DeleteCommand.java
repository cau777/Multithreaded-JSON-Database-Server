package server.commands;

import server.databases.Database;
import server.responses.Response;

public class DeleteCommand implements Command {
    @Override
    public Response execute(Database database, String[] cellPath) {
        database.delete(cellPath);
        return Response.OK();
    }
}
