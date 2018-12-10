package projekti.language;

import static org.junit.Assert.assertEquals;
import org.junit.Before;

import org.junit.Test;

public class LanguageFileReaderTest {

    private LanguageFileReader lfr;
    private Locale english;
    private Locale suomi;

    @Before
    public void beforeTest() {
        this.lfr = new LanguageFileReader();
        english = Locale.createWith(lfr, LanguageFiles.ENGLISH.getResourcePath());
        suomi = Locale.createWith(lfr, LanguageFiles.SUOMI.getResourcePath());
    }

    @Test
    public void normalUseCaseEnglishTest() {
        assertEquals("Welcome to the reading recommendation app !", english.get(LanguageKeys.GREET));

    }

    @Test
    public void normalUseCaseSuomiTest() {
        assertEquals("Tervetuloa lukuvinkki applikaatioon!", suomi.get(LanguageKeys.GREET));
    }

    @Test
    public void asAListEnglishTest() {
        assertEquals("Supported commands:", english.getAsList(LanguageKeys.MAINCOMMANDS).get(0));

    }

    @Test
    public void asListSuomiTest() {
        assertEquals("Tervetuloa lukuvinkki applikaatioon!", (suomi.getAsList(LanguageKeys.GREET).get(0)));
    }

    @Test
    public void listAsAStringSuomiTest() {
        assertEquals("Valitse vinkin tyyppi\nvinkin tyyppi (vaihtoehdot: book, blog, other): \n", suomi.get(LanguageKeys.TYPELIST));
    }

}
