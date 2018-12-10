package projekti.language;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;


public class LanguageReaderTest {
	
	@Test
	public void readLanguageTest() {
		LanguageReader lr = new LanguageReader();
		ArrayList<Locale> locales = lr.readLanguages();
		Locale locale = locales.get(0);
		assertTrue("Welcome to the reading recommendation app !".contentEquals(locale.get(LanguageKeys.GREET)));
		System.out.println(locale.get(LanguageKeys.MAINCOMMANDS));
		Locale suomi = locales.get(1);
		assertTrue("Tervetuloa lukuvinkki applikaatioon!".contentEquals(suomi.get(LanguageKeys.GREET)));
		
	}

}
