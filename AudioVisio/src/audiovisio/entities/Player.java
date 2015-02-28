package audiovisio.entities;

import java.util.Map;

import org.json.simple.JSONObject;

import audiovisio.networking.messages.PlayerMoveMessage;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
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
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class Player extends MovingEntity implements ActionListener{

    private final static float STEP_HEIGHT = 0.05f;
    private final static Vector3f SPAWN_LOCATION = new Vector3f(0,30,0);
    //private final static String DEFAULT_MODEL = "Models/Oto/Oto.mesh.xml";

    //public Spatial model;
    private DirectionalLight light;
    public CharacterControl characterControl;
    public Camera mainCamera;
    private Map<String, KeyTrigger> keyBinds;

    private Geometry mark;

    public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;

    private CapsuleCollisionShape collisionShape;
    private GhostControl ghost;

	public Node node;
	public Mesh mesh;

	public Player() {
        this.collisionShape = new CapsuleCollisionShape(1.5f, 6f, 1);

        this.characterControl = new CharacterControl(this.collisionShape, STEP_HEIGHT);
        this.characterControl.setJumpSpeed(20);
        this.characterControl.setFallSpeed(30);
        this.characterControl.setGravity(30);

        this.attachChild(this.node);
    }

    public Player(Node playerModel, Vector3f spawnLocation){
    	this();

        this.node = playerModel;
        this.node.setLocalScale(0.2f);
        this.node.setLocalTranslation(spawnLocation);

        this.node.addControl(this.characterControl);
    }

    public Player(Node playerModel){
        this(playerModel, SPAWN_LOCATION);
    }

    public void addToScene(Node root, PhysicsSpace physics){
    	addToScene(root);
        physics.add(this.characterControl);
    }

    public void load(JSONObject obj){
        super.load(obj);

        //TODO
    }

    public void setWalkDirection(Vector3f direction){
        this.moveDirection = direction;
        this.characterControl.setWalkDirection(direction);
    }

    public void loadKeyBinds(JSONObject obj){
        JSONObject jsonMap = (JSONObject) obj.get("playerKeyBinds");
        //this.keyBinds = new ObjectMapper().readValue(jsonMap, Map.class);
    }


    public void initKeys(InputManager inputManager, Map<String, KeyTrigger> map) {
        for ( Map.Entry<String, KeyTrigger> entry : map.entrySet()) {
            inputManager.addMapping(entry.getKey(), entry.getValue());
            inputManager.addListener(this, entry.getKey());
        }
    }

    public void initKeys(InputManager inputManager){
        this.initKeys(inputManager, this.keyBinds);
    }

    public void setKeyBinds(Map<String, KeyTrigger> map){
        this.keyBinds = map;
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
                characterControl.jump();
            }
        }
        if(binding.equals("Shoot") && !isPressed){
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

    public void createModel(AssetManager assetManager) {
		Node myCharacter = (Node) assetManager
				.loadModel("Models/Oto/Oto.mesh.xml");
		this.model = myCharacter;
    }

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
        
        PlayerMoveMessage moveMessage = new PlayerMoveMessage(this.ID, walkDirection, this.characterControl.getPhysicsLocation());

        this.setWalkDirection(walkDirection);
        cam.setLocation(this.characterControl.getPhysicsLocation());

	}



}