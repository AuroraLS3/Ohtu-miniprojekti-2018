package projekti.language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class for managing language strings.
 *
 * @author Rsl1122
 */
public class Locale {

	private final Map<String, Object> langMap;

	public Locale(Map<String, Object> langMap) {
		this.langMap = langMap;
	}

	public String get(Lang lang) {
		String key = lang.getKey();
		Object value = langMap.getOrDefault(key, "Missing language key: " + key);
		if (value.getClass() == ArrayList.class) {
			@SuppressWarnings("unchecked")
			ArrayList<String> list = (ArrayList<String>) value;
			StringBuilder sb = new StringBuilder();
			for (String s : list) {
				sb.append(s + "\n");
			}
			return sb.toString();
		} else {
			return value.toString();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getAsList(Lang lang) {
		String key = lang.getKey();
		Object value = langMap.getOrDefault(key, "Missing language key: " + key);
		if (value.getClass() == ArrayList.class) {
			return ((ArrayList<String>) value);
		} else {
			ArrayList<String> list = new ArrayList<>();
			list.add(value.toString());
			return list;
		}
	}

	public String get(Lang lang, Serializable... placeholderValues) {
		String message = get(lang);
		for (int i = 0; i < placeholderValues.length; i++) {
			message = message.replace("${" + i + "}", placeholderValues[i].toString());
		}
		return message;
	}
}
