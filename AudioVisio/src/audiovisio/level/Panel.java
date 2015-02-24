package audiovisio.level;

import org.json.simple.JSONObject;

import audiovisio.utils.JSONHelper;

import com.jme3.math.Vector3f;

/**
 * Represents a panel in the world.
 */
public class Panel {
	/**
	 * The location of the panel on the grid.
	 */
	public Vector3f location;
	
	public Panel() {}

    public Panel(Vector3f location){
        this.location = location;
    }
    
    /**
     * Load the panel from a JSONObject
     * 
     * @param obj The JSONObject to load from
     */
    public void load(JSONObject obj) {
    	JSONObject location = (JSONObject) obj.get("location");
    	this.location = JSONHelper.readVector3f(location);
    }
    
    /**
     * Save the panel to a JSONObject
     * 
     * @param obj The JSONObject to save to
     */
    public void save(JSONObject obj) {
    	obj.put("type", "panel");
    	JSONObject location = new JSONObject();
    	obj.put("location", JSONHelper.saveVector3f(this.location));
    }
}