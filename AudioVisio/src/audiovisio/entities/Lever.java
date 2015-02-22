package audiovisio.entities;

import org.json.simple.JSONObject;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Lever extends InteractableEntity {

    public static final int LEVER_BOX_SIZE = 1;
    public static final int LEVER_BOX_LENGTH = 3;
    public static final int LEVER_BOX_ANGLE = 60;

    private Box shape;
    private Boolean isOn;

    @SuppressWarnings("deprecation")
	public Lever(){
        this.isOn = false;

        Material material = new Material();

        this.shape = new Box(this.position, LEVER_BOX_SIZE, LEVER_BOX_LENGTH, LEVER_BOX_SIZE);
        this.geometry = new Geometry(this.name, this.shape);
        this.geometry.setLocalRotation(new Quaternion().fromAngles(0, LEVER_BOX_ANGLE, 0));

    }

    public void switchLever(){
        this.isOn = !this.isOn;
        if(this.isOn){
            this.turnedOnEvent();
            this.geometry.setLocalRotation(new Quaternion().fromAngles(0, LEVER_BOX_ANGLE, 0));
        }
        if(!this.isOn){
            this.turnedOffEvent();
            this.geometry.setLocalRotation(new Quaternion().fromAngles(0, -LEVER_BOX_ANGLE, 0));
        }
    }

    private void turnedOnEvent(){

    }

    private void turnedOffEvent(){

    }

    @Override
    public void load(JSONObject obj){
        super.load(obj);
        this.isOn = (Boolean) obj.get("isOn");
    }
    
    @Override
    public void save(JSONObject obj) {
    	super.save(obj);
    	obj.put("type", "lever");
    	
    	obj.put("isOn", this.isOn);
    }

}