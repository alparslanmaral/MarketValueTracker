import static org.junit.Assert.*;
import org.junit.Test;

public class DateTest {

    @Test
    public void testAgeOnBirthdayAlreadyPassedThisYear() {
        Date birthDate = new Date(1, 1, 2000);
        Date today = new Date(15, 6, 2026);
        assertEquals(26, birthDate.ageOn(today));
    }

    @Test
    public void testAgeOnBirthdayNotYetReachedThisYear() {
        Date birthDate = new Date(25, 12, 2000);
        Date today = new Date(15, 6, 2026);
        assertEquals(25, birthDate.ageOn(today));
    }

    @Test
    public void testAgeOnExactlyToday() {
        Date birthDate = new Date(15, 6, 2000);
        Date today = new Date(15, 6, 2026);
        assertEquals(26, birthDate.ageOn(today));
    }

    @Test
    public void testAgeOnLeapYearBirthdayFeb29() {
        Date birthDate = new Date(29, 2, 2000);
        Date today = new Date(1, 3, 2026);
        assertEquals(26, birthDate.ageOn(today));
    }

    @Test
    public void testDaysInMonthLeapYear() {
        assertEquals(29, Date.daysInMonth(2, 2024));
    }

    @Test
    public void testDaysInMonthNonLeapYear() {
        assertEquals(28, Date.daysInMonth(2, 2023));
    }

    @Test
    public void testIsLeapYearCenturyRule() {
        assertFalse(Date.isLeapYear(1900));
        assertTrue(Date.isLeapYear(2000));
        assertTrue(Date.isLeapYear(2024));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidDayFeb30ThrowsException() {
        new Date(30, 2, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidMonth13ThrowsException() {
        new Date(1, 13, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidDayZeroThrowsException() {
        new Date(0, 5, 2023);
    }

    @Test
    public void testFromStringAndToStringRoundTrip() {
        Date date = Date.fromString("2001-11-02");
        assertEquals("2001-11-02", date.toString());
        assertEquals(2, date.getDay());
        assertEquals(11, date.getMonth());
        assertEquals(2001, date.getYear());
    }

    @Test
    public void testIsBefore() {
        Date earlier = new Date(1, 1, 2000);
        Date later = new Date(2, 1, 2000);
        assertTrue(earlier.isBefore(later));
        assertFalse(later.isBefore(earlier));
    }
}
