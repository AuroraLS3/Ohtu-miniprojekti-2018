package projekti.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link Property} class.
 *
 * @author Rsl1122
 */
public class PropertyTest {

    @Test
    public void samePropertyIsEqual() {
        Property<String> property = new Property<>("Test", String.class);
        assertEquals(property, property);
    }

    @Test
    public void duplicatePropertyIsEqual() {
        Property<String> property1 = new Property<>("Test", String.class);
        Property<String> property2 = new Property<>("Test", String.class);
        assertEquals(property1, property2);
    }

    @Test
    public void nonGenericPropertyIsEqual() {
        Property property1 = new Property<>("Test", String.class);
        Property property2 = new Property<>("Test", String.class);
        assertEquals(property1, property2);
    }

    @Test
    public void propertyWithDifferentTypeIsNotEqual() {
        Property property1 = new Property<>("Test", String.class);
        Property property2 = new Property<>("Test", Integer.class);
        assertNotEquals(property1, property2);
    }

    @Test
    public void propertyWithDifferentNameIsNotEqual() {
        Property property1 = new Property<>("Test", String.class);
        Property property2 = new Property<>("DifferentName", String.class);
        assertNotEquals(property1, property2);
    }

    @Test
    public void propertyWithDifferentVerifierIsNotEqual() {
        Property property1 = new Property<>("Test", String.class, property -> false);
        Property property2 = new Property<>("DifferentName", String.class);
        assertNotEquals(property1, property2);
    }

}