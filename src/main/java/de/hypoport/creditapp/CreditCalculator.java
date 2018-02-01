package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.CurrencyCalculator;
import de.hypoport.creditapp.currency.EuroAmount;
import de.hypoport.creditapp.validation.CreditRequestValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Holger on 26.01.2018.
 */
public class CreditCalculator {

    private final CurrencyCalculator currencyCalculator = new CurrencyCalculator();
    private final CreditRequestValidator creditRequestValidator = new CreditRequestValidator();
    private EuroAmount currentDebt;
    private EuroAmount repaymentRate;
    private final CreditRequest creditRequest;

    public CreditCalculator(CreditRequest creditRequest) {
        creditRequestValidator.validate(creditRequest);
        this.creditRequest = creditRequest;
        this.currentDebt = creditRequest.getCreditSum();
        this.repaymentRate = calculateRepaymentRate();
    }

    public CreditPlan calculate() {
        List<CreditPlanEntry> entries = new ArrayList<>();
        Calendar paymentCal = Calendar.getInstance();
        paymentCal.setTime(creditRequest.getPaymentDate());
        paymentCal.set(Calendar.DAY_OF_MONTH, paymentCal.getActualMaximum(Calendar.DAY_OF_MONTH));

        //no calculation needed if no transactions will ever take place
        if (!(creditRequest.getClearancePercentage().compareTo(BigDecimal.ZERO) == 0
                && creditRequest.getInterestPercentage().compareTo(BigDecimal.ZERO) == 0)) {
            //either the debt is 0 or the end of time is reached
            int rateAmount = creditRequest.getLifeSpanYears() * 12;
            for (int i = 0; i < rateAmount; i++) {
                paymentCal.add(Calendar.MONTH, 1);
                paymentCal.set(Calendar.DAY_OF_MONTH, paymentCal.getActualMaximum(Calendar.DAY_OF_MONTH));

                entries.add(createEntry(paymentCal.getTime()));
                if (currentDebt.equals(EuroAmount.ZERO)) {
                    break;
                }
            }
        }

        return new CreditPlan(creditRequest, entries, createSummary(entries));
    }

    private CreditPlanSummary createSummary(List<CreditPlanEntry> entries) {
        EuroAmount totalDebt = creditRequest.getCreditSum();
        EuroAmount totalClearance = EuroAmount.ZERO;
        EuroAmount totalRepaymentRate = EuroAmount.ZERO;
        EuroAmount totalInterest = EuroAmount.ZERO;
        for (CreditPlanEntry entry : entries) {
            totalDebt = currencyCalculator.subtract(totalDebt, entry.getClearance());
            totalClearance = currencyCalculator.add(totalClearance, entry.getClearance());
            totalRepaymentRate = currencyCalculator.add(totalRepaymentRate, entry.getRepaymentRate());
            totalInterest = currencyCalculator.add(totalInterest, entry.getInterest());
        }
        return new CreditPlanSummary(totalDebt, totalInterest, totalClearance, totalRepaymentRate);
    }

    private CreditPlanEntry createEntry(Date month) {
        //debt is smaller than repayment rate - add just it
        if (currencyCalculator.compare(currentDebt, repaymentRate) == -1) {
            repaymentRate = currentDebt;
        }

        EuroAmount monthlyInterest = currencyCalculator.round(calculateMonthlyInterest(currentDebt));
        EuroAmount monthlyClearance = currencyCalculator.subtract(repaymentRate, monthlyInterest);
        currentDebt = currencyCalculator.round(currencyCalculator.subtract(currentDebt, monthlyClearance));
        return new CreditPlanEntry(month, currentDebt, monthlyInterest, monthlyClearance, repaymentRate);
    }

    private EuroAmount calculateMonthlyInterest(EuroAmount euroAmount) {
        EuroAmount interest = currencyCalculator.percentage(euroAmount, creditRequest.getInterestPercentage());
        return new EuroAmount(interest.getValue().divide(new BigDecimal(12), 100, BigDecimal.ROUND_HALF_UP));
    }

    private EuroAmount calculateMonthlyClearance(EuroAmount euroAmount) {
        EuroAmount yearlyClearance = currencyCalculator.percentage(euroAmount, creditRequest.getClearancePercentage());
        return new EuroAmount(yearlyClearance.getValue().divide(new BigDecimal(12), 100, BigDecimal.ROUND_HALF_UP));
    }

    private EuroAmount calculateRepaymentRate() {
        EuroAmount monthlyInterest = calculateMonthlyInterest(creditRequest.getCreditSum());
        EuroAmount monthlyClearance = calculateMonthlyClearance(creditRequest.getCreditSum());
        return currencyCalculator.round(currencyCalculator.add(monthlyInterest, monthlyClearance));
    }
}
