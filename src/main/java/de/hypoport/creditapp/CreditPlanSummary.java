package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.EuroAmount;

/**
 * Created by Holger on 31.01.2018.
 */
public class CreditPlanSummary {
    private final EuroAmount totalDebt;
    private final EuroAmount totalPaidInterest;
    private final EuroAmount totalClearance;
    private final EuroAmount totalRepaymentRate;

    public CreditPlanSummary(EuroAmount totalDebt, EuroAmount totalPaidInterest, EuroAmount totalClearance, EuroAmount totalRepaymentRate) {
        this.totalDebt = totalDebt;
        this.totalPaidInterest = totalPaidInterest;
        this.totalClearance = totalClearance;
        this.totalRepaymentRate = totalRepaymentRate;
    }

    public EuroAmount getTotalDebt() {
        return totalDebt;
    }

    public EuroAmount getTotalPaidInterest() {
        return totalPaidInterest;
    }

    public EuroAmount getTotalClearance() {
        return totalClearance;
    }

    public EuroAmount getTotalRepaymentRate() {
        return totalRepaymentRate;
    }
}
