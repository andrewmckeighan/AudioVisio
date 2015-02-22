package audiovisio.level;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

public class Stair extends Panel{
    private Direction direction;
    
    public Stair() {}

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
    
    @Override
    public void load(JSONObject obj) {
    	super.load(obj);
    	
    	Direction direction = Direction.valueOf((String) obj.get("direction"));
    }
}