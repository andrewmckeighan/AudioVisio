package audiovisio.entities;

import org.json.simple.JSONObject;

public class InteractableEntity extends Entity {

    private InteractableEntity linkedEntity;
    public boolean stuck; //if the entity keeps it state regardless of triggerEvents


    public InteractableEntity(){
        this(false);
    }

    public InteractableEntity(boolean stuck){

    }

    public void load(JSONObject obj){
        super.load(obj);

        // TODO: Figure out how to get a reference to linked entities
        JSONObject linked = (JSONObject) obj.get("linked");
        //"linked": { "type": "door", "location": {"x": 5, "y": 5, "z": 5}}
        //this.linkedEntity = Level.getListOfType(type).getAt(x,y,z);
    }
    
    @Override
    public void save(JSONObject obj) {
    	super.save(obj);
    	
    	// TODO: Figure out how to deal with linked entities
    }

    public void triggerEvent(){
        linkedEntity.onTriggeredEvent();
    }

    private void onTriggeredEvent() {
		// TODO Auto-generated method stub

	}

	public Entity getLinkedEntity(){
        return this.linkedEntity;
    }

    public void setLinkedEntity(InteractableEntity entity){
        this.linkedEntity = entity;
    }

}