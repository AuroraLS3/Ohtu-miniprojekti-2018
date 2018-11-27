package projekti;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import projekti.UI.StubIO;
import projekti.UI.TUI;
import projekti.db.BookDAO;
import projekti.db.Dao;
import projekti.db.DatabaseManager;
import projekti.domain.Book;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Stepdefs {

    DatabaseManager dbm;
    Dao<Book, Integer> bDao;
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
    public void command_new_is_selected() {
        inputLines.add("new");
    }

    @Given("^some book recommendations have been created$")
    public void some_book_recommendations_have_been_created() {
        inputLines.add("new");
        inputLines.add("Helka");
        inputLines.add("Super Hieno Kirja");
        inputLines.add("987654321");
        inputLines.add("hyva kirja");

        inputLines.add("new");
        inputLines.add("Reetta");
        inputLines.add("Great Book");
        inputLines.add("111122222");
        inputLines.add("hyva kirja");

        inputLines.add("new");
        inputLines.add("Heli");
        inputLines.add("Kirjojen Kirja");
        inputLines.add("777777333");
        inputLines.add("hyva kirja");
    }

    @Given("^command delete is selected$")
    public void command_delete_is_selected() {
        inputLines.add("delete");
    }

    @Given("^command update is selected$")
    public void command_update_is_selected() {
        inputLines.add("update");
    }

    @Given("^command select is selected$")
    public void command_select_is_selected() throws Throwable {
        inputLines.add("select");
    }

    @When("^author \"([^\"]*)\" title \"([^\"]*)\" and ISBN \"([^\"]*)\" are entered$")
    public void author_title_and_ISBN_are_entered(String author, String title, String ISBN) {
        inputLines.add(author);
        inputLines.add(title);
        inputLines.add(ISBN);
        inputLines.add("");
    }

    @When("^command all is selected$")
    public void command_all_is_selected() {
        inputLines.add("all");
    }

    @When("^the app processes the input$")
    public void the_app_processes_the_input() throws Throwable {
        inputLines.add("end");

        io = new StubIO(inputLines);
        app = new TUI(bDao, io);
        app.run();
    }
    @When("^command return is entered$")
    public void command_return_is_entered() throws Throwable {
        inputLines.add("return");
    }
    @When("^existing recommendation id \"([^\"]*)\" is entered$")
    public void existing_recommendation_id_is_entered(String id) {
        inputLines.add(id);
    }

    @When("^affirmative response is given when asked for confirmation$")
    public void affirmative_response_is_given_when_asked_for_confirmation() {
        inputLines.add("y");
    }

    @When("^negative response is given when asked for confirmation$")
    public void negative_response_is_given_when_asked_for_confirmation() {
        inputLines.add("n");
    }

    @When("^nonexisting recommendation id \"([^\"]*)\" is entered$")
    public void nonexisting_recommendation_id_is_entered(String id) {
        inputLines.add(id);
    }

    @When("^invalid recommendation id \"([^\"]*)\" is entered$")
    public void invalid_recommendation_id_is_entered(String invalidId) {
        inputLines.add(invalidId);
    }

    @When("^new author \"([^\"]*)\" is entered$")
    public void new_author_is_entered(String author) {
        inputLines.add(author);
    }

    @When("^new title \"([^\"]*)\" is entered$")
    public void new_title_is_entered(String title) {
        inputLines.add(title);
    }

    @When("^new ISBN \"([^\"]*)\" is entered$")
    public void new_ISBN_is_entered(String isbn) {
        inputLines.add(isbn);
    }

    @When("^new description \"([^\"]*)\" is entered$")
    public void new_description_is_entered(String description) {
        inputLines.add(description);
    }

    @When("^no description is entered$")
    public void no_description_is_entered() {
        inputLines.add("");
    }

    @Then("^system will respond with \"([^\"]*)\"$")
    public void system_will_respond_with(String expectedOutput) {
        assertTrue("Did not respond correctly expected to contain '" + expectedOutput + "', output: " + io.getPrints().toString(), io.getPrints().contains(expectedOutput));
    }

    @Then("^system will show a list of existing book recommendations$")
    public void system_will_show_a_list_of_existing_book_recommendations() {
        assertTrue(io.getPrints().contains("1. Helka: Super Hieno Kirja, ISBN: 987654321"));
        assertTrue(io.getPrints().contains("2. Reetta: Great Book, ISBN: 111122222"));
        assertTrue(io.getPrints().contains("3. Heli: Kirjojen Kirja, ISBN: 777777333"));
    }

    @Then("^the list of recommendations will include \"([^\"]*)\"$")
    public void the_list_of_recommendations_will_include(String expectedOutput) {
        assertTrue(io.getPrints().contains(expectedOutput));
    }
    
    @Then("^the list of recommendations will not include \"([^\"]*)\"$")
    public void the_list_of_recommendations_will_not_include(String unexpectedOutput) {
        assertFalse(io.getPrints().contains(unexpectedOutput));
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
