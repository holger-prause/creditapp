package de.hypoport.creditapp.currency;

import java.math.BigDecimal;

/**
 * Created by Holger on 26.01.2018.
 */
public class CurrencyCalculator {

    public EuroAmount add(EuroAmount a, EuroAmount b) {
        return new EuroAmount(a.getValue().add(b.getValue()));
    }

    public EuroAmount subtract(EuroAmount a, EuroAmount b) {
        return new EuroAmount(a.getValue().subtract(b.getValue()));
    }

    public EuroAmount round(EuroAmount euroAmount) {
        return new EuroAmount(euroAmount.getValue().setScale(euroAmount.getCurrency().getDefaultFractionDigits(), BigDecimal.ROUND_HALF_UP));
    }

    public int compare(EuroAmount a, EuroAmount b) {
        return a.getValue().compareTo(b.getValue());
    }

    public EuroAmount percentage(EuroAmount euroAmount, BigDecimal percentage) {
        if (percentage.signum() == -1) {
            throw new IllegalArgumentException("Percentage must positive");
        }

        BigDecimal value = euroAmount.getValue().multiply(percentage).divide(new BigDecimal(100));
        return new EuroAmount(value);
    }
}
