package ro.ubb.springjpa.domain.validators;

/**
 * The interface Validator.
 *
 * @param <T> the type parameter
 */
public interface Validator<T>
{
    /**
     * Validates an object of type T
     * @param object object representing an instance of class T
     * @throws ValidatorException exception thrown if the object cannot be validated
     */
    void validate(T object) throws ValidatorException;
}
