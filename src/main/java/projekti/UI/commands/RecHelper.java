package projekti.UI.commands;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import projekti.UI.*;
import projekti.domain.*;
import projekti.domain.Book.Properties;
import projekti.util.Check;


public class RecHelper {
    private IO io;
    private DBHelper db;
   
    private List<Integer> IDList;

    public RecHelper(IO io, DBHelper db) {
        this.io = io;
        this.db = db;
    }

    public Recommendation askForRecommendation() throws SQLException {
        String recommendationType = askType();
        Integer ID = selectID();

        Recommendation recommendation = db.retrieve(recommendationType, ID);
        Check.notNull(recommendation, () -> new IllegalArgumentException("No recommendation found"));
        return recommendation;
    }
    public String askType() {
        io.println("enter recommendation type");
        io.print("recommendation type (possible choices: book, blog, other): ");
        return io.getInput().toUpperCase();
    }
    private List<Recommendation> getAllRecommendations() throws SQLException {
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.addAll(db.getBookDAO().findAll());
        recommendations.addAll(db.getBlogDAO().findAll());
        recommendations.addAll(db.getOtherDAO().findAll());
        return recommendations;
    }

    /**
     * updates the IDList (list of "fake" IDs displayed to the user, used for
     * selecting, updating or deleting a Recommendation)
     * @param allRecommendations list of all Recommendation, already obtained
     * by calling getAllRecommendations()
     */
    private void updateIDList(List<Recommendation> allRecommendations) {
        this.IDList = allRecommendations.stream()
                .map(r -> r.getProperty(Properties.ID).orElse(-1))
                .collect(Collectors.toList());
    }

    /**
     * fetches all Recommendations from the database and updates the IDList
     * (list of "fake" IDs displayed to the user, used for selecting,
     * updating or deleting a Recommendation)
     * @throws SQLException
     */
    public void updateIDList() throws SQLException {
        updateIDList(getAllRecommendations());
    }

    /**
     * returns the list ID (the one used by the user to refer to a specific
     * recommendation) of the recommendation given as a parameter
     * @param recommendation the given recommendation
     * @return the recommendation's list ID
     */
    public Integer getListID(Recommendation recommendation) {
        Integer ID = recommendation.getProperty(Properties.ID).orElse(null);
        return IDList.indexOf(ID);
    }

    public Integer selectID() { //public until all IDList functionality is a part of RecHelper
        io.println("enter recommendation id (or empty input to go back)");
        io.print("ID: ");
        String id_String = io.getInput();
        Integer ID;
        try {
            ID = Integer.parseInt(id_String);
            if (ID < 0 || ID >= IDList.size()) {
                // just some value that can't be a true ID in the database
                return -1;
            }
            return IDList.get(ID);
        } catch (IllegalArgumentException ex) {
            if (!id_String.isEmpty()) {
                io.println("Not a valid ID. Has to be a number.");
            }
            throw ex;
        }
    }

    private boolean confirm(String message) {
        io.println(message);
        String optionString = "y/n";
        io.println(optionString);
        String val = io.getInput();
        if (val.toLowerCase().equals("y")) {
            return true;
        } else if (val.toLowerCase().equals("n")) {
            return false;
        } else {
            String failMessage = "Invalid input";
            io.println(failMessage);
            return confirm(message);
        }
    }

    public List<Integer> getIDList() {
        return this.IDList;
    }
    
    public IO getIO() {
        return this.io;
    }

    
}