/**
 * An amount of money in whole euros. Money is immutable: add and subtract
 * always return a new Money instead of changing the existing one.
 */
public class Money {

    private static final long THOUSAND = 1000L;
    private static final long MILLION = 1000000L;

    private long amount;

    public Money(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative: " + amount);
        }
        this.amount = amount;
    }

    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money subtract(Money other) {
        long result = this.amount - other.amount;
        if (result < 0) {
            throw new IllegalArgumentException("Cannot subtract " + other + " from " + this);
        }
        return new Money(result);
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.amount >= other.amount;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        if (amount == 0) {
            return "EUR 0";
        }
        if (amount >= MILLION) {
            return formatMillions();
        }
        if (amount >= THOUSAND) {
            return formatThousands();
        }
        return "EUR " + amount;
    }

    private String formatMillions() {
        double millions = amount / (double) MILLION;
        return String.format("EUR %.2fm", millions);
    }

    private String formatThousands() {
        double thousands = amount / (double) THOUSAND;
        return String.format("EUR %.0fk", thousands);
    }
}
