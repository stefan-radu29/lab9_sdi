package ro.ubb.springjpa.domain.validators;

import org.springframework.stereotype.Component;
import ro.ubb.springjpa.domain.model.Client;

/**
 * The type ClientValidator.
 */
@Component
public class ClientValidator implements Validator<Client>
{
    /**
     * Validates an object of type Client
     * @param object object representing an instance of class Client
     * @throws ValidatorException exception thrown if the object cannot be validated
     */
    @Override
    public void validate(Client object) throws ValidatorException
    {
        String errorMessage = "Client Validation Error\n";
        if(object.getFirstName() == null || object.getFirstName().equals(""))
        {
            errorMessage += "Invalid first name!\n";
        }
        if(object.getLastName() == null || object.getLastName().equals(""))
        {
            errorMessage += "Invalid last name!\n";
        }
        if(object.getAddress() == null || object.getAddress().equals("") || !object.getAddress().contains("@"))
        {
            errorMessage += "Invalid address!\n";
        }
        if(!errorMessage.equals("Client Validation Error\n"))
        {
            throw new ValidatorException(errorMessage);
        }
    }
}
