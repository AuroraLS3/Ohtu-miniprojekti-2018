package projekti.language;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test against missing language keys.
 *
 * @author Rsl1122
 */
public class LanguageKeysTest {

    private static Locale ENGLISH;
    private static Locale FINNISH;

    @BeforeClass
    public static void setUpClass() {
        LanguageFileReader languageFileReader = new LanguageFileReader();
        ENGLISH = Locale.createWith(languageFileReader, LanguageFiles.ENGLISH.getResourcePath());
        FINNISH = Locale.createWith(languageFileReader, LanguageFiles.SUOMI.getResourcePath());

    }

    private List<Lang> getAllLanguageKeys() {
        return Arrays.asList(LanguageKeys.values());
    }

    @Test
    public void englishLanguageFileHasAllKeys() {
        testAgainstMissing(ENGLISH, "English");
    }

    @Test
    public void finnishLanguageFileHasAllKeys() {
        testAgainstMissing(FINNISH, "Finnish");
    }

    private void testAgainstMissing(Locale locale, String localeName) {
        List<String> missing = new ArrayList<>();
        for (Lang lang : getAllLanguageKeys()) {
            String found = locale.get(lang);
            if (found == null) {
                missing.add(lang.getKey());
            }
        }
        assertTrue(localeName + " is missing keys: " + missing, missing.isEmpty());
    }

}