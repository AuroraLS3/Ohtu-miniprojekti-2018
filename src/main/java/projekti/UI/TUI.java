package projekti.UI;


import projekti.UI.commands.Command;
import projekti.UI.commands.CreateRecommendation;
import projekti.UI.commands.DBHelper;
import projekti.UI.commands.DeleteRecommendation;
import projekti.UI.commands.ListAll;
import projekti.UI.commands.RecHelper;
import projekti.UI.commands.SelectRecommendation;
import projekti.UI.commands.UpdateRecommendation;
import projekti.db.Dao;
import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;
import projekti.language.LanguageKeys;
import projekti.language.Locale;

import java.sql.SQLException;


public class TUI {
    private IO io;
    private Locale locale;
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
            IO io,
            Locale locale
    ) throws SQLException {
        this.db = new DBHelper(bookDAO, blogDAO, otherDAO);
        this.rh = new RecHelper(io, db, locale);
        this.create = new CreateRecommendation(rh, db);
        this.locale = locale;
        this.delete = new DeleteRecommendation(rh, db);
        this.update = new UpdateRecommendation(rh, db);
        this.select = new SelectRecommendation(rh, db);
        this.all = new ListAll(rh, db);

        this.io = io;
        this.locale = locale;
        rh.updateIDList();
    }

    public void run() throws SQLException {
        io.println(locale.get(LanguageKeys.GREET));
        io.print(locale.get(LanguageKeys.MAINCOMMANDS));

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            io.println("\n: " + locale.get(LanguageKeys.COMMAND));
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
                io.println("\n" + locale.get(LanguageKeys.QUIT));
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
                io.println("\n" + locale.get(LanguageKeys.NONSUP));
                break;
        }
    }
}
