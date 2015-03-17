package audiovisio.level;

import org.json.simple.JSONObject;

/**
 * An interface that describes a level item. It
 * is used for any Entity or other item that will
 * appear in a level. It defines an interface for
 * the level object to interact with the items
 * when saving/loading.
 *
 * @author Matt Gerst
 */
public interface ILevelItem {
    /**
     * Called when loading the object from JSON. The given
     * JSON object should contain the JSONObject that this
     * item should be loaded from.
     *
     * @param obj JSONObject to load from.
     */
    public void load(JSONObject obj);

    /**
     * Called when saving the object to JSON. The given
     * JSON object is the object that the item should
     * be saved to. The preferred method of saving to
     * the object is to just use <code>obj.put(key, value)</code>.
     *
     * @param obj JSONObject to save to.
     */
    public void save(JSONObject obj);
}
