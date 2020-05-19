package ro.ubb.springjpa.domain.validators;

import org.springframework.stereotype.Component;
import ro.ubb.springjpa.domain.model.Book;

/**
 * The type BookValidator.
 */
@Component
public class BookValidator implements Validator<Book>
{

    /**
     * Validates an object of type Book
     * @param object object representing an instance of class Book
     * @throws ValidatorException if the object cannot be validated
     */
    @Override
    public void validate(Book object) throws ValidatorException
    {
        String errorMessage = "Book Validation Error\n";
        if(object.getTitle() == null || object.getTitle().equals(""))
        {
            errorMessage += "Invalid title!\n";
        }
        if(object.getAuthor() == null || object.getAuthor().equals(""))
        {
            errorMessage += "Invalid author!\n";
        }
        if(object.getPublisher() == null || object.getPublisher().equals(""))
        {
            errorMessage +="Invalid publisher!\n";
        }
        if(object.getPublicationYear() <1950 || object.getPublicationYear()>2020)
        {
            errorMessage += "Invalid publication date!\n";
        }
        if(object.getPrice() <= 0)
        {
            errorMessage += "Invalid price!\n";
        }

        if(!errorMessage.equals("Book Validation Error\n"))
        {
            throw new ValidatorException(errorMessage);
        }
    }
}
