package audiovisio.level;

import com.jme3.math.Vector3f;

public class Stair extends Panel{
    private Direction direction;

    public Stair(Vector3f location, Direction direction){
    	super(location);
        this.direction = direction;
    }

    public enum Direction {
    	NORTH,
    	SOUTH,
    	EAST,
    	WEST
    }
}