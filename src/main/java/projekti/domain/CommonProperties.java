package projekti.domain;

import org.apache.commons.validator.routines.UrlValidator;

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
}
