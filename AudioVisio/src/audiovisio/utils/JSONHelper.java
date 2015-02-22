package audiovisio.utils;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

public class JSONHelper {
	public static Vector3f readVector3f(JSONObject location) {
		float locX = ((Double) location.get("x")).floatValue();
		float locY = ((Double) location.get("y")).floatValue();
		float locZ = ((Double) location.get("z")).floatValue();
		
		return new Vector3f(locX, locY, locZ);
	}
	
	public static JSONObject saveVector3f(Vector3f location) {
		JSONObject loc = new JSONObject();
		loc.put("x", location.x);
		loc.put("y", location.y);
		loc.put("z", location.z);
		
		return loc;
	}
}
