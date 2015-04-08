package audiovisio.level;

import audiovisio.rsle.editor.LevelNode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.json.simple.JSONObject;

/**
 * An interface that describes a level item. It
 * is used for anything that will be kept track
 * of by the level. It defines a lifecycle that
 * the item must follow. The lifecycle of an
 * ILevelItem is as follows:
 * <p/>
 * <pre>
 *     Constructed -> load() -> init() -> start()
 * </pre>
 * <p/>
 * These methods are meant to be called by
 * {@link audiovisio.level.Level}. They represent
 * the various stages the game might be in for
 * different purposes. For instance, the game
 * itself will make it all the way to start(),
 * however {@link audiovisio.rsle.RSLE} will only
 * make it to load().
 *
 * The level system keeps track of objects by
 * their ID. All ILevelItems need an ID and a way
 * to keep track of those IDs.
 *
 * It is very important that ALL implementations of ILevelItem
 * have a default constructor that does not rely on other
 * systems to be setup and present. If this is not the case,
 * the level system will NOT be able to load a level file
 * that contains an object of that type.
 *
 * @author Matt Gerst
 */
public interface ILevelItem {

    enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    /**
     * Called when loading the object from JSON. The given
     * JSON object should contain the JSONObject that this
     * item should be loaded from. Do NOT rely on any
     * dependencies (assetManager, jMonkey) at this point.
     *
     * @param loadObj JSONObject to load from.
     */
    void load( JSONObject loadObj );

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
    void init( AssetManager assetManager );

    /**
     * This is the final stage before the game has started.
     * This is where things dealing with game logic should be
     * initialized. It is expected that all dependencies be
     * available at this point. It is at thigs point that the
     * game is expected to have started.
     *
     * @param rootNode The rootNode of the scene
     * @param physics  The physics space
     */
    void start( Node rootNode, PhysicsSpace physics );

    /**
     * Called when saving the object to JSON. The given
     * JSON object is the object that the item should
     * be saved to. The preferred method of saving to
     * the object is to just use {@code codeObj.put(key, value)}.
     *
     * @param codeObj JSONObject to save to.
     */
    void save( JSONObject codeObj );

    /**
     * Used by {@link audiovisio.rsle.RSLE} to get a LevelNode of the
     * object. The LevelNode is used to represent the Item in the
     * level tree. This is NOT meant to be used by the game itself
     * and should not be confused the jMonkey's Node class.
     *
     * @return The Item's LevelNode
     */
    LevelNode getLevelNode();

    /**
     * Used by the level system to manage IDs in the level.
     *
     * @return The ID of the object
     */
    long getID();

    /**
     * Used by the level system to manage the IDs in the level.
     *
     * @param id The new ID of the object
     */
    void setID( long id );

    /**
     * Get the location of the level item.
     *
     * @return
     */
    Vector3f getLocation();
}