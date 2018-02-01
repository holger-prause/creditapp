package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.EuroAmount;
import de.hypoport.creditapp.io.CreditPlanPrinter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Holger on 01.02.2018.
 */
public class Main {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static void main(String[] args) throws ParseException {
        EuroAmount creditSum = new EuroAmount(BigDecimal.valueOf(100000));
        int lifeSpanYears = 10;
        BigDecimal interestPercentage = new BigDecimal(2.12);
        BigDecimal clearancePercentage = new BigDecimal(2);
        Date startDate = DATE_FORMAT.parse("30.11.2015");

        CreditRequest creditRequest
                = new CreditRequest(startDate, creditSum, lifeSpanYears,
                interestPercentage, clearancePercentage);
        CreditCalculator creditCalculator = new CreditCalculator(creditRequest);
        CreditPlan creditPlan = creditCalculator.calculate();

        CreditPlanPrinter creditPlanPrinter = new CreditPlanPrinter();
        creditPlanPrinter.print(creditPlan);
    }
}
