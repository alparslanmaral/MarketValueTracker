import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class FileManagerTest {

    private static final String TEMP_FILE = "file_manager_test_temp.csv";

    @Test
    public void testSaveAndLoadRoundTrip() throws IOException {
        Competition world = new Competition("World");
        Competition europe = new Competition("Europe");
        world.addSubCompetition(europe);

        Club worldClub = new Club("World Club", new Money(1000000));
        worldClub.addPlayer(new Player("World Player", new Date(1, 1, 2000), "MID", new Money(5000000)));
        world.addClub(worldClub);

        Club deepClub = new Club("Deep Club", new Money(2000000));
        deepClub.addPlayer(new Player("Deep Player", new Date(2, 2, 2001), "FWD", new Money(7000000)));
        europe.addClub(deepClub);

        try {
            FileManager.save(world, TEMP_FILE);
            Competition loaded = FileManager.load(TEMP_FILE);

            assertEquals(2, loaded.countClubs());
            assertEquals(2, loaded.countPlayers());
            assertEquals(12000000, loaded.totalMarketValue().getAmount());

            Club loadedDeepClub = loaded.findClub("Deep Club");
            assertNotNull(loadedDeepClub);
            assertEquals(2000000, loadedDeepClub.getBudget().getAmount());
            assertNotNull(loadedDeepClub.findPlayer("Deep Player"));
        }
        finally {
            new File(TEMP_FILE).delete();
        }
    }

    @Test(expected = IOException.class)
    public void testLoadMissingFileThrowsIOException() throws IOException {
        FileManager.load("no_such_file_should_exist.csv");
    }
}
