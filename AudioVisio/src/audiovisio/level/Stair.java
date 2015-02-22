package audiovisio.level;

import org.json.simple.JSONObject;

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
    
    public static Stair load(JSONObject obj) {
    	JSONObject location = (JSONObject) obj.get("location");
    	float locX = ((Double)location.get("x")).floatValue();
    	float locY = ((Double)location.get("y")).floatValue();
    	float locZ = ((Double)location.get("z")).floatValue();
    	
    	Vector3f locVec = new Vector3f(locX, locY, locZ);
    	
    	Direction direction = Direction.valueOf((String) obj.get("direction"));
    	return new Stair(locVec, direction);
    }
}