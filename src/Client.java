import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

/**
 * The driver class. Shows a text menu that demonstrates every feature of
 * the football club and transfer database.
 */
public class Client {

    private static final String DATA_FILE = "data/football.csv";

    private Competition root;
    private TransferMarket market;
    private Scanner scanner;

    public Client() {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        loadInitialData();
        market = new TransferMarket(root);

        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = readMenuChoice();
            exit = handleChoice(choice);
        }
        scanner.close();
    }

    private void loadInitialData() {
        try {
            root = FileManager.load(DATA_FILE);
        }
        catch (IOException e) {
            System.out.println("Could not load " + DATA_FILE + ", using built-in sample data instead.");
            root = buildDefaultHierarchy();
        }
    }

    private Competition buildDefaultHierarchy() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        Competition england = new Competition("England");
        Competition premierLeague = new Competition("Premier League");
        world.addSubCompetition(europe);
        europe.addSubCompetition(england);
        england.addSubCompetition(premierLeague);

        Club exampleClub = new Club("Example FC", new Money(250000000L));
        Player examplePlayer = new Player("Example Player", new Date(17, 5, 2000), "FWD", new Money(60000000L));
        exampleClub.addPlayer(examplePlayer);
        premierLeague.addClub(exampleClub);

        return world;
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Show competition tree");
        System.out.println("2. Show totals for a competition");
        System.out.println("3. Search players by name");
        System.out.println("4. List players by position");
        System.out.println("5. Show the most valuable player");
        System.out.println("6. Make a transfer");
        System.out.println("7. Save to file");
        System.out.println("8. Load from file");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private int readMenuChoice() {
        String line = scanner.nextLine();
        int choice;
        try {
            choice = Integer.parseInt(line.trim());
        }
        catch (NumberFormatException e) {
            choice = -1;
        }
        return choice;
    }

    private boolean handleChoice(int choice) {
        if (choice == 1) {
            showTree();
        }
        else if (choice == 2) {
            showTotals();
        }
        else if (choice == 3) {
            searchByName();
        }
        else if (choice == 4) {
            listByPosition();
        }
        else if (choice == 5) {
            showMostValuable();
        }
        else if (choice == 6) {
            makeTransfer();
        }
        else if (choice == 7) {
            saveToFile();
        }
        else if (choice == 8) {
            loadFromFile();
        }
        else if (choice == 0) {
            return true;
        }
        else {
            System.out.println("Invalid option.");
        }
        return false;
    }

    private void showTree() {
        System.out.print(root.describe(0));
    }

    private void showTotals() {
        System.out.print("Competition name: ");
        String name = scanner.nextLine();
        Competition competition = root.findCompetition(name);
        if (competition == null) {
            System.out.println("Competition not found: " + name);
            return;
        }
        System.out.println("Total market value: " + competition.totalMarketValue());
        System.out.println("Clubs: " + competition.countClubs());
        System.out.println("Players: " + competition.countPlayers());
    }

    private void searchByName() {
        System.out.print("Search text: ");
        String text = scanner.nextLine();
        ArrayList<Player> results = root.searchPlayersByName(text);
        printPlayerList(results);
    }

    private void listByPosition() {
        System.out.print("Position (GK, DEF, MID, FWD): ");
        String position = scanner.nextLine();
        ArrayList<Player> results = root.findPlayersByPosition(position);
        printPlayerList(results);
    }

    private void printPlayerList(ArrayList<Player> players) {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i));
        }
    }

    private void showMostValuable() {
        Player best = root.mostValuablePlayer();
        if (best == null) {
            System.out.println("There are no players yet.");
            return;
        }
        System.out.println(best);
    }

    private void makeTransfer() {
        System.out.print("Player name: ");
        String playerName = scanner.nextLine();
        System.out.print("From club: ");
        String fromClub = scanner.nextLine();
        System.out.print("To club: ");
        String toClub = scanner.nextLine();
        System.out.print("Transfer fee (whole euros): ");
        String feeText = scanner.nextLine();

        long feeAmount;
        try {
            feeAmount = Long.parseLong(feeText.trim());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid fee amount.");
            return;
        }

        boolean success = market.transferPlayer(playerName, fromClub, toClub, new Money(feeAmount));
        if (success) {
            System.out.println("Transfer completed.");
        }
        else {
            System.out.println("Transfer failed. Check the club names, the player, and the buyer's budget.");
        }
    }

    private void saveToFile() {
        try {
            FileManager.save(root, DATA_FILE);
            System.out.println("Saved to " + DATA_FILE);
        }
        catch (IOException e) {
            System.out.println("Could not save to " + DATA_FILE);
        }
    }

    private void loadFromFile() {
        try {
            root = FileManager.load(DATA_FILE);
            market = new TransferMarket(root);
            System.out.println("Loaded from " + DATA_FILE);
        }
        catch (IOException e) {
            System.out.println("Could not load " + DATA_FILE);
        }
    }
}
