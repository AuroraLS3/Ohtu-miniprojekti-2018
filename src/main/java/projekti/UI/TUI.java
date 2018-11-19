package projekti.UI;

import java.util.Scanner;

import projekti.db.BookDAO;
import projekti.domain.Book;

public class TUI {
    private Scanner scanner;
    private BookDAO bookDao; // voidaan antaa konstruktorin parametrina
    public TUI() { 
        scanner = new Scanner(System.in);
        
    }

    public void run() {
        System.out.println("Tervetuloa lukuvinkkiapplikaatioon!\n");
        System.out.println("Tuetut toiminnot:\n ");
        System.out.println("\tnew \tlisää uusi lukuvinkki");
        System.out.println("\tall \tlistaa kaikki lukuvinkit");
        
        System.out.println("\tend \tsulkee ohjelman");
        String input = "";
        while (!input.equals("end")) {
            System.out.println("toiminto: ");
            input = scanner.nextLine();
            switch (input) {
                case "new": //luodaan uusi vinkki tietokantaan
                    String type = "BOOK";

                    System.out.print("kirjailija: ");
                    String author = scanner.nextLine();

                    System.out.print("nimi: ");
                    String title = scanner.nextLine();

                    System.out.print("ISBN: ");
                    String ISBN = scanner.nextLine();

                    Book kirja = new Book(author, title, ISBN);
                    System.out.println("\nuusi vinkki lisätty");
                    continue;

                case "all": //listataan kaikki vinkit tietokannasta;
                    
                    System.out.println("\nkaikki vinkit listattu");
                    continue;

                case "end": 
                    System.out.println("\nlopetetaan ohjelman suoritus");
                    continue;
            }

            System.out.println("\nei tuettu toiminto");
        }   
    }
}