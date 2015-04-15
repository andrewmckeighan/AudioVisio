package audiovisio.utils;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the configuration options for the game. These options
 * are stored in the config.json meta file. It contains information
 * such as keybindings and user preferences.
 *
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

    /**
     * Load the configuration from config.json
     */
    public static void load(){
        JSONObject obj = FileUtils.loadJSONFile(FileUtils.getMetaFile("config.json"));
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

    /**
     * Save the current state of the configuration to the config file.
     */
    public static void save(){
        JSONObject bindings = (JSONObject) jsonObject.get("keybinding");

        bindings.put(KEY_FORWARD, Integer.toHexString(keyMap.get(KEY_FORWARD)));
        bindings.put(KEY_BACKWARD, Integer.toHexString(keyMap.get(KEY_BACKWARD)));
        bindings.put(KEY_LEFT, Integer.toHexString(keyMap.get(KEY_LEFT)));
        bindings.put(KEY_RIGHT, Integer.toHexString(keyMap.get(KEY_RIGHT)));
        bindings.put(KEY_JUMP, Integer.toHexString(keyMap.get(KEY_JUMP)));
        bindings.put(KEY_SHOOT, Integer.toHexString(keyMap.get(KEY_SHOOT)));
        bindings.put(KEY_DEBUG, Integer.toHexString(keyMap.get(KEY_DEBUG)));
        bindings.put(KEY_RELEASE_MOUSE, Integer.toHexString(keyMap.get(KEY_RELEASE_MOUSE)));
        bindings.put(KEY_NO_CLIP, Integer.toHexString(keyMap.get(KEY_NO_CLIP)));

        jsonObject.put("keybinding", bindings);

        FileUtils.saveJSONFile(FileUtils.getMetaFile("config.json"), jsonObject);
    }
}
