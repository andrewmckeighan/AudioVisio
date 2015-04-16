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

        Config.keyMap.put(Config.KEY_FORWARD, Integer.decode((String) bindings.get(Config.KEY_FORWARD)));
        Config.keyMap.put(Config.KEY_BACKWARD, Integer.decode((String) bindings.get(Config.KEY_BACKWARD)));
        Config.keyMap.put(Config.KEY_LEFT, Integer.decode((String) bindings.get(Config.KEY_LEFT)));
        Config.keyMap.put(Config.KEY_RIGHT, Integer.decode((String) bindings.get(Config.KEY_RIGHT)));
        Config.keyMap.put(Config.KEY_JUMP, Integer.decode((String) bindings.get(Config.KEY_JUMP)));
        Config.keyMap.put(Config.KEY_SHOOT, Integer.decode((String) bindings.get(Config.KEY_SHOOT)));
        Config.keyMap.put(Config.KEY_DEBUG, Integer.decode((String) bindings.get(Config.KEY_DEBUG)));
        Config.keyMap.put(Config.KEY_RELEASE_MOUSE, Integer.decode((String) bindings.get(Config.KEY_RELEASE_MOUSE)));
        Config.keyMap.put(Config.KEY_NO_CLIP, Integer.decode((String) bindings.get(Config.KEY_NO_CLIP)));
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
        return Config.keyMap.get(map);
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
        Config.keyMap.put(map, keycode);
    }

    /**
     * Save the current state of the configuration to the config file.
     */
    public static void save(){
        JSONObject bindings = (JSONObject) Config.jsonObject.get("keybinding");

        bindings.put(Config.KEY_FORWARD, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_FORWARD)));
        bindings.put(Config.KEY_BACKWARD, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_BACKWARD)));
        bindings.put(Config.KEY_LEFT, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_LEFT)));
        bindings.put(Config.KEY_RIGHT, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_RIGHT)));
        bindings.put(Config.KEY_JUMP, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_JUMP)));
        bindings.put(Config.KEY_SHOOT, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_SHOOT)));
        bindings.put(Config.KEY_DEBUG, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_DEBUG)));
        bindings.put(Config.KEY_RELEASE_MOUSE, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_RELEASE_MOUSE)));
        bindings.put(Config.KEY_NO_CLIP, "0x" + Integer.toHexString(Config.keyMap.get(Config.KEY_NO_CLIP)));

        Config.jsonObject.put("keybinding", bindings);

        FileUtils.saveJSONFile(FileUtils.getMetaFile("config.json"), Config.jsonObject);
    }
}
