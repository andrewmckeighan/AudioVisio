package audiovisio.entities;

import audiovisio.level.IShootable;
import audiovisio.level.Level;
import audiovisio.networking.messages.SyncCharacterMessage;
import audiovisio.utils.LogHelper;
import audiovisio.utils.PrintHelper;
import com.jme3.asset.AssetManager;
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

/**
 * TODO: make sure all parameters of Player are children of player, and handled entirely within player.
 * TODO: format this to work with matt's level file reading system. (new, load, init, start).
 */


/**
 * This class defines the player object and contains all methods to manage the
 * player as an entity.
 *
 * @author Taylor Premo
 */

//TODO FIX GRAVITY
//Does the online model move with the camera or actual location?


public class Player extends MovingEntity implements ActionListener {

    // Constants
    public static final  Vector3f DEFAULT_SPAWN_LOCATION = new Vector3f(0, 5, 0);
    public static final  Vector3f GRAVITY                = new Vector3f(0, -9.81f, 0); //TODO make this not awful
    public static final  String   DEFAULT_MODEL          = "Models/Oto/Oto.mesh.xml";
    public static final  Vector3f CAMERA_OFFSET          = new Vector3f(0, 5, 0);
    public static final  Vector3f MODEL_OFFSET           = Player.CAMERA_OFFSET.divide(2);
    private static final Vector3f JUMP_FORCE             = new Vector3f(0, 2, 0);
    private static final float    MAX_SHOOT_DISTANCE     = 10.0F;

    //Children

    protected Level level;
    protected Node  model;
    protected Vector3f spawn = Player.DEFAULT_SPAWN_LOCATION;
    //Key Listeners
    private   boolean                up;
    private   boolean                down;
    private   boolean                left;
    private   boolean                right;
    protected BetterCharacterControl characterControl;
    private   Camera                 playerCamera;
    private Vector3f camDir  = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean isServer;
    private Vector3f savedLocation = Player.DEFAULT_SPAWN_LOCATION;
    private Box box;

    public Player(){}

    /**
     * TODO
     *
     * @param level [description]
     */
    public void load( Level level ){
        this.spawn = Player.DEFAULT_SPAWN_LOCATION;
        this.level = level;
    }

    public void init( AssetManager assetManager ){
        //TODO move to subclass

        this.characterControl = new BetterCharacterControl(0.3f, 2.5f, 8.0f);
        this.characterControl.setJumpForce(Player.JUMP_FORCE);
        this.characterControl.setGravity(Player.GRAVITY);

        this.spawn = this.spawn.mult(Level.SCALE);
        //this.setLocalTranslation(this.spawn);
        this.characterControl.warp(this.spawn);

        this.addControl(this.characterControl);
    }

    public void start( Node rootNode, PhysicsSpace physics ){
        //TODO move to subclass

        rootNode.attachChild(this);
        physics.add(this);
    }

    /**
     * sets the players location and direction, used to sync a player with a message sent from a different server/client
     * TODO: see what all this.move can handle.
     *
     * @param location
     * @param direction walkDirection to set
     */
    public void update( Vector3f location, Vector3f direction, Quaternion rotation ){

        this.characterControl.setWalkDirection(direction);
//        if(this.characterControl.getWalkDirection().length() == 0){
//            this.characterControl.warp(location);
//        }
//        this.characterControl.setWalkDirection(direction);

        if (this.playerCamera != null){
            if (!isDebug()){
                this.playerCamera.setLocation(this.getLocalTranslation().add(
                        Player.CAMERA_OFFSET));
            }
            //TODO remove
            if (this.model != null){
                this.model.removeFromParent();
                this.model = null;
            }
        }
    }

    public boolean isServer(){
        return this.isServer;
    }

    public void setServer( boolean isServer ){
        this.isServer = isServer;
    }

    /**
     * ActionHandler for the Player, sets flags for the hotkeys that directly affect the player object.
     *
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
                    if (shotObject instanceof Box && this.box == null){
                        this.box = ((Box) shotObject).pickUp();
                    }
                    if (shotObject instanceof Button && this.box != null){
                        this.box.putDown(((Button) shotObject).getLocalTranslation());
                    }

                    shotObject.update();
                    shotObject.setWasUpdated(true);
                }
            }
        }
    }

    /**
     * Generates a message containing all the info needed to update the character on the other server/client.
     *
     * @return new SyncCharacterMessage to send to other server/client.
     */
    public SyncCharacterMessage getSyncCharacterMessage(){
        SyncCharacterMessage syncCharacterMessage;
        Quaternion q = new Quaternion(0, 0, 0, 0);

        //TODO: determine if this if is needed.
        if (this.isServer()){
            syncCharacterMessage = new SyncCharacterMessage(this.getID(),
                    this.savedLocation, //this.getLocalTranslation(),
                    this.characterControl.getWalkDirection(), q);
        } else {
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

            walkDirection.setY(0); //Y movement will be done on 'jump'
            this.characterControl.setWalkDirection(walkDirection);
            LogHelper.object("Player.getSyncCharMessage", this.getID(), this.getLocalTranslation(), walkDirection, this.playerCamera.getRotation());


            syncCharacterMessage = new SyncCharacterMessage(this.getID(),
                    this.getLocalTranslation(),
                    walkDirection, this.playerCamera.getRotation());
        }
        return syncCharacterMessage;
    }

    public Camera getCam(){
        return this.playerCamera;
    }

    public void setCam( Camera cam ){
        this.playerCamera = cam;
    }

    @Override
    public String toString(){
        String s = "";
        try{

            String localTrans, walkDir, camDir;
            if (this.getLocalTranslation() != null){
                localTrans = PrintHelper.printVector3f(this.getLocalTranslation());
            } else {
                localTrans = "null";
            }
            if (this.characterControl.getWalkDirection() != null){
                walkDir = PrintHelper.printVector3f(this.characterControl.getWalkDirection());
            } else {
                walkDir = "null";
            }
            if (this.playerCamera != null && this.playerCamera.getDirection() != null){
                camDir = PrintHelper.printVector3f(this.playerCamera.getDirection());
            } else {
                camDir = "null";
            }

            s = "Player[" + this.ID + "]\n" +
                    "   located: " + localTrans + "\n" +
                    "   walking: " + walkDir + "\n" +
                    "   looking: " + camDir;
        } catch (NullPointerException nullException){
            s = "Player has not been fully created yet.";
        }
        return s;
    }

    public void warp( Vector3f location ){
        this.characterControl.warp(location);
    }

    public void setDebug( boolean debug ){
        this.debug = debug;

        if (this.debug){
            LogHelper.info("NoClip on");
            this.removeControl(this.characterControl);
        } else {
            LogHelper.info("NoClip off");
            this.addControl(this.characterControl);
        }
    }

    public boolean isDebug(){
        return this.debug;
    }

    private boolean debug;
}
