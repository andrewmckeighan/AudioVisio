package audiovisio.entities;

import audiovisio.level.IShootable;
import audiovisio.level.Level;
import audiovisio.networking.messages.SyncCharacterMessage;
import audiovisio.states.ClientAppState;
import audiovisio.utils.LogHelper;
import audiovisio.utils.PrintHelper;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.json.simple.JSONObject;

/**
 * TODO: make sure all parameters of Player are children of player, and handled entirely within player.
 * TODO: format this to work with matt's level file reading system. (new, load, init, start).
 */


/**
 * This class defines the player object and contains all methods to manage the
 * player as an entity.
 *
 * @author Taylor Premo
 *
 */

//TODO FIX GRAVITY
//TODO FIX GRAVITY
//TODO FIX GRAVITY
//TODO FIX GRAVITY
//TODO FIX GRAVITY
//TODO FIX GRAVITY

//TODO fix movement being twice as far as camera.
//Does the online model move with the camera or actual location?


public class Player extends MovingEntity implements ActionListener {

    // Constants
    public static final  Vector3f DEFAULT_SPAWN_LOCATION = new Vector3f(0, 5, 0);
    public static final  Vector3f GRAVITY                = new Vector3f(0, -9.81f, 0); //TODO make this not awful
    public static final  String   DEFAULT_MODEL          = "Models/Oto/Oto.mesh.xml";
    public static final  Vector3f CAMERA_OFFSET          = new Vector3f(0, 5, 0);
    public static final  Vector3f MODEL_OFFSET           = Player.CAMERA_OFFSET.divide(2);
    private static final Vector3f JUMP_FORCE             = new Vector3f(0, 2, 0);
    private static final float MAX_SHOOT_DISTANCE = 5.0F;
    public  Particle               footSteps;
    //Key Listeners
    private boolean                up;
    private boolean                down;
    private boolean                left;
    private boolean                right;
    //Children
    private Node                   model;
    private BetterCharacterControl characterControl;
    private Camera                 playerCamera;
    //references
    private Node                   rootNode;
    private AssetManager           assetManager;
    private Level                  level;

    //Instance Variables
    private boolean isServer;
    private Vector3f walkDirection = new Vector3f(); //TODO are walk & save directions needed?
    private Vector3f savedLocation = Player.DEFAULT_SPAWN_LOCATION;
    private Vector3f camDir        = new Vector3f();
    private Vector3f camLeft       = new Vector3f();
    private Vector3f spawn         = Player.DEFAULT_SPAWN_LOCATION;

    //Sound Variables
    private AudioNode audio_steps;

    public Player( Node playerModel ){
        this(playerModel, Player.DEFAULT_SPAWN_LOCATION);
    }

    /**
     * Primary constructor for Player. Adds the model and generates a characterControl.
     *
     * @param  playerModel   The modal node for the Player to use. Uses AssetManager to generate, so it must be passed in.
     * @param  spawnLocation The starting location for the Player to appear in.
     */
    public Player( Node playerModel, Vector3f spawnLocation ){
        this.spawn = spawnLocation;

        if (playerModel != null){
            this.setModel(playerModel, spawnLocation);
        }

        this.characterControl = new BetterCharacterControl(0.3f, 2.5f, 8f);
        this.characterControl.setJumpForce(Player.JUMP_FORCE);
        this.characterControl.setGravity(Player.GRAVITY);

        this.characterControl.warp(spawnLocation);
        this.setLocalTranslation(spawnLocation);
        this.addControl(this.characterControl);

        this.move(spawnLocation);

        this.footSteps = new Particle();
    }

    /**
     * attaches the model to the player object
     *
     * @param model model to be attached
     */
    public void setModel( Node model, Vector3f location ){
        this.model = model;

        this.model.setLocalScale(0.5f);
        this.model.setLocalTranslation(location);

        this.attachChild(this.model);
    }

    public Player(Level level){
        this(null, Player.DEFAULT_SPAWN_LOCATION);
        this.level = level;
    }

    public static Node createModel( AssetManager assetManager ){
        return Player.createModel(assetManager, Player.DEFAULT_MODEL);
    }

    /**
     * Creates the Model for the player, needs to use the asset manager so we aren't handling this in the constructor.
     * @param  assetManager The Apps AssetManager used to load the model
     * @param  model        The location of the model file to load
     * @return The Node object that was loaded.
     */
    public static Node createModel( AssetManager assetManager, String model ){
        return (Node) assetManager.loadModel(model);
    }

    public void setSpawn( Vector3f newSpawn ){
        this.spawn = newSpawn;
        this.setLocalTranslation(this.spawn);
    }

    public void setModel( Node model ){
        this.setModel(model, this.spawn);
    }

    /**
     * Adds the Player object to the apps rootNode and PhysicsSpace.
     * @param root    The root node of the app
     * @param physics Physics space of the app
     */
    public void addToScene( Node root, PhysicsSpace physics ){
        this.addToScene(root);
        if (this.model != null){
            root.attachChild(this.model);
        } else {
            LogHelper.warn("Player.addToScene: this.model is null!");
        }

        physics.add(this);
    }

    /**
     * TODO
     * @param loadObj [description]
     */
    public void load( JSONObject loadObj ){
        super.load(loadObj);
        // TODO
    }

    /**
     * ActionHandler for the Player, sets flags for the hotkeys that directly affect the player object.
     * @param binding   key causing the action
     * @param isPressed true = is pressed
     * @param tpf       time per frame
     */
    @Override
    public void onAction( String binding, boolean isPressed, float tpf ){
        if (binding.equals("Up")){
            this.up = isPressed;
        } else if (binding.equals("Down")){
            this.down = isPressed;
        } else if (binding.equals("Left")){
            this.left = isPressed;
        } else if (binding.equals("Right")){
            this.right = isPressed;
        } else if (binding.equals("Jump")){
            if (isPressed){
                this.characterControl.jump();
            }
        }
        if (binding.equals("Shoot") && !isPressed){
            //TODO
            CollisionResults results = new CollisionResults();

            Ray ray = new Ray(this.playerCamera.getLocation(), this.playerCamera.getDirection());

            Node shootables = this.level.getShootables();
            shootables.collideWith(ray, results);
            LogHelper.info("shootables: " + shootables);
            for (Spatial n : shootables.getChildren()){
                LogHelper.info("Shootables" + n);
            }
            LogHelper.info("Shot: " + results);
            if (results.size() > 0){
                CollisionResult closest = results.getClosestCollision();
                LogHelper.info("Shot: " + closest);
                LogHelper.info("distance: " + closest.getDistance());
                if (closest.getDistance() <= Player.MAX_SHOOT_DISTANCE){
                    IShootable shotObject = (IShootable) closest.getGeometry().getParent();
                    assert shotObject instanceof IShootable;
                    shotObject.update();
                }
            }
        }
    }

    /**
     * sets the players location and direction, used to sync a player with a message sent from a different server/client
     * TODO: see what all this.move can handle.
     *
     * @param location  location to set
     * @param direction walkDirection to set
     */
    public void update( Vector3f location, Vector3f direction, Quaternion rotation ){
//        this.savedLocation = location;
//        this.setWalkDirection(direction);

//        this.move(location);

//        this.characterControl.warp(location);
        this.audio_steps = new AudioNode(this.assetManager, "Sound/Effects/Foot steps.ogg",false);

        this.characterControl.setWalkDirection(direction);



        if (this.model != null){
//            LogHelper.info("test" + this.getLocalTranslation().add(MODEL_OFFSET) + ":" + location.add(MODEL_OFFSET));
//            this.model.setLocalTranslation(this.getLocalTranslation().add(MODEL_OFFSET));
            this.model.setLocalTranslation(location.add(Player.MODEL_OFFSET));
        }

        if (this.footSteps != null && this.footSteps.emitter != null){
//            this.footSteps.emitter.setLocalTranslation(this.getLocalTranslation());
            this.footSteps.emitter.setLocalTranslation(location);
            this.footSteps.emitter.setNumParticles((int) direction.length() * 3 + 1);

        }

        if(!this.isServer()){
            if (direction.length() != 0){
                this.audio_steps.setLooping(false);
                this.audio_steps.setPositional(false);
                this.audio_steps.setVolume(3);
                this.rootNode.attachChild(this.audio_steps);
                this.audio_steps.playInstance();
            } else {
                this.audio_steps.stop();
            }
        }

        if (this.playerCamera != null){
            //TODO why isnt this location?
            this.playerCamera.setLocation(location.add(
                    Player.CAMERA_OFFSET));
//            this.model.setLocalRotation(this.playerCamera.getRotation());//TODO remove y coord from rotation/set a max
            if (this.model != null){
                this.model.removeFromParent();
                this.model = null;
            }
        } else {
            if (this.model != null){
//                rotation.set(rotation.getX(), 0, rotation.getZ(), 0);
                this.model.setLocalRotation(rotation);

            }
        }

        if (this instanceof VisualPlayer){
            if (this.model != null){
                this.model.removeFromParent();
                this.model = null;
            }
        }

        if (this instanceof AudioPlayer){
            if (this.footSteps != null){
                this.footSteps.removeFromParent();
                this.footSteps = null;
            }
        }

    }

    /**
     * Generates a message containing all the info needed to update the character on the other server/client.
     * @return new SyncCharacterMessage to send to other server/client.
     */
    public SyncCharacterMessage getSyncCharacterMessage(){
        Quaternion q;
        if (this.model != null){
            q = this.model.getLocalRotation();
        } else {
            q = new Quaternion(0, 0, 0, 0);
        }

        //TODO: determine if this if is needed.
        if (this.isServer()){
            return new SyncCharacterMessage(this.getID(),
                    this.getLocalTranslation(),
                    this.getWalkDirection(), q);
        }
        this.camDir.set(this.playerCamera.getDirection().multLocal(20.6f));
        this.camLeft.set(this.playerCamera.getLeft()).multLocal(20.4f);

        Vector3f walkDirection = new Vector3f(0, 0, 0);

        if (this.up){
            walkDirection.addLocal(this.camDir);
        }
        if (this.down){
            walkDirection.addLocal(this.camDir.negate());
        }
        if (this.left){
            walkDirection.addLocal(this.camLeft);
        }
        if (this.right){
            walkDirection.addLocal(this.camLeft.negate());
        }

//        walkDirection.setY(walkDirection.getY() / 4);
        walkDirection.setY(0); //Y movement will be done on 'jump'
//        this.setWalkDirection(walkDirection);

        return new SyncCharacterMessage(this.getID(),
                this.getLocalTranslation(),
                walkDirection, this.playerCamera.getRotation());
    }

    public boolean isServer(){
        return this.isServer;
    }

    public void setServer( boolean isServer ){
        this.isServer = isServer;
    }

    public Vector3f getWalkDirection(){
        return this.walkDirection;
    }

    public void setWalkDirection( Vector3f walkDirection ){
        this.walkDirection = walkDirection;
    }

    public void updateCam(){
        if (this.playerCamera != null){
            this.playerCamera.setLocation(this.getWorldTranslation().add(
                    Player.CAMERA_OFFSET));
        }
    }

    public void updateLocalTranslation(){
        this.setLocalTranslation(this.savedLocation);
        if (this.model != null){
            this.model.setLocalTranslation(this.savedLocation);
        }
    }

    public void updateModel(){
        if (this.model != null){
            this.model.setLocalTranslation(this.getWorldTranslation().add(
                    Player.MODEL_OFFSET));
        } else {
            LogHelper.warn("no model!");
        }
    }

    public Camera getCam(){
        return this.playerCamera;
    }

    public void setCam( Camera cam ){
        this.playerCamera = cam;
    }

    @Override
    public String toString(){
        try{
            return "Player[" + this.ID + "] located: " + PrintHelper.printVector3f(this.getLocalTranslation()) +
                    " walking: " + PrintHelper.printVector3f(this.walkDirection) +
                    " looking: " + PrintHelper.printVector3f(this.playerCamera.getDirection());
        } catch (NullPointerException nullException){
            return "Player has not been fully created yet.";
        }
    }

    public void setRootNode( Node rootNode ){
        this.rootNode = rootNode;
    }

    public void setAssetManager( AssetManager assetManager ){
        this.assetManager = assetManager;
    }

    public void init(){
        this.footSteps.init(this.assetManager);
        this.footSteps.start(this.rootNode, this.physicsSpace);
    }
}
