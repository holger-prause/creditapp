package de.hypoport.creditapp;

import de.hypoport.creditapp.currency.CurrencyCalculator;
import de.hypoport.creditapp.currency.EuroAmount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Holger on 26.01.2018.
 */
public class CurrencyCalculatorTest {

    private CurrencyCalculator currencyCalculator;
    private List<EuroAmount> compareValues;

    @Before
    public void before() {
        currencyCalculator = new CurrencyCalculator();
        compareValues = createValues("-12.333", "-1", "-0.1", "-0.01", "-0.001", "0", "0.001", "0.01", "0.1", "1", "12.333");
    }

    @Test
    public void add() {
        TestPlan testPlan = new TestPlan();
        testPlan.add(new EuroAmount(BigDecimal.ZERO), new EuroAmount(BigDecimal.ZERO), new EuroAmount(BigDecimal.ZERO));
        testPlan.add(new EuroAmount(BigDecimal.ZERO), new EuroAmount(BigDecimal.ONE), new EuroAmount(BigDecimal.ONE));
        testPlan.add(new EuroAmount(new BigDecimal("0.44")), new EuroAmount(new BigDecimal("0.44")), new EuroAmount(new BigDecimal("0.88")));
        testPlan.add(new EuroAmount(new BigDecimal("4535.346768444567")),
                new EuroAmount(new BigDecimal("32323.76767767")),
                new EuroAmount(new BigDecimal("36859.114446114567")));

        for (TestPlanEntry testPlanEntry : testPlan.entries) {
            assertEquals(testPlanEntry.expected, currencyCalculator.add(testPlanEntry.a, testPlanEntry.b));
        }
    }

    @Test
    public void subtract() {
        TestPlan testPlan = new TestPlan();
        testPlan.add(new EuroAmount(BigDecimal.ZERO), new EuroAmount(BigDecimal.ZERO), new EuroAmount(BigDecimal.ZERO));
        testPlan.add(new EuroAmount(BigDecimal.ZERO), new EuroAmount(BigDecimal.ONE), new EuroAmount(new BigDecimal(-1)));
        testPlan.add(new EuroAmount(new BigDecimal("0.44")), new EuroAmount(new BigDecimal("0.44")), new EuroAmount(new BigDecimal("0.00")));

        testPlan.add(new EuroAmount(new BigDecimal("4535.346768444567")),
                new EuroAmount(new BigDecimal("3234.76767767")),
                new EuroAmount(new BigDecimal("1300.579090774567")));

        testPlan.add(new EuroAmount(new BigDecimal("3234.76767767")),
                new EuroAmount(new BigDecimal("4535.346768444567")),
                new EuroAmount(new BigDecimal("-1300.579090774567")));

        for (TestPlanEntry testPlanEntry : testPlan.entries) {
            assertEquals(testPlanEntry.expected, currencyCalculator.subtract(testPlanEntry.a, testPlanEntry.b));
        }
    }

    @Test
    public void round() {
        TestPlan testPlan = new TestPlan();
        //will round to scale of 2
        testPlan.add(new EuroAmount(BigDecimal.ZERO), null, new EuroAmount(new BigDecimal("0.00")));
        testPlan.add(new EuroAmount(new BigDecimal("0.00")), null, new EuroAmount(new BigDecimal("0.00")));
        testPlan.add(new EuroAmount(new BigDecimal("4324.49")), null, new EuroAmount(new BigDecimal("4324.49")));
        testPlan.add(new EuroAmount(new BigDecimal("4324.56")), null, new EuroAmount(new BigDecimal("4324.56")));
        testPlan.add(new EuroAmount(new BigDecimal("-4324.49")), null, new EuroAmount(new BigDecimal("-4324.49")));
        testPlan.add(new EuroAmount(new BigDecimal("-4324.56")), null, new EuroAmount(new BigDecimal("-4324.56")));
        testPlan.add(new EuroAmount(new BigDecimal("4324.561")), null, new EuroAmount(new BigDecimal("4324.56")));
        testPlan.add(new EuroAmount(new BigDecimal("4324.565")), null, new EuroAmount(new BigDecimal("4324.57")));
        testPlan.add(new EuroAmount(new BigDecimal("-4324.561")), null, new EuroAmount(new BigDecimal("-4324.56")));
        testPlan.add(new EuroAmount(new BigDecimal("-4324.565")), null, new EuroAmount(new BigDecimal("-4324.57")));

        for (TestPlanEntry testPlanEntry : testPlan.entries) {
            assertEquals(testPlanEntry.expected, currencyCalculator.round(testPlanEntry.a));
            assertEquals(2, currencyCalculator.round(testPlanEntry.a).getValue().scale());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void percentageNegative() {
        currencyCalculator.percentage(new EuroAmount(BigDecimal.ONE), new BigDecimal(-1));
    }

    @Test
    public void compareSmallerThan() {
        //compareValues are inserted ascending by value
        for (int i = 0; i < compareValues.size(); i++) {
            EuroAmount a = compareValues.get(i);
            for (int j = i + 1; j < compareValues.size(); j++) {
                EuroAmount b = compareValues.get(j);
                assertEquals(-1, currencyCalculator.compare(a, b));
            }
        }
    }

    @Test
    public void compareBiggerThan() {
        //compareValues are inserted ascending by value
        Collections.reverse(compareValues);
        for (int i = 0; i < compareValues.size(); i++) {
            EuroAmount a = compareValues.get(i);
            for (int j = i + 1; j < compareValues.size(); j++) {
                EuroAmount b = compareValues.get(j);
                assertEquals(1, currencyCalculator.compare(a, b));
            }
        }
    }

    @Test
    public void compareEquals() {
        //compareValues are inserted ascending by value
        compareValues.stream().forEach(e -> assertTrue(currencyCalculator.compare(e, e) == 0));
        assertEquals(0, currencyCalculator.compare(createValue("0"), createValue("0.0")));
        assertEquals(0, currencyCalculator.compare(createValue("0"), createValue("0.00")));
        assertEquals(0, currencyCalculator.compare(createValue("0"), createValue("0.000")));
    }

    @Test
    public void percentage() {
        TestPlan testPlan = new TestPlan();
        //1st argument is the amount
        //2nd argument is the percentage
        testPlan.add(new EuroAmount(new BigDecimal(0)), new EuroAmount(new BigDecimal(0)), new EuroAmount(new BigDecimal(0)));
        testPlan.add(new EuroAmount(new BigDecimal("0.00")), new EuroAmount(new BigDecimal("0.00")), new EuroAmount(new BigDecimal("0.0000")));
        testPlan.add(new EuroAmount(new BigDecimal(100)), new EuroAmount(new BigDecimal(10)), new EuroAmount(new BigDecimal(10)));
        testPlan.add(new EuroAmount(new BigDecimal(100)), new EuroAmount(new BigDecimal("2.23")), new EuroAmount(new BigDecimal("2.23")));
        testPlan.add(new EuroAmount(new BigDecimal("100.22")), new EuroAmount(new BigDecimal("2.23")), new EuroAmount(new BigDecimal("2.234906")));
        testPlan.add(new EuroAmount(new BigDecimal("100.2265654")), new EuroAmount(new BigDecimal("2.23654")),
                new EuroAmount(new BigDecimal("2.24160722579716")));

        for (TestPlanEntry testPlanEntry : testPlan.entries) {
            assertEquals(testPlanEntry.expected, currencyCalculator.percentage(testPlanEntry.a, testPlanEntry.b.getValue()));
        }
    }

    private class TestPlan {
        private final List<TestPlanEntry> entries = new ArrayList<>();

        public void add(EuroAmount a, EuroAmount b, EuroAmount expected) {
            entries.add(new TestPlanEntry(a, b, expected));
        }
    }

    private class TestPlanEntry {
        private final EuroAmount a;
        private final EuroAmount b;
        private final EuroAmount expected;

        public TestPlanEntry(EuroAmount a, EuroAmount b, EuroAmount expected) {
            this.a = a;
            this.b = b;
            this.expected = expected;
        }
    }

    private List<EuroAmount> createValues(String... values) {
        return Arrays.asList(values).stream()
                .map(this::createValue).collect(Collectors.toList());
    }

    private EuroAmount createValue(String value) {
        return new EuroAmount(new BigDecimal(value));
    }
}
