package projekti.language;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import projekti.main.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public class LanguageFileReader {

	public Map<String, Object> readJson(String filename) {
		try (InputStreamReader isr = new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(filename));
				BufferedReader br = new BufferedReader(isr)) {
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, Object>>() {
			}.getType();
			Map<String, Object> json = gson.fromJson(br, type);
			return json;
		} catch (IOException ex) {
			throw new IllegalStateException("Can't read language file");
		}
	}
}
