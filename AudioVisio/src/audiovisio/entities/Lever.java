package audiovisio.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import org.json.simple.JSONObject;

public class Lever extends InteractableEntity {

    public static final  float LEVER_BOX_SIZE   = 0.3f;
    public static final  float LEVER_BOX_LENGTH = 1.3f;
    public static final  float LEVER_BOX_ANGLE  = (float) (Math.PI / 6.0);
    private static final float MASS             = 0f;

    private Box     shape;
    private Boolean isOn;

    public Lever(){
        this(new Vector3f(0f, 0f, 0f));
    }

    public Lever( Vector3f location ){
        this.shape = new Box(Lever.LEVER_BOX_SIZE, Lever.LEVER_BOX_LENGTH, Lever.LEVER_BOX_SIZE);
        this.geometry = new Geometry("lever", this.shape);
        this.geometry.setLocalRotation(new Quaternion().
                fromAngles(Lever.LEVER_BOX_ANGLE, 0, 0));

        this.geometry.setLocalTranslation(location);

        this.physics = new RigidBodyControl(Lever.MASS);
        this.geometry.addControl(this.physics);

        this.isOn = false;
    }

    public Lever( float x, float y, float z ){
        this(new Vector3f(x, y, z));
    }

    public void switchLever(){
        this.isOn = !this.isOn;
        if (this.isOn){
            this.turnedOnEvent();
            this.geometry.setLocalRotation(new Quaternion().fromAngles(0, Lever.LEVER_BOX_ANGLE, 0));
        }
        if (!this.isOn){
            this.turnedOffEvent();
            this.geometry.setLocalRotation(new Quaternion().fromAngles(0, -Lever.LEVER_BOX_ANGLE, 0));
        }
    }

    private void turnedOnEvent(){

    }

    private void turnedOffEvent(){

    }

    @Override
    public void load( JSONObject obj ){
        super.load(obj);
        this.isOn = (Boolean) obj.get("isOn");
    }

    @Override
    public void save( JSONObject obj ){
        super.save(obj);
        obj.put("type", "lever");

        obj.put("isOn", this.isOn);
    }


}