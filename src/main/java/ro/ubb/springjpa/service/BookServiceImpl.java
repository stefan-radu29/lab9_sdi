package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.domain.model.Book;
import ro.ubb.springjpa.domain.validators.BookValidator;
import ro.ubb.springjpa.domain.validators.BookstoreException;
import ro.ubb.springjpa.domain.validators.ValidatorException;
import ro.ubb.springjpa.repository.BookRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    public static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookValidator bookValidator;

    /**
     * Add a book to the book repository.
     * @param book instance of class Book
     * @throws ValidatorException if the book is not valid
     */
    public void addBook(Book book) throws ValidatorException, BookstoreException, SQLException {
        log.trace("addBook - method entered: book={}", book);
        bookValidator.validate(book);
        this.bookRepository.save(book);
        log.trace("addBook - method finished");
    }

    /**
     * Get book list set.
     * @return the set containing all the books inside the book repository
     */
    public Set<Book> getBookList() throws BookstoreException {
        log.trace("getBookList - method entered");
        Iterable<Book> bookList = this.bookRepository.findAll();
        Set<Book> bookSet = StreamSupport.stream(bookList.spliterator(), false).collect(Collectors.toSet());
        log.trace("getBookList - method finished: bookSet={}", bookSet);
        return bookSet;
    }

    /**
     * Updates a book from the book repository
     * @param book instance of the class Book
     * @throws ValidatorException if the book is not valid
     */
    @Transactional
    public void updateBook(Book book) throws ValidatorException, BookstoreException {
        log.trace("updateBook - method entered: book={}", book);
        bookValidator.validate(book);
        this.bookRepository.findById(book.getId())
                .ifPresent(b ->
                {
                    b.setAuthor(book.getAuthor());
                    b.setPrice(book.getPrice());
                    b.setPublicationYear(book.getPublicationYear());
                    b.setPublisher(book.getPublisher());
                    b.setTitle(book.getTitle());
                    log.debug("updateClient - updated: b={}", b);
                });
        log.trace("updateBook - method finished");
    }

    /**
     * Deletes a book from the client repository
     * @param id integer representing the id of the book to be deleted
     */
    public void deleteBook(Integer id) throws BookstoreException {
        log.trace("deleteBook - method entered: id={}", id);
        this.bookRepository.deleteById(id);
        log.trace("deleteBook - method finished");
    }

    /**
     * Returns all the books that contain the searchString in either the title, the author name or the publisher
     * @param searchString string used for filtering the books
     * @return a HashSet containing books
     */
    public Set<Book> filter(String searchString) throws BookstoreException {
        log.trace("filter (Book) - method entered: searchString={}", searchString);
        Set<Book> books = this.getBookList();
        Set<Book> filteredBooks = new HashSet<Book>();

        books.forEach(book->{
            if (book.getTitle().contains(searchString) ||
                    book.getAuthor().contains(searchString) ||
                    book.getPublisher().contains(searchString))
                filteredBooks.add(book);
        });
        log.trace("filter (Book) - method finished: filteredBooks={}", filteredBooks);
        return filteredBooks;
    }

    public Optional<Book> findOne(Integer id) throws BookstoreException {
        return this.bookRepository.findById(id);
    }

}
