package projekti.ui.commands;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import projekti.domain.Recommendation;

public class ListAll implements Command {
    private RecHelper rh;
    private DBHelper db;

    public ListAll(RecHelper rh, DBHelper db) {
        this.rh = rh;
        this.db = db;
    }

    @Override
    public void execute() throws SQLException {
        listRecommendations();
    }

    private void listRecommendations() throws SQLException {
        List<Recommendation> recommendations = rh.getAllRecommendations();
        rh.updateIDList(recommendations);
        for (int i = 0; i < recommendations.size(); i++) {
            rh.getIO().println(i + recommendations.get(i).toString());
        }
    }
}