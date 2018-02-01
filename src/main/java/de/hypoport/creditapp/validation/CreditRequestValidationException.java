package de.hypoport.creditapp.validation;


/**
 * Created by Holger on 31.01.2018.
 */
public class CreditRequestValidationException extends RuntimeException {
    private final CreditRequestValidationErrorCode validationError;

    public CreditRequestValidationException(CreditRequestValidationErrorCode validationError) {
        super(validationError.toString());
        this.validationError = validationError;
    }

    public CreditRequestValidationErrorCode getValidationError() {
        return validationError;
    }
}
