package audiovisio.networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import audiovisio.entities.Button;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerDirectionMessage;
import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.networking.utilities.ServerPlayerDirectionMessageListener;
import audiovisio.utils.LogHelper;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Server extends SimpleApplication implements PhysicsCollisionListener, ActionListener{
	private com.jme3.network.Server myServer;

	public Server(){

	}


	private Node shootables;
	private Geometry mark;
	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	private RigidBodyControl button;
	private Player audioPlayer;
	private Player visualPlayer;
	private Vector3f oldAudioLocation;
	private Vector3f oldVisualLocation;
	private Vector3f newAudioLocation = new Vector3f();
	private Vector3f newVisualLocation = new Vector3f();
	private long oldTime;
	private long newTime;
	private long time;
	private int counter = 0;
	private	float audioVelocity = 0;
	private	float visualVelocity = 0;
	private	float audioDistance = 0;
	private	float visualDistance = 0;
	private	Vector3f audioPosition = new Vector3f();
	private	Vector3f visualPosition = new Vector3f();
	private ServerPlayerDirectionMessageListener pDML= new ServerPlayerDirectionMessageListener(this);

	@Override
	public void simpleInitApp(){
		try{
			myServer = Network.createServer(GeneralUtilities.getPort());
			myServer.start();
		}
		catch(IOException e){
			LogHelper.severe("Error on server start", e);
			System.exit(1);
		}
		
		GeneralUtilities.initializeSerializables();

		assetManager.registerLocator("town.zip", ZipLocator.class);
		sceneModel = assetManager.loadModel("main.scene");
		sceneModel.setLocalScale(2f);

		CollisionShape sceneShape = CollisionShapeFactory
				.createMeshShape((Node) sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.setLocalScale(2f);

		// create geometry for our box
		Box box = new Box(2, 2, 2);
		Geometry boxGeometry = new Geometry("box", box);

		// position our box
		boxGeometry.setLocalTranslation(new Vector3f(2f, 2f, 2f));

		// make box physics
		RigidBodyControl boxPhysics = new RigidBodyControl(0.1f);

		// add box physics to our space
		boxGeometry.addControl(boxPhysics);
		shootables = new Node("Shootables");
		shootables.attachChild(boxGeometry);

		Button testButton = new Button(0f, 1f, 0f);

		Lever testLever = new Lever(3f, 5f, 3f);
		shootables.attachChild(testLever.geometry);

		setAudioPlayer(new Player(audioPlayer));
		setVisualPlayer(new Player(visualPlayer));

		// ///////////////////////
		// Initialization Methods //
		// ///////////////////////
		
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

		getAudioPlayer().addToScene(rootNode, physicsSpace);
		getVisualPlayer().addToScene(rootNode, physicsSpace);
		testButton.addToScene(rootNode, physicsSpace);
		testLever.addToScene(rootNode, physicsSpace);

		// ////////////////////////////
		// Add objects to rootNode //
		// ////////////////////////////
		//rootNode.attachChild(boxGeometry);
		//rootNode.attachChild(shootables);
		rootNode.attachChild(sceneModel);
		
		

		// /////////////////////////////////
		// Add objects to physicsSpace //
		// /////////////////////////////////
		//physicsSpace.add(boxPhysics);
		physicsSpace.addCollisionListener(this);
		physicsSpace.add(landscape);



	}

	@Override
	public void simpleUpdate(float tpf){
		pDML.messageReceived();
		
		Vector3f walkDirection = new Vector3f(0, 0, 0);
		
		player.setWalkDirection(walkDirection);

		if(counter % 1000 == 0){
			if (oldLocation != null
					&& newLocation != null
					&& oldTime != 0
					&& newTime != 0) {
				distance = oldLocation.distance(newLocation);
				time = newTime - oldTime;
				velocity = distance / time;
				velocityMessage = new NetworkMessage("V: " + velocity +
						", D: " + distance +
						", P: " + newLocation +
						"F: " + counter);
			}

			oldLocation = newLocation.clone();
			newLocation = player.characterControl.getPhysicsLocation();


			oldTime = newTime;
			newTime = System.currentTimeMillis();

			counter = 0;
		}


	}

	@Override
	public void destroy(){
		myServer.close();
		super.destroy();
	}

	public Player getAudioPlayer() {
		return audioPlayer;
	}

	public Player getVisualPlayer() {
		return visualPlayer;
	}

	public void setAudioPlayer(Player audioPlayer) {
		this.audioPlayer = audioPlayer;
	}
	
	public void setVisualPlayer(Player visualPlayer) {
		this.visualPlayer = visualPlayer;
	}

	@Override
	public void onAction(String arg0, boolean arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

