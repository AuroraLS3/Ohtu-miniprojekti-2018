package projekti.UI;

import java.sql.SQLException;
import java.util.List;
import projekti.db.Dao;
import projekti.domain.Book;
import projekti.domain.Book.Properties;
import projekti.util.Check;

public class TUI {

    private Dao<Book, Integer> bookDao;
    private IO io;

    public TUI(Dao<Book, Integer> bd, IO io) {
        bookDao = bd;
        this.io = io;
    }

    public void run() throws SQLException {
        io.print("Tervetuloa lukuvinkkiapplikaatioon!\n \n");
        io.print("Tuetut toiminnot:\n ");
        io.print("\tnew \tlisää uusi lukuvinkki \n");
        io.print("\tall \tlistaa kaikki lukuvinkit \n");
        io.print("\tselect \ttarkastele tiettyä vinkkiä \n");
        io.print("\tupdate \tmuokkaa tiettyä vinkkiä \n");
        io.print("\tdelete \tpoista tietyn vinkin \n");
        io.print("\tend \tsulkee ohjelman \n");
        String input = "";
        while (!input.equals("end")) {
            io.print("\ntoiminto: \n");
            input = io.getInput();
            switch (input) { // kaikki toiminnot voidaan refaktoroida omaksi metodikseen myöhemmin
                case "new": //luodaan uusi vinkki tietokantaan
                    //Käyttäjä valitsee vinkin tyyypin, nyt vain kirjat tuettu
                    createBook();
                    break;
                case "all": //listataan kaikki vinkit tietokannasta;
                    List<Book> books = bookDao.findAll();
                    // tulostusasun voisi määrittää kirjan toStringnä
                    books.forEach(s -> {
                        io.print(s.getID() + ". " + s.getAuthor() + ": " + s.getTitle() + ", ISBN: " + s.getISBN());
                        io.print("\n");
                    });
                    break;
                case "end":
                    io.print("\nlopetetaan ohjelman suoritus \n");
                    break;
                case "select":
                    selectBook();
                    break;
                case "update":
                    updateBook();
                    break;
                case "delete":
                    deleteBook();
                    break;
                default:
                    io.print("\nei tuettu toiminto \n");
            }
        }
    }

    private void selectBook() throws SQLException {
        io.print("syötä olion id tai palaa jättämällä tyhjäksi\n");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            Integer ID = Integer.parseInt(id_String);

            Book book = bookDao.findOne(ID);
            Check.notNull(book, () -> new NullPointerException("No book found"));
            io.print(book.getAuthor() + ": " + book.getTitle() + ", ISBN: " + book.getISBN() + "\n"); //myöhemmin myös kuvaus

        } catch (IllegalArgumentException ex) {
            if (!id_String.equals("")) {
                io.print("\n Not a valid ID. Has to be a number.");
            }
        } catch (NullPointerException ex) {
            io.print(ex.getMessage());
        }
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
            book = new Book(author, title, ISBN, "");
            if (!description.isEmpty()) {
                book.setDescription(description);
            }
        } catch (IllegalArgumentException ex) {
            io.print("\n Do not leave empty fields please. \n");
            io.print("\n Book recommendation was not added. \n");
            return;
        }

        if (bookDao.create(book) != null) {
            io.print("\n");
            io.print("new book recommendation added");
            io.print("\n");
        } else {
            io.print("\nvinkkiä ei lisätty \n");
        }
        // oletetaan toistaiseksi, että onnistuu. Daon kanssa ongelmia. io.print("\nuutta vinkkiä ei lisätty");
    }

    private boolean confirm(String message) {
        io.print(message + "\n");
        String optionString = "y/n";
        io.print(optionString + "\n");
        String val = io.getInput();
        if (val.toLowerCase().contains("y")) {
            return true;
        } else if (val.toLowerCase().contains("n")) {
            return false;
        } else {
            String failMessage = "Valintaa ei tunnistettu. \n";
            io.print(failMessage);
            return confirm(message);
        }
    }

    private void deleteBook() throws SQLException {
        io.print("syötä poistettavan olion id tai palaa jättämällä tyhjäksi\n");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            Integer ID = Integer.parseInt(id_String);
            Book book = bookDao.findOne(ID);
            Check.notNull(book, () -> new NullPointerException("No book found with id " + id_String));
            if (confirm("oletko varma, että haluat poistaa lukuvinkin numero " + id_String + "?")) {
                bookDao.delete(ID);
                io.print("\n");
                io.print("vinkin poistaminen onnistui");
                io.print("\n");
            } else {
                io.print("\n");
                io.print("recommendation deletion canceled");
                io.print("\n");
            }
        } catch (IllegalArgumentException ex) {
            if (!id_String.equals("")) {
                io.print("\n");
                io.print("Not a valid ID. Has to be a number.");
                io.print("\n");
            }
        } catch (NullPointerException ex) {
            io.print("\n");
            io.print(ex.getMessage());
            io.print("\n");
        }
    }

    private void updateBook() throws SQLException {
        io.print("syötä muokattavan olion id tai palaa jättämällä tyhjäksi\n");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            Integer ID = Integer.parseInt(id_String);
            Book oldBook = bookDao.findOne(ID);
            Check.notNull(oldBook, () -> new NullPointerException("No book found with id " + id_String));
            Book updatedBook = new Book(oldBook.getProperty(Properties.AUTHOR).orElse(""),
                    oldBook.getProperty(Properties.TITLE).orElse(""),
                    oldBook.getProperty(Properties.ISBN).orElse(""),
                    oldBook.getProperty(Properties.DESCRIPTION).orElse(""));
            updatedBook.setID(oldBook.getProperty(Properties.ID).orElse(-1));
            io.print("enter new author (or empty input to leave it unchanged): ");
            String author = io.getInput();
            if (!author.equals("")) {
                updatedBook.setAuthor(author);
            }
            io.print("enter new title (or empty input to leave it unchanged): ");
            String title = io.getInput();
            if (!title.equals("")) {
                updatedBook.setTitle(title);
            }
            io.print("enter new ISBN (or empty input to leave it unchanged): ");
            String isbn = io.getInput();
            if (!isbn.equals("")) {
                updatedBook.setISBN(isbn);
            }
            io.print("enter new description (or empty input to leave it unchanged): ");
            String description = io.getInput();
            if (!description.equals("")) {
                updatedBook.setDescription(description);
            }
            if (confirm("oletko varma, että haluat muokata lukuvinkkiä numero " + id_String + "?")) {
                if (bookDao.update(updatedBook)) {
                    io.print("\n");
                    io.print("vinkin muokkaaminen onnistui");
                    io.print("\n");
                } else {
                    io.print("\n");
                    io.print("vinkin muokkaaminen epäonnistui");
                    io.print("\n");
                }
            } else {
                io.print("\n");
                io.print("recommendation update canceled");
                io.print("\n");
            }
        } catch (IllegalArgumentException ex) {
            if (!id_String.equals("")) {
                io.print("\n");
                io.print("Not a valid ID. Has to be a number.");
                io.print("\n");
            }
        } catch (NullPointerException ex) {
            io.print("\n");
            io.print(ex.getMessage());
            io.print("\n");
        }
    }
}
