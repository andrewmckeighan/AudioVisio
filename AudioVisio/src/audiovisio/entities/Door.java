package audiovisio.entities;

import org.json.simple.JSONObject;

public class Door extends InteractableEntity {
    private Boolean isOpen;

    public Door(){
        this.isOpen = false;
    }

    public Door(Boolean open){
        this.isOpen = open;
    }

    public Door(Boolean open, Boolean stuck){
        this.isOpen = open;
        this.stuck = stuck;
    }

    @Override
    public void load(JSONObject obj){
        super.load(obj);

        this.isOpen = (Boolean) obj.get("isOpen");
    }
    
    @Override
    public void save(JSONObject obj) {
    	super.save(obj);
    	obj.put("type", "door");
    	
    	obj.put("isOpen", this.isOpen);
    }

    private void onTriggeredEvent(){
        if(!this.stuck){
            this.isOpen = !this.isOpen;
        }
    }

}