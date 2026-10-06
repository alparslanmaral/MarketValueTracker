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
    public void testContainsPlayerTrueForOwnPlayer() {
        Club club = new Club("Example FC", new Money(1000000));
        Player player = new Player("Jane Doe", new Date(1, 1, 2000), "MID", new Money(1000000));
        club.addPlayer(player);
        assertTrue(club.containsPlayer(player));
    }

    @Test
    public void testContainsPlayerFalseForPlayerFromAnotherClub() {
        Club club = new Club("Example FC", new Money(1000000));
        Player outsider = new Player("Jane Doe", new Date(1, 1, 2000), "MID", new Money(1000000));
        assertFalse(club.containsPlayer(outsider));
    }

    @Test
    public void testContainsPlayerFalseForEmptySquad() {
        Club club = new Club("Example FC", new Money(1000000));
        Player outsider = new Player("Jane Doe", new Date(1, 1, 2000), "MID", new Money(1000000));
        assertFalse(club.containsPlayer(outsider));
    }

    @Test
    public void testSearchPlayersByNameOneMatch() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Marcus Whitfield", new Date(1, 1, 2000), "FWD", new Money(1000000)));
        club.addPlayer(new Player("Daniel Osei", new Date(1, 1, 2000), "MID", new Money(1000000)));

        ArrayList<Player> results = club.searchPlayersByName("Marcus");
        assertEquals(1, results.size());
        assertEquals("Marcus Whitfield", results.get(0).getName());
    }

    @Test
    public void testSearchPlayersByNameSeveralMatches() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Marcus Whitfield", new Date(1, 1, 2000), "FWD", new Money(1000000)));
        club.addPlayer(new Player("Marcus Rashford", new Date(1, 1, 2000), "FWD", new Money(1000000)));

        ArrayList<Player> results = club.searchPlayersByName("Marcus");
        assertEquals(2, results.size());
    }

    @Test
    public void testSearchPlayersByNameNoMatch() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Daniel Osei", new Date(1, 1, 2000), "MID", new Money(1000000)));

        ArrayList<Player> results = club.searchPlayersByName("Marcus");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchPlayersByNameDifferentLetterCase() {
        Club club = new Club("Example FC", new Money(1000000));
        club.addPlayer(new Player("Marcus Whitfield", new Date(1, 1, 2000), "FWD", new Money(1000000)));

        ArrayList<Player> results = club.searchPlayersByName("MARCUS");
        assertEquals(1, results.size());
    }

    @Test
    public void testSearchPlayersByNameEmptySquad() {
        Club club = new Club("Example FC", new Money(1000000));
        ArrayList<Player> results = club.searchPlayersByName("Marcus");
        assertTrue(results.isEmpty());
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
