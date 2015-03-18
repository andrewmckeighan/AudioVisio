package audiovisio.level;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import org.json.simple.JSONObject;

import audiovisio.utils.JSONHelper;

import com.jme3.math.Vector3f;

/**
 * Represents a panel in the world.
 */
public class Panel implements ILevelItem {
    private Node model;

	/**
	 * The location of the panel on the grid.
	 */
	public Vector3f location;
	
	public Panel() {}

    public Panel(Vector3f location){
        this.location = location;
    }

    public void initialize(AssetManager assetManager) {
        model = (Node) assetManager.loadModel("Models/Level/Panel/Panel.j3o");
    }
    
    /**
     * Load the panel from a JSONObject
     * 
     * @param obj The JSONObject to load from
     */
    @Override
    public void load(JSONObject obj) {
    	JSONObject location = (JSONObject) obj.get("location");
    	this.location = JSONHelper.readVector3f(location);
    }

    @Override
    public void init(AssetManager assetManager) {
        
    }

    @Override
    public void start() {

    }

    /**
     * Save the panel to a JSONObject
     * 
     * @param obj The JSONObject to save to
     */
    @Override
    public void save(JSONObject obj) {
    	obj.put("type", "panel");
    	JSONObject location = new JSONObject();
    	obj.put("location", JSONHelper.saveVector3f(this.location));
    }
}