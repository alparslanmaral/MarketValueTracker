import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Saves and loads a whole Competition hierarchy to and from a single CSV
 * file. This is the only class that touches the file system.
 */
public class FileManager {

    public static void save(Competition root, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writeCompetition(root, "", writer);
        writer.close();
    }

    /*
     * Writes one competition line, then every club (and its players)
     * directly inside it, then recurses into each sub-competition so
     * that a parent always appears before its children in the file.
     */
    private static void writeCompetition(Competition competition, String parentName, BufferedWriter writer)
            throws IOException {
        writer.write("COMPETITION," + competition.getName() + "," + parentName);
        writer.newLine();

        ArrayList<Club> clubs = competition.getClubs();
        for (int i = 0; i < clubs.size(); i++) {
            Club club = clubs.get(i);
            writer.write("CLUB," + club.getName() + "," + club.getBudget().getAmount() + "," + competition.getName());
            writer.newLine();

            ArrayList<Player> squad = club.getSquad();
            for (int k = 0; k < squad.size(); k++) {
                Player player = squad.get(k);
                writer.write("PLAYER," + player.getName() + "," + player.getBirthDate() + ","
                        + player.getPosition() + "," + player.getMarketValue().getAmount() + "," + club.getName());
                writer.newLine();
            }
        }

        ArrayList<Competition> subCompetitions = competition.getSubCompetitions();
        for (int i = 0; i < subCompetitions.size(); i++) {
            writeCompetition(subCompetitions.get(i), competition.getName(), writer);
        }
    }

    /*
     * The file lists parents before children, so a single top-to-bottom
     * pass is enough: each line attaches itself to a competition or club
     * that has already been read.
     */
    public static Competition load(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        Competition root = null;
        String line = reader.readLine();
        while (line != null) {
            if (!line.trim().isEmpty()) {
                root = loadLine(line, root);
            }
            line = reader.readLine();
        }
        reader.close();
        return root;
    }

    private static Competition loadLine(String line, Competition root) {
        String[] parts = line.split(",");
        String type = parts[0];
        if (type.equals("COMPETITION")) {
            return loadCompetitionLine(parts, root);
        }
        if (type.equals("CLUB")) {
            loadClubLine(parts, root);
        }
        else if (type.equals("PLAYER")) {
            loadPlayerLine(parts, root);
        }
        return root;
    }

    private static Competition loadCompetitionLine(String[] parts, Competition root) {
        String name = parts[1];
        String parentName = parts.length > 2 ? parts[2] : "";
        Competition competition = new Competition(name);
        if (parentName.isEmpty()) {
            return competition;
        }
        Competition parent = root.findCompetition(parentName);
        parent.addSubCompetition(competition);
        return root;
    }

    private static void loadClubLine(String[] parts, Competition root) {
        String name = parts[1];
        long budgetAmount = Long.parseLong(parts[2]);
        String competitionName = parts[3];
        Club club = new Club(name, new Money(budgetAmount));
        Competition competition = root.findCompetition(competitionName);
        competition.addClub(club);
    }

    private static void loadPlayerLine(String[] parts, Competition root) {
        String name = parts[1];
        Date birthDate = Date.fromString(parts[2]);
        String position = parts[3];
        long marketValueAmount = Long.parseLong(parts[4]);
        String clubName = parts[5];
        Player player = new Player(name, birthDate, position, new Money(marketValueAmount));
        Club club = root.findClub(clubName);
        club.addPlayer(player);
    }
}
