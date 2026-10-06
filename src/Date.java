/**
 * A simple calendar date (day, month, year). Dates are immutable: once
 * created, a Date always refers to the same day.
 */
public class Date {

    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        if (year <= 0) {
            throw new IllegalArgumentException("Year must be positive: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12: " + month);
        }
        int maxDay = daysInMonth(month, year);
        if (day < 1 || day > maxDay) {
            throw new IllegalArgumentException("Invalid day " + day + " for month " + month + " of year " + year);
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static boolean isLeapYear(int year) {
        boolean divisibleBy4 = year % 4 == 0;
        boolean divisibleBy100 = year % 100 == 0;
        boolean divisibleBy400 = year % 400 == 0;
        return (divisibleBy4 && !divisibleBy100) || divisibleBy400;
    }

    public static int daysInMonth(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12: " + month);
        }
        int days = DAYS_IN_MONTH[month - 1];
        if (month == 2 && isLeapYear(year)) {
            days = 29;
        }
        return days;
    }

    /*
     * Age is the number of birthdays already reached. If this year's
     * birthday has not happened yet on the given date, we subtract one.
     */
    public int ageOn(Date today) {
        int age = today.year - this.year;
        boolean birthdayNotReachedYet = (today.month < this.month)
                || (today.month == this.month && today.day < this.day);
        if (birthdayNotReachedYet) {
            age--;
        }
        return age;
    }

    public boolean isBefore(Date other) {
        if (this.year != other.year) {
            return this.year < other.year;
        }
        if (this.month != other.month) {
            return this.month < other.month;
        }
        return this.day < other.day;
    }

    public static Date fromString(String text) {
        String[] parts = text.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return new Date(day, month, year);
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
