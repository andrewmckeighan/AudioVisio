package audiovisio.entities;

import org.json.simple.JSONObject;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;


/*
//creat geometry for our button
  Box box = new Box(2,2,2);
  Geometry buttonGeometry = new Geometry("button", box);
  buttonGeometry.setMaterial(pondMat);
  rootNode.attachChild(buttonGeometry);

  //position our button
  buttonGeometry.setLocalTranslation(new Vector3f(2f,2f,2f));

  //make button physics
  RigidBodyControl buttonPhysics = new RigidBodyControl(2f);

  //add button physics to our space
  buttonGeometry.addControl(buttonPhysics);
  bulletAppState.getPhysicsSpace().add(buttonPhysics);

  shootables = new Node("Shootables");
  rootNode.attachChild(shootables);
  shootables.attachChild(buttonGeometry);
 */
public class Button extends InteractableEntity {

    private Cylinder shape;
    private Geometry geometry;
    private Material material;
    //rootNode.attachChild(this.geometry);

    private RigidBodyControl collision; //(physics)
    //RigidBodyControl  (CollisionShape shape, float mass)
    //buttonGeometry.addControl(collision)

    public Button(){

    }

    private void buttonStartPress(){

    }

    private void buttonStopPress(){

    }

    public Geometry getGeometry(){
        return this.geometry;
    }

    public Vector3f getPos(){
        return this.position;
    }

    public void setPos(Vector3f position){
        this.position = position;
    }
    
    @Override
    public void save(JSONObject obj) {
    	super.save(obj);
    	obj.put("type", "button");
    }
}