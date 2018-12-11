package projekti.UI.commands;

import java.sql.SQLException;
import java.util.function.Function;

import projekti.UI.*;
import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;
import projekti.domain.Property;
import projekti.domain.Recommendation;
import projekti.domain.RecommendationFactory;

public class CreateRecommendation implements Command {
   
    private RecHelper rh;
    private DBHelper db;

    public CreateRecommendation(RecHelper rh, DBHelper db) {
        this.rh = rh;
        this.db = db;
    }
    @Override
    public void execute() throws SQLException {
        String recommendationType = rh.askType().toLowerCase();
        Function<Property, String> requestProperty = (Property property) -> {
            rh.getIO().print(property.getName() + ": ");
            return rh.getIO().getInput().trim();
        };
        Recommendation recommendation;
        try {
            recommendation = RecommendationFactory.create()
                    .selectType(recommendationType)
                    .whileMissingProperties(requestProperty)
                    .build();
        } catch (IllegalArgumentException ex) {
            rh.getIO().println("\n " + recommendationType + " recommendation was not added.");
            throw ex;
        }
        if (save(recommendation) != null) {
            rh.getIO().println();
            rh.getIO().println("new " + recommendationType + " recommendation added");
            rh.updateIDList();
        } else {
            rh.getIO().println("\nrecommendation not added");
        }
    }
 
 

    private Recommendation save(Recommendation recommendation) throws SQLException {
        switch (recommendation.getType()) {
            case "BOOK":
                return db.getBookDAO().create((Book) recommendation);
            case "BLOG":
                return db.getBlogDAO().create((Blog) recommendation);
            case "OTHER":
                return db.getOtherDAO().create((Other) recommendation);
            default:
                throw new IllegalArgumentException("No save definition for recommendation of type: " + recommendation.getType());
        }
    } 
} 