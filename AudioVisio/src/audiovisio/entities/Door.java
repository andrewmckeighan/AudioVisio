package audiovisio.entities;

import audiovisio.level.ILevelItem;
import org.json.simple.JSONObject;

public class Door extends InteractableEntity {

    public Door(){
        this.state = false;
    }

    public Door(Boolean open){
        this.state = open;
    }

    public Door(Boolean open, Boolean stuck){
        this.state = open;
        this.stuck = stuck;
    }

    @Override
    public void load(JSONObject obj){
        super.load(obj);

        this.state = (Boolean) obj.get("state");
    }
    
    @Override
    public void save(JSONObject obj) {
    	super.save(obj);
    	obj.put("type", "door");
    	
    	obj.put("state", this.state);
    }

    private void onTriggeredEvent(){
        if(!this.stuck){
            this.state = !this.state;
        }
    }

    private void open(){
        //TODO
        this.state = true;
    }

    private void close(){
        //TODO
        this.state = false;
    }

//    @Override
    private void update(Boolean state){
        if(this.state == false){
            if(state == true){
                this.open();
            }
        }else{
            if(state == false){
                this.close();
            }
        }

        this.state = state;
    }

}