package audiovisio.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.utils.LogHelper;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import audiovisio.entities.*;
import audiovisio.level.*;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
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

public class Client extends SimpleApplication implements ActionListener,
		PhysicsCollisionListener {

	private com.jme3.network.Client myClient;

	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();

	public Client() {
		// super(new StatsAppState(), new FlyCamAppState(), new
		// DebugKeysAppState());
	}

	private Node shootables;
	private Geometry mark;

	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	private RigidBodyControl button;
	private Player player;
	private Vector3f walkDirection = new Vector3f();
	private boolean up = false, down = false, left = false, right = false;
	private ArrayList<Geometry> doorList = new ArrayList<Geometry>();

	// vectors that will be updated each frame,
	// so we don't have to make a new vector each frame.
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	public void simpleInitApp(String IP) {

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

		// ///////////////
		// Materials //
		// ///////////////
		Material pondMat = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md"); // load the material &
														// color
		pondMat.setTexture("DiffuseMap",
				assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));// located
																			// in
																			// jME3-testdata.jar
		pondMat.setTexture("NormalMap", assetManager
				.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
		pondMat.setBoolean("UseMaterialColors", true);
		pondMat.setColor("Diffuse", ColorRGBA.White); // minimum material color
		pondMat.setColor("Specular", ColorRGBA.White); // for shininess
		pondMat.setFloat("Shininess", 64f); // [1,128] for shininess

		Material randomMaterial = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		randomMaterial.setColor("Color", ColorRGBA.randomColor());
		
		Node myCharacter = (Node) assetManager
				.loadModel("Models/Oto/Oto.mesh.xml");

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

		// create geometry for our box
		Box box = new Box(2, 2, 2);
		Geometry buttonGeometry = new Geometry("box", box);
		buttonGeometry.setMaterial(pondMat);

		// position our box
		buttonGeometry.setLocalTranslation(new Vector3f(2f, 2f, 2f));

		// make box physics
		RigidBodyControl boxPhysics = new RigidBodyControl(0.1f);

		// add box physics to our space
		buttonGeometry.addControl(boxPhysics);
		shootables = new Node("Shootables");
		shootables.attachChild(buttonGeometry);

		Button testButton = new Button(0f, 1f, 0f);
		testButton.setMaterial(randomMaterial);
		
		player = new Player(myCharacter);

		// ///////////////////////
		// Initialization Methods //
		// ///////////////////////
		initCrossHairs(); // a "+" in the middle of the screen to help aiming
		initKeys(); // load custom key mappings
		initMark(); // a red sphere to mark the hit

		player.addToScene(rootNode, physicsSpace);
		testButton.addToScene(rootNode, physicsSpace);

		// ////////////////////////////
		// Add objects to rootNode //
		// ////////////////////////////
		rootNode.attachChild(buttonGeometry);
		rootNode.attachChild(shootables);
		rootNode.attachChild(sceneModel);

		rootNode.addLight(ambientLight);
		rootNode.addLight(directionalLight);

		// /////////////////////////////////
		// Add objects to physicsSpace //
		// /////////////////////////////////
		physicsSpace.add(boxPhysics);
		physicsSpace.addCollisionListener(this);
		physicsSpace.add(landscape);

	}

	private void initKeys() {
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

		inputManager.addMapping("Shoot", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));

		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Jump");

		inputManager.addListener(this, "Shoot");

	}

	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) {
				player.characterControl.jump();
			}
		}
		if (binding.equals("Shoot") && !isPressed) {
			CollisionResults results = new CollisionResults();

			Ray ray = new Ray(cam.getLocation(), cam.getDirection());

			shootables.collideWith(ray, results);

			if (results.size() > 0) {
				CollisionResult closest = results.getClosestCollision();
				// Let's interact - we mark the hit with a red dot.
				mark.setLocalTranslation(closest.getContactPoint());
				rootNode.attachChild(mark);

				Geometry collisionGeometry = closest.getGeometry();
				System.out.println("name: " + collisionGeometry.getName());
				if (collisionGeometry.getName().equals("button")) {
					System.out.println("name: "
							+ collisionGeometry.getMaterial());
					Material mat1 = new Material(assetManager,
							"Common/MatDefs/Misc/Unshaded.j3md");
					mat1.setColor("Color", ColorRGBA.randomColor());
					collisionGeometry.setMaterial(mat1);

					String boxName = collisionGeometry.getName();
					for (Geometry door : doorList) {
						if (door.getName().equals(boxName)) {
							rootNode.detachChild(door);
							bulletAppState.getPhysicsSpace().remove(
									door.getControl(0));
							break;
						}
					}
				}
			}
		}
	}

	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);

	}

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

	public void simpleInitApp() {
		simpleInitApp("127.0.0.1");
	}

	@Override
	public void simpleUpdate(float tpf) {

		String message = messageQueue.poll();
		if (message != null) {
			fpsText.setText(message);
		} else {
			fpsText.setText("No message in queue.");
		}

		camDir.set(cam.getDirection().multLocal(0.6f));
		camLeft.set(cam.getLeft()).multLocal(0.4f);

		walkDirection.set(0, 0, 0);

		if (up) {
			walkDirection.addLocal(camDir);
		}
		if (down) {
			walkDirection.addLocal(camDir.negate());
		}
		if (left) {
			walkDirection.addLocal(camLeft);
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate());
		}

		player.characterControl.setWalkDirection(walkDirection);
		cam.setLocation(player.characterControl.getPhysicsLocation());

	}

	@Override
	public void destroy() {
		myClient.close();
		super.destroy();
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		try {
			if ("button".equals(event.getNodeA().getName())){
				Geometry foundGeometry = (Geometry) event.getNodeA();
					Material randomMaterial = new Material(assetManager,
							"Common/MatDefs/Misc/Unshaded.j3md");
					randomMaterial.setColor("Color", ColorRGBA.randomColor());
					foundGeometry.setMaterial(randomMaterial);
				if("Oto-ogremesh".equals(event.getNodeB().getName())) {

					System.out.println("404, " + event.getNodeA().toString() + ", " + randomMaterial);
				}
			}
			if("button".equals(event.getNodeB().getName())) {
				Geometry foundGeometry = (Geometry) event.getNodeB();
					Material randomMaterial = new Material(assetManager,
							"Common/MatDefs/Misc/Unshaded.j3md");
					randomMaterial.setColor("Color", ColorRGBA.randomColor());
					foundGeometry.setMaterial(randomMaterial);
				if ("Oto-ogremesh".equals(event.getNodeA().getName())){

					System.out.println("413, " + event.getNodeB().toString() + ", " + randomMaterial);
				}
			}
		} catch (NullPointerException nullException) {

		}

	}

}