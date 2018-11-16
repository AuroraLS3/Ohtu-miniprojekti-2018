package projekti.domain;

import projekti.util.Check;

/**
 * Object that represents a book.
 *
 * @author Rsl1122
 */
public class Book {

    private final String author;
    private final String title;
    private final String ISBN;
    private final String type;

    public Book(String author, String title, String ISBN) {
        Check.notNull(author, () -> new IllegalArgumentException("Author should not be null"));
        Check.notNull(title, () -> new IllegalArgumentException("Title should not be null"));
        Check.notNull(ISBN, () -> new IllegalArgumentException("ISBN should not be null"));

        Check.isFalse(author.isEmpty(), () -> new IllegalArgumentException("Author should not be empty"));
        Check.isFalse(title.isEmpty(), () -> new IllegalArgumentException("Title should not be empty"));
        Check.isFalse(ISBN.isEmpty(), () -> new IllegalArgumentException("ISBN should not be empty"));

        this.author = author;
        this.title = title;
        this.ISBN = ISBN;
        this.type = "BOOK";
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getType() {
        return type;
    }
}
