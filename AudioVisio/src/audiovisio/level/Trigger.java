package audiovisio.level;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

public class Trigger {
	private Vector3f location;

	public Trigger (Vector3f location) {
		this.location = location;
	}

	public static Trigger load(JSONObject obj) {
		JSONObject location = (JSONObject) obj.get("location");
		float locX = (float) location.get("x");
		float locY = (float) location.get("y");
		float locZ = (float) location.get("z");

		return new Trigger(new Vector3f(locX, locY, locZ));
	}

	public void save(JSONObject obj) {

	}
}