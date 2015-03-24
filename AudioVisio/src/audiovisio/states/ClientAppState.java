package audiovisio.states;

import audiovisio.AudioVisio;
import audiovisio.WorldManager;
import audiovisio.entities.Button;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.networking.SyncManager;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.networking.messages.SyncCharacterMessage;
import audiovisio.networking.messages.SyncRigidBodyMessage;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
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
import com.jme3.network.NetworkClient;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import java.io.IOException;

//import audiovisio.networking.utilities.GeneralUtilities; //TODO is this needed?

/**
 * This class manages the client, sending messages to the server, and all
 * methods to work with Jmonkey's SimpleApplication.
 * 
 * @author Taylor Premo
 * @author James Larson
 * @author Matt Gerst
 */

public class ClientAppState extends AbstractAppState implements
		PhysicsCollisionListener {

	// Networking
	private AudioVisio AV;
	private InputManager IM;
	private AssetManager AM;
	public NetworkClient myClient = null;
	private WorldManager worldManager = null;
	private BitmapFont guiFont;

	// On Screen Message
	private CharSequence velocityMessage = "";
	private Vector3f oldLocation = null;
	private Vector3f newLocation = new Vector3f();
	private long oldTime = 0;
	private long newTime = 0;
	private int counter = 0;

	public ClientAppState() {
	}

	// public void startClient() {
	// this.setPauseOnLostFocus(false);
	// this.start();
	// }
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		NetworkUtils.initializeSerializables();
		AV = (AudioVisio) app;
        AM = AV.getAssetManager();
        IM = AV.getInputManager();
//		try {
			//myClient = Network.connectToServer(IP, NetworkUtils.getPort());
//			myClient = Network.connectToServer("127.0.0.1", NetworkUtils.getPort());
            myClient = Network.createClient();
//			myClient.start();
//		} catch (IOException e) {
//			LogHelper.severe("Error on client start", e);
//			System.exit(1);
//		}
		// ///////////
		// Physics //
		// ///////////
		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

		// ////////////////////
		// Load Scene (map) //
		// ////////////////////
		AM.registerLocator("town.zip", ZipLocator.class);
		Spatial sceneModel = AM.loadModel("main.scene");
		sceneModel.setLocalScale(2f);

		// /////////////////
		// Create Camera //
		// /////////////////
		AV.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		AV.getFlyByCamera().setMoveSpeed(100);

		// ////////////////
		// Sync Manager //
		// ////////////////
		SyncManager syncManager = new SyncManager(AV, myClient);
		syncManager.setMaxDelay(NetworkUtils.NETWORK_SYNC_FREQUENCY);
		syncManager.setMessageTypes(SyncCharacterMessage.class,
				SyncRigidBodyMessage.class, PlayerJoinMessage.class,
				PlayerLeaveMessage.class);
		stateManager.attach(syncManager);

		worldManager = new WorldManager(AV, this, AV.getRootNode());
		stateManager.attach(worldManager);
		syncManager.addObject(-1, worldManager);

		// ////////////
		// Lighting //
		// ////////////
		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.White.mult(1.3f));

		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(ColorRGBA.White);
		directionalLight.setDirection(new Vector3f(2.8f, -2.8f, -2.8f)
				.normalizeLocal());

		// We set up collision detection for the scene by creating a
		// compound collision shape and a static RigidBodyControl with mass
		// zero.
		CollisionShape sceneShape = CollisionShapeFactory
				.createMeshShape(sceneModel);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.setLocalScale(2f);

		// /////////////////////
		// Generate entities //
		// /////////////////////
		Button testButton = new Button(0f, 1f, 0f);
		testButton.createMaterial(AM);

		Lever testLever = new Lever(3f, 5f, 3f);
		testLever.createMaterial(AM);

		// worldManager.addPlayer(myClient.getId());
		// Player p = (Player) worldManager.getPlayer(myClient.getId());

		// initKeys(p);

		// //////////////////////////
		// Initialization Methods //
		// //////////////////////////
		// TODO may be moved to Player/elsewhere
		initCrossHairs(); // a "+" in the middle of the screen to help aiming
		// initMark(); // a red sphere to mark the hit

		// /////////////////////////
		// Add entities to Scene //
		// /////////////////////////
		// TODO this will probably be moved into WorldManager.
		// testButton.addToScene(rootNode, physicsSpace);
		// testLever.addToScene(rootNode, physicsSpace);

		// ///////////////////////////
		// Add objects to rootNode //
		// ///////////////////////////
		AV.getRootNode().attachChild(sceneModel);
		AV.getRootNode().addLight(ambientLight);
		AV.getRootNode().addLight(directionalLight);

		// ///////////////////////////////
		// Add objects to physicsSpace //
		// ///////////////////////////////
		physicsSpace.addCollisionListener(this);
		physicsSpace.add(landscape);
	}

	/**
	 * Initializes all variables used to run the client, is called by Jmonkey on
	 * this.start()
	 * 
	 * @param IP
	 *            The IP address of the server we are connecting to.
	 */

	public void simpleInitApp(String IP) {

	}

	public void simpleInitApp() {
		simpleInitApp("127.0.0.1");
	}

	/**
	 * Gives mappings to all hotkeys and actions by the user. Adds appropriate
	 * listeners to the player.
	 * 
	 * @param player
	 *            The player entity that is affected by this clients inputs.
	 */

	public void initKeys(Player player) {
		IM.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		IM.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		IM.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		IM.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		IM.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

		IM.addMapping("Shoot", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));

		IM.addListener(player, "Up");
		IM.addListener(player, "Down");
		IM.addListener(player, "Left");
		IM.addListener(player, "Right");
		IM.addListener(player, "Jump");

		IM.addListener(player, "Shoot");

	}

	/**
	 * Creates a sphere to show where on an object the player 'shoots' (where
	 * the ray from their camera collides with the closest valid object.) TODO
	 * may be moved.
	 */

	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		Geometry mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(AM, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);

	}

	/**
	 * Creates a '+' char on the center of the screen and adds it to the
	 * 'guiNode' TODO may be moved
	 */

	private void initCrossHairs() {
		AV.setDisplayStatView(false);
		guiFont = AM.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation(
		// center
		AV.getWidth() / 2 - ch.getLineWidth() / 2,
		AV.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		AV.getGuiNode().attachChild(ch);

	}

	/**
	 * Handles updates made to the client every frame.
	 * 
	 * Specifically, generates an on screen message: updateMessage(); and
	 * creates a message to send to the server with the players information.
	 * 
	 * @param tpf
	 *            time per frame
	 */

	@Override
	public void update(float tpf) {
		Player player = ((Player) worldManager.getPlayer(myClient.getId()));
		if (player != null) {

			updateMessage(player);

			SyncCharacterMessage msg = player.getSyncCharacterMessage();
			myClient.send(msg);
		}
	}

	/**
	 * Creates on screen text that displays the users velocity, distance moved
	 * since last update, current position, and the current frame #
	 */

	private void updateMessage(Player player) {

		if (counter % 1000 == 0) { // We only update once every 1000 frames so
									// that the player can actually move.
			assert newLocation != null;
			if (oldLocation != null && oldTime != 0 && newTime != 0) {
				oldLocation.distance(newLocation);
			}

			oldLocation = newLocation.clone();
			newLocation = player.getLocalTranslation();

			oldTime = newTime;
			newTime = System.currentTimeMillis();

			counter = 0;

			float distance = oldLocation.distance(newLocation);
			long time = newTime - oldTime;
			float velocity = distance / time;
			velocityMessage = ("ID: " + this.getId() + "V: " + velocity
					+ ", D: " + distance + ", P: " + newLocation + "F: " + counter);
		}

		AV.setFPSText(velocityMessage);
		counter++;
	}

	@Override
	public void cleanup() {
		myClient.close();
	}

	/**
	 * TODO rewrite Handles collisions of entities
	 * 
	 * @param event
	 *            The collision event containing the two nodes that have
	 *            collided.
	 */

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getNodeA().getParent() instanceof Entity
				&& event.getNodeB().getParent() instanceof Entity) {
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
		}

	}

	public long getId() {
		return this.myClient.getId();
	}

	public boolean isAudioClient() {
		return this.getId() % 2 == 0;
	}

	public boolean isVideoClient() {
		return !this.isAudioClient();
	}
}