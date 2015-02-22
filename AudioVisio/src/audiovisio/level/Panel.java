package audiovisio.level;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

public class Panel {
	public Vector3f location;
	
	public Panel() {}

    public Panel(Vector3f location){
        this.location = location;
    }
    
    public void load(JSONObject obj) {
    	JSONObject location = (JSONObject) obj.get("location");
    	float locX = ((Double)location.get("x")).floatValue();
    	float locY = ((Double)location.get("y")).floatValue();
    	float locZ = ((Double)location.get("z")).floatValue();
    	
    	Vector3f locVec = new Vector3f(locX, locY, locZ);
    }
    
    public void save(JSONObject obj) {
    	
    }
}