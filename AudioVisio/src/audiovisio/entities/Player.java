package audiovisio.entities;

import audiovisio.networking.messages.SyncCharacterMessage;
import audiovisio.utils.LogHelper;
import audiovisio.utils.PrintHelper;

import org.json.simple.JSONObject;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

import java.text.DecimalFormat;

/**
 * This class defines the player object and contains all methods to manage the
 * player as an entity.
 *
 * @author Taylor Premo
 *
 */
public class Player extends MovingEntity implements ActionListener {

    // Constants
    public static final Vector3f SPAWN_LOCATION     = new Vector3f(0, 5, 0);
    public static final Vector3f GRAVITY            = new Vector3f(0, -9.81f, 0);
    public static final String DEFAULT_MODEL        = "Models/Oto/Oto.mesh.xml";
    public static final Vector3f CAMERA_OFFSET      = new Vector3f(0, 5, 0);
    public static final Vector3f MODEL_OFFSET       = CAMERA_OFFSET.divide(2);

    //Key Listeners
    private boolean up                              = false;
    private boolean down                            = false;
    private boolean left                            = false;
    private boolean right                           = false;

    //Instance Variables
    private boolean isServer                        = false;
    private Node model                              = null;
    private BetterCharacterControl characterControl = null;
    private Vector3f savedLocation                  = SPAWN_LOCATION;
    private Camera playerCamera                     = null;
    private Vector3f camDir                         = new Vector3f();
    private Vector3f camLeft                        = new Vector3f();
    private Vector3f walkDirection                  = new Vector3f();

    /**
     * Primary constructor for Player. Adds the model and generates a characterControl.
     *
     * @param  playerModel   The modal node for the Player to use. Uses AssetManager to generate, so it must be passed in.
     * @param  spawnLocation The starting location for the Player to appear in.
     */
    public Player(Node playerModel, Vector3f spawnLocation) {
        if (playerModel != null) {
            this.model = playerModel;
            this.model.setLocalScale(0.2f);
            this.model.setLocalTranslation(spawnLocation);
            this.attachChild(this.model);
        }

        this.characterControl = new BetterCharacterControl(0.3f, 2.5f, 8f);
        characterControl.setJumpForce(new Vector3f(0, 0, 0));
        characterControl.setGravity(GRAVITY);

        this.characterControl.warp(spawnLocation);
        this.setLocalTranslation(spawnLocation);
        this.addControl(this.characterControl);

        this.move(spawnLocation);
    }

    public Player(Node playerModel) {
        this(playerModel, SPAWN_LOCATION);
    }


    public Player() {
        this(null, SPAWN_LOCATION);
    }

    /**
     * Creates the Model for the player, needs to use the asset manager so we aren't handling this in the constructor.
     * @param  assetManager The Apps AssetManager used to load the model
     * @param  model        The location of the model file to load
     * @return              The Node object that was loaded.
     */
    public static Node createModel(AssetManager assetManager, String model) {
        return (Node) assetManager.loadModel(model);
    }

    public static Node createModel(AssetManager assetManager) {
        return createModel(assetManager, DEFAULT_MODEL);
    }

    /**
     * attaches the model to the player object
     * @param model model to be attached
     */
    public void setModel(Node model, Vector3f location) {
        this.model = model;

        this.model.setLocalScale(0.5f);
        this.model.setLocalTranslation(location);

        this.attachChild(this.model);
    }

    public void setModel(Node model) {
        setModel(model, SPAWN_LOCATION);
    }

    /**
     * Adds the Player object to the apps rootNode and PhysicsSpace.
     * @param root    The root node of the app
     * @param physics Physics space of the app
     */
    public void addToScene(Node root, PhysicsSpace physics) {
        addToScene(root);
        if (this.model != null) {
            root.attachChild(this.model);
        } else {
            LogHelper.warn("Player.addToScene: this.model is null!");
        }
        physics.add(this);
    }

    /**
     * TODO
     * @param obj [description]
     */
    public void load(JSONObject obj) {
        super.load(obj);
        // TODO
    }

    /**
     * ActionHandler for the Player, sets flags for the hotkeys that directly affect the player object.
     * @param binding   key causing the action
     * @param isPressed true = is pressed
     * @param tpf       time per frame
     */
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Up")) {
            this.up = isPressed;
        } else if (binding.equals("Down")) {
            this.down = isPressed;
        } else if (binding.equals("Left")) {
            this.left = isPressed;
        } else if (binding.equals("Right")) {
            this.right = isPressed;
        } else if (binding.equals("Jump")) {
            if (isPressed) {
                characterControl.jump();
            }
        }
        if (binding.equals("Shoot") && !isPressed) {
            //TODO
        }
    }

    /**
     * sets the players positon and direction, used to sync a player with a message sent from a different server/client
     * @param position  position to set
     * @param direction walkDirection to set
     */
    public void update(Vector3f position, Vector3f direction) {
        this.savedLocation = position;
        this.setWalkDirection(direction);

        this.characterControl.warp(this.savedLocation);
        this.characterControl.setWalkDirection(direction);

        this.model.setLocalTranslation(position);

        if (this.playerCamera != null) {
            this.playerCamera.setLocation(this.getLocalTranslation().add(
                                              CAMERA_OFFSET));
        }

    }

    /**
     * Generates a message containing all the info needed to update the character on the other server/client.
     * @return new SyncCharacterMessage to send to other server/client.
     */
    public SyncCharacterMessage getSyncCharacterMessage() {
        //TODO: determine if this if is needed.
        if (this.isServer()) {
            return new SyncCharacterMessage(this.getID(),
                    this.characterControl, this.getLocalTranslation(),
                    this.getWalkDirection(), this.camDir);
        }
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

        this.setWalkDirection(walkDirection);

        return new SyncCharacterMessage(this.getID(),
                this.characterControl, this.getLocalTranslation(),
                this.getWalkDirection(), this.camDir);
    }

    public void updateCam() {
        if (this.playerCamera != null) {
            this.playerCamera.setLocation(this.getWorldTranslation().add(
                                              CAMERA_OFFSET));
        }
    }

    public void updateLocalTranslation() {
        this.setLocalTranslation(this.savedLocation);
        if (this.model != null) {
            this.model.setLocalTranslation(this.savedLocation);
        }
    }

    public void updateModel() {
        if (this.model != null) {
            this.model.setLocalTranslation(this.getWorldTranslation().add(
                                               MODEL_OFFSET));
        } else {
            LogHelper.warn("no model!");
        }
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean isServer) {
        this.isServer = isServer;
    }

    public Camera getCam() {
        return this.playerCamera;
    }

    public void setCam(Camera cam) {
        this.playerCamera = cam;
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    @Override
    public String toString(){
        try {
            return "Player[" + this.ID + "] located: " + PrintHelper.printVector3f(this.getLocalTranslation()) + "walking: " + PrintHelper.printVector3f(this.walkDirection) + "looking: " + PrintHelper.printVector3f(this.playerCamera.getDirection());
        }catch (NullPointerException nullException){
            return "Player has not been fully created yet.";
        }
    }
}