package projekti.domain;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.Optional;

/**
 * Contains common properties that might be used in multiple different objects.
 *
 * @author Rsl1122
 */
public class CommonProperties {

    public static final Property<Integer> ID = new Property<>("ID", Integer.class);
    public static final Property<String> TITLE = new Property<>("NAME", String.class, title -> title != null && !title.isEmpty());
    public static final Property<String> DESCRIPTION = new Property<>("DESCRIPTION", String.class);
    public static final Property<String> URL = new Property<>("URL", String.class, url -> validateUrl(url));

    private static boolean validateUrl(String url) {
        return url == null || url.isEmpty() || new UrlValidator().isValid(url);
    }

    static String turnDescriptionToString(Recommendation recommendation) {
        StringBuilder builder = new StringBuilder();

        Optional<String> descriptionProperty = recommendation.getProperty(DESCRIPTION);
        if (descriptionProperty.isPresent()) {

            int currentLength = 0;
            int charPerLine = 100;
            // Split description on multiple lines, split between space characters if over 100 characters.
            String[] words = descriptionProperty.get().split(" ");

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                builder.append(word);
                currentLength += word.length();

                if (currentLength > charPerLine) {
                    builder.append("\n");
                    currentLength = 0;
                }

                // Separate words with spaces again, but don't add trailing space
                if (i < words.length - 1) {
                    builder.append(" ");
                    currentLength++;
                }
            }
        }

        return builder.toString();
    }
}
