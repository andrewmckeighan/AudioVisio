package audiovisio.level;

import com.jme3.asset.AssetManager;
import org.json.simple.JSONObject;

/**
 * An interface that describes a level item. It
 * is used for anything that will be kept track
 * of by the level. It defines a lifecycle that
 * the item must follow. The lifecycle of an
 * ILevelItem is as follows:
 *
 * <pre>
 *     Constructed -> load() -> init() -> start()
 * </pre>
 *
 * These methods are meant to be called by
 * {@link audiovisio.level.Level}. They represent
 * the various stages the game might be in for
 * different purposes. For instance, the game
 * itself will make it all the way to start(),
 * however {@link audiovisio.rsle.RSLE} will only
 * make it to load().
 *
 * @author Matt Gerst
 */
public interface ILevelItem {
    /**
     * Called when loading the object from JSON. The given
     * JSON object should contain the JSONObject that this
     * item should be loaded from. Do NOT rely on any
     * dependencies (assetManager, jMonkey) at this point.
     *
     * @param obj JSONObject to load from.
     */
    public void load(JSONObject obj);

    /**
     * Called after loading the item. At this point,
     * dependencies such as the assetManager, and other
     * parts of jMonkey should be available. This is where
     * things like models should be loaded. This stage is
     * meant for a future 3D level editor, which means that
     * stuff like game logic should not be done or
     * initialized here.
     *
     * @param assetManager A reference to the assetManager to allow
     *                     loading of assets.
     */
    public void init(AssetManager assetManager);

    /**
     * This is the final stage before the game has started.
     * This is where things dealing with game logic should be
     * initialized. It is expected that all dependencies be
     * available at this point. It is at thigs point that the
     * game is expected to have started.
     */
    public void start();

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
