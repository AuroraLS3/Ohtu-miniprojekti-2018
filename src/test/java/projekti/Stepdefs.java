package projekti;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import projekti.UI.StubIO;
import projekti.UI.TUI;
import projekti.db.BookDAO;
import projekti.db.DatabaseManager;

public class Stepdefs {

    DatabaseManager dbm;
    BookDAO bDao;
    TUI app;
    StubIO io;
    List<String> inputLines;

    @Before
    public void cleanDB() throws SQLException, IOException {
        if (dbm != null) {
            dbm.disconnect();
        }
        File testDB = new File("./build/test.mv.db");
        Files.deleteIfExists(testDB.toPath());
        this.dbm = new DatabaseManager("jdbc:h2:./build/test", "sa", "");
        bDao = new BookDAO(dbm);
        bDao.findAll();
        inputLines = new ArrayList<>();
    }

    @Given("^command new is selected$")
    public void command_new_is_selected() throws Throwable {
        inputLines.add("new");
    }

    @Given("^some book recommendations have been created$")
    public void some_book_recommendations_have_been_created() throws Throwable {
        inputLines.add("new");
        inputLines.add("Helka");
        inputLines.add("Super Hieno Kirja");
        inputLines.add("987654321");

        inputLines.add("new");
        inputLines.add("Reetta");
        inputLines.add("Great Book");
        inputLines.add("111122222");

        inputLines.add("new");
        inputLines.add("Heli");
        inputLines.add("Kirjojen Kirja");
        inputLines.add("777777333");
    }

    @When("^author \"([^\"]*)\" title \"([^\"]*)\" and ISBN \"([^\"]*)\" are entered$")
    public void author_title_and_ISBN_are_entered(String author, String title, String ISBN) throws Throwable {
        inputLines.add(author);
        inputLines.add(title);
        inputLines.add(ISBN);
    }

    @When("^command all is selected$")
    public void command_all_is_selected() throws Throwable {
        inputLines.add("all");
    }

    @When("^the app processes the input$")
    public void the_app_processes_the_input() throws Throwable {
        inputLines.add("end");

        io = new StubIO(inputLines);
        app = new TUI(bDao, io);
        app.run();
    }

    @Then("^system will respond with \"([^\"]*)\"$")
    public void system_will_respond_with(String expectedOutput) throws Throwable {
        assertTrue(io.getPrints().contains(expectedOutput));
    }

    @Then("^system will show a list of existing book recommendations$")
    public void system_will_show_a_list_of_existing_book_recommendations() throws Throwable {
        assertTrue(io.getPrints().contains("1. Helka: Super Hieno Kirja, ISBN: 987654321"));
        assertTrue(io.getPrints().contains("2. Reetta: Great Book, ISBN: 111122222"));
        assertTrue(io.getPrints().contains("3. Heli: Kirjojen Kirja, ISBN: 777777333"));
    }
//    @Given("^command login is selected$")
//    public void command_login_selected() throws Throwable {
//        inputLines.add("login");
//    }
//
//    @Given("^command new user is selected$")
//    public void command_new_user_is_selected() throws Throwable {
//        inputLines.add("new");
//    }
//
//    @Given("^user \"([^\"]*)\" with password \"([^\"]*)\" is created$")
//    public void user_with_password_is_created(String username, String password) throws Throwable {
//        inputLines.add("new");
//        inputLines.add(username);
//        inputLines.add(password);
//    }

//    @When("^username \"([^\"]*)\" and password \"([^\"]*)\" are entered$")
//    public void a_username_and_password_are_entered(String username, String password) throws Throwable {
//       inputLines.add(username);
//       inputLines.add(password);
//
//       io = new StubIO(inputLines);
//       app = new App(io, auth);
//       app.run();
//    }

//    @Then("^system will respond with \"([^\"]*)\"$")
//    public void system_will_respond_with(String expectedOutput) throws Throwable {
//        assertTrue(io.getPrints().contains(expectedOutput));
//    }
}
