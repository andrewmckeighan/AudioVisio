package audiovisio.level;

import com.jme3.asset.AssetManager;
import org.json.simple.JSONObject;

import audiovisio.utils.JSONHelper;

import com.jme3.math.Vector3f;

public class Trigger implements ILevelItem {
	private Vector3f location;
	
	public Trigger() {}

	public Trigger (Vector3f location) {
		this.location = location;
	}

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

    @Override
	public void save(JSONObject obj) {
		JSONObject location = JSONHelper.saveVector3f(this.location);
		obj.put("location", this.location);
	}
}