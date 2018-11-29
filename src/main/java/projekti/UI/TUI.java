package projekti.UI;

import projekti.db.Dao;
import projekti.domain.Book;
import projekti.domain.Book.Properties;
import projekti.util.Check;

import java.sql.SQLException;
import java.util.List;

public class TUI {

    private Dao<Book, Integer> bookDao;
    private IO io;

    public TUI(Dao<Book, Integer> bd, IO io) {
        bookDao = bd;
        this.io = io;
    }

    public void run() throws SQLException {
        io.println("Welcome to the reading recommendation app!");
        io.println("Supported commands:");
        io.println("\tnew \tadd a new recommendation");
        io.println("\tall \tlist all existing recommendations");
        io.println("\tselect \tselect a specific recommendation");
        io.println("\tupdate \tupdate information for an existing recommendation");
        io.println("\tdelete \tremove a recommendation");
        io.println("\tend \tclose the program");

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            io.println("\ncommand: ");
            input = io.getInput();
            try {
                performAction(input);
            } catch (IllegalArgumentException e) {
                io.println(e.getMessage());
            }
        }
    }

    private void performAction(String input) throws SQLException {
        switch (input.toLowerCase()) { 
            case "new": 
                //Käyttäjä valitsee vinkin tyyypin, nyt vain kirjat tuettu
                createBook();
                break;
            case "all":
                List<Book> books = bookDao.findAll();

                books.forEach(book -> {
                    io.println(book.toString());
                });
                break;
            case "end":
                io.println("\nshutting down program");
                break;
            case "select":
                bookSelection();
                break;
            case "update":
                Integer updateID = selectID();
                updateBook(updateID);
                break;
            case "delete":
                Integer deleteID = selectID();
                deleteBook(deleteID);
                break;
            default:
                io.println("\nnon-supported command");
                break;
        }
    }

    private void bookSelection() throws SQLException {
        Book book = askForBook();

        String input = "";

        selectionLoop:
        while (!input.equals("return")) {
            if (book == null) {
                return;
            }
            io.println(book.toStringWithDescription());
            io.println();

            io.println("\ncommands for the selected recommendation: ");
            io.println("\tedit \tedit the recommendation");
            io.println("\tdelete \tremove the recommendation");
            io.println("\treturn \treturn to the main program");

            input = io.getInput();
            switch (input) {
                case "edit":
                    book = updateBook(book.getProperty(Properties.ID).orElse(null));
                    break;

                case "delete":
                    deleteBook(book.getProperty(Properties.ID).orElse(null));
                    break selectionLoop;
                case "return":
                    io.println();
                    break;
                default:
                    io.println("\nnon-supported command");
                    break;
            }

        }
    }

    private Book askForBook() throws SQLException {
        Integer ID = selectID();

        Book book = bookDao.findOne(ID);
        Check.notNull(book, () -> new IllegalArgumentException("No book found"));
        return book;
    }

    private void createBook() throws SQLException {
        io.print("author: ");
        String author = io.getInput().trim();

        io.print("title: ");
        String title = io.getInput().trim();

        io.print("ISBN: ");
        String ISBN = io.getInput().trim();

        io.print("Description (optional): ");
        String description = io.getInput().trim();

        Book book;
        try {
            book = new Book(author, title, ISBN);
            if (!description.isEmpty()) {
                book.setDescription(description);
            }
        } catch (IllegalArgumentException ex) {
            io.println("\n Book recommendation was not added.");
            throw ex;
        }

        if (bookDao.create(book) != null) {
            io.println();
            io.println("new book recommendation added");
        } else {
            io.println("\nrecommendation not added");
        }
       
    }

    private Integer selectID() {
        io.println("syötä olion id tai palaa jättämällä tyhjäksi");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            return Integer.parseInt(id_String);
        } catch (IllegalArgumentException ex) {
            if (!id_String.isEmpty()) {
                io.println("Not a valid ID. Has to be a number.");
            }
            throw ex;
        }
    }

    private boolean confirm(String message) {
        io.println(message);
        String optionString = "y/n";
        io.println(optionString);
        String val = io.getInput();
        if (val.toLowerCase().contains("y")) {
            return true;
        } else if (val.toLowerCase().contains("n")) {
            return false;
        } else {
            String failMessage = "Invalid input";
            io.println(failMessage);
            return confirm(message);
        }
    }

    private void deleteBook(Integer knownID) throws SQLException {
        Book book = bookDao.findOne(knownID);
        Check.notNull(book, () -> new IllegalArgumentException("No book found with id " + knownID));

        if (confirm("Are you sure you want to delete recommendation " + knownID + "?")) {
            bookDao.delete(knownID);
            io.println();
            io.println("recommendation successfully deleted");
        } else {
            io.println();
            io.println("recommendation deletion canceled");
        }
    }

    private Book updateBook(Integer knownID) throws SQLException {
        Book oldBook = bookDao.findOne(knownID);
        Check.notNull(oldBook, () -> new IllegalArgumentException("No book found with id " + knownID));
        Book updatedBook = new Book(oldBook.getProperty(Properties.AUTHOR).orElse(""),
                oldBook.getProperty(Properties.TITLE).orElse(""),
                oldBook.getProperty(Properties.ISBN).orElse(""),
                oldBook.getProperty(Properties.DESCRIPTION).orElse(""));
        updatedBook.setID(oldBook.getProperty(Properties.ID).orElse(-1));
        io.print("enter new author (or empty input to leave it unchanged): ");
        String author = io.getInput();
        if (!author.isEmpty()) {
            updatedBook.setAuthor(author);
        }
        io.print("enter new title (or empty input to leave it unchanged): ");
        String title = io.getInput();
        if (!title.isEmpty()) {
            updatedBook.setTitle(title);
        }
        io.print("enter new ISBN (or empty input to leave it unchanged): ");
        String isbn = io.getInput();
        if (!isbn.isEmpty()) {
            updatedBook.setISBN(isbn);
        }
        io.print("enter new description (or empty input to leave it unchanged): ");
        String description = io.getInput();
        if (!description.isEmpty()) {
            updatedBook.setDescription(description);
        }
        if (confirm("are you sure you want to update recommendation " + knownID + "?")) {
            if (bookDao.update(updatedBook)) {
                io.println();
                io.println("update successful");
                return updatedBook;
            } else {
                io.println();
                io.println("update failed");
                return oldBook;
            }
        } else {
            io.println();
            io.println("recommendation update canceled");
            return oldBook;
        }
    }
}
