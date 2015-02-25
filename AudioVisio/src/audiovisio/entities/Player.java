package audiovisio.entities;

import java.util.Map;

import org.json.simple.JSONObject;

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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class Player extends MovingEntity implements ActionListener{

    private final float STEP_HEIGHT = 0.05f;

    //public Spatial model;
    private DirectionalLight light;
    public CharacterControl characterControl;
    public Camera mainCamera;
    private Map<String, KeyTrigger> keyBinds;

    private Geometry mark;

    private boolean up = false, down = false, left = false, right = false;

    ///////////////////////////////////

    private CapsuleCollisionShape collisionShape;
    private GhostControl ghost;

	private Node node;


    public Player(){
    }

    public void setNode(Node n){
        this.node = n;
    }

    public void init(){
        this.collisionShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        this.characterControl = new CharacterControl(this.collisionShape, STEP_HEIGHT);
        this.characterControl.setJumpSpeed(20);
        this.characterControl.setFallSpeed(30);
        this.characterControl.setGravity(30);
        //this.characterControl.setPhysicsLocation(new Vector3f(10, 30, 15));

         //GhostControl ghost = new GhostControl(new BoxCollisionShape(new Vector3f(1, 1, 1))); // a box-shaped ghost
        this.ghost = new GhostControl(this.collisionShape);
        //this.ghost.setPhysicsLocation(new Vector3f(10, 30, 15));

        /*
        / Load any model
        Node myCharacter = (Node) assetManager
                .loadModel("Models/Oto/Oto.mesh.xml");
        rootNode.attachChild(myCharacter);
         */

        this.node.addControl(this.characterControl);
        this.node.addControl(this.ghost);
        //this.node.setLocalTranslation(new Vector3f(10, 30, 15));
    }

    public void addToScene(Node root, PhysicsSpace physics){
        root.attachChild(this.node);
        //this.node.setLocalTranslation(new Vector3f(10, 30, 15));
        physics.add(this.characterControl);
        physics.add(this.ghost);
    }

    public void load(JSONObject obj){
        super.load(obj);

        //TODO
    }

    public void setMoveDirection(Vector3f direction){
        this.moveDirection = direction;
        characterControl.setWalkDirection(direction);
        mainCamera.setLocation(characterControl.getPhysicsLocation());
    }

    public void loadKeyBinds(JSONObject obj){
        JSONObject jsonMap = (JSONObject) obj.get("playerKeyBinds");
        //this.keyBinds = new ObjectMapper().readValue(jsonMap, Map.class);
    }

    public void setKeyBinds(Map<String, KeyTrigger> map){
        this.keyBinds = map;
    }

    /** A centered plus sign to help the player aim. */
    public void initCrossHairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
            settings.getWidth() / 2 - ch.getLineWidth()/2,
            settings.getHeight() / 2 + ch.getLineHeight()/2,
            0);
        guiNode.attachChild(ch);
    }

    /** A red ball that marks the last spot that was "hit" by the "shot". */
    public void initMark() {
        Sphere sphere = new Sphere(30, 30, 0.2f);
        mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);
    }

    public Spatial makeCharacter(AssetManager assetManager) {
    // load a character from jme3test-test-data
        this.model = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        this.model.scale(0.5f);
        this.model.setLocalTranslation(-1.0f, -1.5f, -0.6f);

    // We must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        this.model.addLight(sun);

        //this.model
        return this.model;
    }

    @Override
    public void simpleUpdate(float tpf){
        frontDirection.set(cam.getDirection().multLocal(0.6f));
        leftDirection.set(cam.getLeft()).multLocal(0.4f);

        moveDirection.set(0, 0, 0);

        if(up){
            moveDirection.addLocal(frontDirection);
        }
        if(down){
            moveDirection.addLocal(frontDirection.negate());
        }
        if(left){
            moveDirection.addLocal(leftDirection);
        }
        if(right){
            moveDirection.addLocal(leftDirection.negate());
        }

        characterControl.setWalkDirection(moveDirection);
        cam.setLocation(characterControl.getPhysicsLocation());
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

    public void initKeys(InputManager inputManager) {
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

}