package audiovisio.level;

import org.junit.Before;
import org.junit.Test;

public class LevelWriterTest {
	
	Level level;

	/**
	 *
	 */
	@Before
	public void setUp() {
		level = new Level("Test Writer Level", "Test Author", "1.0");
		level.setFileName("../test_level.json");
	}

	/**
	 *
	 */
	@Test
	public void testWrite() {
		LevelWriter.write(level);
	}

}
