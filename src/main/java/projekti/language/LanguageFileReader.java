package projekti.language;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import projekti.main.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LanguageFileReader {


	public ArrayList<Locale> readLanguages(String filename) {
		ArrayList<Locale> locales = new ArrayList<>();
		for (HashMap<String, Object> map : readJson(filename)) {
			locales.add(new Locale(map));
		}
		return locales;
	}

	private HashMap<String, Object>[] readJson(String filename) {
		try (InputStreamReader isr = new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(filename));
				BufferedReader br = new BufferedReader(isr)) {
			Gson gson = new Gson();
			@SuppressWarnings("unchecked")
			HashMap<String, Object>[] json = gson.fromJson(br, HashMap[].class);
			return json;
		} catch (IOException ex) {
			throw new IllegalStateException("Can't read language file");
		}
	}
}
