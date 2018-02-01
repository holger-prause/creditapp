package de.hypoport.creditapp;

import java.util.List;

/**
 * Created by Holger on 26.01.2018.
 */
public class CreditPlan {
    private final List<CreditPlanEntry> repaymentPlanEntries;
    private final CreditPlanSummary summary;
    private final CreditRequest creditRequest;

    public CreditPlan(CreditRequest creditRequest, List<CreditPlanEntry> repaymentPlanEntries, CreditPlanSummary summary) {
        this.creditRequest = creditRequest;
        this.repaymentPlanEntries = repaymentPlanEntries;
        this.summary = summary;
    }

    public List<CreditPlanEntry> getRepaymentPlanEntries() {
        return repaymentPlanEntries;
    }

    public CreditPlanSummary getSummary() {
        return summary;
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }
}
