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
        io.println("Tervetuloa lukuvinkkiapplikaatioon!");
        io.println("Tuetut toiminnot:");
        io.println("\tnew \tlisää uusi lukuvinkki");
        io.println("\tall \tlistaa kaikki lukuvinkit");
        io.println("\tselect \ttarkastele tiettyä vinkkiä");
        io.println("\tupdate \tmuokkaa tiettyä vinkkiä");
        io.println("\tdelete \tpoista tietyn vinkin");
        io.println("\tend \tsulkee ohjelman");

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            io.println("\ntoiminto: ");
            input = io.getInput();
            performAction(input);
        }
    }

    private void performAction(String input) throws SQLException {
        switch (input.toLowerCase()) { // kaikki toiminnot voidaan refaktoroida omaksi metodikseen myöhemmin
            case "new": //luodaan uusi vinkki tietokantaan
                //Käyttäjä valitsee vinkin tyyypin, nyt vain kirjat tuettu
                createBook();
                break;
            case "all": //listataan kaikki vinkit tietokannasta;
                List<Book> books = bookDao.findAll();
               
                books.forEach(book -> {
                    io.println(book.toString());
                });
                break;
            case "end":
                io.println("\nlopetetaan ohjelman suoritus");
                break;
            case "select":
                bookSelection();
                break;
            case "update":
                updateBook(null);
                break;
            case "delete":
                deleteBook(null);
                break;
            default:
                io.println("\nei tuettu toiminto");
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

            io.println("\ntoiminnot valitulle vinkille:");
            io.println("\tedit \tmuokkaa valittua vinkkiä");
            io.println("\tdelete \tpoista valittu vinkki");
            io.println("\treturn \tlopeta vinkin tarkastelu");

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
                    io.println("\nei tuettu toiminto");
                    break;
            }

        }
    }

    private Book askForBook() throws SQLException {
        try {
            Integer ID = selectID();

            Book book = bookDao.findOne(ID);
            Check.notNull(book, () -> new NullPointerException("No book found"));
            return book;
        } catch (NullPointerException ex) {
            io.print(ex.getMessage());
        }
        return null;
    }

    private void createBook() throws SQLException {
        io.print("kirjailija: ");
        String author = io.getInput();

        io.print("nimi: ");
        String title = io.getInput();

        io.print("ISBN: ");
        String ISBN = io.getInput();

        io.print("Kuvaus (valinnainen): ");
        String description = io.getInput();

        Book book;
        try {
            book = new Book(author, title, ISBN);
            if (!description.isEmpty()) {
                book.setDescription(description);
            }
        } catch (IllegalArgumentException ex) {
            io.println("\n " + ex.getMessage());
            io.println("\n Book recommendation was not added.");
            return;
        }

        if (bookDao.create(book) != null) {
            io.println();
            io.println("new book recommendation added");
        } else {
            io.println("\nvinkkiä ei lisätty");
        }
        // oletetaan toistaiseksi, että onnistuu. Daon kanssa ongelmia. io.print("\nuutta vinkkiä ei lisätty");
    }

    private Integer selectID() {
        io.println("syötä olion id tai palaa jättämällä tyhjäksi");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            return Integer.parseInt(id_String);
        } catch (IllegalArgumentException ex) {
            if (!id_String.equals("")) {
                io.println("Not a valid ID. Has to be a number.");
            }
        }
        return null;
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
            String failMessage = "Valintaa ei tunnistettu.";
            io.println(failMessage);
            return confirm(message);
        }
    }


    private void deleteBook(Integer knownID) throws SQLException {
        try {
            Integer ID = -1;
            if (knownID == null) {
                ID = selectID();
            } else {
                ID = knownID;
            }
            Integer idFinal = ID;        //valittaa muuten että pitää olla final
            Book book = bookDao.findOne(ID);
            Check.notNull(book, () -> new NullPointerException("No book found with id " + idFinal));

            if (confirm("oletko varma, että haluat poistaa lukuvinkin numero " + ID + "?")) {
                bookDao.delete(ID);
                io.println();
                io.println("vinkin poistaminen onnistui");
            } else {
                io.println();
                io.println("recommendation deletion canceled");
            }
        } catch (NullPointerException ex) {
            io.println();
            io.println(ex.getMessage());
        }
    }


 
    private Book updateBook(Integer knownID) throws SQLException {

        try {
            Integer ID = -1;
            if (knownID == null) {
                ID = selectID();
            } else {
                ID = knownID;
            }
            Integer idFinal = ID;       //valittaa muuten että pitää olla final
            Book oldBook = bookDao.findOne(ID);
            Check.notNull(oldBook, () -> new NullPointerException("No book found with id " + idFinal));

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
            if (confirm("oletko varma, että haluat muokata lukuvinkkiä numero " + ID + "?")) {
                if (bookDao.update(updatedBook)) {
                    io.println();
                    io.println("vinkin muokkaaminen onnistui");
                    return updatedBook;
                } else {
                    io.println();
                    io.println("vinkin muokkaaminen epäonnistui");
                    return oldBook;
                }
            } else {
                io.println();
                io.println("recommendation update canceled");
                return oldBook;
            }
        } catch (NullPointerException ex) {
            io.println();
            io.println(ex.getMessage());
            return null;
        }
    }
}
