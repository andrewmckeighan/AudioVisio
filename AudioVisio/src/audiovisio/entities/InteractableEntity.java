package audiovisio.entities;

import org.json.simple.JSONObject;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class InteractableEntity extends Entity {

    private InteractableEntity linkedEntity;
    public boolean stuck; //if the entity keeps it state regardless of triggerEvents

    public Geometry geometry;
    public Material material;
    public RigidBodyControl physics;


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

    public void setMaterial(Material mat) {
        this.geometry.setMaterial(mat);
    }

    public void addToScene(Node root, PhysicsSpace physics) {
        addToScene(root);
    	physics.add(this.physics);
    }

}