package projekti.UI.commands;

import java.sql.SQLException;

import projekti.domain.Recommendation;
import projekti.domain.Blog.Properties;
import projekti.language.LanguageKeys;

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
        if (rh.confirm(rh.getLocale().get(LanguageKeys.DELETECONFIRM) + ID + "?")) {
            delete(recommendation);
            rh.getIO().println();
            rh.getIO().println(rh.getLocale().get(LanguageKeys.DELSUCCESS));
            rh.updateIDList();
        } else {
            rh.getIO().println();
            rh.getIO().println(rh.getLocale().get(LanguageKeys.DELCANCEL));
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
                throw new IllegalArgumentException(rh.getLocale().get(LanguageKeys.NORETDEF) + recommendation.getType());
        }
    }

   
}