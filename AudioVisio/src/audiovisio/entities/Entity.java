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

    private Geometry geometry;
    private RigidBodyControl physics;
    public Vector3f position;

    public Entity(){

    }
    
    public static Entity load(JSONObject obj){
		return null;
    }
    
    public static void save(JSONObject obj){
    	
    }

	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		
	}

}