package projekti.UI.commands;

import java.sql.SQLException;

import projekti.domain.Recommendation;
import projekti.domain.Blog.Properties;

public class DeleteRecommendation implements Command {
    private RecHelper rh;
    private DBHelper db;

    public DeleteRecommendation(RecHelper rh, DBHelper db) {
        this.rh = rh;
        this.db = db;
    }
    @Override
    public void execute() throws SQLException {
        deleteRecommendation(rh.askForRecommendation());
    }

   
    public void execute(Recommendation recommendation) throws SQLException {
        deleteRecommendation(recommendation);
    }

    public void deleteRecommendation(Recommendation recommendation) throws SQLException {
        Integer ID = rh.getListID(recommendation);
        if (rh.confirm("Are you sure you want to delete recommendation " + ID + "?")) {
            delete(recommendation);
            rh.getIO().println();
            rh.getIO().println("recommendation successfully deleted");
            rh.updateIDList();
        } else {
            rh.getIO().println();
            rh.getIO().println("recommendation deletion canceled");
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

   
}