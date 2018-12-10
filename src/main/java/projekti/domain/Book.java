package projekti.domain;

import projekti.util.Check;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.validator.routines.UrlValidator;
/**
 * Object that represents a book.
 *
 * @author Rsl1122
 */
public class Book extends AbstractPropertyStore implements Recommendation {

    /**
     * Class that lists properties of a Book object.
     * <p>
     * Use this to access properties {@code book.getProperty(Properties.NAME)}
     */
    public static class Properties {

        public static final Property<String> AUTHOR = new Property<>("AUTHOR", String.class, author -> author != null && !author.isEmpty());
        public static final Property<String> TITLE = CommonProperties.TITLE;
        public static final Property<String> ISBN = new Property<>("ISBN", String.class, isbn -> isbn != null && !isbn.isEmpty());
        public static final Property<Integer> ID = CommonProperties.ID;
        public static final Property<String> URL = CommonProperties.URL;
        public static final Property<String> DESCRIPTION = CommonProperties.DESCRIPTION;

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
     * Create a new book.
     *
     * @param author      Author of the book, not null or empty.
     * @param title       Title of the book, not null or empty.
     * @param isbn        ISBN of the book, not null or empty.
     * @param url		  URL address of the book, valid url or empty.
     * @param description Description of the book, can be empty
     */
    public Book(String author, String title, String isbn, String url, String description) {
        Check.notNull(author, () -> new IllegalArgumentException("Author should not be null"));
        Check.notNull(title, () -> new IllegalArgumentException("Title should not be null"));
        Check.notNull(isbn, () -> new IllegalArgumentException("ISBN should not be null"));
        Check.isFalse(author.isEmpty(), () -> new IllegalArgumentException("Author should not be empty"));
        Check.isFalse(title.isEmpty(), () -> new IllegalArgumentException("Title should not be empty"));
        Check.isFalse(isbn.isEmpty(), () -> new IllegalArgumentException("ISBN should not be empty"));
        addProperty(Properties.AUTHOR, author);
        addProperty(Properties.TITLE, title);
        addProperty(Properties.ISBN, isbn);
        addProperty(Properties.DESCRIPTION, description);
        handleUrlProperty(url);
        this.type = "BOOK";
    }

    private void handleUrlProperty(String url) {
    	if (url != null) {
            Check.isTrue(new UrlValidator().isValid(url), () -> new IllegalArgumentException("URL should be valid"));
            addProperty(Properties.URL, url);
    	}
    }

    @Deprecated
    public Book(String author, String title, String isbn, String description) {
    	this(author, title, isbn, null, description);
    }

    @Deprecated
    public Book(String author, String title, String isbn) {
        this(author, title, isbn, null, null);
    }

    Book() {
        this.type = "BOOK";
    }

//    public void setID(Integer id) {
//        addProperty(Properties.ID, id);
//    }
//
//    public void setDescription(String description) {
//        addProperty(Properties.DESCRIPTION, description);
//    }

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
        Book book = (Book) o;
        return super.equals(o) && Objects.equals(type, book.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return ". "
                + getProperty(Properties.AUTHOR).orElse("Not Specified")
                + ": " + getProperty(Properties.TITLE).orElse("Not Specified")
                + ", ISBN: " + getProperty(Properties.ISBN).orElse("-") + ", URL: " + getProperty(Properties.URL).orElse("-");
    }

    public String toStringWithDescription() {
        StringBuilder builder = new StringBuilder(toString());
        builder.append("\nDescription: ");
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
