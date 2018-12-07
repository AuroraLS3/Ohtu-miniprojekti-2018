package projekti.language;

import java.io.Serializable;
import java.util.Map;

/**
 * Class for managing language strings.
 *
 * @author Rsl1122
 */
public class Locale {

    private final Map<String, String> langMap;

    public Locale(Map<String, String> langMap) {
        this.langMap = langMap;
    }

    public String get(Lang lang) {
        String key = lang.getKey();
        return langMap.getOrDefault(key, "Missing language key: " + key);
    }

    public String get(Lang lang, Serializable... placeholderValues) {
        String message = get(lang);
        for (int i = 0; i < placeholderValues.length; i++) {
            message = message.replace("${" + i + "}", placeholderValues[i].toString());
        }
        return message;
    }
}
