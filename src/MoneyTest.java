import static org.junit.Assert.*;
import org.junit.Test;

public class MoneyTest {

    @Test
    public void testAddTwoAmounts() {
        Money a = new Money(1000);
        Money b = new Money(2500);
        assertEquals(3500, a.add(b).getAmount());
    }

    @Test
    public void testSubtractToExactlyZero() {
        Money a = new Money(5000);
        Money b = new Money(5000);
        assertEquals(0, a.subtract(b).getAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractResultingNegativeThrowsException() {
        Money a = new Money(100);
        Money b = new Money(200);
        a.subtract(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeAmountThrowsException() {
        new Money(-1);
    }

    @Test
    public void testToStringZero() {
        assertEquals("EUR 0", new Money(0).toString());
    }

    @Test
    public void testToStringThousands() {
        assertEquals("EUR 500k", new Money(500000).toString());
    }

    @Test
    public void testToStringMillions() {
        assertEquals("EUR 45.00m", new Money(45000000).toString());
    }

    @Test
    public void testToStringLargeValue() {
        assertEquals("EUR 2500.00m", new Money(2500000000L).toString());
    }

    @Test
    public void testIsGreaterThanOrEqualTrueWhenLarger() {
        Money a = new Money(500);
        Money b = new Money(100);
        assertTrue(a.isGreaterThanOrEqual(b));
    }

    @Test
    public void testIsGreaterThanOrEqualTrueWhenEqual() {
        Money a = new Money(500);
        Money b = new Money(500);
        assertTrue(a.isGreaterThanOrEqual(b));
    }

    @Test
    public void testIsGreaterThanOrEqualFalseWhenSmaller() {
        Money a = new Money(100);
        Money b = new Money(500);
        assertFalse(a.isGreaterThanOrEqual(b));
    }
}
