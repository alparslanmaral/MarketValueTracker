import static org.junit.Assert.*;
import org.junit.Test;

public class TransferMarketTest {

    private Competition buildSampleHierarchy() {
        Competition league = new Competition("League");
        Club buyer = new Club("Buyer FC", new Money(10000000));
        Club seller = new Club("Seller FC", new Money(1000000));
        seller.addPlayer(new Player("Target Player", new Date(1, 1, 2000), "MID", new Money(4000000)));
        league.addClub(buyer);
        league.addClub(seller);
        return league;
    }

    @Test
    public void testTransferSuccessMovesPlayerAndUpdatesBudgets() {
        Competition league = buildSampleHierarchy();
        TransferMarket market = new TransferMarket(league);

        boolean success = market.transferPlayer("Target Player", "Seller FC", "Buyer FC", new Money(3000000));

        assertTrue(success);
        assertNull(league.findClub("Seller FC").findPlayer("Target Player"));
        assertNotNull(league.findClub("Buyer FC").findPlayer("Target Player"));
        assertEquals(7000000, league.findClub("Buyer FC").getBudget().getAmount());
        assertEquals(4000000, league.findClub("Seller FC").getBudget().getAmount());
    }

    @Test
    public void testTransferFailsInsufficientBudget() {
        Competition league = buildSampleHierarchy();
        TransferMarket market = new TransferMarket(league);

        boolean success = market.transferPlayer("Target Player", "Seller FC", "Buyer FC", new Money(50000000));

        assertFalse(success);
        assertNotNull(league.findClub("Seller FC").findPlayer("Target Player"));
    }

    @Test
    public void testTransferFailsPlayerNotInSellingClub() {
        Competition league = buildSampleHierarchy();
        TransferMarket market = new TransferMarket(league);

        boolean success = market.transferPlayer("Nobody", "Seller FC", "Buyer FC", new Money(1000));

        assertFalse(success);
    }

    @Test
    public void testTransferFailsSameClub() {
        Competition league = buildSampleHierarchy();
        TransferMarket market = new TransferMarket(league);

        boolean success = market.transferPlayer("Target Player", "Seller FC", "Seller FC", new Money(1000));

        assertFalse(success);
    }

    @Test
    public void testTransferFailsUnknownClub() {
        Competition league = buildSampleHierarchy();
        TransferMarket market = new TransferMarket(league);

        boolean success = market.transferPlayer("Target Player", "Seller FC", "Unknown FC", new Money(1000));

        assertFalse(success);
    }
}
