package projekti;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import projekti.ui.StubIO;
import projekti.ui.TUI;
import projekti.db.*;
import projekti.domain.Blog;
import projekti.domain.Book;
import projekti.domain.Other;
import projekti.language.LanguageFileReader;
import projekti.language.Locale;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Stepdefs {

    private DatabaseManager databaseManager;
    private Dao<Book, Integer> bookDAO;
    private Dao<Blog, Integer> blogDAO;
    private Dao<Other, Integer> otherDAO;
    private TUI app;
    private StubIO io;
    private Locale locale;
    private List<String> inputLines;

    @Before
    public void cleanDB() throws SQLException, IOException {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        File testDB = new File("./build/test.mv.db");
        Files.deleteIfExists(testDB.toPath());
        this.databaseManager = new DatabaseManager("jdbc:h2:./build/test", "sa", "");
        bookDAO = new BookDAO(databaseManager);
        blogDAO = new BlogDAO(databaseManager);
        otherDAO = new OtherDAO(databaseManager);

        inputLines = new ArrayList<>();
        inputLines.add("English");
        locale = Locale.createWith(new LanguageFileReader(), "json/english.lang.json");
    }

    @Given("^language has been selected$")
    public void languageIsSelected() {
    	inputLines.add("English");
    }

    @Given("^command new is selected$")
    public void commandNewIsSelected() {
        inputLines.add("new");
    }

    @Given("^some book recommendations have been created$")
    public void someBookRecommendationsHaveBeenCreated() {
        inputLines.add("new");
        inputLines.add("book");
        inputLines.add("Helka");
        inputLines.add("Super Hieno Kirja");
        inputLines.add("987654321");
        inputLines.add("");
        inputLines.add("hyva kirja");

        inputLines.add("new");
        inputLines.add("book");
        inputLines.add("Reetta");
        inputLines.add("Great Book");
        inputLines.add("111122222");
        inputLines.add("");
        inputLines.add("hyva kirja");

        inputLines.add("new");
        inputLines.add("book");
        inputLines.add("Heli");
        inputLines.add("Kirjojen Kirja");
        inputLines.add("777777333");
        inputLines.add("");
        inputLines.add("hyva kirja");
    }

    @Given("^some blog recommendations have been created$")
    public void someBlogRecommendationsHaveBeenCreated() {
        inputLines.add("new");
        inputLines.add("blog");
        inputLines.add("Super Hieno Blogi");
        inputLines.add("http://www.faketestfaketestfaketesturl.com/1/");
        inputLines.add("hyva blogi");

        inputLines.add("new");
        inputLines.add("blog");
        inputLines.add("Great Blog");
        inputLines.add("http://www.faketestfaketestfaketesturl.com/2/");
        inputLines.add("hyva blogi");


        inputLines.add("new");
        inputLines.add("blog");
        inputLines.add("Blogien Blogi");
        inputLines.add("http://www.faketestfaketestfaketesturl.com/3/");
        inputLines.add("hyva blogi");
    }

    @Given("^some other recommendations have been created$")
    public void someOtherRecommendationsHaveBeenCreated() {
        inputLines.add("new");
        inputLines.add("other");
        inputLines.add("Super Hieno Sivusto");
        inputLines.add("http://www.faketestfaketestfaketesturl.com/1/");
        inputLines.add("hyva sivusto");

        inputLines.add("new");
        inputLines.add("other");
        inputLines.add("Great Website");
        inputLines.add("http://www.faketestfaketestfaketesturl.com/2/");
        inputLines.add("hyva sivusto");

        inputLines.add("new");
        inputLines.add("other");
        inputLines.add("Sivustojen Sivusto");
        inputLines.add("http://www.faketestfaketestfaketesturl.com/3/");
        inputLines.add("hyva sivusto");
    }
    @Given("^command delete is selected$")
    public void commandDeleteIsSelected() {
        inputLines.add("delete");
    }

    @Given("^command update is selected$")
    public void commandUpdateIsSelected() {
        inputLines.add("update");
    }

    @Given("^command select is selected$")
    public void commandSelectIsSelected() {
        inputLines.add("select");
    }

    @Given("^recommendation type \"([^\"]*)\" is selected$")
    public void recommendationTypeIsSelected(String recommendationType) {
        inputLines.add(recommendationType);
    }

    @When("^author \"([^\"]*)\" title \"([^\"]*)\" and ISBN \"([^\"]*)\" are entered$")
    public void authorTitleAndISBNAreEntered(String author, String title, String ISBN) {
        inputLines.add(author);
        inputLines.add(title);
        inputLines.add(ISBN);
        inputLines.add("");
    }

    @When("^command all is selected$")
    public void commandAllIsSelected() {
        inputLines.add("all");
    }

    @When("^description \"([^\"]*)\" is entered in edit mode$")
    public void descriptionIsEnteredToEditMode(String description) {
        inputLines.add("");
        inputLines.add("");
        inputLines.add("");
        inputLines.add(description);
    }

    @When("^command edit is selected$")
    public void commandEditIsSelected() {
        inputLines.add("edit");
    }

    @When("^the app processes the input$")
    public void theAppProcessesTheInput() throws Throwable {
        inputLines.add("end");

        io = new StubIO(inputLines);
        app = new TUI(bookDAO, blogDAO, otherDAO, io, locale);
        app.run();
    }

    @When("^command return is entered$")
    public void commandReturnIsEntered() {
        inputLines.add("return");
    }

    @When("^command edit is entered$")
    public void commandEditIsEntered() {
        inputLines.add("edit");
    }

    @When("^existing recommendation id \"([^\"]*)\" is entered$")
    public void existingRecommendationIdIsEntered(String id) {
        inputLines.add(id);
    }

    @When("^affirmative response is given when asked for confirmation$")
    public void affirmativeResponseIsGivenWhenAskedForConfirmation() {
        inputLines.add("y");
    }

    @When("^negative response is given when asked for confirmation$")
    public void negativeResponseIsGivenWhenAskedForConfirmation() {
        inputLines.add("n");
    }
    @When("^other response is given when asked for confirmation$")
    public void invalidResponseIsGivenWhenAskedForConfirmation() {
        inputLines.add("maybe");
    }

    @When("^nonexisting recommendation id \"([^\"]*)\" is entered$")
    public void nonexistingRecommendationIdIsEntered(String id) {
        inputLines.add(id);
    }

    @When("^invalid recommendation id \"([^\"]*)\" is entered$")
    public void invalidRecommendationIdIsEntered(String invalidId) {
        inputLines.add(invalidId);
    }

    @When("^new author \"([^\"]*)\" is entered$")
    public void newAuthorIsEntered(String author) {
        inputLines.add(author);
    }

    @When("^new title \"([^\"]*)\" is entered$")
    public void newTitleIsEntered(String title) {
        inputLines.add(title);
    }

    @When("^new ISBN \"([^\"]*)\" is entered$")
    public void newISBNIsEntered(String isbn) {
        inputLines.add(isbn);
    }

    @When("^new description \"([^\"]*)\" is entered$")
    public void newDescriptionIsEntered(String description) {
        inputLines.add(description);
    }

    @When("^no description is entered$")
    public void noDescriptionIsEntered() {
        inputLines.add("");
    }

    @When("^new url \"([^\"]*)\" is entered$")
    public void newUrlIsEntered(String url) {
        inputLines.add(url);
    }

    @Then("^system will respond with \"([^\"]*)\"$")
    public void systemWillRespondWith(String expectedOutput) {
        assertTrue("Did not respond correctly expected to contain '" + expectedOutput + "', output: " + io.getPrints().toString(), io.getPrints().contains(expectedOutput));
    }

    @Then("^system will show a list of existing book recommendations$")
    public void systemWillShowAListOfExistingBookRecommendations() {
        assertTrue(io.getPrints().contains("0. Helka: Super Hieno Kirja, ISBN: 987654321, URL: -"));
        assertTrue(io.getPrints().contains("1. Reetta: Great Book, ISBN: 111122222, URL: -"));
        assertTrue(io.getPrints().contains("2. Heli: Kirjojen Kirja, ISBN: 777777333, URL: -"));
    }

    @Then("^the list of recommendations will include \"([^\"]*)\"$")
    public void theListOfRecommendationsWillInclude(String expectedOutput) {
        assertTrue(io.getPrints().contains(expectedOutput));
    }

    @Then("^the list of recommendations will not include \"([^\"]*)\"$")
    public void theListOfRecommendationsWillNotInclude(String unexpectedOutput) {
        assertFalse(io.getPrints().contains(unexpectedOutput));
    }
}
