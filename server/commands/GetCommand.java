package server.commands;

import server.databases.Database;
import server.responses.Response;
import server.responses.ValueResponse;

public class GetCommand implements Command {
    @Override
    public Response execute(Database database, String[] cellPath) {
        Object value = database.get(cellPath);
        return new ValueResponse("OK", value);
    }
}
