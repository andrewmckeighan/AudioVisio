package audiovisio.level;

import audiovisio.entities.Entity;
import audiovisio.entities.Player;
import audiovisio.networking.SyncManager;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import audiovisio.utils.LogHelper;
import audiovisio.utils.VersionString;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

/**
 * Holds information about the items in a level.
 *
 * Handles the loading/saving of levels and items from the
 * JSON files.
 */
public class Level {
    public static final VersionString CURRENT_LEVEL_FORMAT = new VersionString("0.4");
    public static final Vector3f      SCALE                = new Vector3f(5.2F, 10.2F, 5.2F);

    public static final String KEY_NAME       = "name";
    public static final String KEY_AUTHOR     = "author";
    public static final String KEY_VERSION    = "version";
    public static final String KEY_FORMAT     = "format";
    public static final String KEY_LEVEL_DATA = "level";

    public static final String KEY_SPAWN        = "spawn";
    public static final String KEY_AUDIO_SPAWN  = "p1";
    public static final String KEY_VISUAL_SPAWN = "p2";

    public static final long STARTING_ID = 10;
    public static Node shootables;
    JSONObject levelData;
    private Vector3f pAudioSpawn  = Player.DEFAULT_SPAWN_LOCATION;
    private Vector3f pVisualSpawn = Player.DEFAULT_SPAWN_LOCATION;
    /**
     * Non-Player items will start at ID 10.
     */
    private long     NEXT_ID      = Level.STARTING_ID;
    private String name;
    private String author;
    private String version;
    private Map<Long, ILevelItem> levelItems = new HashMap<Long, ILevelItem>();
    private String fileName;

    /**
     * Create an instance of Level with the given metadata.
     *
     * @param name    The name of the level
     * @param author  The author of the level
     * @param version The version number of the level
     */
    public Level( String name, String author, String version ){
        this.name = name;
        this.author = author;
        this.version = version;
    }

    /**
     * Create an instance of Level from the given JSONObject.
     *
     * @param obj      The JSON object loaded from the level file
     * @param fileName The filepath of the level's json file
     */
    public Level( JSONObject obj, String fileName ){
        this.name = (String) obj.get(Level.KEY_NAME);
        this.author = (String) obj.get(Level.KEY_AUTHOR);
        this.version = (String) obj.get(Level.KEY_VERSION);

        this.levelData = obj;

        this.fileName = fileName;

        LogHelper.info(String.format("Found level '%s' by '%s'", this.name, this.author));
    }

    /**
     * Load the level from the levelData JSON object. The method creates
     * and loads all the ILevelItems in the level.
     */
    public void loadLevel(){
        JSONObject spawns = (JSONObject) this.levelData.get(Level.KEY_SPAWN);

        this.pAudioSpawn = JSONHelper.readVector3f((JSONObject) spawns.get(Level.KEY_AUDIO_SPAWN));
        this.pVisualSpawn = JSONHelper.readVector3f((JSONObject) spawns.get(Level.KEY_VISUAL_SPAWN));

        JSONArray level = (JSONArray) this.levelData.get(Level.KEY_LEVEL_DATA);
        LogHelper.info(String.format("Loading level '%s'", this.name));

        for (Object item : level){
            JSONObject itemJson = (JSONObject) item;
            String type = (String) itemJson.get("type");

            // Start the ID counter at the next available ID.
            // This is mainly used in the level editors
            this.NEXT_ID = Math.max(this.NEXT_ID, ((Long) itemJson.get("id")).intValue() + 1);

            ILevelItem lvlItem = LevelRegistry.getItemForType(type);
            lvlItem.load(itemJson);
            this.levelItems.put(lvlItem.getID(), lvlItem);
            LogHelper.fine("Load item of type: " + type);
        }
    }

    /**
     * Save the level to the levelData JSONObject.
     */
    public void saveLevel(){
        this.levelData = new JSONObject();

        this.levelData.put(Level.KEY_NAME, this.name);
        this.levelData.put(Level.KEY_AUTHOR, this.author);
        this.levelData.put(Level.KEY_VERSION, this.version);
        this.levelData.put(Level.KEY_FORMAT, Level.CURRENT_LEVEL_FORMAT.getVersion());

        JSONObject objSpawn = new JSONObject();
        objSpawn.put(Level.KEY_AUDIO_SPAWN, JSONHelper.saveVector3f(this.pAudioSpawn));
        objSpawn.put(Level.KEY_VISUAL_SPAWN, JSONHelper.saveVector3f(this.pVisualSpawn));

        this.levelData.put(Level.KEY_SPAWN, objSpawn);

        JSONArray level = new JSONArray();
        for (ILevelItem item : this.levelItems.values()){
            JSONObject obj = new JSONObject();
            item.save(obj);
            level.add(obj);
        }

        this.levelData.put(Level.KEY_LEVEL_DATA, level);
    }

    /**
     * Initialize the items in the level. It gives the items a reference
     * to the asset manager to allow them to load things like models and
     * meshes.
     *
     * @param assetManager A reference to the game's assetManager
     */
    public void init( AssetManager assetManager, SyncManager syncManager ){
        LogHelper.info(String.format("Initializing level: '%s'", this.name));
        for (ILevelItem item : this.levelItems.values()){
            item.init(assetManager);
            syncManager.addObject(item.getID(), item);

            if (item instanceof ITriggerable){
                ITriggerable linkable = (ITriggerable) item;

                Map<Long, ITriggerable> links = this.resolveLinks(item.getID(), linkable.getLinked());
                linkable.resolveLinks(links);
            }

            if (item instanceof IShootable){
                shootables.attachChild((Spatial) item);
            }
        }
    }

    private Map<Long, ITriggerable> resolveLinks( Long parent, Set<Long> requested ){
        HashMap<Long, ITriggerable> links = new HashMap<Long, ITriggerable>();

        if (requested == null){ return links; }

        for (Long id : requested){
            if (!this.levelItems.containsKey(id)){
                LogHelper.warn(String.format("Level object %d requested link to non-existent object %d", parent, id));
                continue;
            }

            ILevelItem item = this.levelItems.get(id);

            if (!(item instanceof ITriggerable)){
                LogHelper.warn(String.format("Level object %d attempting to link to non-linkable object %d", parent, id));
                continue;
            }

            ITriggerable linkable = (ITriggerable) item;

            links.put(id, (ITriggerable) item);
            LogHelper.info(String.format("Linked objs %d -> %d", parent, id));
        }

        return links;
    }

    /**
     * Has the items in the level perform their game start methods.
     *
     * @param rootNode
     */
    public void start( Node rootNode, PhysicsSpace physics ){
        LogHelper.info(String.format("Starting level '%s'", this.name));
        for (ILevelItem item : this.levelItems.values()){
            item.start(rootNode, physics);
        }
    }

    /**
     * Add item to the level.
     *
     * @param item The item to add.
     */
    public void addItem( ILevelItem item ){
        this.levelItems.put(item.getID(), item);
    }

    /**
     * Get all the items in the level.
     *
     * @return A list of all items
     */
    public Collection<ILevelItem> getItems(){
        return this.levelItems.values();
    }

    public Set<Long> getLinkables(){
        HashSet<Long> linkable = new HashSet<Long>();

        for (ILevelItem item : this.levelItems.values()){
            if (item instanceof ITriggerable){
                linkable.add(item.getID());
            }
        }

        return linkable;
    }

    /**
     * Get the list of panels in the level
     */
    public List<Panel> getPanels(){
        List<Panel> panels = new ArrayList<Panel>();

        for (ILevelItem item : this.levelItems.values()){
            if (item instanceof Panel){ panels.add((Panel) item); }
        }

        return panels;
    }

    /**
     * Get the list of entities in the level
     */
    public List<Entity> getEntities(){
        List<Entity> entities = new ArrayList<Entity>();

        for (ILevelItem item : this.levelItems.values()){
            if (item instanceof Entity){ entities.add((Entity) item); }
        }

        return entities;
    }

    /**
     * Get the list of triggers in the level
     *
     * @return
     */
    public List<Trigger> getTriggers(){
        List<Trigger> triggers = new ArrayList<Trigger>();

        for (ILevelItem item : this.levelItems.values()){
            if (item instanceof Trigger){ triggers.add((Trigger) item); }
        }

        return triggers;
    }

    /**
     * Get the name of the level
     */
    public String getName(){
        return this.name;
    }

    public void setName( String name ){
        this.name = name;
    }

    /**
     * Get the author of the level
     */
    public String getAuthor(){
        return this.author;
    }

    public void setAuthor( String author ){
        this.author = author;
    }

    /**
     * Get the version string of the level
     */
    public String getVersion(){
        return this.version;
    }

    public void setVersion( String version ){
        this.version = version;
    }

    /**
     * Get the spawn location set by the level for the Audio player.
     *
     * @return spawn location
     */
    public Vector3f getAudioSpawn(){
        return this.pAudioSpawn;
    }

    /**
     * Get the spawn location set by the level for the Visual player.
     *
     * @return spawn location
     */
    public Vector3f getVisualSpawn(){
        return this.pVisualSpawn;
    }

    /**
     * Get the filename of the level's JSON
     * file.
     *
     * @return The filename
     */
    public String getFileName(){
        return this.fileName;
    }

    /**
     * Set the filename of the level's JSON file.
     *
     * @param fileName path to the JSON file
     */
    protected void setFileName( String fileName ){
        this.fileName = fileName;
    }

    /**
     * Set the File for the Level
     *
     * @param loadedFile The file object to use
     */
    public void setFile( File loadedFile ){
        this.fileName = loadedFile.getAbsolutePath();
    }

    /**
     * Regenerate the Ids for a level. This is intended to be
     * used by the level editors to help make ID numbers more
     * manageable. This currently does not deal with linked
     * objects, however it does keep a mapping of old->new
     * IDs in preparation for linked objects.
     */
    public void regenIds(){
        this.resetNextId();

        // This is used to keep track of old->new IDs. It is intended
        // to be used to fix linked objects
        HashMap<Long, Long> remappedIds = new HashMap<Long, Long>();

        for (ILevelItem item : this.levelItems.values()){
            long oldId = item.getID();
            long newId = this.getNextId();

            remappedIds.put(newId, oldId);
            item.setID(newId);
        }
    }

    /**
     * Get the next available ID for level objects.
     *
     * @return Next ID
     */
    public long getNextId(){
        long curID = this.NEXT_ID;
        this.NEXT_ID++;
        return curID;
    }

    /**
     * Reset the NEXT_ID counter to the starting ID for
     * level objects.
     */
    public void resetNextId(){
        this.NEXT_ID = Level.STARTING_ID;
    }

    /**
     * Get the root {@link audiovisio.rsle.editor.LevelNode} for
     * the level.
     *
     * @return The root LevelNode
     */
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(this.name, true);
        root.setSourceLevel(this);

        LevelNode lvlName = new LevelNode("Name", this.name, false);
        LevelNode lvlAuthor = new LevelNode("Author", this.author, false);
        LevelNode lvlVersion = new LevelNode("Version", this.version, false);

        LevelNode lvlSpawn = new LevelNode("Spawns", true);
        LevelNode lvlAudio = LevelUtils.vector2node("Audio", this.pAudioSpawn);
        LevelNode lvlVisual = LevelUtils.vector2node("Visual", this.pVisualSpawn);

        lvlSpawn.add(lvlAudio);
        lvlSpawn.add(lvlVisual);

        root.add(lvlName);
        root.add(lvlAuthor);
        root.add(lvlVersion);
        root.add(lvlSpawn);

        return root;
    }

    public ILevelItem getItem( Long id ){
        return this.levelItems.get(id);
    }

    public Node getShootables() {
        return this.shootables;
    }
}