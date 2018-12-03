package projekti.main;


import projekti.UI.TUI;
import projekti.UI.TextIO;
import projekti.db.BlogDAO;
import projekti.db.BookDAO;
import projekti.db.DatabaseManager;

import java.sql.SQLException;


public class Main {

    /**
     * This is the main method.
     *
     * @param args these are the CLI args...
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        DatabaseManager databaseManager = new DatabaseManager("jdbc:h2:./build/vinkit", "sa", "");
        databaseManager.connect();
        databaseManager.setupSchema();
        BookDAO bookDAO = new BookDAO(databaseManager);
        BlogDAO blogDAO = new BlogDAO(databaseManager);
        TUI app = new TUI(bookDAO, blogDAO, new TextIO());
        app.run();
    }

}
