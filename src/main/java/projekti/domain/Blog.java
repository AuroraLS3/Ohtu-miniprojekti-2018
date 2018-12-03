package projekti.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Object that represents a blog.
 *
 * @author Rsl1122
 */
public class Blog extends AbstractPropertyStore implements Recommendation {

    /**
     * Class that lists properties of a Blog object.
     * <p>
     * Use this to access properties {@code blog.getProperty(Properties.NAME)}
     */
    public static class Properties {

        public static final Property<String> TITLE = CommonProperties.TITLE;
        // TODO Add URL Validator (Move the best URL Property definition to CommonProperties and refer to that here)
        public static final Property<String> URL = new Property<>("URL", String.class, url -> true);
        public static final Property<String> DESCRIPTION = CommonProperties.DESCRIPTION;
        public static final Property<Integer> ID = CommonProperties.ID;

        public static List<Property> getAll() {
            List<Property> properties = new ArrayList<>();
            for (Field field : Properties.class.getDeclaredFields()) {
                if (!Modifier.isPublic(field.getModifiers())) {
                    continue;
                }
                try {
                    properties.add((Property) field.get(null));
                } catch (IllegalAccessException ignored) {
                    /* Inaccessible field */
                }
            }
            return properties;
        }
    }

    @Override
    public List<Property> getProperties() {
        return Properties.getAll();
    }

    private final String type;

    /**
     * Create a new blog.
     *
     * @param title       Title of the blog, not null or empty.
     * @param url         url of the blog, not null or empty.
     * @param description Description of the blog, can be empty
     */
    public Blog(String title, String url, String description) {
        addProperty(Properties.TITLE, title);
        addProperty(Properties.URL, url);
        addProperty(Properties.DESCRIPTION, description);

        this.type = "BLOG";
    }

    public Blog(String title, String url) {
        this(title, url, null);
    }

    Blog() {
        this.type = "BLOG";
    }

    public void setTitle(String title) {
        addProperty(Properties.TITLE, title);
    }

    public void setURL(String url) {
        addProperty(Properties.URL, url);
    }

    public void setID(Integer id) {
        addProperty(Properties.ID, id);
    }

    public void setDescription(String description) {
        addProperty(Properties.DESCRIPTION, description);
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Blog blog = (Blog) o;
        return Objects.equals(type, blog.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return getProperty(Properties.ID).orElse(-1) + ". "
                + ": " + getProperty(Properties.TITLE).orElse("Not Specified")
                + ", URL: " + getProperty(Properties.URL).orElse("-");
    }

    @Override
    public String toStringWithDescription() {
        StringBuilder builder = new StringBuilder(toString());
        builder.append("\nKuvaus: ");
        Optional<String> descriptionProperty = getProperty(Properties.DESCRIPTION);
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
