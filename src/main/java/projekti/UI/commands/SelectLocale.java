package projekti.UI.commands;

import java.util.HashMap;

import projekti.UI.TUI;
import projekti.language.LanguageFileReader;
import projekti.language.LanguageFiles;
import projekti.language.LanguageKeys;
import projekti.language.Locale;


public class SelectLocale implements Command{
	
	private RecHelper rh;
	private HashMap<String, Locale> choices;
	
	public SelectLocale(RecHelper rh) {
		this.rh = rh;
		createChoices();
	}
	
	private void createChoices() {
		this.choices = new HashMap<>();
		for(LanguageFiles f : LanguageFiles.values()) {
			Locale locale = Locale.createWith(new LanguageFileReader(), f.getResourcePath());
			choices.put(locale.get(LanguageKeys.NAMEOFTHELANG), locale);
		}
	}

	@Override
	public void execute() {
		rh.setLocale(askLocale());
	}
	
	private Locale askLocale() {
		Locale l = null;
		while (l == null) {
			rh.getIO().println("Select Language");
			rh.getIO().println("Choices: ");
			for(String s : choices.keySet()) {
				rh.getIO().print(s + "\n");
			}
			String choice = rh.getIO().getInput();
			l = choices.get(choice);
		}
		return l;
		
	}

}
