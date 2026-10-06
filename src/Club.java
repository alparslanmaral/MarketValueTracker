import java.util.ArrayList;

/**
 * A football club: a name, a transfer budget, and a squad of players.
 */
public class Club {

    private String name;
    private Money budget;
    private ArrayList<Player> squad;

    public Club(String name, Money budget) {
        validateName(name);
        this.name = name;
        this.budget = budget;
        this.squad = new ArrayList<Player>();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Club name cannot be empty");
        }
        if (name.contains(",")) {
            throw new IllegalArgumentException("Club name cannot contain a comma");
        }
    }

    public void addPlayer(Player player) {
        if (findPlayer(player.getName()) != null) {
            throw new IllegalArgumentException("Player already in squad: " + player.getName());
        }
        squad.add(player);
    }

    public Player removePlayer(String playerName) {
        for (int i = 0; i < squad.size(); i++) {
            if (squad.get(i).getName().equalsIgnoreCase(playerName)) {
                return squad.remove(i);
            }
        }
        return null;
    }

    public Player findPlayer(String playerName) {
        for (int i = 0; i < squad.size(); i++) {
            if (squad.get(i).getName().equalsIgnoreCase(playerName)) {
                return squad.get(i);
            }
        }
        return null;
    }

    public Money squadValue() {
        Money total = new Money(0);
        for (int i = 0; i < squad.size(); i++) {
            total = total.add(squad.get(i).getMarketValue());
        }
        return total;
    }

    public int getPlayerCount() {
        return squad.size();
    }

    public ArrayList<Player> getPlayersByPosition(String position) {
        ArrayList<Player> result = new ArrayList<Player>();
        for (int i = 0; i < squad.size(); i++) {
            Player player = squad.get(i);
            if (player.getPosition().equalsIgnoreCase(position)) {
                result.add(player);
            }
        }
        return result;
    }

    public Player mostValuablePlayer() {
        if (squad.isEmpty()) {
            return null;
        }
        Player best = squad.get(0);
        for (int i = 1; i < squad.size(); i++) {
            Player candidate = squad.get(i);
            if (candidate.getMarketValue().getAmount() > best.getMarketValue().getAmount()) {
                best = candidate;
            }
        }
        return best;
    }

    public void setBudget(Money newBudget) {
        this.budget = newBudget;
    }

    public String getName() {
        return name;
    }

    public Money getBudget() {
        return budget;
    }

    public ArrayList<Player> getSquad() {
        return squad;
    }

    @Override
    public String toString() {
        return name + " - budget: " + budget + " - players: " + squad.size();
    }
}
