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
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.network.Server;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;






//import for catching port exceptions
import java.io.IOException;
import java.util.ArrayList;
//import for server logs
import java.util.logging.Logger;






//import for creating geometric objects
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import Server.NetworkUtils;

//may throw an error if the port is being used
public class ServerMain extends SimpleApplication{
	//server instance variable
	private Server myServer;
	
	//protected Geometry player;
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
	  
	  private CharacterControl player;
	
	public static void main (String[] args){
		//intializes serializable variables from networkutils class
		NetworkUtils.initializeSerializables();
		//initiates a new server app with a headless type (no server messaging?)
		ServerMain app = new ServerMain();
		app.start(JmeContext.Type.Headless);
	}
	
	//initializes the server, throws exceptions (for ?)
	@Override
	public void simpleInitApp(){
		try{
			myServer = Network.createServer(NetworkUtils.PORT);
			myServer.start();
		}
		catch(IOException e){
			//submits the catch to server log
			Logger.getLogger(ServerMain.class.getName().log(Level.SEVERE, null, e));
		}
        player = CreateGeometrics.createPlayer();
        //rootNode.attachChild(player);
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
	
	@Override
	public void simpleUpdate(float tpf){
		
//		if(isRunning){
//            player.rotate(3*tpf,2*tpf, 1*tpf);
//        }
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
		Serializer.broadcast(new NetworkMessage("Hello World! server time is: " + tpf));
	}
	
	// must override destroy method from super
	@Override
	public void destroy(){
		myServer.close();
		super.destroy();
	}
	
	private void initKeys() {
	    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
	    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
	    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
	    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
	    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

	    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

	    inputManager.addListener(this, "Up");
	    inputManager.addListener(this, "Down");
	    inputManager.addListener(this, "Left");
	    inputManager.addListener(this, "Right");
	    inputManager.addListener(this, "Jump");

	    inputManager.addListener(this, "Shoot");
	  }
	
	private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("Pause") && !keyPressed){
                isRunning = !isRunning;
            }
        }
    }
	
	private void initLight(){
		  //add light so we can actually see.
		  AmbientLight al = new AmbientLight();
		  al.setColor(ColorRGBA.White.mult(1.3f));
		  rootNode.addLight(al);

		  DirectionalLight dl = new DirectionalLight();
		  dl.setColor(ColorRGBA.White);
		  dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		  rootNode.addLight(dl);
	  }
	
	public void onAction (String binding, boolean isPressed, float tpf){
		  if(binding.equals("Up")){
			  up = isPressed;
		  }else if(binding.equals("Down")){
			  down = isPressed;
		  }else if(binding.equals("Left")){
			  left = isPressed;
		  }else if(binding.equals("Right")){
			  right = isPressed;
		  }else if(binding.equals("Jump")){
			  if(isPressed){
				  player.jump();
			  }
		  }
	    if(binding.equals("Shoot") && !isPressed){
	      CollisionResults results = new CollisionResults();

	      Ray ray = new Ray(cam.getLocation(), cam.getDirection());

	      shootables.collideWith(ray, results);

	      if(results.size() > 0){
	        CollisionResult closest = results.getClosestCollision();
	        // Let's interact - we mark the hit with a red dot.
	        mark.setLocalTranslation(closest.getContactPoint());
	        rootNode.attachChild(mark);

	        Geometry collisionGeometry = closest.getGeometry();
	        System.out.println("name: " + collisionGeometry.getName());
	        if(collisionGeometry.getName().equals("button")){
	          System.out.println("name: " + collisionGeometry.getMaterial());
	          Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	          mat1.setColor("Color", ColorRGBA.randomColor());
	          collisionGeometry.setMaterial(mat1);

	          String boxName = collisionGeometry.getName();
	          for(Geometry door : doorList){
	            if(door.getName().equals(boxName)){
	              rootNode.detachChild(door);
	              bulletAppState.getPhysicsSpace().remove(door.getControl(0));
	              break;
	            }
	          }
	        }
	      }
	    }
	  }
	
	protected void initMark() {
	    Sphere sphere = new Sphere(30, 30, 0.2f);
	    mark = new Geometry("BOOM!", sphere);
	    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    mark_mat.setColor("Color", ColorRGBA.Red);
	    mark.setMaterial(mark_mat);
	  }

	  /** A centred plus sign to help the player aim. */
	  protected void initCrossHairs() {
	    setDisplayStatView(false);
	    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
	    BitmapText ch = new BitmapText(guiFont, false);
	    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
	    ch.setText("+"); // crosshairs
	    ch.setLocalTranslation( // center
	      settings.getWidth() / 2 - ch.getLineWidth()/2, settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
	    guiNode.attachChild(ch);
	  }
		
}
