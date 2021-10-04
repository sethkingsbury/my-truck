package models;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Money class for dealing with money calculations. Makes sure no rounding errors occur
 */
public class Money {

    /**
     * Amount in the dollars column
     */
    private int dollars;
    /**
     * Amount in the cents column
     */
    private int cents;

    /**
     * Constructor for a given float amount
     *
     * @param amount float
     */
    public Money(float amount) {
        setAmountFloat(amount);
    }


    /**
     * Constructor for given collars and cents
     *
     * @param dollars int dollars
     * @param cents int cents
     */
    public Money(int dollars, int cents) {
        this.dollars = dollars;
        this.cents = cents;
    }

    public Money(int amount) {
        setAmountInt(amount);
    }

    /**
     * Sets the amount of money to the given float
     *
     * @param amount float dollars.cents
     */
    public void setAmountFloat(float amount) {
        BigDecimal newAmount = new BigDecimal(amount);
        int amountInt = newAmount.round(MathContext.DECIMAL32).multiply(new BigDecimal(100)).intValue();
        this.dollars = amountInt / 100;
        this.cents = amountInt - (dollars * 100);
    }

    public void setAmountInt(int amount) {
        this.dollars = amount / 100;
        this.cents = amount - (dollars * 100);
    }

    public boolean equals(int compare) {
        return compare == (dollars * 100 + cents);
    }

    /**
     * Returns a new money object created from the given float
     *
     * @param amount float dollars.cents
     * @return Money object
     */
    public Money convertToMoney(float amount) {
        return new Money(amount);
    }

    /**
     * Adds the given money onto this money
     *
     * @param money2
     */
    public void add(Money money2) {
        dollars += money2.getDollars();
        cents += money2.getCents();
        if (cents >= 100) {
            dollars += 1;
            cents -= 100;
        }
    }

    /**
     * Adds the given float amount onto this money
     *
     * @param amount
     */
    public void add(float amount) {
        Money amountMoney = convertToMoney(amount);
        add(amountMoney);
    }

    /**
     * Subtracts the given money from this money
     *
     * @param money2
     */
    public void subtract(Money money2) {
        dollars -= money2.getDollars();
        cents -= money2.getCents();
        if (cents < 0) {
            dollars -= 1;
            cents += 100;
        }
        if (dollars < 0) {
            dollars = 0;
            cents = 0;
        }
    }

    /**
     * Subtracts the given float amount from this money
     *
     * @param amount
     */
    public void subtract(float amount) {
        Money amountMoney = new Money(amount);
        subtract(amountMoney);
    }

    public static Money multiply(Money money1, int multiplier) {
        int totalDollars = money1.getDollars() * multiplier;
        int totalCents = money1.getCents() * multiplier;

        if (totalCents >= 100) {
            totalDollars += totalCents / 100;
            totalCents -= (totalCents / 100) * 100;
        }

        return new Money(totalDollars, totalCents);
    }

    /**
     * Returns the total as an int number of cents
     *
     * @return
     */
    public int getAsCents(){
        return (dollars*100 + cents);
    }

    /**
     * Returns the total as a float
     *
     * @return
     */
    public float getAsFloat(){
        return (new Float(getAsCents()) / 100);
    }

    /**
     * Gets the cents in the cents column
     *
     * @return
     */
    public int getCents() {
        return cents;
    }

    /**
     * Gets the amount in the dollars column
     *
     * @return
     */
    public int getDollars() {
        return dollars;
    }

    /**
     * Print string in dollars.cents format
     *
     * @return
     */
    @Override
    public String toString() {
        String printString;
        if (cents == 0) {
            printString = String.format("$%d.00", dollars);
        } else {
            printString = String.format("$%d.%d", dollars, cents);
        }
        return printString;
    }
}
