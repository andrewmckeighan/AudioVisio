package audiovisio.utils;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

/**
 * A collection of methods to help with reading
 * and writing common data structures to JSON
 */
public class JSONHelper {
	/**
	 * Read a Vector3f from a JSONObject.
	 * 
	 * The JSON object should be of the form:
	 * <pre>
	 * {
	 *    "x": 2,
	 *    "y": 3,
	 *    "z": 1
	 * }
	 * </pre>
	 * 
	 * The JSONObject passed to this function should be the
	 * object obtained from using <code>obj.get("location")</code>
	 * 
	 * @param obj The json object to read
	 * @return The Vector3f created from the JSONObject
	 */
	public static Vector3f readVector3f(JSONObject obj) {
		float locX = ((Double) obj.get("x")).floatValue();
		float locY = ((Double) obj.get("y")).floatValue();
		float locZ = ((Double) obj.get("z")).floatValue();
		
		return new Vector3f(locX, locY, locZ);
	}
	
	/**
	 * Save a Vector3f to a JSONObject.
	 * 
	 * The JSON object that is returned is:
	 * <pre>
	 * {
	 *    "x": 2,
	 *    "y": 3,
	 *    "z": 1
	 * }
	 * </pre>
	 * 
	 * @param vec The Vector3f to save to a json Object
	 * @return The JSONObject created from the Vector3f
	 */
	public static JSONObject saveVector3f(Vector3f vec) {
		JSONObject loc = new JSONObject();
		loc.put("x", vec.x);
		loc.put("y", vec.y);
		loc.put("z", vec.z);
		
		return loc;
	}

    public static int getInt(JSONObject obj, String key) {
        return ((Long) obj.get(key)).intValue();
    }
}
