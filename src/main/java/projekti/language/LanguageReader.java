package projekti.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import projekti.main.Main;

public class LanguageReader {

	private final String filename = "json/lang.json";

	public ArrayList<Locale> readLanguages() {
		ArrayList<Locale> locales = new ArrayList<>();
		for (HashMap<String, Object> map : readJson()) {
			locales.add(new Locale(map));
		}
		return locales;
	}

	private HashMap<String, Object>[] readJson() {
		try (InputStreamReader isr = new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(filename));
				BufferedReader br = new BufferedReader(isr)) {
			Gson gson = new Gson();
			@SuppressWarnings("unchecked")
			HashMap<String, Object>[] json = gson.fromJson(br, HashMap[].class);
			return json;

		} catch (IOException ex) {
			return null;
		}
	}

}
