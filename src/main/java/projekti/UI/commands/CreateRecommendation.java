package projekti.ui.commands;

import java.sql.SQLException;
import java.util.function.Function;

import projekti.domain.Property;
import projekti.domain.Recommendation;
import projekti.domain.RecommendationFactory;
import projekti.language.LanguageKeys;

public class CreateRecommendation implements Command {
   
    private RecHelper rh;
    private DBHelper db;

    public CreateRecommendation(RecHelper rh, DBHelper db) {
        this.rh = rh;
        this.db = db;
    }
    @Override
    public void execute() throws SQLException {
        createRecommendation();
    }
 
    private void createRecommendation() throws SQLException {
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
        	rh.getIO().println("\n" + recommendationType + rh.getLocale().get(LanguageKeys.RECNOTADDED));
            throw ex;
        }
        if (save(recommendation) != null) {
            rh.getIO().println();
            rh.getIO().println("new " + recommendationType + rh.getLocale().get(LanguageKeys.RECADDED));
            rh.updateIDList();
        } else {
        	rh.getIO().println("\n" + recommendationType + rh.getLocale().get(LanguageKeys.RECNOTADDED));
        }
    }

    private Recommendation save(Recommendation recommendation) throws SQLException {
        return db.getDAO().create(recommendation);
    } 
} 