package projekti.UI;


import projekti.UI.commands.Command;
import projekti.UI.commands.CreateRecommendation;
import projekti.UI.commands.DBHelper;
import projekti.UI.commands.DeleteRecommendation;
import projekti.UI.commands.ListAll;
import projekti.UI.commands.RecHelper;
import projekti.UI.commands.SelectLocale;
import projekti.UI.commands.SelectRecommendation;
import projekti.UI.commands.UpdateRecommendation;
import projekti.db.Dao;
import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;
import projekti.language.LanguageKeys;
import projekti.language.Locale;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class TUI {
    private IO io;
    private Locale locale;
    private RecHelper rh;
    private DBHelper db;
    private Map<String, Command> commands;
    private Command selectLocale;

    public TUI(
            Dao<Book, Integer> bookDAO,
            Dao<Blog, Integer> blogDAO,
            Dao<Other, Integer> otherDAO,
            IO io,
            Locale locale
    ) throws SQLException {
        this.db = new DBHelper(bookDAO, blogDAO, otherDAO);
        this.rh = new RecHelper(io, db, locale);
        this.commands = new HashMap<>();
        this.commands.put("all", new ListAll(rh, db));
        this.commands.put("new", new CreateRecommendation(rh, db));
        this.commands.put("delete", new DeleteRecommendation(rh, db));
        this.commands.put("select", new SelectRecommendation(rh, db));
        this.commands.put("update", new UpdateRecommendation(rh, db));
        this.commands.put("end", () -> io.println("\n" + locale.get(LanguageKeys.QUIT)));
        this.selectLocale = new SelectLocale(rh);
        rh.updateIDList();
        this.io = io;
        this.locale = locale;
    }

    public void run() throws SQLException {
    	selectLocale.execute();
    	this.locale = rh.getLocale();
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
        Command command = commands.getOrDefault(input.toLowerCase().trim(),
            () -> io.println("\n" + locale.get(LanguageKeys.NONSUP)));
        command.execute();
    }
}
