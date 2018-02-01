package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.EuroAmount;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Holger on 31.01.2018.
 */
public class CreditRequest {
    private final Date paymentDate;
    private final EuroAmount creditSum;
    private final int lifeSpanYears;
    private final BigDecimal interestPercentage;
    private final BigDecimal clearancePercentage;

    public CreditRequest(Date paymentDate, EuroAmount creditSum, int lifeSpanYears, BigDecimal interestPercentage, BigDecimal clearancePercentage) {
        this.paymentDate = paymentDate;
        this.creditSum = creditSum;
        this.lifeSpanYears = lifeSpanYears;
        this.interestPercentage = interestPercentage;
        this.clearancePercentage = clearancePercentage;
    }

    public EuroAmount getCreditSum() {
        return creditSum;
    }

    public int getLifeSpanYears() {
        return lifeSpanYears;
    }

    public BigDecimal getInterestPercentage() {
        return interestPercentage;
    }

    public BigDecimal getClearancePercentage() {
        return clearancePercentage;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }
}
