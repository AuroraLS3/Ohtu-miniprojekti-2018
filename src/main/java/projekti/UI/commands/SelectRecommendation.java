package projekti.UI.commands;

import java.sql.SQLException;

import projekti.domain.Recommendation;
import projekti.language.LanguageKeys;

public class SelectRecommendation implements Command {
    private RecHelper rh;
    private UpdateRecommendation update;
    private DeleteRecommendation delete;

    public SelectRecommendation(RecHelper rh, DBHelper db) {
        this.rh = rh;
        this.update = new UpdateRecommendation(rh, db);
        this.delete = new DeleteRecommendation(rh, db);
    }
    
    @Override
    public void execute() throws SQLException {
        selectRecommendation();
    }
    private void selectRecommendation() throws SQLException {
        Recommendation recommendation = rh.askForRecommendation();

        String input = "";

        selectionLoop:
        while (!input.equals("return")) {
            if (recommendation == null) {
                return;
            }
            rh.getIO().println(rh.getListID(recommendation) + recommendation.toStringWithDescription());
            rh.getIO().println();
            rh.getIO().print(rh.getLocale().get(LanguageKeys.SELECTEDCOMMANDS));

            input = rh.getIO().getInput();
            switch (input) {
                case "edit":
                    recommendation = update.execute(recommendation);
                    break;
                case "delete":
                    delete.execute(recommendation);
                    break selectionLoop;
                case "return":
                    rh.getIO().println();
                    break;
                default:
                    rh.getIO().println(rh.getLocale().get(LanguageKeys.NONSUP));
                    break;
            }
        }
    }

}   