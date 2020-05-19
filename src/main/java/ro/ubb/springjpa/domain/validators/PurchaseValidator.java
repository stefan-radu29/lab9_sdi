package ro.ubb.springjpa.domain.validators;

import org.springframework.stereotype.Component;
import ro.ubb.springjpa.domain.model.Purchase;

/**
 * The type PurchaseValidator
 */
@Component
public class PurchaseValidator implements Validator<Purchase> {

    /**
     * Validates an object of type purchase
     * @param purchase object representing an instance of class Purchase
     * @throws ValidatorException exception thrown if the object cannot be validated
     */
    @Override
    public void validate(Purchase purchase) throws ValidatorException{
        String errorMessage = "Purchase Validation Error\n";
        if (purchase.getClientId() == 0)
            errorMessage += "Invalid Client Id";
        if (purchase.getBookId() == 0)
            errorMessage += "Invalid Book Id";
        if (purchase.getLibrary() == null || purchase.getLibrary().equals(""))
            errorMessage += "Invalid library";
        if(!errorMessage.equals("Purchase Validation Error\n"))
            throw new ValidatorException(errorMessage);
    }
}
