package projekti.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class LanguageFileReaderTest {
	@Test
	public void readLanguageTest() {
		LanguageFileReader lr = new LanguageFileReader();	
		Locale locale = Locale.createWith(new LanguageFileReader(), LanguageFiles.ENGLISH.getResourcePath());
		assertTrue("Welcome to the reading recommendation app !".contentEquals(locale.get(LanguageKeys.GREET)));
		assertTrue("Supported commands:".contentEquals(locale.getAsList(LanguageKeys.MAINCOMMANDS).get(0)));
		Locale suomi = Locale.createWith(new LanguageFileReader(), LanguageFiles.SUOMI.getResourcePath());
		assertTrue("Tervetuloa lukuvinkki applikaatioon!".contentEquals(suomi.get(LanguageKeys.GREET)));
		assertEquals("Tervetuloa lukuvinkki applikaatioon!", (suomi.getAsList(LanguageKeys.GREET).get(0)));
		assertEquals("Valitse vinkin tyyppi\nvinkin tyyppi (vaihtoehdot: book, blog, other): \n", suomi.get(LanguageKeys.TYPELIST));
	}

}
