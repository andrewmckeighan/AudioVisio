package audiovisio.level;

import org.json.simple.JSONObject;

import audiovisio.utils.JSONHelper;

import com.jme3.math.Vector3f;

public class Panel {
	public Vector3f location;
	
	public Panel() {}

    public Panel(Vector3f location){
        this.location = location;
    }
    
    public void load(JSONObject obj) {
    	JSONObject location = (JSONObject) obj.get("location");
    	this.location = JSONHelper.readVector3f(location);
    }
    
    public void save(JSONObject obj) {
    	obj.put("type", "panel");
    	JSONObject location = new JSONObject();
    	obj.put("location", JSONHelper.saveVector3f(this.location));
    }
}