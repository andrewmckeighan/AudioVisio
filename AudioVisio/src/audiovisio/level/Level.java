package audiovisio.level;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import audiovisio.entities.Entity;

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
    	JSONArray triggers = (JSONArray) levelData.get("triggers");
    	for (Object triggerObj : triggers) {
    		JSONObject triggerJson = (JSONObject) triggerObj;
    		
    		Trigger trigger = new Trigger();
    		trigger.load(triggerJson);
    		triggerList.add(trigger);
    	}
    	
    	JSONArray panels = (JSONArray) levelData.get("panels");
    	for (Object panelObj : panels) {
    		JSONObject panelJson = (JSONObject) panelObj;
    		
    		Panel panel = new Panel();
    		panel.load(panelJson);
    		panelList.add(panel);
    	}
    	
    	JSONArray stairs = (JSONArray) levelData.get("stairs");
    	for (Object stairObj : stairs) {
    		JSONObject stairJson = (JSONObject) stairObj;
    		
    		Stair stair = new Stair();
    		stair.load(stairJson);
    		panelList.add(stair);
    	}
    }
    
    @SuppressWarnings("unchecked")
	public void saveLevel() {
    	levelData = new JSONObject();
    	
    	levelData.put("name", this.name);
    	levelData.put("author", this.author);
    	levelData.put("version", this.version);
    	
    	for(Panel panel : panelList) {
    		panel.save(levelData);
    	}
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