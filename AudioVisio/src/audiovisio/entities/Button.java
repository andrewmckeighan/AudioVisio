package audiovisio.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import org.json.simple.JSONObject;

/**
 * //TODO this needs to be easy to walk over! (maybe no physical collision?)
 */
public class Button extends InteractableEntity {

    public final float MASS = 0f;
    public Cylinder         shape;
    // rootNode.attachChild(this.geometry);
    public RigidBodyControl collision; // (physics)
    // RigidBodyControl (CollisionShape shape, float mass)
    // buttonGeometry.addControl(collision)

    public Button(){
        this(new Vector3f(0f, 0f, 0f));
    }

    public Button( Vector3f location ){
        this.shape = new Cylinder(8, 8, 2.0f, 0.2f, true);

        this.geometry = new Geometry("button", this.shape);
        this.geometry.setLocalRotation(new Quaternion().fromAngles(
                (float) Math.PI / 2, 0, 0));
        this.geometry.setLocalTranslation(location);

        this.physics = new RigidBodyControl(this.MASS);
        this.geometry.addControl(this.physics);

        this.attachChild(this.geometry);

    }

    public Button( float x, float y, float z ){
        this(new Vector3f(x, y, z));
    }

    public Geometry getGeometry(){
        return this.geometry;
    }

    public Vector3f getPos(){
        return this.position;
    }

    public void setPos( Vector3f position ){
        this.position = position;
    }

    @Override
    public void save( JSONObject obj ){
        super.save(obj);
        obj.put("type", "button");
    }

    //    @Override
    private void update( Boolean state ){
        if (this.state == false){
            if (state == true){
                this.startPress();
            }
        } else {
            if (state == false){
                this.stopPress();
            }
        }

        this.state = state;
    }

    public void startPress(){
        //particles for startPress
        //change color of button to something
    }

    public void stopPress(){
        //particles for stopPress
        //change the color again.

    }
}