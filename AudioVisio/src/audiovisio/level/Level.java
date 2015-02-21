package audiovisio.level;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import audiovisio.entities.Entity;

public class Level {
	
	private String name;
	private String author;
	private String version;

    private List<Panel> panelList;
    private List<Entity> entityList;
    private List<Trigger> triggerList;

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
    }
}