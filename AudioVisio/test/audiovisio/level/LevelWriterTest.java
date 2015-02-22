package audiovisio.level;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import audiovisio.entities.Button;

import com.jme3.math.Vector3f;

public class LevelWriterTest {
	
	Level level;
	
	@Before
	public void setUp() {
		level = new Level("Test Writer Level", "Test Author", "1.0");
		level.setFileName("../test_level.json");
		
		level.addPanel(new Panel(new Vector3f(1F, 2F, 3F)));
		
		Button btn = new Button();
		btn.position = new Vector3f(1F, 2F, 4F);
		level.addEntity(btn);
		
		Stair stair = new Stair(new Vector3f(1F, 1F, 3F), Stair.Direction.SOUTH);
		level.addPanel(stair);
	}

	@Test
	public void testWrite() {
		LevelWriter.write(level);
	}

}
