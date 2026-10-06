import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class ClubTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAddPlayerDuplicateThrowsException() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Jane Doe", new Date(1, 1, 2000), "MID", new Money(1000000)));
        club.addPlayer(new Player("Jane Doe", new Date(1, 1, 2000), "DEF", new Money(500000)));
    }

    @Test
    public void testRemovePlayerMissingReturnsNull() {
        Club club = new Club("Example FC", new Money(1000000));
        assertNull(club.removePlayer("Nobody"));
    }

    @Test
    public void testSquadValueEmptySquadIsZero() {
        Club club = new Club("Example FC", new Money(1000000));
        assertEquals(0, club.squadValue().getAmount());
    }

    @Test
    public void testMostValuablePlayerEmptySquadReturnsNull() {
        Club club = new Club("Example FC", new Money(1000000));
        assertNull(club.mostValuablePlayer());
    }

    @Test
    public void testMostValuablePlayerPicksHighestValue() {
        Club club = new Club("Example FC", new Money(1000000));
        Player cheap = new Player("Cheap Player", new Date(1, 1, 2000), "DEF", new Money(1000000));
        Player expensive = new Player("Expensive Player", new Date(1, 1, 2000), "FWD", new Money(9000000));
        club.addPlayer(cheap);
        club.addPlayer(expensive);
        assertEquals("Expensive Player", club.mostValuablePlayer().getName());
    }

    @Test
    public void testFindPlayerCaseInsensitive() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Jane Doe", new Date(1, 1, 2000), "MID", new Money(1000000)));
        assertNotNull(club.findPlayer("jane doe"));
    }

    @Test
    public void testGetPlayersByPositionFiltersCorrectly() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Keeper One", new Date(1, 1, 2000), "GK", new Money(1000000)));
        club.addPlayer(new Player("Striker One", new Date(1, 1, 2000), "FWD", new Money(1000000)));
        club.addPlayer(new Player("Striker Two", new Date(1, 1, 2000), "FWD", new Money(1000000)));

        ArrayList<Player> forwards = club.getPlayersByPosition("FWD");
        assertEquals(2, forwards.size());

        ArrayList<Player> keepers = club.getPlayersByPosition("gk");
        assertEquals(1, keepers.size());

        ArrayList<Player> defenders = club.getPlayersByPosition("DEF");
        assertTrue(defenders.isEmpty());
    }
}
