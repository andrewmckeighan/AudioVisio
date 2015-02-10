package Server;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.box;

public class CreateGeometrics {
	
	//used to create a box object server-side with a specific "material" and shape (vector3f)
	
	private Material m;
	private Box b;
	
	public CreateGeometrics(){
		m = new Material(assestManager, "Common/MatDefs/Misc/Unshaded.j3md");
		b = new Box(Vector3f.ZER0, new Vector3f(1,1,1));
	}
	
	public Geometry createBox(){
		Geometry box = new Geometry("box", b);
		box.setMaterial(m);
		return box;
	}

}