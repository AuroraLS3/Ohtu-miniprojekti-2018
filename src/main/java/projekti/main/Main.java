package projekti.main;


import projekti.ui.TUI;
import projekti.ui.TextIO;
import projekti.db.BlogDAO;
import projekti.db.BookDAO;
import projekti.db.DatabaseManager;
import projekti.db.OtherDAO;
import projekti.language.LanguageFileReader;
import projekti.language.LanguageFiles;
import projekti.language.Locale;

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
        OtherDAO otherDAO = new OtherDAO(databaseManager);
        TUI app = new TUI(bookDAO, blogDAO, otherDAO, new TextIO(), Locale.createWith(new LanguageFileReader(),
        		LanguageFiles.ENGLISH.getResourcePath()));
        app.run();
    }

}
