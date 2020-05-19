package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.domain.model.Book;
import ro.ubb.springjpa.domain.model.Client;
import ro.ubb.springjpa.domain.model.Purchase;
import ro.ubb.springjpa.domain.validators.BookstoreException;
import ro.ubb.springjpa.domain.validators.PurchaseValidator;
import ro.ubb.springjpa.domain.validators.ValidatorException;
import ro.ubb.springjpa.repository.PurchaseRepository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseValidator purchaseValidator;

    /**
     * Checks if the book and client ids related to the purchase exist
     * @param purchase instance of the class Purchase
     * @return True if both the Client and the Book related to the purchase exist, false otherwise
     */
    private Boolean checkBookClientAvailability(Purchase purchase) throws BookstoreException {
        log.trace("checkBookClientAvailability - method entered: purchase={}", purchase);
        Optional<Client> optionalClient = this.clientService.findOne(purchase.getClientId());
        Optional<Book> optionalBook = this.bookService.findOne(purchase.getBookId());
        boolean available = optionalClient.isPresent() && optionalBook.isPresent();
        log.trace("checkBookClientAvailability - method finished: available = {}", available);
        return available;
    }

    /**
     * Adds a purchase from the purchase repository
     * @param purchase instance of the class Purchase
     * @throws ValidatorException if the purchase is not valid
     * @throws BookstoreException if the book or client doesnt exist
     */
    public void add(Purchase purchase) throws BookstoreException, ValidatorException, SQLException
    {
        log.trace("add (Purchase) - method entered: purchase={}", purchase);
        if(checkBookClientAvailability(purchase)) {
            purchaseValidator.validate(purchase);
            this.purchaseRepository.save(purchase);
            log.trace("add (Purchase) - method finished");
        }
        else {
            log.trace("add (Purchase) - throw BookstoreException (invalid book id/client id)");
            throw new BookstoreException("Invalid book id and/or client id!\n");
        }
    }

    /**
     * Get purchase list set
     * @return the set containing all the purchases inside the purchase repository
     */
    public Set<Purchase> getPurchaseList() throws BookstoreException {
        log.trace("getPurchaseList - method entered");
        Iterable<Purchase> purchaseList = this.purchaseRepository.findAll();
        Set<Purchase> purchaseSet = StreamSupport.stream(purchaseList.spliterator(), false).collect(Collectors.toSet());
        log.trace("getPurchaseList - method finished: purchaseSet={}", purchaseSet);
        return purchaseSet;
    }

    /**
     * Updates a purchase from the purchase repository
     * @param purchase instance of the class Purchase
     * @throws ValidatorException if the purchase is not valid
     * @throws BookstoreException if the book or client doesnt exist
     */
    @Transactional
    public void updatePurchase(Purchase purchase) throws ValidatorException, BookstoreException {
        log.trace("updatePurchase - method entered: purchase={}", purchase);
        if(checkBookClientAvailability(purchase)) {
            purchaseValidator.validate(purchase);
            purchaseRepository.findById(purchase.getId())
                    .ifPresent(p ->
                    {
                    p.setBookId(purchase.getBookId());
                    p.setClientId(purchase.getBookId());
                    p.setLibrary(purchase.getLibrary());
                    log.trace("updatePurchase - updated: p={}", p);
                    });
            log.trace("updatePurchase - method finished");
        }
        else {
            log.trace("updatePurchase - throw BookstoreException (invalid book id/client id)");
            throw new BookstoreException("Invalid book id and/or client id!\n");
        }
    }

    /**
     * Deletes a purchase from the purchase repository
     * @param id integer representing the id of the purchase to be deleted
     */
    public void deletePurchase(Integer id) throws BookstoreException {
        log.trace("deletePurchase - method entered: id={}", id);
        this.purchaseRepository.deleteById(id);
        log.trace("deletePurchase - method finished");
    }

    /**
     * Function takes a clientId and deletes the purchase having the respective clientId
     * @param clientId integer representing the id of the client to be deleted
     */
    public void deletePurchaseWithClientID(int clientId) throws BookstoreException {
        log.trace("deletePurchaseWithClientID - method entered: clientId={}", clientId);
        this.purchaseRepository.deleteByClientId(clientId);
        log.trace("deletePurchaseWithClientID - method finished");
    }

    /**
     * Function takes a bookId and deletes the purchase having the respective bookId
     * @param bookId integer representing the id of the book to be deleted
     */
    public void deletePurchaseWithBookID(int bookId) throws BookstoreException {
        log.trace("deletePurchaseWithBookID - method entered: bookId={}", bookId);
        this.purchaseRepository.deleteByBookId(bookId);
        log.trace("deletePurchaseWithBookID - method finished");
    }

    /**
     * Returns the total amount of money a client has spent on books
     * @param clientId integer
     * @return a double representing the amount of money the client with clientId has spent
     */
    public double getMoneySpentForClient(int clientId) throws BookstoreException {
        log.trace("getMoneySpentForClient - method entered: clientId={}", clientId);
        double sum = this.purchaseRepository.findAll().stream()
                .filter(purchase -> purchase.getClientId() == clientId)
                .map(purchase -> purchase.getBookId())
                .map(bookId -> {
                    try {
                        return this.bookService.findOne(bookId);
                    } catch (BookstoreException e) {
                        e.printStackTrace();
                    }
                    return Optional.<Book>empty();
                })
                .map(bookOptional -> bookOptional.orElse(null))
                .filter(Objects::nonNull)
                .map(book -> book.getPrice())
                .mapToDouble(floatPrice -> Double.parseDouble(((Number) floatPrice).toString()))
                .sum();
        log.trace("getMoneySpentForClient - method finished: sum={}", sum);
        return sum;
    }

    /**
     * Returns top 3 clients, sorted based on amount of money spent.
     * @return a list containing 3 clients or less than 3 if there are less than 3 clients
     */
    public List<Map.Entry<Client, Double>> reportTop3ClientsBasedOnMoneySpent() throws BookstoreException {
        log.trace("reportTop3ClientsBasedOnMoneySpent - method entered");
        List<Map.Entry<Client, Double>> sortedClientsBasedOnMoneySpent = this.clientService.getClientList().stream()
                .map(client -> {
                    try {
                        return new AbstractMap.SimpleEntry<Client, Double> (client, getMoneySpentForClient(client.getId()));
                    } catch (BookstoreException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .sorted(Comparator.comparingDouble(entry ->  entry.getValue()))
                .collect(Collectors.toList());

        Collections.reverse(sortedClientsBasedOnMoneySpent);

        if(sortedClientsBasedOnMoneySpent.size() > 3)
        {
            List<Map.Entry<Client, Double>> sortedClients = sortedClientsBasedOnMoneySpent.subList(0,3);
            log.trace("reportTop3ClientsBasedOnMoneySpent - method finished: sortedClients={}", sortedClients);
            return sortedClients;
        }
        else
        {
            log.trace("reportTop3ClientsBasedOnMoneySpent - method finished: sortedClients={}", sortedClientsBasedOnMoneySpent);
            return sortedClientsBasedOnMoneySpent;
        }
    }

    /**
     * Returns how many times a book was sold.
     * @param bookId integer
     * @return an integer representing the sales of the book with bookId
     */
    public long getBookSales(int bookId) throws BookstoreException {
        log.trace("getBookSales - method entered: bookId={}", bookId);
        long count = this.purchaseRepository.countByBookId(bookId);
        log.trace("getBookSales - method finished: count={}", count);
        return count;
    }

    /**
     * Returns top 3 books, sorted based on sales.
     * @return a list containing 3 books or less than 3 if there are less than 3 books
     */
    public List<Map.Entry<Book, Long>> reportTop3BestSellingBooks() throws BookstoreException {
        log.trace("reportTop3BestSellingBooks - method entered");
        List<Map.Entry<Book, Long>> sortedBooksBasedOnSales = this.bookService.getBookList().stream()
                .map(book -> {
                    try {
                        return new AbstractMap.SimpleEntry<Book, Long> (book, getBookSales(book.getId()));
                    } catch (BookstoreException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .sorted(Comparator.comparingLong(entry -> entry.getValue()))
                .collect(Collectors.toList());

        Collections.reverse(sortedBooksBasedOnSales);

        if(sortedBooksBasedOnSales.size() > 3) {
            List<Map.Entry<Book, Long>> sortedBooks = sortedBooksBasedOnSales.subList(0, 3);
            log.trace("reportTop3BestSellingBooks - method finished: sortedBooks={}", sortedBooks);
            return sortedBooks;
        }
        else {
            log.trace("reportTop3BestSellingBooks - method finished: sortedBooks={}", sortedBooksBasedOnSales);
            return sortedBooksBasedOnSales;
        }
    }

    @Override
    @Transactional
    public void cascadeDeleteBook(int bookId) throws BookstoreException {
        this.deletePurchaseWithBookID(bookId);
        this.bookService.deleteBook(bookId);
    }

    @Override
    @Transactional
    public void cascadeDeleteClient(int clientId) throws BookstoreException {
        this.deletePurchaseWithClientID(clientId);
        this.clientService.deleteClient(clientId);
    }
}
