package projekti.UI;

import java.sql.SQLException;
import java.util.List;
import projekti.db.Dao;
import projekti.domain.Book;

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
                default:
                    io.print("\nei tuettu toiminto \n");
            }

        }
    }

    private void createBook() throws SQLException {
        io.print("kirjailija: ");
        String author = io.getInput();

        io.print("nimi: ");
        String title = io.getInput();

        io.print("ISBN: ");
        String ISBN = io.getInput();
        Book book = null;
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
}
