package projekti.ui.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import projekti.db.*;
import projekti.domain.*;

public class DBHelper {
    private Dao<Book, Integer> bookDAO;
    private Dao<Blog, Integer> blogDAO;
    private Dao<Other, Integer> otherDAO;


    public DBHelper(
        Dao<Book, Integer> bookDAO,
        Dao<Blog, Integer> blogDAO,
        Dao<Other, Integer> otherDAO
    ) {
        this.bookDAO = bookDAO;
        this.blogDAO = blogDAO;
        this.otherDAO = otherDAO;
    }

    public Recommendation retrieve(String recommendationType, Integer ID)throws SQLException {
        switch (recommendationType) {
            case "BOOK":
                return bookDAO.findOne(ID);
            case "BLOG":
                return blogDAO.findOne(ID);
            case "OTHER":
                return otherDAO.findOne(ID);
            default:
                throw new IllegalArgumentException("No retrieve definition for recommendation of type: " + recommendationType);
        }
    }
    
    public Dao<Book, Integer> getBookDAO() {
        return this.bookDAO;
    }

    public Dao<Blog, Integer> getBlogDAO() {
        return this.blogDAO;
    }

    public Dao<Other, Integer> getOtherDAO() {
        return this.otherDAO;
    }
}