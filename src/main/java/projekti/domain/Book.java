package projekti.domain;

import projekti.util.Check;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Object that represents a book.
 *
 * @author Rsl1122
 */
public class Book extends AbstractPropertyStore {

    /**
     * Class that lists properties of a Book object.
     * <p>
     * Use this to access properties {@code book.getProperty(Properties.NAME)}
     */
    public static class Properties {
        public static Property<String> AUTHOR = new Property<>("author", String.class, author -> author != null && !author.isEmpty());
        public static Property<String> TITLE = CommonProperties.TITLE;
        public static Property<String> ISBN = new Property<>("isbn", String.class, isbn -> isbn != null && !isbn.isEmpty());
        public static Property<Integer> ID = CommonProperties.ID;
    }

    @Override
    public List<Property> getProperties() {
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

    private final String type;

    private Integer id;

    /**
     * Create a new book.
     *
     * @param author Author of the book, not null or empty.
     * @param title  Title of the book, not null or empty.
     * @param isbn   ISBN of the book, not null or empty.
     */
    public Book(String author, String title, String isbn) {
        Check.notNull(author, () -> new IllegalArgumentException("Author should not be null"));
        Check.notNull(title, () -> new IllegalArgumentException("Title should not be null"));
        Check.notNull(isbn, () -> new IllegalArgumentException("ISBN should not be null"));

        Check.isFalse(author.isEmpty(), () -> new IllegalArgumentException("Author should not be empty"));
        Check.isFalse(title.isEmpty(), () -> new IllegalArgumentException("Title should not be empty"));
        Check.isFalse(isbn.isEmpty(), () -> new IllegalArgumentException("ISBN should not be empty"));

        addProperty(Properties.AUTHOR, author);
        addProperty(Properties.TITLE, title);
        addProperty(Properties.ISBN, isbn);

        this.type = "BOOK";
    }

    public void setID(Integer id) {
        addProperty(Properties.ID, id);
    }

    /**
     * Get the database ID of the book.
     *
     * @return database ID of the book.
     * @deprecated Use {@code book.getProperty(Properties.ID)} instead.
     */
    @Deprecated
    public Integer getID() {
        return getProperty(Properties.ID).orElse(null);
    }

    /**
     * Get the author of the book.
     *
     * @return Author of the book.
     * @deprecated Use {@code book.getProperty(Properties.AUTHOR)} instead.
     */
    @Deprecated
    public String getAuthor() {
        return getProperty(Properties.AUTHOR).orElse(null);
    }

    /**
     * Get the title of the book.
     *
     * @return Title of the book.
     * @deprecated Use {@code book.getProperty(Properties.TITLE)} instead.
     */
    @Deprecated
    public String getTitle() {
        return getProperty(Properties.TITLE).orElse(null);
    }

    /**
     * Get the ISBN of the book.
     *
     * @return ISBN of the book.
     * @deprecated Use {@code book.getProperty(Properties.ISBN)} instead.
     */
    @Deprecated
    public String getISBN() {
        return getProperty(Properties.ISBN).orElse(null);
    }

    public String getType() {
        return type;
    }
}
