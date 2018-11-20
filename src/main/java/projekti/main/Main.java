package projekti.main;


import java.sql.SQLException;

import projekti.UI.TUI;
import projekti.db.*;


public class Main {

	/**
     * This is the main method.
     * 
     * @param args these are the CLI args...
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        DatabaseManager dbm = new DatabaseManager("jdbc:h2:./build/vinkit", "sa", "");
        dbm.connect();
        dbm.setupSchema();
       
        BookDAO bDao = new BookDAO(dbm);
        
        bDao.findAll();

        // bDao.create(new Book("matti", "minä", "1234"));
        
        TUI app = new TUI(bDao);
        app.run();
    }

}
