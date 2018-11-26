package projekti.UI;

import java.sql.SQLException;
import java.util.List;
import projekti.db.Dao;
import projekti.domain.Book;
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
                    io.print("\nlopetetaan ohjelman suoritus");
                    break;

                case "select":
                    Book book = selectBook();
                    String s_input ="";
                    io.print("\tedit \tmuokkaa valittua vinkkiä\n");
                    io.print("\tdelete \tpoista valittu vinkki\n");
                    io.print("\treturn \tlopeta vinkin tarkastelu\n");
                    while(!s_input.equals("return")){

                        s_input = io.getInput();
                        switch(s_input) {
                            case "edit":
                            break;

                            case "delete":
                            break;
                        
                            case "return":
                            io.print("\n");
                            break;

                    }
                }
                    break;
                default:
                    io.print("\nei tuettu toiminto \n");
            }

        }
    }
    private Book selectBook() throws SQLException {
        io.print("syötä olion id tai palaa jättämällä tyhjäksi\n");
        io.print("ID: ");
        String id_String = io.getInput();
        try {
            Integer ID = Integer.parseInt(id_String);
        
            Book book = bookDao.findOne(ID);
            Check.notNull(book, () -> new NullPointerException("No book found"));
            io.print(book.getAuthor() + ": " + book.getTitle() + ", ISBN: " + book.getISBN() + "\n"); //myöhemmin myös kuvaus
            return book;

        } catch (IllegalArgumentException ex) {
            if (!id_String.equals("")) {
                io.print("\n Not a valid ID. Has to be a number.");
            }
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
        Book book;
        try {
            book = new Book(author, title, ISBN);
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
}
