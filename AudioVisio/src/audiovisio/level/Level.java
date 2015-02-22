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

    public Level(JSONObject obj){
    	this.name = (String) obj.get("name");
    	this.author = (String) obj.get("author");
    	this.version = (String) obj.get("version");
    	
    	JSONArray triggers = (JSONArray) obj.get("triggers");
    	for (Object triggerObj : triggers) {
    		JSONObject triggerJson = (JSONObject) triggerObj;
    		
    		Trigger trigger = Trigger.load(triggerJson);
    		triggerList.add(trigger);
    	}
    	
    	JSONArray panels = (JSONArray) obj.get("panels");
    	for (Object panelObj : panels) {
    		JSONObject panelJson = (JSONObject) panelObj;
    		
    		Panel panel = Panel.load(panelJson);
    		panelList.add(panel);
    	}
    	
    	JSONArray stairs = (JSONArray) obj.get("stairs");
    	for (Object stairObj : stairs) {
    		JSONObject stairJson = (JSONObject) stairObj;
    		
    		Stair stair = Stair.load(stairJson);
    		panelList.add(stair);
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
}