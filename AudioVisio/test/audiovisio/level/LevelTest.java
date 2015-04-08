package audiovisio.level;

import static org.junit.Assert.*;

import audiovisio.Items;
import audiovisio.networking.SyncManager;
import com.jme3.asset.AssetManager;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class LevelTest {

    Level level;
    JSONObject levelData;

    @Before
    public void setUp(){
        levelData = new JSONObject();
        levelData.put("name", "Test Level");
        levelData.put("author", "R29");
        levelData.put("version", "2.2");
        levelData.put("format", Level.CURRENT_LEVEL_FORMAT.toString());
        level = new Level(levelData, "testFile.json");
    }

    @Test
    public void testLevelMetadata(){
        assertEquals("Test Level", level.getName());
        assertEquals("R29", level.getAuthor());
        assertEquals("2.2", level.getVersion());
    }

    @Test
    public void testLevelCreate(){
        level = new Level("Test Level2", "R29", "1.0");

        assertEquals("Test Level2", level.getName());
        assertEquals("R29", level.getAuthor());
        assertEquals("1.0", level.getVersion());
    }

    /**
     * The next ID in a newly created level should be the
     * levels starting ID.
     */
    @Test
    public void testNextId(){
        assertEquals(Level.STARTING_ID, level.getNextId());
    }

    @Test
    public void testSaveLevel(){
        level.saveLevel();
        JSONObject levelObj = level.levelData;

        assertEquals("Test Level", levelObj.get("name"));
        assertEquals("R29", levelObj.get("author"));
        assertEquals("2.2", levelObj.get("version"));
        assertEquals(Level.CURRENT_LEVEL_FORMAT.toString(), levelObj.get("format"));
    }

    @Test
    public void testAddItem(){
        ILevelItem panel = new Panel();
        panel.setID(level.getNextId());

        level.addItem(panel);

        assertEquals(panel, level.getItem(panel.getID()));
    }

    /**
     * TODO: Figure out how to test {@link Level#init(AssetManager, SyncManager)}.
     */
    @Test
    public void testInit(){
    }
}
