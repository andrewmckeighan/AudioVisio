package userInteraction;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/** A demo of how the user can select objects in space for interaction */
public class Main extends SimpleApplication{
	
	public static void main(String[] args){
		Main app = new Main();
		app.start();
	}
	private Node shootables;
	private Geometry mark;

	@Override
	public void simpleInitApp() {
		initCrossHair(); //draw the + in the middle of the screen
		initKeys(); //load key map
		initMark(); //load the place the user hit the object
		
		/** generate boxes, and floor */
		shootables = new Node("Shootables");
		rootNode.attachChild(shootables);
		shootables.attachChild(makeCube("Box1", -2f, 0f, 1f));
		shootables.attachChild(makeCube("Box2", -2f, 0f, 1f));
		shootables.attachChild(makeCube("Box3", -2f, 0f, 1f));
		shootables.attachChild(makeCube("Box4", -2f, 0f, 1f));
		shootables.attachChild(makeFloor());
		shootables.attachChild(makePlayer());
		
	}
	
	private Spatial makePlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	private Spatial makeFloor() {
		// TODO Auto-generated method stub
		return null;
	}

	private Spatial makeCube(String string, float f, float g, float h) {
		// TODO Auto-generated method stub
		return null;
	}

	private void initMark() {
		// TODO Auto-generated method stub
		
	}

	private void initCrossHair() {
		// TODO Auto-generated method stub
		
	}

	private void initKeys(){
		
	}

}
