package com.food.ordering.system.domain.valueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isGreaterThanZero(){
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money otherMmoney){
        return this.amount != null && this.amount.compareTo(otherMmoney.getAmount()) > 0;
    }

    public Money add(Money augend){
        return new Money(setScale(this.amount.add(augend.getAmount())));
    }

    public Money subtract(Money subtrahend){
        return new Money(setScale(this.amount.subtract(subtrahend.getAmount())));
    }

    public Money multiply(int multiplier){
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    /**
     * With this method we can set the scale of an input.
     * When we set the sclae after a big decimal, the result will be on that scale too
     * Treats repeating numbers too
     * @param input
     * @return
     */
    private BigDecimal setScale(BigDecimal input){
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
