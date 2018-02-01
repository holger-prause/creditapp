package de.hypoport.creditapp.validation;

/**
 * Created by Holger on 31.01.2018.
 */
public enum CreditRequestValidationErrorCode {
    CREDITSUM_REQUIRED(100),
    MIN_CREDITSUM_REQUIRED(101),
    MAX_CREDITSUM_EXCEEDED(102),
    MIN_LIFESPAN_REQUIRED(103),
    MAX_LIFESPAN_EXCEEDED(104),

    CLEARANCE_REQUIRED(105),
    MIN_CLEARANCE_REQUIRED(106),
    MAX_CLEARANCE_EXCEEDED(107),

    INTEREST_REQUIRED(108),
    MIN_INTEREST_REQUIRED(109),
    MAX_INTEREST_EXCEEDED(110),

    PAYMENT_DATE_REQUIRED(111);

    private final long value;

    CreditRequestValidationErrorCode(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}