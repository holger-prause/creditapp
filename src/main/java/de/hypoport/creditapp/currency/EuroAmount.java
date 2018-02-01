package de.hypoport.creditapp.currency;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Holger on 26.01.2018.
 */
public class EuroAmount {

    public final static EuroAmount ZERO = new EuroAmount(new BigDecimal("0.00"));

    private final CurrencyCalculator currencyCalculator = new CurrencyCalculator();
    private final Currency currency = Currency.getInstance("EUR");
    private final BigDecimal value;

    public EuroAmount(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EuroAmount that = (EuroAmount) o;
        return currencyCalculator.compare(this, that) == 0;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
