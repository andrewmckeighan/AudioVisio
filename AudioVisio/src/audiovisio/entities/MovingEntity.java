package audiovisio.entities;

import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

public class MovingEntity extends Entity {

    protected Vector3f frontDirection;
    protected Vector3f leftDirection;
    protected Vector3f moveDirection;

    public MovingEntity(){

    }

    public void load( JSONObject loadObj ){
        super.load(loadObj);

        this.frontDirection = (Vector3f) loadObj.get("frontDirection");
        this.leftDirection = (Vector3f) loadObj.get("leftDirection");
        this.moveDirection = (Vector3f) loadObj.get("moveDirection");
    }

}