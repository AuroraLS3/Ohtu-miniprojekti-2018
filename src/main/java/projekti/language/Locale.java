package projekti.language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) value;
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
    public List<String> getAsList(Lang lang) {
        String key = lang.getKey();
        Object value = langMap.getOrDefault(key, "Missing language key: " + key);
        if (value instanceof List) {
            return ((List<String>) value);
        } else {
            return Collections.singletonList(value.toString());
        }
    }

    public String get(Lang lang, Serializable... placeholderValues) {
        String message = get(lang);
        for (int i = 0; i < placeholderValues.length; i++) {
            message = message.replace("${" + i + "}", placeholderValues[i].toString());
        }
        return message;
    }

    public static Locale createWith(LanguageFileReader lfr, String filename) {
        return new Locale(lfr.readJson(filename));
    }
}
