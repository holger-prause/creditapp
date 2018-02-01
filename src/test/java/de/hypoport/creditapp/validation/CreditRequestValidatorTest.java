package de.hypoport.creditapp.validation;

import de.hypoport.creditapp.CreditRequest;
import de.hypoport.creditapp.currency.CurrencyCalculator;
import de.hypoport.creditapp.currency.EuroAmount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Holger on 31.01.2018.
 */
public class CreditRequestValidatorTest {
    private CreditRequestValidator creditRequestValidator;
    private CurrencyCalculator currencyCalculator;
    private CreditRequest request;

    @Before
    public void before() {
        creditRequestValidator = new CreditRequestValidator();
        currencyCalculator = new CurrencyCalculator();
        request = new CreditRequest(Calendar.getInstance().getTime(), CreditRequestValidator.MIN_CREDITSUM, CreditRequestValidator.MIN_LIFESPAN,
                CreditRequestValidator.MIN_INTEREST, CreditRequestValidator.MIN_CLEARANCE);
    }

    @Test
    public void validateCreditsumNull() {
        CreditRequest creditRequest = changeCreditsum(null, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.CREDITSUM_REQUIRED);
    }

    @Test
    public void validateCreditsumTooSmall() {
        EuroAmount toSmall = currencyCalculator.subtract(CreditRequestValidator.MIN_CREDITSUM, new EuroAmount(new BigDecimal("0.00001")));
        CreditRequest creditRequest = changeCreditsum(toSmall, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MIN_CREDITSUM_REQUIRED);
    }

    @Test
    public void validateCreditsumTooBig() {
        EuroAmount toBig = currencyCalculator.add(CreditRequestValidator.MAX_CREDITSUM, new EuroAmount(new BigDecimal("0.00001")));
        CreditRequest creditRequest = changeCreditsum(toBig, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MAX_CREDITSUM_EXCEEDED);
    }

    @Test
    public void validateLifespanTooSmall() {
        CreditRequest creditRequest = changeLifespan(CreditRequestValidator.MIN_LIFESPAN - 1, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MIN_LIFESPAN_REQUIRED);
    }

    @Test
    public void validateLifespanTooBig() {
        CreditRequest creditRequest = changeLifespan(CreditRequestValidator.MAX_LIFESPAN + 1, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MAX_LIFESPAN_EXCEEDED);
    }

    @Test
    public void validateInterestNull() {
        CreditRequest creditRequest = changeInterest(null, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.INTEREST_REQUIRED);
    }

    @Test
    public void validateInterestTooSmall() {
        BigDecimal toSmall = CreditRequestValidator.MIN_INTEREST.subtract(new BigDecimal("0.00001"));
        CreditRequest creditRequest = changeInterest(toSmall, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MIN_INTEREST_REQUIRED);
    }

    @Test
    public void validateInterestTooBig() {
        BigDecimal toBig = CreditRequestValidator.MAX_INTEREST.add(new BigDecimal("0.00001"));
        CreditRequest creditRequest = changeInterest(toBig, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MAX_INTEREST_EXCEEDED);
    }

    @Test
    public void validateClearanceNull() {
        CreditRequest creditRequest = changeClearance(null, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.CLEARANCE_REQUIRED);
    }

    @Test
    public void validateClearanceTooSmall() {
        BigDecimal toSmall = CreditRequestValidator.MIN_CLEARANCE.subtract(new BigDecimal("0.00001"));
        CreditRequest creditRequest = changeClearance(toSmall, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MIN_CLEARANCE_REQUIRED);
    }

    @Test
    public void validateClearanceTooBig() {
        BigDecimal toBig = CreditRequestValidator.MAX_CLEARANCE.add(new BigDecimal("0.00001"));
        CreditRequest creditRequest = changeClearance(toBig, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.MAX_CLEARANCE_EXCEEDED);
    }

    @Test
    public void validatePaymentDateNull() {
        CreditRequest creditRequest = changePaymentDate(null, request);
        assertValidationException(creditRequest, CreditRequestValidationErrorCode.PAYMENT_DATE_REQUIRED);
    }

    @Test
    public void validateMin() {
        creditRequestValidator.validate(request);
    }

    @Test
    public void validateMax() {
        request = new CreditRequest(Calendar.getInstance().getTime(), CreditRequestValidator.MAX_CREDITSUM, CreditRequestValidator.MAX_LIFESPAN,
                CreditRequestValidator.MAX_INTEREST, CreditRequestValidator.MAX_CLEARANCE);
        creditRequestValidator.validate(request);
    }

    //change stuff based on valid request so you dont have to adopt all test cases in case of changing constants
    private CreditRequest changeCreditsum(EuroAmount euroAmount, CreditRequest oldRequest) {
        return new CreditRequest(oldRequest.getPaymentDate(), euroAmount, oldRequest.getLifeSpanYears(), oldRequest.getInterestPercentage(), oldRequest.getClearancePercentage());
    }

    private CreditRequest changeLifespan(int lifespan, CreditRequest oldRequest) {
        return new CreditRequest(oldRequest.getPaymentDate(), oldRequest.getCreditSum(), lifespan, oldRequest.getInterestPercentage(), oldRequest.getClearancePercentage());
    }

    private CreditRequest changeInterest(BigDecimal interest, CreditRequest oldRequest) {
        return new CreditRequest(oldRequest.getPaymentDate(), oldRequest.getCreditSum(), oldRequest.getLifeSpanYears(), interest, oldRequest.getClearancePercentage());
    }

    private CreditRequest changeClearance(BigDecimal clearance, CreditRequest oldRequest) {
        return new CreditRequest(oldRequest.getPaymentDate(), oldRequest.getCreditSum(), oldRequest.getLifeSpanYears(), oldRequest.getInterestPercentage(), clearance);
    }

    private CreditRequest changePaymentDate(Date paymentDate, CreditRequest oldRequest) {
        return new CreditRequest(paymentDate, oldRequest.getCreditSum(), oldRequest.getLifeSpanYears(), oldRequest.getInterestPercentage(), oldRequest.getClearancePercentage());
    }

    private void assertValidationException(CreditRequest creditRequest, CreditRequestValidationErrorCode expected) {
        boolean thrown = false;
        try {
            creditRequestValidator.validate(creditRequest);
        } catch (CreditRequestValidationException e) {
            thrown = true;
            assertEquals(e.getValidationError(), expected);
        }

        assertTrue(thrown);
    }
}
