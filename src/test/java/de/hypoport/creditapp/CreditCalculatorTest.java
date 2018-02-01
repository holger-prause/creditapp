package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.EuroAmount;
import de.hypoport.creditapp.validation.CreditRequestValidationException;
import de.hypoport.creditapp.validation.CreditRequestValidator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Holger on 26.01.2018.
 */
public class CreditCalculatorTest {

    @Test
    public void zeroInterestAndClearance() {
        CreditRequest creditRequest
                = requestCredit(new Date(), CreditRequestValidator.MIN_CREDITSUM, CreditRequestValidator.MIN_LIFESPAN,
                new BigDecimal(0), new BigDecimal(0));

        CreditCalculator repaymentCalculator = new CreditCalculator(creditRequest);
        CreditPlan creditPlan = repaymentCalculator.calculate();

        assertEquals(creditRequest, creditPlan.getCreditRequest());
        assertEquals(0, creditPlan.getRepaymentPlanEntries().size());
        assertEquals(CreditRequestValidator.MIN_CREDITSUM, creditPlan.getSummary().getTotalDebt());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalClearance());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalPaidInterest());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalRepaymentRate());
    }

    @Test
    public void zeroInterestAnd10PercentClearance() {
        final EuroAmount creditSum = new EuroAmount(new BigDecimal(1000000));
        CreditRequest creditRequest
                = requestCredit(new Date(), creditSum, 1,
                new BigDecimal(0), new BigDecimal(10));

        CreditCalculator repaymentCalculator = new CreditCalculator(creditRequest);
        CreditPlan creditPlan = repaymentCalculator.calculate();

        assertEquals(creditRequest, creditPlan.getCreditRequest());
        assertEquals(12, creditPlan.getRepaymentPlanEntries().size());
        assertEquals(new EuroAmount(new BigDecimal("900000.04")), creditPlan.getSummary().getTotalDebt());
        assertEquals(new EuroAmount(new BigDecimal("99999.96")), creditPlan.getSummary().getTotalClearance());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalPaidInterest());
        assertEquals(new EuroAmount(new BigDecimal("99999.96")), creditPlan.getSummary().getTotalRepaymentRate());
    }

    @Test
    public void tenPercentInterestAndZeroClearance() {
        final EuroAmount creditSum = new EuroAmount(new BigDecimal(1000000));
        CreditRequest creditRequest
                = requestCredit(new Date(), creditSum, 1,
                new BigDecimal(10), new BigDecimal(0));

        CreditCalculator repaymentCalculator = new CreditCalculator(creditRequest);
        CreditPlan creditPlan = repaymentCalculator.calculate();

        assertEquals(creditRequest, creditPlan.getCreditRequest());
        assertEquals(12, creditPlan.getRepaymentPlanEntries().size());
        assertEquals(creditSum, creditPlan.getSummary().getTotalDebt());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalClearance());
        assertEquals(new EuroAmount(new BigDecimal("99999.96")), creditPlan.getSummary().getTotalPaidInterest());
        assertEquals(new EuroAmount(new BigDecimal("99999.96")), creditPlan.getSummary().getTotalRepaymentRate());
    }

    @Test
    public void endReachedByRepayment() {
        final EuroAmount creditSum = new EuroAmount(new BigDecimal(1000000));
        CreditRequest creditRequest
                = requestCredit(new Date(), creditSum, 10,
                new BigDecimal(0), new BigDecimal(50));

        CreditCalculator repaymentCalculator = new CreditCalculator(creditRequest);
        CreditPlan creditPlan = repaymentCalculator.calculate();
        assertEquals(24, creditPlan.getRepaymentPlanEntries().size());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalDebt());
        assertEquals(creditSum, creditPlan.getSummary().getTotalClearance());
        assertEquals(EuroAmount.ZERO, creditPlan.getSummary().getTotalPaidInterest());
        assertEquals(creditSum, creditPlan.getSummary().getTotalRepaymentRate());
    }

    @Test(expected = CreditRequestValidationException.class)
    public void validation() {
        final EuroAmount creditSum = new EuroAmount(new BigDecimal(1));
        CreditRequest creditRequest
                = requestCredit(new Date(), creditSum, 1,
                new BigDecimal(10), new BigDecimal(0));
        CreditCalculator repaymentCalculator = new CreditCalculator(creditRequest);
        repaymentCalculator.calculate();
    }

    private CreditRequest requestCredit(Date date, EuroAmount creditSum, int lifeSpanYears, BigDecimal interestPercentage, BigDecimal clearancePercentage) {
        return new CreditRequest(date, creditSum, lifeSpanYears, interestPercentage, clearancePercentage);
    }
}
