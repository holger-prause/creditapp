package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.EuroAmount;

import java.util.Date;

/**
 * Created by Holger on 16.11.2017.
 */
public class CreditPlanEntry {
    private final Date paymentDate;
    private final EuroAmount totalDebt;
    private final EuroAmount interest;
    private final EuroAmount clearance;
    private final EuroAmount repaymentRate;

    public CreditPlanEntry(Date paymentDate, EuroAmount totalDebt, EuroAmount interest, EuroAmount clearance, EuroAmount repaymentRate) {
        this.paymentDate = paymentDate;
        this.totalDebt = totalDebt;
        this.interest = interest;
        this.clearance = clearance;
        this.repaymentRate = repaymentRate;
    }

    public EuroAmount getTotalDebt() {
        return totalDebt;
    }

    public EuroAmount getInterest() {
        return interest;
    }

    public EuroAmount getClearance() {
        return clearance;
    }

    public EuroAmount getRepaymentRate() {
        return repaymentRate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }
}
