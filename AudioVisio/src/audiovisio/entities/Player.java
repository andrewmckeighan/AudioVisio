package audiovisio.entities;

import org.json.simple.JSONObject;

import audiovisio.networking.messages.PlayerMoveMessage;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

public class Player extends MovingEntity implements ActionListener{

    private final static float STEP_HEIGHT = 0.05f;
    private final static Vector3f SPAWN_LOCATION = new Vector3f(0,30,0);
    private final static String DEFAULT_MODEL = "Models/Oto/Oto.mesh.xml";

    public CharacterControl characterControl;
    public Camera mainCamera;
    public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;

    private CapsuleCollisionShape collisionShape;
    public Node node;
	public Mesh mesh;

	/**
	 * Creates the collision for player, and sets the physics perameters for the player.
	 */
	public Player() {
        this.collisionShape = new CapsuleCollisionShape(1.5f, 6f, 1);

        this.characterControl = new CharacterControl(this.collisionShape, STEP_HEIGHT);
        this.characterControl.setJumpSpeed(20);
        this.characterControl.setFallSpeed(30);
        this.characterControl.setGravity(30);
    }

	/**
	 * creates a player with a given model, and spawn point.
	 * @param playerModel
	 * @param spawnLocation
	 */
    public Player(Node playerModel, Vector3f spawnLocation){
    	this();

        this.node = playerModel;
        this.node.setLocalScale(0.2f);
        this.node.setLocalTranslation(spawnLocation);

        this.node.addControl(this.characterControl);
    }
    
    /**
     * creates a player with a given model
     * @param playerModel
     */
    public Player(Node playerModel){
        this(playerModel, SPAWN_LOCATION);
    }

    /**
     * adds the player to the rootNode and PhysicsSpace of the client.
     * @param root
     * @param physics
     */
    public void addToScene(Node root, PhysicsSpace physics){
    	addToScene(root);
        physics.add(this.characterControl);
    }

    public void load(JSONObject obj){
        super.load(obj);

        //TODO
    }

    /**
     * moves the character in the direction of the vector3f
     * @param direction
     */
    public void setWalkDirection(Vector3f direction){
        this.moveDirection = direction;
        this.characterControl.setWalkDirection(direction);
    }

    /**
     * action handler for when the user moves/shoots
     * @param binding the keyword for the action.
     * @param isPressed
     * @param tpf time per frame
     */
    public void onAction(String binding, boolean isPressed, float tpf){
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
                characterControl.jump();
            }
        }
        if(binding.equals("Shoot") && !isPressed){
        	//TODO
            //CollisionResults results = new CollisionResults();

            //Ray ray = new Ray(cam.getLocation(), cam.getDirection());

            /*
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
            }*/
        }
    }

    /**
     * generates the default model for the player.
     * @param assetManager
     */
    public void createModel(AssetManager assetManager) {
		Node myCharacter = (Node) assetManager
				.loadModel(DEFAULT_MODEL);
		this.model = myCharacter;
		
		this.node = myCharacter;
		
		this.node.setLocalScale(0.2f);
        this.node.setLocalTranslation(SPAWN_LOCATION);

        this.node.addControl(this.characterControl);
    }

    /**
     * updates the players position & camera based on the keys the user pressed.
     * @param cam
     * @param camDir
     * @param camLeft
     */
	public void update(Camera cam, Vector3f camDir, Vector3f camLeft) {
        camDir.set(cam.getDirection().multLocal(0.6f));
        camLeft.set(cam.getLeft()).multLocal(0.4f);

        Vector3f walkDirection = new Vector3f(0, 0, 0);

        if (this.up) {
            walkDirection.addLocal(camDir);
        }
        if (this.down) {
            walkDirection.addLocal(camDir.negate());
        }
        if (this.left) {
            walkDirection.addLocal(camLeft);
        }
        if (this.right) {
            walkDirection.addLocal(camLeft.negate());
        }
        
        new PlayerMoveMessage(this.ID, walkDirection, this.characterControl.getPhysicsLocation());

        this.setWalkDirection(walkDirection);
        cam.setLocation(this.characterControl.getPhysicsLocation());

	}

}