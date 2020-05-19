package ro.ubb.springjpa.domain.validators;

/**
 * The type ValidatorException.
 */
public class ValidatorException extends Exception
{
    /**
     * Instantiates a new ValidatorException.
     *
     * @param errorMessage the error message
     */
    public ValidatorException(String errorMessage)
    {
        super(errorMessage);
    }
}
