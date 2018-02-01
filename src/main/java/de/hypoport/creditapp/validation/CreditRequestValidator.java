package de.hypoport.creditapp.validation;

import de.hypoport.creditapp.CreditRequest;
import de.hypoport.creditapp.currency.CurrencyCalculator;
import de.hypoport.creditapp.currency.EuroAmount;

import java.math.BigDecimal;


/**
 * Created by Holger on 31.01.2018.
 */
public class CreditRequestValidator {
    public static final EuroAmount MAX_CREDITSUM = new EuroAmount(new BigDecimal(10000000));
    public static final EuroAmount MIN_CREDITSUM = new EuroAmount(new BigDecimal(100000));

    public static final BigDecimal MIN_INTEREST = new BigDecimal(0);
    public static final BigDecimal MAX_INTEREST = new BigDecimal(100);

    public static final BigDecimal MIN_CLEARANCE = new BigDecimal(0);
    public static final BigDecimal MAX_CLEARANCE = new BigDecimal(100);

    public static final int MAX_LIFESPAN = 15;
    public static final int MIN_LIFESPAN = 1;

    private final CurrencyCalculator currencyCalculator = new CurrencyCalculator();

    public void validate(CreditRequest creditRequest) {
        EuroAmount creditSum = creditRequest.getCreditSum();
        check(creditSum == null, CreditRequestValidationErrorCode.CREDITSUM_REQUIRED);
        check(currencyCalculator.compare(creditSum, MIN_CREDITSUM) == -1, CreditRequestValidationErrorCode.MIN_CREDITSUM_REQUIRED);
        check(currencyCalculator.compare(creditSum, MAX_CREDITSUM) == 1, CreditRequestValidationErrorCode.MAX_CREDITSUM_EXCEEDED);

        check(creditRequest.getLifeSpanYears() < MIN_LIFESPAN, CreditRequestValidationErrorCode.MIN_LIFESPAN_REQUIRED);
        check(creditRequest.getLifeSpanYears() > MAX_LIFESPAN, CreditRequestValidationErrorCode.MAX_LIFESPAN_EXCEEDED);

        check(creditRequest.getPaymentDate() == null, CreditRequestValidationErrorCode.PAYMENT_DATE_REQUIRED);

        BigDecimal interestPercentage = creditRequest.getInterestPercentage();
        check(interestPercentage == null, CreditRequestValidationErrorCode.INTEREST_REQUIRED);
        check(interestPercentage.compareTo(MIN_INTEREST) == -1, CreditRequestValidationErrorCode.MIN_INTEREST_REQUIRED);
        check(interestPercentage.compareTo(MAX_INTEREST) == 1, CreditRequestValidationErrorCode.MAX_INTEREST_EXCEEDED);

        BigDecimal clearancePercentage = creditRequest.getClearancePercentage();
        check(clearancePercentage == null, CreditRequestValidationErrorCode.CLEARANCE_REQUIRED);
        check(clearancePercentage.compareTo(MIN_CLEARANCE) == -1, CreditRequestValidationErrorCode.MIN_CLEARANCE_REQUIRED);
        check(clearancePercentage.compareTo(MAX_CLEARANCE) == 1, CreditRequestValidationErrorCode.MAX_CLEARANCE_EXCEEDED);
    }

    private void check(boolean errorCondition, CreditRequestValidationErrorCode validationError) {
        if (errorCondition) {

            throw new CreditRequestValidationException(validationError);
        }
    }
}
