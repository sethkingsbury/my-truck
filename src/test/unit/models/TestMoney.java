package unit.models;

import models.Money;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;

public class TestMoney {

    @Test
    public void testCreation() {
        Money money1 = new Money(12, 34);

        assertEquals(money1.getDollars(), 12);
        assertEquals(money1.getCents(), 34);
    }

    @Test
    public void testCreationFromFloat() {
        Money money1 = new Money((float) 12.34);

        assertEquals(money1.getDollars(), 12);
        assertEquals(money1.getCents(), 34);
    }

    @Test
    public void testGetAsCents() {
        Money money1 = new Money(12, 34);

        assertEquals(money1.getAsCents(), 1234);
    }

    @ParameterizedTest
    @ValueSource(doubles = {12.34, 0.53, 5.00, 93.15, 0.66, 2.66})
    public void testAddMoney(double testVal) {
        BigDecimal value = new BigDecimal(testVal);
        BigDecimal base = new BigDecimal(12.34);

        Money money1 = new Money(12, 34);
        Money money2 = new Money(value.floatValue());

        money1.add(money2);
        int actual = (base.add(value)).multiply(new BigDecimal(100)).round(MathContext.DECIMAL32).intValue();

        assertEquals(money1.getAsCents(), actual);
    }

    @ParameterizedTest
    @ValueSource(doubles = {12.34, 0.53, 5.00, 93.15, 0.34, 0.35, 0.33})
    public void testSubtractMoney(double testVal) {
        BigDecimal value = new BigDecimal(testVal);
        BigDecimal base = new BigDecimal(12.34);

        Money money1 = new Money(12, 34);
        Money money2 = new Money(value.floatValue());

        money1.subtract(money2);
        int actual = (base.subtract(value)).multiply(new BigDecimal(100)).round(MathContext.DECIMAL32).intValue();
        if (actual < 0) {
            actual = 0;
        }

        assertEquals(money1.getAsCents(), actual);
    }
}
