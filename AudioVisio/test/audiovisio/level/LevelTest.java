package audiovisio.level;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LevelTest {
	
	Level level;
	
	@Before
	public void setUp() {
		level = LevelReader.read("../example_level.json");
	}
	
	@Test
	public void testLevel() {
		assertEquals("Level Name", level.getName());
		assertEquals("Author Name", level.getAuthor());
		assertEquals("1.0", level.getVersion());
	}

	@Test
	public void testLevelLoaded() {
		level.loadLevel();
		
		assertEquals(2, level.getTriggers().size());
		assertEquals(2, level.getPanels().size());
		assertEquals(0, level.getEntities().size());
	}

}
