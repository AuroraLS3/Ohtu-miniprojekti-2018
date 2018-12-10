package projekti.UI.commands;



import java.sql.SQLException;

import projekti.UI.*;
import projekti.domain.Recommendation;
import projekti.util.Check;


public class RecHelper {
    private IO io;
    private DBHelper db;
    public TUI tui; //only used for updateIDList which will be made a part of this class later

    public RecHelper(TUI tui, IO io, DBHelper db) {
        this.tui = tui;
        this.io = io;
        this.db = db;
    }

    public Recommendation askForRecommendation() throws SQLException {
        String recommendationType = askType();
        Integer ID = tui.selectID();

        Recommendation recommendation = db.retrieve(recommendationType, ID);
        Check.notNull(recommendation, () -> new IllegalArgumentException("No recommendation found"));
        return recommendation;
    }
    public String askType() {
        io.println("enter recommendation type");
        io.print("recommendation type (possible choices: book, blog, other): ");
        return io.getInput().toUpperCase();
    }

    public IO getIO() {
        return this.io;
    }

    
}