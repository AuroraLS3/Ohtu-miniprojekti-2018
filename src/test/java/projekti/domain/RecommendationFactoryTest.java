package projekti.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;

/**
 * //TODO Class Javadoc Comment
 *
 * @author Rsl1122
 */
public class RecommendationFactoryTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private RecommendationFactory factory;

    @Before
    public void setUp() {
        factory = RecommendationFactory.create();
    }

    private void recommendationHasAllProperties(Recommendation recommendation) {
        for (Property property : recommendation.getProperties()) {
            assertTrue("Did not have property: " + property.getName(), recommendation.getProperty(property).isPresent());
        }
    }

    @Test
    public void bookCanBeCreatedWithValidParameters() {
        Iterator<String> input = Arrays.asList(
                "Matti Meikäläinen",
                "Esimerkki-ihmisen arkipäivä",
                "700-4343",
                "http://www.suomalainen.fi",
                "Hyvä kuvaus"
        ).iterator();

        Recommendation book = factory.selectType("BOOK")
                .whileMissingProperties(property -> input.next())
                .build();
        book.addProperty(CommonProperties.ID, 0);

        recommendationHasAllProperties(book);
    }

    @Test
    public void blogCanBeCreatedWithValidParameters() {
        Iterator<String> input = Arrays.asList("Medium", "http://www.medium.com", "Kuvaus").iterator();

        Recommendation blog = factory.selectType("BLOG")
                .whileMissingProperties(property -> input.next())
                .build();
        blog.addProperty(CommonProperties.ID, 0);

        recommendationHasAllProperties(blog);
    }

    @Test
    public void otherCanBeCreatedWithValidParameters() {
        Iterator<String> input = Arrays.asList("Google", "http://www.google.com", "Kuvaus").iterator();

        Recommendation other = factory.selectType("OTHER")
                .whileMissingProperties(property -> input.next())
                .build();
        other.addProperty(CommonProperties.ID, 0);

        recommendationHasAllProperties(other);
    }

    @Test
    public void invalidInputRejectedAfterThreeAttempts() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Invalid value given as 'AUTHOR' 3 times in a row.");
        Iterator<String> input = Arrays.asList("", "", "").iterator();

        factory.selectType("BOOK")
                // Will throw exception on 3rd attempt
                .whileMissingProperties(property -> input.next());
    }

    @Test
    public void invalidTypeRejected() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Invalid recommendation type given: 'INVALID'");

        factory.selectType("INVALID");
    }

}