import java.util.ArrayList;

/**
 * A football competition, such as "World", "Europe" or "Premier League".
 * A competition can contain clubs directly and can also contain smaller
 * sub-competitions, which is what makes this class a recursive data
 * structure (a Competition contains Competitions).
 */
public class Competition {

    private String name;
    private ArrayList<Competition> subCompetitions;
    private ArrayList<Club> clubs;

    public Competition(String name) {
        validateName(name);
        this.name = name;
        this.subCompetitions = new ArrayList<Competition>();
        this.clubs = new ArrayList<Club>();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Competition name cannot be empty");
        }
        if (name.contains(",")) {
            throw new IllegalArgumentException("Competition name cannot contain a comma");
        }
    }

    public void addSubCompetition(Competition competition) {
        subCompetitions.add(competition);
    }

    public void addClub(Club club) {
        clubs.add(club);
    }

    /*
     * Each recursive method below combines this competition's own clubs
     * with the same information gathered from every sub-competition.
     */
    public Money totalMarketValue() {
        Money total = new Money(0);
        for (int i = 0; i < clubs.size(); i++) {
            total = total.add(clubs.get(i).squadValue());
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            total = total.add(subCompetitions.get(i).totalMarketValue());
        }
        return total;
    }

    public int countClubs() {
        int count = clubs.size();
        for (int i = 0; i < subCompetitions.size(); i++) {
            count += subCompetitions.get(i).countClubs();
        }
        return count;
    }

    public int countPlayers() {
        int count = 0;
        for (int i = 0; i < clubs.size(); i++) {
            count += clubs.get(i).getPlayerCount();
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            count += subCompetitions.get(i).countPlayers();
        }
        return count;
    }

    public Club findClub(String clubName) {
        for (int i = 0; i < clubs.size(); i++) {
            if (clubs.get(i).getName().equalsIgnoreCase(clubName)) {
                return clubs.get(i);
            }
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            Club found = subCompetitions.get(i).findClub(clubName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public Competition findCompetition(String competitionName) {
        if (name.equalsIgnoreCase(competitionName)) {
            return this;
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            Competition found = subCompetitions.get(i).findCompetition(competitionName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public Player mostValuablePlayer() {
        Player best = null;
        for (int i = 0; i < clubs.size(); i++) {
            best = betterPlayer(best, clubs.get(i).mostValuablePlayer());
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            best = betterPlayer(best, subCompetitions.get(i).mostValuablePlayer());
        }
        return best;
    }

    private Player betterPlayer(Player a, Player b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (b.getMarketValue().getAmount() > a.getMarketValue().getAmount()) {
            return b;
        }
        return a;
    }

    public ArrayList<Player> findPlayersByPosition(String position) {
        ArrayList<Player> result = new ArrayList<Player>();
        for (int i = 0; i < clubs.size(); i++) {
            result.addAll(clubs.get(i).getPlayersByPosition(position));
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            result.addAll(subCompetitions.get(i).findPlayersByPosition(position));
        }
        return result;
    }

    public ArrayList<Player> searchPlayersByName(String text) {
        ArrayList<Player> result = new ArrayList<Player>();
        String lowerText = text.toLowerCase();
        for (int i = 0; i < clubs.size(); i++) {
            ArrayList<Player> squad = clubs.get(i).getSquad();
            for (int k = 0; k < squad.size(); k++) {
                if (squad.get(k).getName().toLowerCase().contains(lowerText)) {
                    result.add(squad.get(k));
                }
            }
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            result.addAll(subCompetitions.get(i).searchPlayersByName(text));
        }
        return result;
    }

    public int depth() {
        if (subCompetitions.isEmpty()) {
            return 1;
        }
        int maxChildDepth = 0;
        for (int i = 0; i < subCompetitions.size(); i++) {
            int childDepth = subCompetitions.get(i).depth();
            if (childDepth > maxChildDepth) {
                maxChildDepth = childDepth;
            }
        }
        return 1 + maxChildDepth;
    }

    public String describe(int level) {
        String result = buildIndent(level) + name + "\n";
        for (int i = 0; i < clubs.size(); i++) {
            Club club = clubs.get(i);
            result += buildIndent(level + 1) + club.getName() + " (players: " + club.getPlayerCount()
                    + ", value: " + club.squadValue() + ")\n";
        }
        for (int i = 0; i < subCompetitions.size(); i++) {
            result += subCompetitions.get(i).describe(level + 1);
        }
        return result;
    }

    private String buildIndent(int level) {
        String indentText = "";
        for (int i = 0; i < level; i++) {
            indentText += "  ";
        }
        return indentText;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Competition> getSubCompetitions() {
        return subCompetitions;
    }

    public ArrayList<Club> getClubs() {
        return clubs;
    }
}
