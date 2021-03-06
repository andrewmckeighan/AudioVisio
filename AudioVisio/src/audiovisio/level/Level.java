package audiovisio.level;

import audiovisio.entities.Entity;
import audiovisio.entities.Player;
import audiovisio.networking.SyncManager;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.states.ClientAppState;
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
    public static final VersionString CURRENT_LEVEL_FORMAT = new VersionString("0.6");
    public static final Vector3f SCALE = new Vector3f(8.2F, 10.2F, 8.2F);

    public static final String KEY_NAME       = "name";
    public static final String KEY_AUTHOR     = "author";
    public static final String KEY_VERSION    = "version";
    public static final String KEY_FORMAT     = "format";
    public static final String KEY_LEVEL_DATA = "level";

    public static final String KEY_SPAWN          = "spawn";
    public static final String KEY_SPAWN_LOCATION = "location";
    public static final String KEY_SPAWN_ROTATION = "rotation";
    public static final String KEY_AUDIO_SPAWN    = "p2";
    public static final String KEY_VISUAL_SPAWN   = "p1";

    /**
     * Non-Player items will start at ID 10.
     */
    public static final long STARTING_ID = 10;

    public  JSONObject levelData;
    private Node       shootables;
    private Vector3f pAudioSpawn  = Player.DEFAULT_SPAWN_LOCATION;
    private Vector3f pVisualSpawn = Player.DEFAULT_SPAWN_LOCATION;
    private long     NEXT_ID      = Level.STARTING_ID;
    private String name;
    private String author;
    private String version;
    private Map<Long, ILevelItem> levelItems = new HashMap<Long, ILevelItem>();
    private String fileName;
    private LevelBox levelBox;

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

    // LIFECYCLE

    /**
     * Initialize the items in the level. It gives the items a reference
     * to the asset manager to allow them to load things like models and
     * meshes.
     *
     * @param assetManager A reference to the game's assetManager
     */
    public void init( AssetManager assetManager, SyncManager syncManager ){
        LogHelper.info(String.format("Initializing level: '%s'", this.name));

        this.shootables = new Node("shootables");

        this.levelBox = new LevelBox(assetManager);

        Vector3f min = new Vector3f();
        Vector3f max = new Vector3f();

        for (ILevelItem item : this.levelItems.values()){
            item.init(assetManager);
            syncManager.addObject(item.getID(), item);

            if (item instanceof ITriggerable){
                ITriggerable linkable = (ITriggerable) item;

                Map<Long, ITriggerable> links = this.resolveLinks(item.getID(), linkable.getLinked());
                linkable.resolveLinks(links);
            }

            if (item instanceof IShootable){
                this.shootables.attachChild((Spatial) item);
            }

            Vector3f location = item.getLocation();
            location.setY(location.getY() + Level.SCALE.getY());
            min.minLocal(item.getLocation());
            max.maxLocal(item.getLocation());
        }

        this.levelBox.setSize(min, max);
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

        rootNode.attachChild(this.shootables);

        if (ClientAppState.isAudio){
            this.levelBox.start(rootNode, physics);
        }
    }

    /**
     * Get the root {@link audiovisio.rsle.editor.LevelNode} for
     * the level.
     *
     * @return The root LevelNode
     */
    public LevelNode getLevelNode() {
        LevelNode root = new LevelNode(this.name, true);
        root.setSourceLevel(this);

        LevelNode lvlName = new LevelNode("Name", this.name, false);
        LevelNode lvlAuthor = new LevelNode("Author", this.author, false);
        LevelNode lvlVersion = new LevelNode("Version", this.version, false);

        LevelNode lvlSpawn = new LevelNode("Spawns", true);
        LevelNode lvlAudio = LevelUtils.vector2node("Audio", this.pAudioSpawn);
        LevelNode lvlAudioRot = new LevelNode("Rotation", 0F, false);
        LevelNode lvlVisual = LevelUtils.vector2node("Visual", this.pVisualSpawn);
        LevelNode lvlVisualRot = new LevelNode("Rotation", 0F, false);

        lvlAudio.add(lvlAudioRot);
        lvlVisual.add(lvlVisualRot);

        lvlSpawn.add(lvlAudio);
        lvlSpawn.add(lvlVisual);

        root.add(lvlName);
        root.add(lvlAuthor);
        root.add(lvlVersion);
        root.add(lvlSpawn);

        return root;
    }

    /**
     * Load the level from the levelData JSON object. The method creates
     * and loads all the ILevelItems in the level.
     */
    public void loadLevel() {
        JSONObject spawns = (JSONObject) this.levelData.get(Level.KEY_SPAWN);

        JSONObject audio = (JSONObject) spawns.get(Level.KEY_AUDIO_SPAWN);
        float pAudioRotation = ((Double) audio.get(Level.KEY_SPAWN_ROTATION)).floatValue();
        this.pAudioSpawn = JSONHelper.readVector3f((JSONObject) audio.get(Level.KEY_SPAWN_LOCATION));

        JSONObject visual = (JSONObject) spawns.get(Level.KEY_VISUAL_SPAWN);
        float pVisualRotation = ((Double) visual.get(Level.KEY_SPAWN_ROTATION)).floatValue();
        this.pVisualSpawn = JSONHelper.readVector3f((JSONObject) visual.get(Level.KEY_SPAWN_LOCATION));

        JSONArray level = (JSONArray) this.levelData.get(Level.KEY_LEVEL_DATA);
        LogHelper.info(String.format("Loading level '%s'", this.name));

        for (Object item : level) {
            JSONObject itemJson = (JSONObject) item;
            String type = (String) itemJson.get("type");

            // Start the ID counter at the next available ID.
            // This is mainly used in the level editors
            this.NEXT_ID = Math.max(this.NEXT_ID, ((Long) itemJson.get("id")).intValue() + 1);

            ILevelItem lvlItem;
            if (LevelRegistry.typeHasSubTypes(type) && itemJson.containsKey("subtype")) {
                String subtype = (String) itemJson.get("subtype");

                lvlItem = LevelRegistry.getItemForSubType(type, subtype);
                lvlItem.load(itemJson);
                this.levelItems.put(lvlItem.getID(), lvlItem);
                LogHelper.fine(String.format("Load item of type: %s:%s", type, subtype));
            } else {
                lvlItem = LevelRegistry.getItemForType(type);
                lvlItem.load(itemJson);
                this.levelItems.put(lvlItem.getID(), lvlItem);
                LogHelper.fine("Load item of type: " + type);
            }
        }
    }

    // METADATA

    /**
     * Save the level to the levelData JSONObject.
     */
    public void saveLevel(){
        this.levelData = new JSONObject();

        // Metadata
        this.levelData.put(Level.KEY_NAME, this.name);
        this.levelData.put(Level.KEY_AUTHOR, this.author);
        this.levelData.put(Level.KEY_VERSION, this.version);
        this.levelData.put(Level.KEY_FORMAT, Level.CURRENT_LEVEL_FORMAT.getVersion());

        // Spawns
        JSONObject objSpawn = new JSONObject();

        JSONObject audioSpawn = new JSONObject();
        audioSpawn.put(Level.KEY_SPAWN_LOCATION, JSONHelper.saveVector3f(this.pAudioSpawn));
        audioSpawn.put(Level.KEY_SPAWN_ROTATION, 0F);
        objSpawn.put(Level.KEY_AUDIO_SPAWN, audioSpawn);

        JSONObject visualSpawn = new JSONObject();
        visualSpawn.put(Level.KEY_SPAWN_LOCATION, JSONHelper.saveVector3f(this.pVisualSpawn));
        visualSpawn.put(Level.KEY_SPAWN_ROTATION, 0F);
        objSpawn.put(Level.KEY_VISUAL_SPAWN, visualSpawn);

        this.levelData.put(Level.KEY_SPAWN, objSpawn);

        // Level Data
        JSONArray level = new JSONArray();
        for (ILevelItem item : this.levelItems.values()){
            JSONObject obj = new JSONObject();
            item.save(obj);
            level.add(obj);
        }

        this.levelData.put(Level.KEY_LEVEL_DATA, level);
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
     * Resolve the links that have been requested by a ITriggerable. This will
     * determine which items are triggered by it. Invalid links are logged to
     * the console.
     *
     * @param parent The ID of the parent object
     * @param requested The links the parent object requested
     * @return A map of ID -> triggerable instance
     */
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
     * Regenerate the Ids for a level. This is intended to be
     * used by the level editors to help make ID numbers more
     * manageable. This currently does not deal with linked
     * objects, however it does keep a mapping of old->new
     * IDs in preparation for linked objects.
     */
    public void regenIds(){
        this.resetNextId();

        // This is used to keep track of old->new IDs. It is used
        // to fix linked objects
        HashMap<Long, Long> remappedIds = new HashMap<Long, Long>();

        for (ILevelItem item : this.levelItems.values()){
            long oldId = item.getID();
            long newId = this.getNextId();

            remappedIds.put(oldId, newId);
            item.setID(newId);
        }

        // Fix linked objects
        for (ILevelItem item : this.levelItems.values()){
            if (item instanceof ITriggerable){
                ITriggerable trig = (ITriggerable) item;

                Set<Long> ids = trig.getLinked();
                Set<Long> newIds = new HashSet<Long>();

                for (Long id : ids){
                    newIds.add(remappedIds.get(id));
                }

                trig.setLinks(newIds);
            }
        }
    }

    /**
     * Reset the NEXT_ID counter to the starting ID for
     * level objects.
     */
    public void resetNextId(){
        this.NEXT_ID = Level.STARTING_ID;
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
     * Remove an item from the level.
     *
     * @param id The item to move
     * @return true if remove sucessful, false otherwise
     */
    public boolean removeItem( long id ){
        if (this.levelItems.containsKey(id)){
            this.levelItems.remove(id);
            return true;
        }

        return false;
    }

    /**
     * Set the spawn location and rotation of the audio player.
     *
     * @param loc Spawn location
     * @param rotation rotation
     */
    public void setAudioSpawn(Vector3f loc, float rotation) {
        this.pAudioSpawn = loc;
    }

    /**
     * Set the spawn location and rotation of the Visual player.
     *
     * @param loc      Spawn location
     * @param rotation Rotation
     */
    public void setVisualSpawn(Vector3f loc, float rotation) {
        this.pVisualSpawn = loc;
    }

    // HELPER METHODS

    /**
     * Retreive an item by its ID
     *
     * @param id The id of the item wanted
     * @return the item if it exists, null otherwise
     */
    public ILevelItem getItem(Long id) {
        return this.levelItems.get(id);
    }

    @Override
    public String toString() {
        return String.format("%s by %s (%s)", this.name, this.author, this.version);
    }

    public Vector3f getSpawn(long id) {
        Vector3f spawn = null;
        if (id % 2 == 1) {
            //audio
            spawn = this.getAudioSpawn();
        } else {
            //visual
            spawn = this.getVisualSpawn();
        }

        return spawn;
    }

    /**
     * Get the name of the level
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the level
     *
     * @param name The level's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the author of the level
     */
    public String getAuthor() {
        return this.author;
    }

    // LEVEL ITEMS

    /**
     * Set the author of the level
     *
     * @param author The level's new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the version string of the level
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Set the version number of the level
     *
     * @param version The new version number
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the filename of the level's JSON
     * file.
     *
     * @return The filename
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Set the filename of the level's JSON file.
     *
     * @param fileName path to the JSON file
     */
    protected void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the next available ID for level objects.
     *
     * @return Next ID
     */
    public long getNextId() {
        long curID = this.NEXT_ID;
        this.NEXT_ID++;
        return curID;
    }

    /**
     * Get all the items in the level.
     *
     * @return A list of all items
     */
    public Collection<ILevelItem> getItems(){
        return this.levelItems.values();
    }

    /**
     * Get the set of level items that implement the ITriggerable interface and can
     * be linked to other objects.
     *
     * @return A set of item IDs
     */
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
     * Get the Node containing the shootables in the level.
     *
     * @return Node containing shootables
     */
    public Node getShootables(){
        return this.shootables;
    }

    public LevelBox getLevelBox() {
        return levelBox;
    }
}