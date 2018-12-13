package projekti.main;


import projekti.ui.TUI;
import projekti.ui.TextIO;
import projekti.db.DatabaseManager;
import projekti.language.LanguageFileReader;
import projekti.language.LanguageFiles;
import projekti.language.Locale;

import java.sql.SQLException;
import projekti.db.RecommendationDAO;


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
        RecommendationDAO recommendationDAO = new RecommendationDAO(databaseManager);
        TUI app = new TUI(recommendationDAO, new TextIO(), Locale.createWith(new LanguageFileReader(),
        		LanguageFiles.ENGLISH.getResourcePath()));
        app.run();
    }

}
