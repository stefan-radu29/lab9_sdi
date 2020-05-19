package ro.ubb.springjpa.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.springjpa.service.*;
import ro.ubb.springjpa.domain.model.Book;
import ro.ubb.springjpa.domain.model.Client;
import ro.ubb.springjpa.domain.model.Purchase;
import ro.ubb.springjpa.domain.validators.BookstoreException;
import ro.ubb.springjpa.domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Console.
 */
@Component
public class Console
{
    @Autowired
    private ClientService clientService;

    @Autowired
    private BookService bookService;

    @Autowired
    private PurchaseService purchaseService;

    /**
     * Run the program
     */
    public void run()
    {
        while (true) {
            try {
                String textMenu = "0. Exit.\n" +
                        "1. Add client.\n" +
                        "2. Add book.\n" +
                        "3. List all clients.\n" +
                        "4. List all books.\n" +
                        "5. Update client.\n" +
                        "6. Update book.\n" +
                        "7. Delete client.\n" +
                        "8. Delete book.\n" +
                        "9. Filter/Partial search clients.\n" +
                        "10. Filter/Partial search books.\n" +
                        "11. Purchase book.\n" +
                        "12. Update purchase\n" +
                        "13. Delete purchase\n" +
                        "14. Get purchases.\n" +
                        "15. Get top 3 clients based on amount of money spent.\n" +
                        "16. Get top 3 best-selling books.\n";
                System.out.println(textMenu);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String choice = reader.readLine();
                switch (choice) {
                    case ("0"):
                        return;
                    case ("1"): {
                        this.addClient();
                        break;
                    }
                    case ("2"): {
                        this.addBook();
                        break;
                    }
                    case ("3"): {
                        this.listAllClients();
                        break;
                    }
                    case ("4"): {
                        this.listAllBooks();
                        break;
                    }
                    case ("5"): {
                        this.updateClient();
                        break;
                    }
                    case("6"): {
                        this.updateBook();
                        break;
                    }
                    case ("7"): {
                        this.deleteClient();
                        break;
                    }
                    case("8"): {
                        this.deleteBook();
                        break;
                    }
                    case ("9"): {
                        this.filterClients();
                        break;
                    }
                    case("10"): {
                        this.filterBooks();
                        break;
                    }
                    case("11"): {
                        this.purchaseBook();
                        break;
                    }
                    case("12"): {
                        this.updatePurchase();
                        break;
                    }
                    case("13"): {
                        this.deletePurchase();
                        break;
                    }
                    case("14"): {
                        this.getPurchases();
                        break;
                    }
                    case("15"): {
                        this.getTop3ClientsBasedOnMoneySpent();
                        break;
                    }
                    case("16"): {
                        this.getTop3BestSellingBooks();
                        break;
                    }
                    default:
                        System.out.println("Not a valid choice!\n");
                }
            }
            catch(IOException | BookstoreException | ValidatorException | NumberFormatException | NullPointerException | SQLException exception)
            {
                System.out.println(exception.toString());
            }
        }
    }

    /**
     * Prints on the screen the top 3 books ordered based sales.
     */
    private void getTop3BestSellingBooks() throws BookstoreException {
        System.out.println("Top 3 books ordered based on sales:");

        AtomicInteger rank = new AtomicInteger(1);
        this.purchaseService.reportTop3BestSellingBooks().forEach(entry -> {
            System.out.println(rank + ". " + entry.getKey().toString() + " sold " + entry.getValue().toString() + " times.");
            rank.addAndGet(1);
        });

        System.out.println();
    }

    /**
     * Prints on the screen the top 3 clients ordered based on money spent.
     */
    private void getTop3ClientsBasedOnMoneySpent() throws BookstoreException {
        System.out.println("Top 3 clients ordered based on amount of money spent:");

        AtomicInteger rank = new AtomicInteger(1);
        this.purchaseService.reportTop3ClientsBasedOnMoneySpent().forEach(entry -> {
            System.out.println(rank + ". " + entry.getKey().toString() + " spent " + entry.getValue().toString());
            rank.addAndGet(1);
        });

        System.out.println();
    }

    /**
     * Prints on the screen all the purchases.
     */
    private void getPurchases() throws BookstoreException {
        Set<Purchase> allPurchases = this.purchaseService.getPurchaseList();
        allPurchases.forEach(System.out::println);
    }

    /**
     * Filters the books based on a string read from the keyboard
     * @throws IOException if there is an error concerning the reading of data from the console
     */
    private void filterBooks() throws IOException, BookstoreException {
        System.out.println("Search for books: ");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String searchString = reader.readLine();

        Set<Book> filteredBooks = this.bookService.filter(searchString);

        if (filteredBooks.size() != 0)
            filteredBooks.forEach(System.out::println);
        else
            System.out.println("No result!\n");
    }

    /**
     * Reads data from the keyboard and updates an existing book.
     * @throws IOException if there is an error concerning the reading of data from the console
     * @throws ValidatorException if the book created with data read from the console is not valid
     */
    private void updateBook() throws IOException, ValidatorException, BookstoreException {
        System.out.println("Book{id, title, author, publisher, publicationYear, price}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());
        String title = reader.readLine();
        String author = reader.readLine();
        String publisher = reader.readLine();
        int publicationYear = Integer.parseInt(reader.readLine());
        float price = Float.parseFloat(reader.readLine());

        Book newBook = new Book(title, author, publisher, publicationYear, price);
        newBook.setId(id);

        this.bookService.updateBook(newBook);

    }

    /**
     * Reads data from the keyboard and deletes the book having a certain id.
     * @throws IOException if there is an error concerning the reading of data from the console
     */
    private void deleteBook() throws IOException, BookstoreException {
        System.out.println("Book{id}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());

        this.purchaseService.cascadeDeleteBook(id);
    }

    /**
     * Filters the clients based on a string read from the keyboard (partial search).
     * @throws IOException if there is an error concerning the reading of data from the console
     */
    private void filterClients() throws IOException, BookstoreException {
        System.out.println("Search: ");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String searchString = reader.readLine();

        Set<Client> filteredClients = this.clientService.filter(searchString);

        if(filteredClients.size()!=0) {
            filteredClients.forEach(System.out::println);
        }
        else
        {
            System.out.println("No result!\n");
        }

    }

    /**
     * Reads data from the keyboard and deletes the client having a certain id.
     * @throws IOException if there is an error concerning the reading of data from the console
     */
    private void deleteClient() throws IOException, BookstoreException {
        System.out.println("Client{id}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());

        this.purchaseService.cascadeDeleteClient(id);
    }

    /**
     * Reads data from the keyboard and updates an existing client.
     * @throws IOException if there is an error concerning the reading of data from the console
     * @throws ValidatorException if the book created with data read from the console is not valid
     */
    private void updateClient() throws IOException, ValidatorException, BookstoreException {
        System.out.println("Client{id, firstName, lastName, address}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());
        String firstName = reader.readLine();
        String lastName = reader.readLine();
        String address = reader.readLine();

        Client newClient = new Client(firstName, lastName, address);
        newClient.setId(id);

        this.clientService.updateClient(newClient);
    }

    /**
     * Lists all books stored by the program
     */
    private void listAllBooks() throws BookstoreException {
        Set<Book> allBooks = this.bookService.getBookList();
        allBooks.forEach(System.out::println);
    }

    /**
     * Lists all clients stored by the program
     */
    private void listAllClients() throws BookstoreException {
        Set<Client> allClient = this.clientService.getClientList();
        allClient.forEach(System.out::println);
    }

    /**
     * Reads data from the keyboard, creates an object of type Book and stores it
     * @throws IOException if there is an error concerning the reading of data from the console
     * @throws ValidatorException if the book created with data read from the console is not valid
     * @throws BookstoreException
     */
    private void addBook() throws IOException, ValidatorException, BookstoreException, SQLException {
        System.out.println("Book{id, title, author, publisher, publicationYear, price}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());
        String title = reader.readLine();
        String author = reader.readLine();
        String publisher = reader.readLine();
        int publicationYear = Integer.parseInt(reader.readLine());
        float price = Float.parseFloat(reader.readLine());

        Book newBook = new Book(title, author, publisher, publicationYear, price);
        newBook.setId(id);

        this.bookService.addBook(newBook);
    }

    /**
     * Reads data from the keyboard, creates an object of type Client and stores it
     * @throws IOException if there is an error concerning the reading of data from the console
     * @throws ValidatorException if the client created with data read from the console is not valid
     * @throws BookstoreException
     */
    private void addClient() throws IOException, ValidatorException, BookstoreException, SQLException {
        System.out.println("Client{id, firstName, lastName, address}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());
        String firstName = reader.readLine();
        String lastName = reader.readLine();
        String address = reader.readLine();

        Client newClient = new Client(firstName, lastName, address);
        newClient.setId(id);

        this.clientService.addClient(newClient);
    }

    /**
     * Reads data from the keyboard, creates an object of type Purchase and stores it
     * @throws IOException if there is an error concerning the reading of data from the console
     * @throws ValidatorException if the client created with data read from the console is not valid
     * @throws BookstoreException
     */
    private void purchaseBook() throws IOException, BookstoreException, ValidatorException, SQLException {
        System.out.println("Client{id} Book{id} Purchase{library}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int idClient = Integer.parseInt(reader.readLine());
        int idBook = Integer.parseInt(reader.readLine());
        String library = reader.readLine();

        Purchase newPurchase = new Purchase(idClient, idBook, library);

        this.purchaseService.add(newPurchase);
    }

    /**
     * Reads data from the keyboard and updates an existing purchase
     * @throws IOException if there is an error concerning the reading of data from the console
     * @throws ValidatorException if the client created with data read from the console is not valid
     * @throws BookstoreException
     */
    private void updatePurchase() throws IOException, ValidatorException, BookstoreException {
        System.out.println("Purchase{id} Client{id} Book{id} Purchase{library}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());
        int idClient = Integer.parseInt(reader.readLine());
        int idBook = Integer.parseInt(reader.readLine());
        String library = reader.readLine();

        Purchase newPurchase = new Purchase(idClient, idBook, library);
        newPurchase.setId(id);

        this.purchaseService.updatePurchase(newPurchase);
    }

    private void deletePurchase() throws IOException, BookstoreException {
        System.out.println("Purchase{id}");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(reader.readLine());

        this.purchaseService.deletePurchase(id);
    }
}
