package server.commands;

import server.databases.*;
import server.responses.Response;

public interface Command {
    Response execute(Database database, String[] cellPath);
}
