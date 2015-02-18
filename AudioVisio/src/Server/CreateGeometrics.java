package Server;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class CreateGeometrics {
	
	//used to create a box object server-side with a specific "material" and shape (vector3f)
	
	private Material m;
	private Box b;
	
//	public CreateGeometrics(){
//		m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		b = new Box(Vector3f.ZERO, new Vector3f(1,1,1));
//	}
	
	public Geometry createBox(){
		Geometry box = new Geometry("box", b);
		box.setMaterial(m);
		return box;
	}
	
	public static CharacterControl createPlayer(){
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
		CharacterControl player = new CharacterControl(capsuleShape, 0.05f);
		return player;
	}
	
//	 protected Geometry makeCube(String name, float x, float y, float z) {
//		    Box box = new Box(2, 2, 2);
//		    Geometry cube = new Geometry(name, box);
//		    cube.setLocalTranslation(x, y, z);
//		    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		    mat1.setColor("Color", ColorRGBA.randomColor());
//		    cube.setMaterial(mat1);
//		    return cube;
//		  }
	 
//	 protected Geometry makeFloor() {
//		    Box box = new Box(15, .2f, 15);
//		    Geometry floor = new Geometry("the Floor", box);
//		    floor.setLocalTranslation(0, -4, -5);
//		    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		    mat1.setColor("Color", ColorRGBA.Gray);
//		    floor.setMaterial(mat1);
//		    return floor;
//		  }

}