package audiovisio.level;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

public class Panel {
	public Vector3f location;

    public Panel(Vector3f location){
        this.location = location;
    }
    
    public static Panel load(JSONObject obj) {
    	JSONObject location = (JSONObject) obj.get("location");
    	float locX = (float) location.get("x");
    	float locY = (float) location.get("y");
    	float locZ = (float) location.get("z");
    	
    	Vector3f locVec = new Vector3f(locX, locY, locZ);
    	return new Panel(locVec);
    }
    
    public void save(JSONObject obj) {
    	
    }
}