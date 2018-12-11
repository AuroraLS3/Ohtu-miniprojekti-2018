package projekti.language;

import java.io.Serializable;
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

    @SuppressWarnings("unchecked")
    public String get(Lang lang) {
        String key = lang.getKey();
        Object value = langMap.getOrDefault(key, "Missing language key: " + key);
        if (value instanceof List) {
            return toStringList((List<String>) value);
        } else {
            return value.toString();
        }
    }

    private String toStringList(List<String> value) {
        StringBuilder builder = new StringBuilder();

        int max = value.size();
        for (int i = 0; i < max; i++) {
            builder.append(value.get(i));

            // Avoid adding newline after last element
            if (i + 1 < max) {
                builder.append("\n");
            }
        }
        return builder.toString();
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
