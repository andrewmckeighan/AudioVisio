package audiovisio.level;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import audiovisio.entities.Button;
import audiovisio.entities.Door;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;

/**
 * Holds information about the items in a level.
 *
 * Handles the loading/saving of levels and items from the
 * JSON files.
 */
public class Level {
	
	private String name;
	private String author;
	private String version;

    private List<ILevelItem> levelItems = new ArrayList<ILevelItem>();

    @Deprecated
    private List<Panel> panelList = new ArrayList<Panel>();
    @Deprecated
    private List<Entity> entityList = new ArrayList<Entity>();
    @Deprecated
    private List<Trigger> triggerList = new ArrayList<Trigger>();
    
    private String fileName;
    
    JSONObject levelData;
    
    /**
     * Create an instance of Level with the given metadata.
     * 
     * @param name The name of the level
     * @param author The author of the level
     * @param version The version number of the level
     */
    public Level(String name, String author, String version) {
    	this.name = name;
    	this.author = author;
    	this.version = version;
    }

    /**
     * Create an instance of Level from the given JSONObject.
     *  
     * @param obj The JSON object loaded from the level file
     * @param fileName The filepath of the level's json file
     */
    public Level(JSONObject obj, String fileName){
    	this.name = (String) obj.get("name");
    	this.author = (String) obj.get("author");
    	this.version = (String) obj.get("version");

        this.levelData = obj;
    	
    	this.fileName = fileName;
    }

    /**
     * Load the level from the levelData JSON object. The method loads
     * all the Panels, Triggers, and Entities in the level and 
     * initializes them.
     */
    public void loadLevel() {
    	JSONArray level = (JSONArray) this.levelData.get("level");
    	
    	for (Object item : level) {
    		JSONObject itemJson = (JSONObject) item;
    		String type = (String) itemJson.get("type");
    		
    		if (type.equalsIgnoreCase( "trigger" )) {
    			Trigger trigger = new Trigger();
    			trigger.load(itemJson);
                this.levelItems.add(trigger);
    		} else if (type.equalsIgnoreCase( "panel" )) {
    			Panel panel = new Panel();
    			panel.load(itemJson);
                this.levelItems.add(panel);
    		} else if (type.equalsIgnoreCase( "stair" )) {
    			Stair stair = new Stair();
    			stair.load(itemJson);
                this.levelItems.add(stair);
    		} else if (type.equalsIgnoreCase( "door" )) {
    			Door door = new Door();
    			door.load(itemJson);
                this.levelItems.add(door);
    		} else if (type.equalsIgnoreCase( "button" )) {
    			Button button = new Button();
    			button.load(itemJson);
                this.levelItems.add(button);
    		} else if (type.equalsIgnoreCase( "lever" )) {
    			Lever lever = new Lever();
    			lever.load(itemJson);
                this.levelItems.add(lever);
    		}
    	}
    }
    
    /**
     * Save the level to the levelData JSONObject.
     */
	public void saveLevel() {
        this.levelData = new JSONObject();

        this.levelData.put("name", this.name);
        this.levelData.put("author", this.author);
        this.levelData.put("version", this.version);
    	
    	JSONArray level = new JSONArray();
        for (ILevelItem item : this.levelItems) {
            JSONObject obj = new JSONObject();
            item.save(obj);
            level.add(obj);
        }

        this.levelData.put("level", level);
    }

    /**
     * Add item to the level.
     *
     * @param item The item to add.
     */
    public void addItem(ILevelItem item) {
        this.levelItems.add(item);
    }
    
	/**
	 * Get the list of panels in the level
	 */
    public List<Panel> getPanels() {
    	List<Panel> panels = new ArrayList<Panel>();

        for (ILevelItem item : this.levelItems) {
            if (item instanceof Panel) {
                panels.add( (Panel) item );
            }
        }

        return panels;
    }

    /**
     * Add a panel to the level.
     *
     * @param panel The panel to add
     */
    @Deprecated
    public void addPanel(Panel panel) {
        this.levelItems.add(panel);
    }
    
    /**
     * Get the list of entities in the level
     */
    public List<Entity> getEntities() {
    	List<Entity> entities = new ArrayList<Entity>();

        for (ILevelItem item : this.levelItems) {
            if (item instanceof Entity) {
                entities.add( (Entity) item );
            }
        }

        return entities;
    }
    
    /**
     * Add an entity to the level
     * 
     * @param entity The entity to add
     */
    @Deprecated
    public void addEntity(Entity entity) {
        this.levelItems.add(entity);
    }
    
    /**
     * Get the list of triggers in the level
     * @return
     */
    public List<Trigger> getTriggers() {
    	List<Trigger> triggers = new ArrayList<Trigger>();

        for (ILevelItem item : this.levelItems) {
            if (item instanceof Trigger) {
                triggers.add( (Trigger) item );
            }
        }

        return triggers;
    }   
    
    /**
     * Add a trigger to the level
     * 
     * @param trigger The trigger to add
     */
    @Deprecated
    public void addTrigger(Trigger trigger) {
        this.levelItems.add(trigger);
    }
    
    /**
     * Get the name of the level
     */
    public String getName() {
    	return this.name;
    }
    
    /**
     * Get the author of the level
     */
    public String getAuthor() {
    	return this.author;
    }
    
    /**
     * Get the version string of the level
     */
    public String getVersion() {
    	return this.version;
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
}