package projekti.UI.commands;

import java.sql.SQLException;
import java.util.function.Function;

import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;
import projekti.domain.Property;
import projekti.domain.Recommendation;
import projekti.domain.RecommendationFactory;
import projekti.language.LanguageKeys;
import projekti.domain.Book.Properties;

public class UpdateRecommendation implements Command {
    private RecHelper rh;
    private DBHelper db;

    public UpdateRecommendation(RecHelper rh, DBHelper db) {
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
            rh.getIO().print(rh.getLocale().get(LanguageKeys.ENTERNEW) + property.getName() + rh.getLocale().get(LanguageKeys.ORLEAVE));
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
            rh.getIO().println("\n " + recommendationType + rh.getLocale().get(LanguageKeys.UPDATEFAIL));
            throw ex;
        }

        Integer ID = recommendation.getProperty(Properties.ID).orElse(null);
        updatedRecommendation.addProperty(Properties.ID, ID);
        ID = rh.getIDList().indexOf(ID);

        if (rh.confirm(rh.getLocale().get(LanguageKeys.UPDATECONFIR) + ID + "?")) {
            if (update(updatedRecommendation)) {
                rh.getIO().println();
                rh.getIO().println(rh.getLocale().get(LanguageKeys.UPDATESUCCES));
                rh.updateIDList();
                return updatedRecommendation;
            } else {
                rh.getIO().println();
                rh.getIO().println(rh.getLocale().get(LanguageKeys.UPDATEFAIL));
                return recommendation;
            }
        } else {
            rh.getIO().println();
            rh.getIO().println(rh.getLocale().get(LanguageKeys.UPDATECANCEL));
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