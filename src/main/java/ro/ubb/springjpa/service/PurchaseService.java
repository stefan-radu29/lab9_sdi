package ro.ubb.springjpa.service;

import ro.ubb.springjpa.domain.model.Book;
import ro.ubb.springjpa.domain.model.Client;
import ro.ubb.springjpa.domain.model.Purchase;
import ro.ubb.springjpa.domain.validators.BookstoreException;
import ro.ubb.springjpa.domain.validators.ValidatorException;

import java.sql.SQLException;
import java.util.*;

public interface PurchaseService
{
    /**
     * Adds a purchase from the purchase repository
     * @param purchase instance of the class Purchase
     * @throws ValidatorException if the purchase is not valid
     * @throws BookstoreException if the book or client doesnt exist
     */
    public void add(Purchase purchase) throws BookstoreException, ValidatorException, SQLException;

    /**
     * Get purchase list set
     * @return the set containing all the purchases inside the purchase repository
     */
    public Set<Purchase> getPurchaseList() throws BookstoreException;

    /**
     * Updates a purchase from the purchase repository
     * @param purchase instance of the class Purchase
     * @throws ValidatorException if the purchase is not valid
     * @throws BookstoreException if the book or client doesnt exist
     */
    public void updatePurchase(Purchase purchase) throws ValidatorException, BookstoreException;

    /**
     * Deletes a purchase from the purchase repository
     * @param id integer representing the id of the purchase to be deleted
     */
    public void deletePurchase(Integer id) throws BookstoreException;

    /**
     * Function takes a clientId and deletes the purchase having the respective clientId
     * @param clientId integer representing the id of the client to be deleted
     */
    public void deletePurchaseWithClientID(int clientId) throws BookstoreException;

    /**
     * Function takes a bookId and deletes the purchase having the respective bookId
     * @param bookId integer representing the id of the book to be deleted
     */
    public void deletePurchaseWithBookID(int bookId) throws BookstoreException;

    /**
     * Returns the total amount of money a client has spent on books
     * @param clientId integer
     * @return a double representing the amount of money the client with clientId has spent
     */
    public double getMoneySpentForClient(int clientId) throws BookstoreException;

    /**
     * Returns top 3 clients, sorted based on amount of money spent.
     * @return a list containing 3 clients or less than 3 if there are less than 3 clients
     */
    public List<Map.Entry<Client, Double>> reportTop3ClientsBasedOnMoneySpent() throws BookstoreException;

    /**
     * Returns how many times a book was sold.
     * @param bookId integer
     * @return an integer representing the sales of the book with bookId
     */
    public long getBookSales(int bookId) throws BookstoreException;

    /**
     * Returns top 3 books, sorted based on sales.
     * @return a list containing 3 books or less than 3 if there are less than 3 books
     */
    public List<Map.Entry<Book, Long>> reportTop3BestSellingBooks() throws BookstoreException;

    void cascadeDeleteBook(int bookId) throws BookstoreException;

    void cascadeDeleteClient(int clientId) throws BookstoreException;
}
