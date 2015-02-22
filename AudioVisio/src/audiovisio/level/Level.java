package audiovisio.level;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import audiovisio.entities.Button;
import audiovisio.entities.Door;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;

public class Level {
	
	private String name;
	private String author;
	private String version;

    private List<Panel> panelList = new ArrayList<>();
    private List<Entity> entityList = new ArrayList<>();
    private List<Trigger> triggerList = new ArrayList<>();
    
    private String fileName;
    
    JSONObject levelData;
    
    public Level(String name, String author, String version) {
    	this.name = name;
    	this.author = author;
    	this.version = version;
    }

    public Level(JSONObject obj, String fileName){
    	this.name = (String) obj.get("name");
    	this.author = (String) obj.get("author");
    	this.version = (String) obj.get("version");
    	
    	levelData = obj;
    	
    	this.fileName = fileName;
    }
    
    public void loadLevel() {
    	JSONArray level = (JSONArray) levelData.get("level");
    	
    	for (Object item : level) {
    		JSONObject itemJson = (JSONObject) item;
    		String type = (String) itemJson.get("type");
    		
    		if (type.equalsIgnoreCase("trigger")) {
    			Trigger trigger = new Trigger();
    			trigger.load(itemJson);
    			triggerList.add(trigger);
    		} else if (type.equalsIgnoreCase("panel")) {
    			Panel panel = new Panel();
    			panel.load(itemJson);
    			panelList.add(panel);
    		} else if (type.equalsIgnoreCase("stair")) {
    			Stair stair = new Stair();
    			stair.load(itemJson);
    			panelList.add(stair);
    		} else if (type.equalsIgnoreCase("door")) {
    			Door door = new Door();
    			door.load(itemJson);
    			entityList.add(door);
    		} else if (type.equalsIgnoreCase("button")) {
    			Button button = new Button();
    			button.load(itemJson);
    			entityList.add(button);
    		} else if (type.equalsIgnoreCase("lever")) {
    			Lever lever = new Lever();
    			lever.load(itemJson);
    			entityList.add(lever);
    		}
    	}
    }
    
    @SuppressWarnings("unchecked")
	public void saveLevel() {
    	levelData = new JSONObject();
    	
    	levelData.put("name", this.name);
    	levelData.put("author", this.author);
    	levelData.put("version", this.version);
    	
    	JSONArray level = new JSONArray();
    	for (Panel panel : panelList) {
    		JSONObject obj = new JSONObject();
    		panel.save(obj);
    		level.add(obj);
    	}
    	
    	for (Trigger trigger : triggerList) {
    		JSONObject obj = new JSONObject();
    		trigger.save(obj);
    		level.add(obj);
    	}
    	
    	for (Entity entity : entityList) {
    		JSONObject obj = new JSONObject();
    		entity.save(obj);
    		level.add(obj);
    	}
    	
    	levelData.put("level", level);
    }
    
    public List<Panel> getPanels() {
    	return panelList;
    }
    
    public List<Entity> getEntities() {
    	return entityList;
    }
    
    public List<Trigger> getTriggers() {
    	return triggerList;
    }
    
    public void addPanel(Panel panel) {
    	panelList.add(panel);
    }
    
    public void addEntity(Entity entity) {
    	entityList.add(entity);
    }
    
    public void addTrigger(Trigger trigger) {
    	triggerList.add(trigger);
    }
    
    public String getName() {
    	return this.name;
    }
    
    public String getAuthor() {
    	return this.author;
    }
    
    public String getVersion() {
    	return this.version;
    }
    
    public String getFileName() {
    	return this.fileName;
    }
    
    protected void setFileName(String fileName) {
    	this.fileName = fileName;
    }
}