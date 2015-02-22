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

import org.json.simple.JSONObject;

import audiovisio.utils.JSONHelper;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;

public class Entity extends SimpleApplication{

    protected Geometry geometry;
    private RigidBodyControl physics;
    public Vector3f position;
    public String name;

    public Entity(){

    }

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

	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub

	}

}