/*
 * Entity
 *
 * Version information
 *
 * 02/18/15
 *
 * Copyright notice
 */

package audiovisio.entities;

import audiovisio.level.ILevelItem;
import org.json.simple.JSONObject;

import audiovisio.utils.JSONHelper;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Entity extends Node implements ILevelItem{

    public Geometry geometry;
    private RigidBodyControl physics;
    public Vector3f position;
    public String name;
    public Spatial model;
    
    protected long ID = -2;

    public Entity(){

    }

    /**
     * Create and instance of Entity class, 
     * @param obj the JSON object that is read.
     */
    public void load(JSONObject obj){
    	JSONObject location = (JSONObject) obj.get("location");
        this.position = JSONHelper.readVector3f(location);
        this.name = (String) obj.get("name");
    }

    @SuppressWarnings("unchecked")
    public void save(JSONObject obj){
    	JSONObject location = JSONHelper.saveVector3f(this.position);
    	obj.put("location", location);
    	obj.put("name", this.name);
    }

    /**
     * add the entities to rootNode so they are rendered in the game space.
     * @param root rootNode from simpleApplication
     */
    public void addToScene(Node root){
        root.attachChild(this);
    }

    /**
     * TODO: checks if the two collisions are meaningful.
     * @param entityB the entity that this is colliding with.
     */
	public void collisionTrigger(Entity entityB) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the iD
	 */
	public long getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(long iD) {
		ID = iD;
	}

    @Override
    public String toString(){
        return "Entity[" + this.ID + "]";
    }
}