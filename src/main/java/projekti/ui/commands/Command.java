package projekti.ui.commands;

import java.sql.SQLException;

public interface Command {
    public void execute() throws SQLException;

}