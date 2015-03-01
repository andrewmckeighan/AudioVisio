package audiovisio.networking;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import audiovisio.entities.Button;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.networking.listeners.ClientMessageListener;
import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.networking.utilities.GeneralUtilities;
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
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class Client extends SimpleApplication implements PhysicsCollisionListener {

	private com.jme3.network.Client myClient;
	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
	private Geometry mark;
	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	private Player currentPlayer;
	private Player networkedPlayer;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();
	private Vector3f oldLocation;
	private Vector3f newLocation = new Vector3f();
	private long oldTime;
	private long newTime;
	private int counter = 0;

	ClientMessageListener messageListener = new ClientMessageListener(this);
	NetworkMessage velocityMessage = new NetworkMessage("");
	
	/**
	 * Default client constructor
	 */
	public Client() {
		
	}

	/**
	 * Client Initialization
	 * @param IP Specified server connection IP address
	 */
	public void simpleInitApp(String IP) {
		GeneralUtilities.initializeSerializables();
		
		try {
			myClient = Network.connectToServer(IP, GeneralUtilities.getPort());
			myClient.start();
		} catch (IOException e) {
			LogHelper.severe("Error on client start", e);
			System.exit(1);
		}

		// /////////////////////
		// Load Scene (map) //
		// /////////////////////
		assetManager.registerLocator("town.zip", ZipLocator.class);
		sceneModel = assetManager.loadModel("main.scene");
		sceneModel.setLocalScale(2f);

		// ///////////////////
		// Create Camera //
		// ///////////////////
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		flyCam.setMoveSpeed(100);

		// //////////////
		// Lighting //
		// //////////////
		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.White.mult(1.3f));

		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(ColorRGBA.White);
		directionalLight.setDirection(new Vector3f(2.8f, -2.8f, -2.8f)
		.normalizeLocal());

		// /////////////
		// Physics //
		// /////////////
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

		// We set up collision detection for the scene by creating a
		// compound collision shape and a static RigidBodyControl with mass
		// zero.
		CollisionShape sceneShape = CollisionShapeFactory
				.createMeshShape((Node) sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.setLocalScale(2f);

		/////////////////////////
		// Generate entities //
		/////////////////////////
		Button testButton = new Button(0f, 1f, 0f);
		testButton.createMaterial(assetManager);

		Lever testLever = new Lever(3f, 5f, 3f);
		testLever.createMaterial(assetManager);

		currentPlayer = new Player();
		currentPlayer.setCam(cam);
		currentPlayer.createModel(assetManager);

		
		networkedPlayer = new Player();
		networkedPlayer.createModel(assetManager);

		// ///////////////////////
		// Initialization Methods //
		// ///////////////////////
		initCrossHairs(); // a "+" in the middle of the screen to help aiming
		initKeys(); // load custom key mappings
		initMark(); // a red sphere to mark the hit

		///////////////////////////
		//Add entities to Scene //
		///////////////////////////
		currentPlayer.addToScene(rootNode, physicsSpace);
		networkedPlayer.addToScene(rootNode, physicsSpace);

		testButton.addToScene(rootNode, physicsSpace);
		testLever.addToScene(rootNode, physicsSpace);

		// ////////////////////////////
		// Add objects to rootNode //
		// ////////////////////////////
		rootNode.attachChild(sceneModel);
		rootNode.addLight(ambientLight);
		rootNode.addLight(directionalLight);

		// /////////////////////////////////
		// Add objects to physicsSpace //
		// /////////////////////////////////
		physicsSpace.addCollisionListener(this);
		physicsSpace.add(landscape);

		myClient.addMessageListener(messageListener);
	}

	/**
	 * Client initialization using default constructor
	 */
	public void simpleInitApp() {
		simpleInitApp("127.0.0.1");
	}
	
	/**
	 * Initialization for key mapping
	 */
	private void initKeys() {
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

		inputManager.addMapping("Shoot", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));

		inputManager.addListener(currentPlayer, "Up");
		inputManager.addListener(currentPlayer, "Down");
		inputManager.addListener(currentPlayer, "Left");
		inputManager.addListener(currentPlayer, "Right");
		inputManager.addListener(currentPlayer, "Jump");

		inputManager.addListener(currentPlayer, "Shoot");

	}

	/**
	 * Initialization for ball that shows where the player hit the given object
	 */
	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);

	}
	
	/**
	 * Initialization for cross-hairs
	 */
	private void initCrossHairs() {
		setDisplayStatView(false);
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation(

		// center
		settings.getWidth() / 2 - ch.getLineWidth() / 2,
		settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);

	}
	
	/**
	 * Updates App to current status
	 * Generates position from user input/server messages
	 */
	@Override
	public void simpleUpdate(float tpf) {
		updateFpsText();
		PlayerSendMovementMessage sendMessage = currentPlayer.getUpdateMessage(camDir, camLeft);
		myClient.send(sendMessage);
		currentPlayer.update(sendMessage);
		//player.update(cam, receivedMessage);
		
		updateVelocityMessage();
	}

	/**
	 * Updates displayed generic server text
	 */
	private void updateFpsText(){
		String message = messageQueue.poll();
		if (message != null) {
			fpsText.setText(message);
		} else {
			fpsText.setText("No message in queue.");
		}
	}

	/**
	 * Updates displayed velocity text
	 */
	private void updateVelocityMessage(){

		if (counter % 1000 == 0) {
			if (oldLocation != null && newLocation != null && oldTime != 0
					&& newTime != 0) {
				oldLocation.distance(newLocation);
			}

			oldLocation = newLocation.clone();
			newLocation = currentPlayer.characterControl.getPhysicsLocation();

			oldTime = newTime;
			newTime = System.currentTimeMillis();

			counter = 0;
		}

		//messageListener.NetworkMessageHandler(velocityMessage);
		counter++;
	}

	/**
	 * Override server default destruction
	 */
	@Override
	public void destroy() {
		myClient.close();
		super.destroy();
	}
	
	/**
	 * collision handling
	 */
	@Override
	public void collision(PhysicsCollisionEvent event) {
		try {
			Entity entityA = (Entity) event.getNodeA().getParent();
			Entity entityB = (Entity) event.getNodeB().getParent();
			entityA.collisionTrigger(entityB);
			entityB.collisionTrigger(entityA);
			if ("button".equals(event.getNodeA().getName())) {

				if ("Oto-ogremesh".equals(event.getNodeB().getName())) {
					Button b = (Button) event.getNodeA().getParent();
					b.startPress();
				}
			}
			if ("button".equals(event.getNodeB().getName())) {

				if ("Oto-ogremesh".equals(event.getNodeA().getName())) {
					Button b = (Button) event.getNodeB().getParent();
					b.startPress();
				}
			}
		} catch (NullPointerException nullException) {
			// System.out.println("nullException Caught: " + nullException);
		} catch (ClassCastException castException) {
			LogHelper.warn("castException Caught: ", castException);
			System.out.println("castException Caught: " + castException);
		}

	}

	public Player getPlayer() {
		return currentPlayer;
	}

	public void updatePlayer(PlayerLocationMessage msg) {
		try {
			if(msg.getPlayerID() == myClient.getId()){
				currentPlayer.update(msg);
			}else{
				networkedPlayer.update(msg);
			}
		} catch (NullPointerException e) {
			LogHelper.warn("null exception, client cannot find player: ", e);
		}
	}
}