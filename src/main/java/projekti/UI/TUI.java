package projekti.UI;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import projekti.db.BookDAO;
import projekti.domain.Book;

public class TUI {
    private Scanner scanner;
    private BookDAO bookDao; // voidaan antaa konstruktorin parametrina
    public TUI(BookDAO bd) { 
        scanner = new Scanner(System.in);
        bookDao = bd;
       
    }

    public void run() throws SQLException {
        System.out.println("Tervetuloa lukuvinkkiapplikaatioon!\n");
        System.out.println("Tuetut toiminnot:\n ");
        System.out.println("\tnew \tlisää uusi lukuvinkki");
        System.out.println("\tall \tlistaa kaikki lukuvinkit");
        
        System.out.println("\tend \tsulkee ohjelman");
        String input = "";
        while (!input.equals("end")) {
            System.out.println("\ntoiminto: ");
            input = scanner.nextLine();
            switch (input) { // kaikki toiminnot voidaan refaktoroida omaksi metodikseen myöhemmin
                case "new": //luodaan uusi vinkki tietokantaan
                    //Käyttäjä valitsee vinkin tyyypin, nyt vain kirjat tuettu
                    createBook();
                    continue;

                case "all": //listataan kaikki vinkit tietokannasta;
                    List<Book> books = bookDao.findAll();
                    // tulostusasun voisi määrittää kirjan toStringnä
                    books.forEach(s -> System.out.println(s.getAuthor() + ": " + s.getTitle() + ", ISBN: " + s.getISBN()));
                    continue;

                case "end": 
                    System.out.println("\nlopetetaan ohjelman suoritus");
                    continue;
            }

            System.out.println("\nei tuettu toiminto");
        }   
    }
    private void createBook() throws SQLException {
        System.out.print("kirjailija: ");
        String author = scanner.nextLine();

        System.out.print("nimi: ");
        String title = scanner.nextLine();

        System.out.print("ISBN: ");
        String ISBN = scanner.nextLine();

        Book book = new Book(author, title, ISBN);

        if (!bookDao.create(book).equals(null)) {
            System.out.println("\nuusi vinkki lisätty");
        } else {
            System.out.println("\nvinkkiä ei lisätty");
        }
        // oletetaan toistaiseksi, että onnistuu. Daon kanssa ongelmia. System.out.println("\nuutta vinkkiä ei lisätty");
        
    }
}