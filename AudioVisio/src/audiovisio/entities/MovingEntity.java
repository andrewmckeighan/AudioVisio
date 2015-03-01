package audiovisio.entities;

import org.json.simple.JSONObject;

import com.jme3.math.Vector3f;

public class MovingEntity extends Entity {

	protected Vector3f frontDirection;
	protected Vector3f leftDirection;
    protected Vector3f moveDirection;

    public MovingEntity(){

    }

    public void load(JSONObject obj){
        super.load(obj);

        this.frontDirection = (Vector3f) obj.get("frontDirection");
        this.leftDirection = (Vector3f) obj.get("leftDirection");
        this.moveDirection = (Vector3f) obj.get("moveDirection");
    }

}