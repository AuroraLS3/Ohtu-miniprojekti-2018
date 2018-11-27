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
            io.print("\ntoiminto: ");
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
                    Book book = selectBook();
                    if (book == null) { 
                        break;
                    }

                    String s_input = "";
                    while (!s_input.equals("return")) {
                        io.print("\ntoiminnot valitulle vinkille: \n\tedit \tmuokkaa valittua vinkkiä\n");
                        io.print("\tdelete \tpoista valittu vinkki\n");
                        io.print("\treturn \tlopeta vinkin tarkastelu\n");
                    
                        s_input = io.getInput();
                        switch (s_input) {
                            case "edit":
                                updateBook(book.getProperty(Properties.ID).orElse(null));
                                break;

                            case "delete":
                                deleteBook(book.getProperty(Properties.ID).orElse(null));
                                s_input = "return";
                                break;
                        
                            case "return":
                                io.print("\n");
                                break;
                        }

                    }

                    break;

                case "update":
                    updateBook(null);
                    break;
                case "delete":
                    deleteBook(null);
                    
                
                    break;
                default:
                    io.print("\nei tuettu toiminto \n");
            }
        }
    }
    private Book selectBook() throws SQLException {
        try {
            Integer ID = selectID();

            Book book = bookDao.findOne(ID);
            Check.notNull(book, () -> new NullPointerException("No book found"));
            io.print("\n" + book.getProperty(Properties.AUTHOR).orElse(null) + ": " + book.getProperty(Properties.TITLE).orElse(null) + ", ISBN: " 
                + book.getProperty(Properties.ISBN).orElse(null) + "\n"); 
            io.print("Description: " + book.getProperty(Properties.DESCRIPTION).orElse(null));
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
    private Integer selectID() {
        io.print("syötä olion id tai palaa jättämällä tyhjäksi\n");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            Integer ID = Integer.parseInt(id_String);
            return ID;
        } catch (IllegalArgumentException ex) {
            if (!id_String.equals("")) {
                io.print("Not a valid ID. Has to be a number.");
            }
        }
        return null;
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
                io.print("\n");
                io.print("vinkin poistaminen onnistui");
                io.print("\n");
            } else {
                io.print("\n");
                io.print("recommendation deletion canceled");
                io.print("\n");
            }
        } catch (NullPointerException ex) {
            io.print("\n");
            io.print(ex.getMessage());
            io.print("\n");
        }
    }

    private void updateBook(Integer knownID) throws SQLException {
        
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
            if (confirm("oletko varma, että haluat muokata lukuvinkkiä numero " + ID + "?")) {
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
        }  catch (NullPointerException ex) {
            io.print("\n");
            io.print(ex.getMessage());
            io.print("\n");
        }
    }
}
