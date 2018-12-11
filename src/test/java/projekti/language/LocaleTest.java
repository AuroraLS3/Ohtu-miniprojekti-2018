package projekti.language;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Locale} class;
 *
 * @author Rsl1122
 */
public class LocaleTest {

    private static Locale underTest;

    @BeforeClass
    public static void setUpClass() {
        Map<String, Object> testLocale = new HashMap<>();
        testLocale.put(LanguageKeys.GREET.getKey(), "Hello");
        testLocale.put(LanguageKeys.COMMAND.getKey(), "Command ${0}");
        testLocale.put(LanguageKeys.MAINCOMMANDS.getKey(), Arrays.asList("One", "Two"));
        testLocale.put(LanguageKeys.TYPELIST.getKey(), Arrays.asList("${0}", "Wat"));

        underTest = new Locale(testLocale);
    }

    @Test
    public void singleStringIsReturned() {
        String expected = "Hello";
        String result = underTest.get(LanguageKeys.GREET);
        assertEquals(expected, result);
    }

    @Test
    public void placeholdersAreReplaced() {
        String expected = "Command example";
        String result = underTest.get(LanguageKeys.COMMAND, "example");
        assertEquals(expected, result);
    }

    @Test
    public void listsAreParsedWithNewLines() {
        String expected = "One\nTwo";
        String result = underTest.get(LanguageKeys.MAINCOMMANDS);
        assertEquals(expected, result);
    }

    @Test
    public void placeholdersAreReplacedOnLists() {
        String expected = "Wat\nWat";
        String result = underTest.get(LanguageKeys.TYPELIST, "Wat");
        assertEquals(expected, result);
    }

    @Test
    public void listsAreParsed() {
        List<String> expected = Arrays.asList("One", "Two");
        List<String> result = underTest.getAsList(LanguageKeys.MAINCOMMANDS);
        assertEquals(expected, result);
    }

    @Test
    public void singleElementListsAreParsed() {
        List<String> expected = Collections.singletonList("Hello");
        List<String> result = underTest.getAsList(LanguageKeys.GREET);
        assertEquals(expected, result);
    }

}