package projekti.UI;

import projekti.UI.commands.Command;
import projekti.UI.commands.CreateRecommendation;
import projekti.UI.commands.DBHelper;
import projekti.UI.commands.DeleteRecommendation;
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
import projekti.domain.Property;
import projekti.domain.RecommendationFactory;

public class TUI {
    private IO io;
   

    private RecHelper rh;
    private DBHelper db;
    private Command create;
    private DeleteRecommendation delete;

    public TUI(
            Dao<Book, Integer> bookDAO,
            Dao<Blog, Integer> blogDAO,
            Dao<Other, Integer> otherDAO,
            IO io
    ) throws SQLException {
        this.db = new DBHelper(bookDAO, blogDAO, otherDAO);
        this.rh = new RecHelper(io, db);

        this.create = new CreateRecommendation(rh, db);
        this.delete = new DeleteRecommendation(rh, db);
        this.io = io;
        rh.updateIDList();
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
                
                break;
            case "delete":
                delete.execute();
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
            io.println(rh.getListID(recommendation) + recommendation.toStringWithDescription());
            io.println();

            io.println("\ncommands for the selected recommendation: ");
            io.println("\tedit \tedit the recommendation");
            io.println("\tdelete \tremove the recommendation");
            io.println("\treturn \treturn to the main program");

            input = io.getInput();
            switch (input) {
                case "edit":
                    recommendation = updateRecommendation(recommendation);
                    break;
                case "delete":
                    delete.execute(recommendation);
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
        rh.updateIDList(); 
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
        ID = rh.getIDList().indexOf(ID);

        if (rh.confirm("are you sure you want to update recommendation " + ID + "?")) {
            if (update(updatedRecommendation)) {
                io.println();
                io.println("update successful");
                rh.updateIDList();
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
