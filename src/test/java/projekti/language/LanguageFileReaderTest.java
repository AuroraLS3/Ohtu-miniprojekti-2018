package projekti.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class LanguageFileReaderTest {
	@Test
	public void readLanguageTest() {
		LanguageFileReader lr = new LanguageFileReader();
		ArrayList<Locale> locales = lr.readLanguages("json/lang.json");
		Locale locale = locales.get(0);
		assertTrue("Welcome to the reading recommendation app !".contentEquals(locale.get(LanguageKeys.GREET)));
		assertTrue("Supported commands:".contentEquals(locale.getAsList(LanguageKeys.MAINCOMMANDS).get(0)));
		Locale suomi = locales.get(1);
		assertTrue("Tervetuloa lukuvinkki applikaatioon!".contentEquals(suomi.get(LanguageKeys.GREET)));
		assertEquals("Tervetuloa lukuvinkki applikaatioon!", (suomi.getAsList(LanguageKeys.GREET).get(0)));
		assertEquals("Valitse vinkin tyyppi\nvinkin tyyppi (vaihtoehdot: book, blog, other): \n", suomi.get(LanguageKeys.TYPELIST));
	}

}
