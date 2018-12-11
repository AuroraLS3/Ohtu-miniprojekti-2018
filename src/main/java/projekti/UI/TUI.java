package projekti.UI;

import projekti.UI.commands.*;
import projekti.db.Dao;
import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;

import java.sql.SQLException;

public class TUI {
    private IO io;
   
    private RecHelper rh;
    private DBHelper db;
    private Command create;
    private Command update;
    private Command delete;
    private Command select;
    private Command all;

    public TUI(
            Dao<Book, Integer> bookDAO,
            Dao<Blog, Integer> blogDAO,
            Dao<Other, Integer> otherDAO,
            IO io
    ) throws SQLException {
        this.db = new DBHelper(bookDAO, blogDAO, otherDAO);
        this.rh = new RecHelper(io, db);

        this.create = new CreateRecommendation(rh, db);
        this.delete = new DeleteRecommendation(rh, db);
        this.update = new UpdateRecommendation(rh, db);
        this.select = new SelectRecommendation(rh, db);
        this.all = new ListAll(rh, db);

        this.io = io;
        rh.updateIDList();
    }

    public void run() throws SQLException {
        io.println("Welcome to the reading recommendation app!");
        io.println("Supported commands:");
        io.println("\tnew \tadd a new recommendation");
        io.println("\tall \tlist all existing recommendations");
        io.println("\tselect \tselect a specific recommendation");
        io.println("\tupdate \tupdate information for an existing recommendation");
        io.println("\tdelete \tremove a recommendation");
        io.println("\tend \tclose the program");

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            io.println("\ncommand: ");
            input = io.getInput();
            try {
                performAction(input);
            } catch (IllegalArgumentException e) {
                io.println(e.getMessage());
            }
        }
    }

    private void performAction(String input) throws SQLException {
        switch (input.toLowerCase()) {
            case "new":
                create.execute();
                break;
            case "all":
                all.execute();
                break;
            case "end":
                io.println("\nshutting down program");
                break;
            case "select":
                select.execute();
                break;
            case "update":
                update.execute();              
                break;
            case "delete":
                delete.execute();
                break;
            default:
                io.println("\nnon-supported command");
                break;
        }
    }
}
