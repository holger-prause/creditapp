package de.hypoport.creditapp.io;

import de.hypoport.creditapp.CreditPlan;
import de.hypoport.creditapp.CreditPlanEntry;
import de.hypoport.creditapp.CreditPlanSummary;
import de.hypoport.creditapp.CreditRequest;
import de.hypoport.creditapp.currency.EuroAmount;

import java.text.SimpleDateFormat;

/**
 * Created by Holger on 01.02.2018.
 */
public class CreditPlanPrinter {
    private final String COLUMN_DISPLAY_FORMAT = "%-25s -%-15s %-15s %-30s %-15s";
    private final String[] header = new String[]{"Datum", "Restschuld", "Zinsen", "Tilgung(+) / Auszahlung(-)", "Rate"};
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public void print(CreditPlan creditPlan) {
        printHeader(creditPlan);
        creditPlan.getRepaymentPlanEntries().forEach(this::printEntry);
        printSummary(creditPlan);
    }

    private void printEntry(CreditPlanEntry creditPlanEntry) {
        String paymentDateAsString = DATE_FORMAT.format(creditPlanEntry.getPaymentDate());
        String[] columnValues = new String[]{
                paymentDateAsString,
                creditPlanEntry.getTotalDebt().toString(),
                creditPlanEntry.getInterest().toString(),
                creditPlanEntry.getClearance().toString(),
                creditPlanEntry.getRepaymentRate().toString()
        };
        System.out.println(String.format(COLUMN_DISPLAY_FORMAT, columnValues));
    }

    private void printHeader(CreditPlan creditPlan) {
        System.out.println(String.format(COLUMN_DISPLAY_FORMAT, header));

        CreditRequest creditRequest = creditPlan.getCreditRequest();
        String paymentDateAsString = DATE_FORMAT.format(creditRequest.getPaymentDate());
        String[] columnValues = new String[]{
                paymentDateAsString,
                creditRequest.getCreditSum().toString(),
                EuroAmount.ZERO.toString(),
                "-"+creditRequest.getCreditSum().toString(),
                "-"+creditRequest.getCreditSum().toString()
        };
        System.out.println(String.format(COLUMN_DISPLAY_FORMAT, columnValues));
    }

    private void printSummary(CreditPlan creditPlan) {
        CreditPlanSummary summary = creditPlan.getSummary();

        String[] columnValues = new String[]{
                "Zinsbindungsende",
                summary.getTotalDebt().toString(),
                summary.getTotalPaidInterest().toString(),
                summary.getTotalClearance().toString(),
                summary.getTotalRepaymentRate().toString(),
        };
        System.out.println(String.format(COLUMN_DISPLAY_FORMAT, columnValues));
    }
}
