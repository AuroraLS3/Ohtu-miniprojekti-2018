package projekti.UI.commands;

import java.sql.SQLException;
import java.util.function.Function;

import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;
import projekti.domain.Property;
import projekti.domain.Recommendation;
import projekti.domain.RecommendationFactory;
import projekti.domain.Book.Properties;

public class UpdateRecommendation implements Command {
    private RecHelper rh;
    private DBHelper db;

    public UpdateRecommendation(RecHelper rh, DBHelper db){
        this.rh = rh;
        this.db = db;
    }

    @Override
    public void execute() throws SQLException {
        updateRecommendation(rh.askForRecommendation());
    }

    public Recommendation execute(Recommendation recommendation) throws SQLException {
        return updateRecommendation(recommendation);
    }

    private Recommendation updateRecommendation(Recommendation recommendation) throws SQLException {
        Function<Property, String> requestProperty = (Property property) -> {
            rh.getIO().print("enter new " + property.getName() + " (or empty input to leave it unchanged): ");
            String userInput = rh.getIO().getInput().trim();
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
            rh.getIO().println("\n " + recommendationType + " recommendation was not updated.");
            throw ex;
        }

        Integer ID = recommendation.getProperty(Properties.ID).orElse(null);
        updatedRecommendation.addProperty(Properties.ID, ID);
        ID = rh.getIDList().indexOf(ID);

        if (rh.confirm("are you sure you want to update recommendation " + ID + "?")) {
            if (update(updatedRecommendation)) {
                rh.getIO().println();
                rh.getIO().println("update successful");
                rh.updateIDList();
                return updatedRecommendation;
            } else {
                rh.getIO().println();
                rh.getIO().println("update failed");
                return recommendation;
            }
        } else {
            rh.getIO().println();
            rh.getIO().println("recommendation update canceled");
            return recommendation;
        }
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
}