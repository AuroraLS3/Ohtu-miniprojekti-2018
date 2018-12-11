package projekti.UI;

import projekti.UI.commands.Command;
import projekti.UI.commands.CreateRecommendation;
import projekti.UI.commands.DBHelper;
import projekti.UI.commands.DeleteRecommendation;
import projekti.UI.commands.RecHelper;
import projekti.UI.commands.SelectLocale;
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
import projekti.language.LanguageKeys;
import projekti.language.Locale;

public class TUI {
    private IO io;


    private RecHelper rh;
    private DBHelper db;
    private Command create;
    private Locale locale;

    private DeleteRecommendation delete;

    public TUI(
            Dao<Book, Integer> bookDAO,
            Dao<Blog, Integer> blogDAO,
            Dao<Other, Integer> otherDAO,
            IO io,
            Locale locale
    ) throws SQLException {
        this.db = new DBHelper(bookDAO, blogDAO, otherDAO);
        this.rh = new RecHelper(io, db);
        this.create = new CreateRecommendation(rh, db);
        this.locale = locale;
        this.delete = new DeleteRecommendation(rh, db);
        this.io = io;
        this.locale = locale;
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
        io.println(locale.get(LanguageKeys.GREET));
        io.print(locale.get(LanguageKeys.MAINCOMMANDS));

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            io.println("\n: " + locale.get(LanguageKeys.COMMAND));
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
                io.println("\n" + locale.get(LanguageKeys.QUIT));
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
                io.println("\n" + locale.get(LanguageKeys.NONSUP));
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

            io.println(locale.get(LanguageKeys.SELECTEDCOMMANDS));

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
                    io.println("\n" + locale.get(LanguageKeys.NONSUP));
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
        io.println(locale.get(LanguageKeys.SELECTIDQUERY));
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
                io.println(locale.get(LanguageKeys.NONVALIDID));
            }
            throw ex;
        }
    }

    private boolean confirm(String message) {
        io.println(message);
        String optionString = "y/n";
        io.println(optionString);
        String val = io.getInput();
        if (val.equalsIgnoreCase(("y"))) {
            return true;
        } else if (val.equalsIgnoreCase("n")) {
            return false;
        } else {
            String failMessage = locale.get(LanguageKeys.CONFIRMFAIL);
            io.println(failMessage);
            return confirm(message);
        }
    }

    private void deleteRecommendation(Recommendation recommendation) throws SQLException {
        Integer ID = getListID(recommendation);
        if (confirm(locale.get(LanguageKeys.DELETECONFIRM) + ID + "?")) {
            delete(recommendation);
            io.println();
            io.println(locale.get(LanguageKeys.DELSUCCESS));
            updateIDList();
        } else {
            io.println();
            io.println(locale.get(LanguageKeys.DELCANCEL));
        }
    }

    private Recommendation updateRecommendation(Recommendation recommendation) throws SQLException {
        Function<Property, String> requestProperty = (Property property) -> {
            io.print(locale.get(LanguageKeys.ENTERNEW) + property.getName() + locale.get(LanguageKeys.ORLEAVE));
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
            io.println("\n " + recommendationType + locale.get(LanguageKeys.NOTUP));
            throw ex;
        }

        Integer ID = recommendation.getProperty(Properties.ID).orElse(null);
        updatedRecommendation.addProperty(Properties.ID, ID);
        ID = rh.getIDList().indexOf(ID);
        if (rh.confirm(locale.get(LanguageKeys.UPDATECONFIR) + ID + "?")) {
            if (update(updatedRecommendation)) {
                io.println();
                io.println(locale.get(LanguageKeys.UPDATESUCCES));
                rh.updateIDList();
                return updatedRecommendation;
            } else {
                io.println();
                io.println(locale.get(LanguageKeys.UPDATEFAIL));
                return recommendation;
            }
        } else {
            io.println();
            io.println(locale.get(LanguageKeys.UPDATECANCEL));
            return recommendation;
        }
    }

    public Locale getLocale() {
    	return this.locale;
    }

    public void setLocale(Locale locale) {
    	this.locale = locale;
    }
}
