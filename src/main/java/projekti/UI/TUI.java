package projekti.UI;

import projekti.UI.commands.Command;
import projekti.UI.commands.CreateRecommendation;
import projekti.UI.commands.DBHelper;
import projekti.UI.commands.RecHelper;
import projekti.db.Dao;
import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Book.Properties;
import projekti.domain.Other;
import projekti.domain.Recommendation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import projekti.domain.Property;
import projekti.domain.RecommendationFactory;

public class TUI {
    private IO io;
    private List<Integer> IDList;

    private RecHelper rh;
    private DBHelper db;
    private Command create;
    
    public TUI(
            Dao<Book, Integer> bookDAO,
            Dao<Blog, Integer> blogDAO,
            Dao<Other, Integer> otherDAO,
            IO io
    ) throws SQLException {
        this.db = new DBHelper(bookDAO, blogDAO, otherDAO);
        this.rh = new RecHelper(this, io, db);
        this.create = new CreateRecommendation(rh, db);
    
        this.io = io;
        updateIDList();
    }

   
    private boolean update(Recommendation recommendation) throws SQLException {
        switch (recommendation.getType()) {
            case "BOOK":
                return db.getBookDAO().update((Book) recommendation);
            case "BLOG":
                return db.getBlogDAO().update((Blog) recommendation);
            case "OTHER":
                return db.getOtherDAO().update((Other) recommendation);
            default:
                throw new IllegalArgumentException("No retrieve definition for recommendation of type: " + recommendation.getType());
        }
    }

    private void delete(Recommendation recommendation) throws SQLException {
        switch (recommendation.getType()) {
            case "BOOK":
                db.getBookDAO().delete(recommendation.getProperty(Properties.ID).orElse(null));
                return;
            case "BLOG":
                db.getBlogDAO().delete(recommendation.getProperty(Properties.ID).orElse(null));
                return;
            case "OTHER":
                db.getOtherDAO().delete(recommendation.getProperty(Properties.ID).orElse(null));
                return;
            default:
                throw new IllegalArgumentException("No retrieve definition for recommendation of type: " + recommendation.getType());
        }
    }

    public void run() throws SQLException {
        io.println("Welcome to the reading recommendation app!");
        io.println("Supported commands:");
        io.println("\tnew \tadd a new recommendation");
        io.println("\tall \tlist all existing recommendations");
        io.println("\tselect \tselect a specific recommendation");
        io.println("\tupdate \tupdate information for an existing recommendation");
        io.println("\tdelete \tremove a recommendation");
        io.println("\tend \tclose the program");

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            io.println("\ncommand: ");
            input = io.getInput();
            try {
                performAction(input);
            } catch (IllegalArgumentException e) {
                io.println(e.getMessage());
            }
        }
    }

    private void performAction(String input) throws SQLException {
        switch (input.toLowerCase()) {
            case "new":
                create.execute();
               
                break;
            case "all":
                listRecommendations();
                break;
            case "end":
                io.println("\nshutting down program");
                break;
            case "select":
                selectRecommendation();
                break;
            case "update":
                updateRecommendation(rh.askForRecommendation());
                updateIDList();
                break;
            case "delete":
                deleteRecommendation(rh.askForRecommendation());
                updateIDList();
                break;
            default:
                io.println("\nnon-supported command");
                break;
        }
    }

    private void selectRecommendation() throws SQLException {
        Recommendation recommendation = rh.askForRecommendation();

        String input = "";

        selectionLoop:
        while (!input.equals("return")) {
            if (recommendation == null) {
                return;
            }
            io.println(getListID(recommendation) + recommendation.toStringWithDescription());
            io.println();

            io.println("\ncommands for the selected recommendation: ");
            io.println("\tedit \tedit the recommendation");
            io.println("\tdelete \tremove the recommendation");
            io.println("\treturn \treturn to the main program");

            input = io.getInput();
            switch (input) {
                case "edit":
                    recommendation = updateRecommendation(recommendation);
                    updateIDList();
                    break;
                case "delete":
                    deleteRecommendation(recommendation);
                    updateIDList();
                    break selectionLoop;
                case "return":
                    io.println();
                    break;
                default:
                    io.println("\nunsupported command");
                    break;
            }
        }
    }

    

    private void listRecommendations() throws SQLException {
        List<Recommendation> recommendations = getAllRecommendations();
        updateIDList(recommendations);
        for (int i = 0; i < recommendations.size(); i++) {
            io.println(i + recommendations.get(i).toString());
        }
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
    private Integer getListID(Recommendation recommendation) {
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

    private void deleteRecommendation(Recommendation recommendation) throws SQLException {
        Integer ID = getListID(recommendation);
        if (confirm("Are you sure you want to delete recommendation " + ID + "?")) {
            delete(recommendation);
            io.println();
            io.println("recommendation successfully deleted");
            updateIDList();
        } else {
            io.println();
            io.println("recommendation deletion canceled");
        }
    }

    private Recommendation updateRecommendation(Recommendation recommendation) throws SQLException {
        Function<Property, String> requestProperty = (Property property) -> {
            io.print("enter new " + property.getName() + " (or empty input to leave it unchanged): ");
            String userInput = io.getInput().trim();
            if (userInput.isEmpty()) {

                return (String) recommendation.getProperty(property).orElse("");
            }

            return userInput;
        };

        String recommendationType = recommendation.getType().toLowerCase();
        Recommendation updatedRecommendation;
        try {
            updatedRecommendation = RecommendationFactory.create()
                    .selectType(recommendationType)
                    .whileMissingProperties(requestProperty)
                    .build();
        } catch (IllegalArgumentException ex) {
            io.println("\n " + recommendationType + " recommendation was not updated.");
            throw ex;
        }

        Integer ID = recommendation.getProperty(Properties.ID).orElse(null);
        updatedRecommendation.addProperty(Properties.ID, ID);
        ID = IDList.indexOf(ID);

        if (confirm("are you sure you want to update recommendation " + ID + "?")) {
            if (update(updatedRecommendation)) {
                io.println();
                io.println("update successful");
                updateIDList();
                return updatedRecommendation;
            } else {
                io.println();
                io.println("update failed");
                return recommendation;
            }
        } else {
            io.println();
            io.println("recommendation update canceled");
            return recommendation;
        }
    }
}
