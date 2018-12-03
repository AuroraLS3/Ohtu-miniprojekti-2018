package projekti.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BookTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void canCreateBookWithAllParameters() {
        new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "1323213", "https://www.hieno-tarina.com", "hyviä ajatuksia");
    }

    @Test
    public void canNotCreateBookWithoutAuthor() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Author should not be null");
        new Book(null, "Esimerkki-ihmisen arkipäivä", "1323213", "");
    }

    @Test
    public void canNotCreateBookWithoutAuthorName() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Author should not be empty");
        new Book("", "Esimerkki-ihmisen arkipäivä", "1323213", "");
    }

    @Test
    public void canNotCreateBookWithoutTitle() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Title should not be null");
        new Book("Matti Meikäläinen", null, "1323213", "");
    }

    @Test
    public void canNotCreateBookWithEmptyTitle() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Title should not be empty");
        new Book("Matti Meikäläinen", "", "1323213", "");
    }

    @Test
    public void canNotCreateBookWithoutISBN() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("ISBN should not be null");
        new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", null, "");
    }

    @Test
    public void canNotCreateBookWithEmptyISBN() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("ISBN should not be empty");
        new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "", "");
    }
    
    @Test
    public void canNotCreateBookWithInvalidURL() {
    	expected.expect(IllegalArgumentException.class);
    	expected.expectMessage("URL should be valid or empty");
    	new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "ISBN:1512sdfaf", "asdfasgdwefsadf1414124-com", "olipas hieno!");
    }

    @Test
    public void propertiesAreReturned() {
        assertFalse(Book.Properties.getAll().isEmpty());
    }

    @Test
    public void propertiesAreReturnedByABook() {
        assertEquals(
                Book.Properties.getAll(),
                new Book("Matti Meikäläinen", "Esimerkki-ihmisen arkipäivä", "1323213", "Description").getProperties()
        );
    }

}