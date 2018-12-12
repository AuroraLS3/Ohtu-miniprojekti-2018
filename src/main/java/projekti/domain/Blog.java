package projekti.domain;

import projekti.util.Check;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Object that represents a blog.
 *
 * @author Rsl1122
 */
public class Blog extends AbstractPropertyStore implements Recommendation {

    private final String type;

    /**
     * Class that lists properties of a Blog object.
     * <p>
     * Use this to access properties {@code blog.getProperty(Properties.NAME)}
     */
    public static class Properties {

        public static final Property<String> TITLE = CommonProperties.TITLE;
        public static final Property<String> URL = CommonProperties.URL;
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

    /**
     * Create a new blog.
     *
     * @param title Title of the blog, not null or empty.
     * @param url url of the blog, not null or empty.
     * @param description Description of the blog, can be empty
     */
    public Blog(String title, String url, String description) {
        Check.notNull(title, () -> new IllegalArgumentException("Title should not be null"));
        Check.isFalse(title.isEmpty(), () -> new IllegalArgumentException("Title should not be empty"));
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

    public String getTitle() {
        return "" + getProperty(Properties.TITLE);
    }

    public void setURL(String url) {
        addProperty(Properties.URL, url);
    }

    public Property getURL() {
        return Properties.URL;
    }

    public void setID(Integer id) {
        addProperty(Properties.ID, id);
    }

    public void setDescription(String description) {
        addProperty(Properties.DESCRIPTION, description);
    }

    public Property getDescription() {
        return Properties.DESCRIPTION;
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
        return super.equals(o) && Objects.equals(type, blog.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return ". "
                + getProperty(Properties.TITLE).orElse("Not Specified")
                + ", URL: " + getProperty(Properties.URL).orElse("-");
    }

    @Override
    public String toStringWithDescription() {
        return toString() + "\nDescription: " + CommonProperties.turnDescriptionToString(this);
    }
}
