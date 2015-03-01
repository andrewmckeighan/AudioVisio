package audiovisio.entities;

import org.json.simple.JSONObject;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;

//create geometry for our button
// Box box = new Box(2,2,2);
// Geometry buttonGeometry = new Geometry("button", box);
// buttonGeometry.setMaterial(pondMat);
// rootNode.attachChild(buttonGeometry);

// //position our button
// buttonGeometry.setLocalTranslation(new Vector3f(2f,2f,2f));

// //make button physics
// RigidBodyControl buttonPhysics = new RigidBodyControl(2f);

// //add button physics to our space
// buttonGeometry.addControl(buttonPhysics);
// bulletAppState.getPhysicsSpace().add(buttonPhysics);

// shootables = new Node("Shootables");
// rootNode.attachChild(shootables);
// shootables.attachChild(buttonGeometry);

public class Button extends InteractableEntity  {

	public final float MASS = 0f;

	public Cylinder shape;

	// rootNode.attachChild(this.geometry);

	public RigidBodyControl collision; // (physics)

	// RigidBodyControl (CollisionShape shape, float mass)
	// buttonGeometry.addControl(collision)

	public Button() {
		this(new Vector3f(0f, 0f, 0f));
	}

	public Button(float x, float y, float z){
		this(new Vector3f(x, y, z));
	}

	public Button(Vector3f location) {
		this.shape = new Cylinder(8, 8, 2.0f, 0.2f, true);

		this.geometry = new Geometry("button", this.shape);
		this.geometry.setLocalRotation(new Quaternion().fromAngles(
				(float) Math.PI / 2, 0, 0));
		this.geometry.setLocalTranslation(location);

		this.physics = new RigidBodyControl(MASS);
		this.geometry.addControl(this.physics);
		
		this.attachChild(this.geometry);

	}

	public void startPress() {
	}

	public void stopPress() {

	}

	public Geometry getGeometry() {
		return this.geometry;
	}

	public Vector3f getPos() {
		return this.position;
	}

	public void setPos(Vector3f position) {
		this.position = position;
	}

	@Override
	public void save(JSONObject obj) {
		super.save(obj);
		obj.put("type", "button");
	}
}