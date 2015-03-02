package audiovisio.entities;

import org.json.simple.JSONObject;

import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.utils.LogHelper;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

public class Player extends MovingEntity implements ActionListener {

	private final static Vector3f SPAWN_LOCATION = new Vector3f(0, 5, 0);
	private final static String DEFAULT_MODEL = "Models/Oto/Oto.mesh.xml";
	private static final Vector3f CAMERA_OFFSET = new Vector3f(0, 5, 0);

	public BetterCharacterControl characterControl;
	private Camera playerCamera;
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;

	public Node model;
	public Mesh mesh;
	private Vector3f savedLocation;

	/**
	 * creates a player with a given model, and spawn point.
	 * 
	 * @param playerModel
	 * @param spawnLocation
	 */
	public Player(Node playerModel, Vector3f spawnLocation) {
		if (playerModel != null) {
			this.model = playerModel;
			this.model.setLocalScale(0.2f);
			this.model.setLocalTranslation(spawnLocation);
			this.attachChild(this.model);
		}

		this.characterControl = new BetterCharacterControl(.9f, 1.9f, 800f);
		characterControl.setJumpForce(new Vector3f(0, 0, 0));
		characterControl.setGravity(new Vector3f(0, -1, 0));

		this.characterControl.warp(spawnLocation);
		this.setLocalTranslation(spawnLocation);
		this.addControl(this.characterControl);
		
		this.move(new Vector3f());
	}

	/**
	 * creates a player with a given model
	 * 
	 * @param playerModel
	 */
	public Player(Node playerModel) {
		this(playerModel, SPAWN_LOCATION);
	}

	/**
	 * Creates the collision for player, and sets the physics parameters for the
	 * player.
	 */
	public Player() {
		this(null, SPAWN_LOCATION);
	}

	/**
	 * generates the default model for the player.
	 * 
	 * @param assetManager
	 */
	public void createModel(AssetManager assetManager) {
		Node myCharacter = (Node) assetManager.loadModel(DEFAULT_MODEL);
		this.model = myCharacter;

		this.model.setLocalScale(0.5f);
		this.model.setLocalTranslation(SPAWN_LOCATION);

		// this.model.addControl(this.characterControl);
		this.attachChild(this.model);
	}

	/**
	 * adds the player to the rootNode and PhysicsSpace of the client.
	 * 
	 * @param root
	 * @param physics
	 */
	public void addToScene(Node root, PhysicsSpace physics) {
		addToScene(root);
		if(this.model != null){
			root.attachChild(this.model);
		}
		physics.add(this);
		//physics.add(this.characterControl);
	}

	public void load(JSONObject obj) {
		super.load(obj);

		// TODO
	}

	/**
	 * action handler for when the user moves/shoots
	 * 
	 * @param binding
	 *            the keyword for the action.
	 * @param isPressed
	 * @param tpf
	 *            time per frame
	 */
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
				characterControl.jump();
			}
		}
		if (binding.equals("Shoot") && !isPressed) {
			// TODO
			// CollisionResults results = new CollisionResults();

			// Ray ray = new Ray(cam.getLocation(), cam.getDirection());

			/*
			 * shootables.collideWith(ray, results);
			 * 
			 * if(results.size() > 0){ CollisionResult closest =
			 * results.getClosestCollision(); // Let's interact - we mark the
			 * hit with a red dot.
			 * mark.setLocalTranslation(closest.getContactPoint());
			 * rootNode.attachChild(mark);
			 * 
			 * Geometry collisionGeometry = closest.getGeometry();
			 * System.out.println("name: " + collisionGeometry.getName());
			 * if(collisionGeometry.getName().equals("button")){
			 * System.out.println("name: " + collisionGeometry.getMaterial());
			 * Material mat1 = new Material(assetManager,
			 * "Common/MatDefs/Misc/Unshaded.j3md"); mat1.setColor("Color",
			 * ColorRGBA.randomColor()); collisionGeometry.setMaterial(mat1);
			 * 
			 * String boxName = collisionGeometry.getName(); for(Geometry door :
			 * doorList){ if(door.getName().equals(boxName)){
			 * rootNode.detachChild(door);
			 * bulletAppState.getPhysicsSpace().remove(door.getControl(0));
			 * break; } } } }
			 */
		}
	}

	/**
	 * updates the players position based on the message received from the
	 * server
	 * 
	 * @param cam
	 *            players camera
	 * @param walkDirection
	 *            direction the player is going to move
	 */
	public void update(Vector3f position, Vector3f direction) {
		this.savedLocation = position;

		this.characterControl.warp(this.savedLocation);
		this.characterControl.setWalkDirection(direction);

		if (this.playerCamera != null) {
			this.playerCamera.setLocation(this.getLocalTranslation().add(CAMERA_OFFSET));
		}

	}

	/**
	 * Updates the players direction based on the message received from the
	 * client
	 * 
	 * @param msg
	 *            The message sent from the client
	 */
	public void update(PlayerSendMovementMessage msg) {
		this.update(msg.getPosition(), msg.getDirection());
	}

	public void update(PlayerLocationMessage msg) {
		this.update(msg.getPosition(), msg.getDirection());
	}

	/**
	 * generates where the player is trying to move based on keypresses, and
	 * returns that message.
	 * 
	 * @param cam
	 *            camera that the player uses.
	 * @param camDir
	 *            vector directed straight forward of the player
	 * @param camLeft
	 *            vector directed straight left of the player
	 * @return the message that is sent to the server
	 */
	public PlayerSendMovementMessage getUpdateMessage() {
		this.camDir.set(this.playerCamera.getDirection().multLocal(20.6f));
		this.camLeft.set(this.playerCamera.getLeft()).multLocal(20.4f);

		Vector3f walkDirection = new Vector3f(0, 0, 0);

		if (this.up) {
			walkDirection.addLocal(this.camDir);
		}
		if (this.down) {
			walkDirection.addLocal(this.camDir.negate());
		}
		if (this.left) {
			walkDirection.addLocal(this.camLeft);
		}
		if (this.right) {
			walkDirection.addLocal(this.camLeft.negate());
		}

		return new PlayerSendMovementMessage(this.getLocalTranslation(),
				walkDirection);
	}

	public PlayerLocationMessage getLocationMessage(int ID) {
		return new PlayerLocationMessage(ID, this.getLocalTranslation(),
				this.characterControl.getWalkDirection());
	}

	public Camera getCam() {
		return this.playerCamera;
	}

	public void setCam(Camera cam) {
		this.playerCamera = cam;
	}

	public void updateLocalTranslation() {
		this.setLocalTranslation(this.savedLocation);
		if (this.model != null) {
			this.model.setLocalTranslation(this.savedLocation);
		}
	}

	public void updateCam() {
		if (this.playerCamera != null) {
			this.playerCamera.setLocation(this.getWorldTranslation().add(CAMERA_OFFSET));
		}
	}

	public void updateModel() {
		if (this.model != null) {
			this.model.setLocalTranslation(this.getWorldTranslation());
		}
	}

}