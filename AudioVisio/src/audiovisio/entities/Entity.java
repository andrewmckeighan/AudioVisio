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
        this.position = (Vector3f) obj.get("geometry");
        this.name = (String) obj.get("name");
    }

    public static void save(JSONObject obj){

    }

	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub

	}

}