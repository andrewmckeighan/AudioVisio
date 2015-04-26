package audiovisio.utils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

/**
 * A collection of methods to help with reading
 * and writing common data structures to JSON
 */
public class JSONHelper {
    public static final String KEY_ID   = "id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_COLOR = "color";

    // Keys for the location node
    public static final String KEY_LOCATION   = "location";
    public static final String KEY_LOCATION_X = "x";
    public static final String KEY_LOCATION_Y = "y";
    public static final String KEY_LOCATION_Z = "z";

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
     *
     * @return The Vector3f created from the JSONObject
     */
    public static Vector3f readVector3f( JSONObject obj ){
        float locX = ((Double) obj.get(JSONHelper.KEY_LOCATION_X)).floatValue();
        float locY = ((Double) obj.get(JSONHelper.KEY_LOCATION_Y)).floatValue();
        float locZ = ((Double) obj.get(JSONHelper.KEY_LOCATION_Z)).floatValue();

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
     *
     * @return The JSONObject created from the Vector3f
     */
    public static JSONObject saveVector3f( Vector3f vec ){
        JSONObject loc = new JSONObject();
        loc.put(JSONHelper.KEY_LOCATION_X, ((Float) vec.x).doubleValue());
        loc.put(JSONHelper.KEY_LOCATION_Y, ((Float) vec.y).doubleValue());
        loc.put(JSONHelper.KEY_LOCATION_Z, ((Float) vec.z).doubleValue());

        return loc;
    }

    public static int getInt( JSONObject obj, String key ){
        return ((Long) obj.get(key)).intValue();
    }

    public static ColorRGBA readColor(String colorString) {
        ColorRGBA returnColor = ColorRGBA.LightGray;
        if (colorString == "lightGrey") {
            returnColor = ColorRGBA.LightGray;
        } else if (colorString == "red") {
            returnColor = ColorRGBA.Red;
        } else if (colorString == "blue") {
            returnColor = ColorRGBA.Blue;
        } else if (colorString == "green") {
            returnColor = ColorRGBA.Green;
        } else {
            LogHelper.warn("color: " + colorString + " is not supported! defaulting to lightGrey.");
        }

        return returnColor;
    }
}
