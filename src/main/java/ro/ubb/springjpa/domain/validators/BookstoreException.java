package ro.ubb.springjpa.domain.validators;

/**
 * The type BookstoreException.
 */
public class BookstoreException extends Exception
{
    /**
     * Instantiates a new BookstoreException.
     *
     * @param errorMessage the error message
     */
    public BookstoreException(String errorMessage)
    {
        super(errorMessage);
    }
}
