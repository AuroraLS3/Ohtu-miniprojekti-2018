package projekti.ui.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import projekti.db.*;
import projekti.domain.*;

public class DBHelper {

    private Dao<Recommendation, Integer> DAO;

    public DBHelper(Dao<Recommendation, Integer> DAO) {
        this.DAO = DAO;
    }

    public Recommendation retrieve(Integer ID) throws SQLException {
        return DAO.findOne(ID);
    }

    public Dao<Recommendation, Integer> getDAO() {
        return this.DAO;
    }

}
