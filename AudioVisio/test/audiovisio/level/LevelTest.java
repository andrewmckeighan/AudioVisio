package audiovisio.level;

import static org.junit.Assert.*;

import org.junit.Test;

public class LevelTest {

	@Test
	public void testLevel() {
		Level level = LevelReader.read("../example_level.json");
		
		assertEquals(2, level.getTriggers().size());
		assertEquals(2, level.getPanels().size());
		assertEquals(0, level.getEntities().size());
	}

}
