import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class CompetitionTest {

    @Test
    public void testTotalMarketValueEmptyCompetition() {
        Competition competition = new Competition("Empty League");
        assertEquals(0, competition.totalMarketValue().getAmount());
    }

    @Test
    public void testTotalMarketValueOnlyClubs() {
        Competition league = new Competition("League");
        Club club = new Club("Club A", new Money(1000000));
        club.addPlayer(new Player("Player One", new Date(1, 1, 2000), "MID", new Money(5000000)));
        league.addClub(club);
        assertEquals(5000000, league.totalMarketValue().getAmount());
    }

    @Test
    public void testTotalMarketValueOnlySubCompetitions() {
        Competition top = new Competition("Top");
        Competition sub = new Competition("Sub");
        Club club = new Club("Club A", new Money(1000000));
        club.addPlayer(new Player("Player One", new Date(1, 1, 2000), "MID", new Money(3000000)));
        sub.addClub(club);
        top.addSubCompetition(sub);
        assertEquals(3000000, top.totalMarketValue().getAmount());
    }

    @Test
    public void testTotalMarketValueMultipleLevelsDeep() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        Competition england = new Competition("England");
        world.addSubCompetition(europe);
        europe.addSubCompetition(england);

        Club worldClub = buildClubWithOnePlayer("World Club", 1000000);
        Club europeClub = buildClubWithOnePlayer("Europe Club", 2000000);
        Club englandClub = buildClubWithOnePlayer("England Club", 4000000);
        world.addClub(worldClub);
        europe.addClub(europeClub);
        england.addClub(englandClub);

        assertEquals(7000000, world.totalMarketValue().getAmount());
    }

    private Club buildClubWithOnePlayer(String clubName, long playerValue) {
        Club club = new Club(clubName, new Money(1000000));
        club.addPlayer(new Player(clubName + " Player", new Date(1, 1, 2000), "MID", new Money(playerValue)));
        return club;
    }

    @Test
    public void testFindClubAtTopLevel() {
        Competition league = new Competition("League");
        Club club = new Club("Top Club", new Money(1000000));
        league.addClub(club);
        assertSame(club, league.findClub("Top Club"));
    }

    @Test
    public void testFindClubDeepInHierarchy() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        world.addSubCompetition(europe);
        Club deepClub = new Club("Deep Club", new Money(1000000));
        europe.addClub(deepClub);
        assertSame(deepClub, world.findClub("deep club"));
    }

    @Test
    public void testFindClubNotFoundReturnsNull() {
        Competition league = new Competition("League");
        assertNull(league.findClub("Nobody FC"));
    }

    @Test
    public void testCountPlayersAcrossHierarchy() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        world.addSubCompetition(europe);

        Club clubA = new Club("Club A", new Money(1000000));
        clubA.addPlayer(new Player("Player A", new Date(1, 1, 2000), "MID", new Money(1000000)));
        clubA.addPlayer(new Player("Player B", new Date(1, 1, 2000), "DEF", new Money(1000000)));
        world.addClub(clubA);

        Club clubB = new Club("Club B", new Money(1000000));
        clubB.addPlayer(new Player("Player C", new Date(1, 1, 2000), "FWD", new Money(1000000)));
        europe.addClub(clubB);

        assertEquals(3, world.countPlayers());
    }

    @Test
    public void testMostValuablePlayerNoPlayersReturnsNull() {
        Competition league = new Competition("League");
        league.addClub(new Club("Empty Squad Club", new Money(1000000)));
        assertNull(league.mostValuablePlayer());
    }

    @Test
    public void testMostValuablePlayerAcrossSubCompetitions() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        world.addSubCompetition(europe);

        Club clubA = new Club("Club A", new Money(1000000));
        clubA.addPlayer(new Player("Cheap Player", new Date(1, 1, 2000), "MID", new Money(1000000)));
        world.addClub(clubA);

        Club clubB = new Club("Club B", new Money(1000000));
        clubB.addPlayer(new Player("Expensive Player", new Date(1, 1, 2000), "FWD", new Money(9000000)));
        europe.addClub(clubB);

        assertEquals("Expensive Player", world.mostValuablePlayer().getName());
    }

    @Test
    public void testFindPlayersByPositionAcrossHierarchy() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        world.addSubCompetition(europe);

        Club clubA = new Club("Club A", new Money(1000000));
        clubA.addPlayer(new Player("Keeper A", new Date(1, 1, 2000), "GK", new Money(1000000)));
        world.addClub(clubA);

        Club clubB = new Club("Club B", new Money(1000000));
        clubB.addPlayer(new Player("Keeper B", new Date(1, 1, 2000), "GK", new Money(1000000)));
        clubB.addPlayer(new Player("Striker B", new Date(1, 1, 2000), "FWD", new Money(1000000)));
        europe.addClub(clubB);

        ArrayList<Player> keepers = world.findPlayersByPosition("GK");
        assertEquals(2, keepers.size());
    }

    @Test
    public void testSearchPlayersByNameCaseInsensitiveContains() {
        Competition league = new Competition("League");
        Club club = new Club("Club A", new Money(1000000));
        club.addPlayer(new Player("Marcus Whitfield", new Date(1, 1, 2000), "FWD", new Money(1000000)));
        club.addPlayer(new Player("Daniel Osei", new Date(1, 1, 2000), "MID", new Money(1000000)));
        league.addClub(club);

        ArrayList<Player> results = league.searchPlayersByName("MARCUS");
        assertEquals(1, results.size());
        assertEquals("Marcus Whitfield", results.get(0).getName());
    }

    @Test
    public void testFindClubOfPlayerAtTopLevel() {
        Competition league = new Competition("League");
        Club club = new Club("Club A", new Money(1000000));
        Player player = new Player("Player A", new Date(1, 1, 2000), "MID", new Money(1000000));
        club.addPlayer(player);
        league.addClub(club);

        assertSame(club, league.findClubOfPlayer(player));
    }

    @Test
    public void testFindClubOfPlayerInDeepSubCompetition() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        world.addSubCompetition(europe);
        Club deepClub = new Club("Deep Club", new Money(1000000));
        Player player = new Player("Deep Player", new Date(1, 1, 2000), "MID", new Money(1000000));
        deepClub.addPlayer(player);
        europe.addClub(deepClub);

        assertSame(deepClub, world.findClubOfPlayer(player));
    }

    @Test
    public void testFindClubOfPlayerNotFoundReturnsNull() {
        Competition league = new Competition("League");
        league.addClub(new Club("Club A", new Money(1000000)));
        Player outsider = new Player("Outsider", new Date(1, 1, 2000), "MID", new Money(1000000));

        assertNull(league.findClubOfPlayer(outsider));
    }

    @Test
    public void testFindClubOfPlayerEmptyCompetitionReturnsNull() {
        Competition league = new Competition("League");
        Player outsider = new Player("Outsider", new Date(1, 1, 2000), "MID", new Money(1000000));

        assertNull(league.findClubOfPlayer(outsider));
    }

    @Test
    public void testFindClubOfPlayerDistinguishesSameNamePlayersInDifferentClubs() {
        Competition league = new Competition("League");
        Club clubA = new Club("Club A", new Money(1000000));
        Club clubB = new Club("Club B", new Money(1000000));
        Player playerInA = new Player("Same Name", new Date(1, 1, 2000), "MID", new Money(1000000));
        Player playerInB = new Player("Same Name", new Date(1, 1, 2000), "MID", new Money(1000000));
        clubA.addPlayer(playerInA);
        clubB.addPlayer(playerInB);
        league.addClub(clubA);
        league.addClub(clubB);

        assertSame(clubA, league.findClubOfPlayer(playerInA));
        assertSame(clubB, league.findClubOfPlayer(playerInB));
    }

    @Test
    public void testDepthSingleLevel() {
        Competition league = new Competition("League");
        assertEquals(1, league.depth());
    }

    @Test
    public void testDepthMultipleLevels() {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        Competition england = new Competition("England");
        world.addSubCompetition(europe);
        europe.addSubCompetition(england);
        assertEquals(3, world.depth());
    }
}
