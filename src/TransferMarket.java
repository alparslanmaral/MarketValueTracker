/**
 * Handles player transfers between two clubs that both belong to the same
 * competition hierarchy, validating the move before it happens.
 */
public class TransferMarket {

    private Competition root;

    public TransferMarket(Competition root) {
        this.root = root;
    }

    /*
     * A transfer only goes ahead if both clubs exist, the clubs are
     * different, the player is really in the selling club's squad, and
     * the buying club can afford the fee. Any failure leaves everything
     * unchanged.
     */
    public boolean transferPlayer(String playerName, String fromClubName, String toClubName, Money fee) {
        if (fromClubName.equalsIgnoreCase(toClubName)) {
            return false;
        }

        Club fromClub = root.findClub(fromClubName);
        Club toClub = root.findClub(toClubName);
        if (fromClub == null || toClub == null) {
            return false;
        }

        Player player = fromClub.findPlayer(playerName);
        if (player == null) {
            return false;
        }

        if (!toClub.getBudget().isGreaterThanOrEqual(fee)) {
            return false;
        }

        fromClub.removePlayer(playerName);
        toClub.addPlayer(player);
        toClub.setBudget(toClub.getBudget().subtract(fee));
        fromClub.setBudget(fromClub.getBudget().add(fee));
        return true;
    }
}
