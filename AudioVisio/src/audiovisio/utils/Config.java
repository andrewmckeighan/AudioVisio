package audiovisio.utils;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matt Gerst
 */
public final class Config {
    public static final String KEY_FORWARD       = "Forward";
    public static final String KEY_BACKWARD      = "Backward";
    public static final String KEY_LEFT          = "Left";
    public static final String KEY_RIGHT         = "Right";
    public static final String KEY_JUMP          = "Jump";
    public static final String KEY_SHOOT         = "Shoot";
    public static final String KEY_DEBUG         = "Debug";
    public static final String KEY_RELEASE_MOUSE = "ReleaseMouse";
    public static final String KEY_NO_CLIP       = "NoClip";

    private static JSONObject jsonObject;
    private static Map<String, Integer> keyMap = new HashMap<String, Integer>();

    public static void init( JSONObject obj ){
        Config.jsonObject = obj;

        JSONObject bindings = (JSONObject) obj.get("keybinding");

        keyMap.put(KEY_FORWARD, Integer.decode((String) bindings.get(KEY_FORWARD)));
        keyMap.put(KEY_BACKWARD, Integer.decode((String) bindings.get(KEY_BACKWARD)));
        keyMap.put(KEY_LEFT, Integer.decode((String) bindings.get(KEY_LEFT)));
        keyMap.put(KEY_RIGHT, Integer.decode((String) bindings.get(KEY_RIGHT)));
        keyMap.put(KEY_JUMP, Integer.decode((String) bindings.get(KEY_JUMP)));
        keyMap.put(KEY_SHOOT, Integer.decode((String) bindings.get(KEY_SHOOT)));
        keyMap.put(KEY_DEBUG, Integer.decode((String) bindings.get(KEY_DEBUG)));
        keyMap.put(KEY_RELEASE_MOUSE, Integer.decode((String) bindings.get(KEY_RELEASE_MOUSE)));
        keyMap.put(KEY_NO_CLIP, Integer.decode((String) bindings.get(KEY_NO_CLIP)));
    }

    /**
     * Get the value for the specified mapping. This should be the
     * same string that is used to register the mapping with the
     * inputManager.
     *
     * @param map The mapping to get
     *
     * @return The keycode of the mapping
     */
    public static int getKeyMapping( String map ){
        return keyMap.get(map);
    }

    /**
     * Set the value for the specified mapping. This should be the
     * same string that is used to register the mapping with the
     * inputManager.
     *
     * @param map     The mapping to set
     * @param keycode The keycode to set the mapping to
     */
    public static void setKeyMapping( String map, int keycode ){
        keyMap.put(map, keycode);
    }
}
