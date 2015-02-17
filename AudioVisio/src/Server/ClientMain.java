package Server;
//imports for jme3 server apps
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;






//import for creating geometric objects
import com.jme3.scene.Geometry;



import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;


//import for catching port exceptions
import java.io.IOException;
//import for server logs
import java.util.logging.Level;
import java.util.logging.Logger;






//import for network messaging
import AudioVisio.NetworkUtils.NetworkMessage;

public class ClientMain extends SimpleApplication{
	//Client instance variable
	private Client myClient;
	//Shares variables between threads
	//prevents message loss
	private ConcurrentLinkedQueue<String> messageQueue;
	
	protected Geometry player;
	Boolean isRunning = true;
	
	private Node shootables;
	  private Geometry mark;

	  private Spatial sceneModel;
	  private BulletAppState bulletAppState;
	  private RigidBodyControl landscape;
	  private RigidBodyControl button;
	  //private CharacterControl player;
	  private Vector3f walkDirection = new Vector3f();
	  private boolean up = false, down = false, left = false, right = false;
	  private ArrayList<Geometry> doorList = new ArrayList<Geometry>();
	  private Vector3f camDir = new Vector3f();
	  private Vector3f camLeft = new Vector3f();
	
	public static void main(String[] args){
		//intializes serializable variables from networkutils class
		NetworkUtils.initializeSerializables();
		//starts Client normally
		ClientMain app = new ClientMain();
		app.start();
	}
	//connects to local host, at PORT
	@Override
	public void simpleInitApp(){
		try{
			myClient = Network.connectToServer("127.0.0.1", NetworkUtils.PORT);
			myClient.start();
		}
		catch(IOException e){
			Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, e);
		}
		player = CreateGeometrics.createPlayer();
        rootNode.attachChild(player);
		initKeys();
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		//flyby camera
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		flyCam.setMoveSpeed(100);
		initKeys();
		initLight();

		//load scene from a zip file.
		assetManager.registerLocator("town.zip", ZipLocator.class);
		sceneModel = assetManager.loadModel("main.scene");
		sceneModel.setLocalScale(2f);

		// We set up collision detection for the scene by creating a
	    // compound collision shape and a static RigidBodyControl with mass zero.
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.setLocalScale(2f);

		//Create player collision detection
		//by creating a capsule collison shape and a CharacterControl to adjust extra settings
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
		player = new CharacterControl(capsuleShape, 0.05f);
		player.setJumpSpeed(20);
		player.setFallSpeed(30);
		player.setGravity(30);
		player.setPhysicsLocation(new Vector3f(0, 10, 0));

	  //create material for our button
	  Material pondMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"); //load the material & color
	  pondMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
	  pondMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
	  pondMat.setBoolean("UseMaterialColors",true);
	  pondMat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
	  pondMat.setColor("Specular",ColorRGBA.White); // for shininess
	  pondMat.setFloat("Shininess", 64f); // [1,128] for shininess

	  //create geometry for our button
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

	  //create a door
	  Box door = new Box(2,4,2);
	  Geometry doorGeometry = new Geometry("button", door);
	  doorGeometry.setMaterial(pondMat);
	  rootNode.attachChild(doorGeometry);

	  //position
	  doorGeometry.setLocalTranslation(new Vector3f(20f,10f,20f));

	  //make physics
	  RigidBodyControl doorPhysics = new RigidBodyControl(2f);

	  //add physics to space
	  doorGeometry.addControl(doorPhysics);
	  bulletAppState.getPhysicsSpace().add(doorPhysics);

	  doorList.add(doorGeometry);

		// Attach the scene and the player to the root and physics space so they show up in our world.
		rootNode.attachChild(sceneModel);
		bulletAppState.getPhysicsSpace().add(landscape);
		bulletAppState.getPhysicsSpace().add(player);
	  //bulletAppState.getPhysicsSpace().add(button);

	    initCrossHairs(); // a "+" in the middle of the screen to help aiming
	    //initKeys();       // load custom key mappings
	    initMark();       // a red sphere to mark the hit

	}
	//sends messages from messageQueue
	@Override
	public void simpleUpdate(float tpf){
		
//		if(isRunning){
//          player.rotate(3*tpf,2*tpf, 1*tpf);
//      }
		camDir.set(cam.getDirection().multLocal(0.6f));
		  camLeft.set(cam.getLeft()).multLocal(0.4f);

		  walkDirection.set(0, 0, 0);

		  if(up){
			  walkDirection.addLocal(camDir);
		  }
		  if(down){
			  walkDirection.addLocal(camDir.negate());
		  }
		  if(left){
			  walkDirection.addLocal(camLeft);
		  }
		  if(right){
			  walkDirection.addLocal(camLeft.negate());
		  }

		  player.setWalkDirection(walkDirection);
		  cam.setLocation(player.getPhysicsLocation());
		  
		String message = messageQueue.poll();
		if(message !=null){
			fpsText.setText(message);
		}
		//if no messages
		else{
			fpsText.setText("No message in queue.");
		}
	};
	
	private class NetworkMessageListener implements MessageListener<Client>{
		//runs seperate thread from renderer
		public void messageRecieved(Client source, Message m){
			if(m instanceof NetworkMessage){
				NetworkMessage message = (NetworkMessage) m;
			}
			
			messageQueue.add(message.getMessage());
		}
		
	}
	//must override client destroy method from super
	@Override
	public void destroy(){
		myClient.close();
		super.destroy();
	}

}