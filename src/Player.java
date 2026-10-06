import java.util.ArrayList;

/**
 * A single football player belonging to a club's squad.
 */
public class Player {

    private static final ArrayList<String> VALID_POSITIONS = new ArrayList<String>();
    static {
        VALID_POSITIONS.add("GK");
        VALID_POSITIONS.add("DEF");
        VALID_POSITIONS.add("MID");
        VALID_POSITIONS.add("FWD");
    }

    private String name;
    private Date birthDate;
    private String position;
    private Money marketValue;

    public Player(String name, Date birthDate, String position, Money marketValue) {
        validateName(name);
        String normalizedPosition = validatePosition(position);
        this.name = name;
        this.birthDate = birthDate;
        this.position = normalizedPosition;
        this.marketValue = marketValue;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        if (name.contains(",")) {
            throw new IllegalArgumentException("Player name cannot contain a comma");
        }
    }

    private String validatePosition(String position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        String upperPosition = position.toUpperCase();
        if (!VALID_POSITIONS.contains(upperPosition)) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        return upperPosition;
    }

    public int getAge(Date today) {
        return birthDate.ageOn(today);
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPosition() {
        return position;
    }

    public Money getMarketValue() {
        return marketValue;
    }

    @Override
    public String toString() {
        return name + " (" + position + ") - born " + birthDate + " - value " + marketValue;
    }
}
