package audiovisio.states;

import audiovisio.AudioVisio;
import audiovisio.Items;
import audiovisio.WorldManager;
import audiovisio.entities.Entity;
import audiovisio.entities.InteractableEntity;
import audiovisio.entities.Player;
import audiovisio.level.ILevelItem;
import audiovisio.level.Level;
import audiovisio.level.LevelReader;
import audiovisio.networking.CollisionEvent;
import audiovisio.networking.SyncManager;
import audiovisio.networking.messages.*;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
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
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

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

    public static final int FPS = 1;
    public NetworkClient myClient = Network.createClient();
    // Networking
    private AudioVisio   audioVisioApp;
    private InputManager inputManager;
    private AssetManager assetManager;
    private Node         rootNode;
    private Level level;

    private WorldManager worldManager;

    //On Screen Message
    private CharSequence velocityMessage = "";
    private Vector3f oldLocation;
    private Vector3f newLocation = new Vector3f();
    private long oldTime;
    private long newTime;
    private int  counter;
    private CopyOnWriteArrayList<CollisionEvent> collisionEvents = new CopyOnWriteArrayList<CollisionEvent>();
    private float updateCounter;

    //private static int ID;

    public ClientAppState(){
    }

    @Override
    public void initialize( AppStateManager stateManager, Application app ){
        LogHelper.info("Starting client...");

        this.audioVisioApp = (AudioVisio) app;
        this.rootNode = this.audioVisioApp.getRootNode();
        this.assetManager = this.audioVisioApp.getAssetManager();
        this.inputManager = this.audioVisioApp.getInputManager();

        Items.init();

        try{
            this.level = LevelReader.read(AudioVisio.level);
            this.level.loadLevel();
        } catch (Exception e){
            LogHelper.info("exception: ", e);
        }
//        try{
////            this.myClient = Network.connectToServer(IP, NetworkUtils.getPort());
//            this.myClient = Network.connectToServer("127.0.0.1", NetworkUtils.getPort());
//            this.myClient.start();
//        } catch (IOException e){
//            LogHelper.severe("Error on client start", e);
//            System.exit(1);
//        }

        // ///////////
        // Physics //
        // ///////////
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

        // ////////////////////
        // Load Scene (map) //
        // ////////////////////
//        this.assetManager.registerLocator("town.zip", ZipLocator.class);
//        Spatial sceneModel = this.assetManager.loadModel("main.scene");
//        sceneModel.setLocalScale(2f);

        // /////////////////
        // Create Camera //
        // /////////////////
        this.audioVisioApp.getFlyByCamera().setEnabled(true);
        this.audioVisioApp.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        this.audioVisioApp.getFlyByCamera().setMoveSpeed(100);

        // ////////////////
        // Sync Manager //
        // ////////////////
        SyncManager syncManager = new SyncManager(this.audioVisioApp, this.myClient);
        syncManager.setMaxDelay(NetworkUtils.NETWORK_SYNC_FREQUENCY);
        syncManager.setMessageTypes(SyncCharacterMessage.class,
                SyncRigidBodyMessage.class, PlayerJoinMessage.class,
                PlayerLeaveMessage.class, TriggerActionMessage.class);
        stateManager.attach(syncManager);

        this.worldManager = new WorldManager(this.audioVisioApp, this, this.audioVisioApp.getRootNode());
        stateManager.attach(this.worldManager);
        syncManager.addObject(-1, this.worldManager);

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
//        CollisionShape sceneShape = CollisionShapeFactory
//                .createMeshShape(sceneModel);
//        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
//        sceneModel.setLocalScale(2f);

//        // /////////////////////
//        // Generate entities //
//        // /////////////////////
//        Button testButton = new Button();

//        Lever testLever = new Lever(3f, 5f, 3f);

        // //////////////////////////
        // Initialization Methods //
        // //////////////////////////
        // TODO may be moved to Player/elsewhere
        this.initCrossHairs(); // a "+" in the middle of the screen to help aiming

        // ///////////////////////////
        // Add objects to rootNode //
        // ///////////////////////////
//        rootNode.attachChild(sceneModel);
        this.rootNode.addLight(ambientLight);
        this.rootNode.addLight(directionalLight);

        // ///////////////////////////////
        // Add objects to physicsSpace //
        // ///////////////////////////////
        physicsSpace.addCollisionListener(this);
//        physicsSpace.add(landscape);

        this.level.init(this.assetManager, syncManager);
        this.level.start(this.rootNode, physicsSpace);
        LogHelper.info("Client Started!");

    }

    /**
     * Creates a '+' char on the center of the screen and adds it to the
     * 'guiNode' TODO may be moved
     */

    private void initCrossHairs(){
        this.audioVisioApp.setDisplayStatView(false);
        BitmapFont guiFont = this.assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation(
                // center
                this.audioVisioApp.getWidth() / 2 - ch.getLineWidth() / 2,
                this.audioVisioApp.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        this.audioVisioApp.getGuiNode().attachChild(ch);

    }

    /**
     * Handles updates made to the client every frame.
     *
     * Specifically, generates an on screen message: updateMessage(); and
     * creates a message to send to the server with the players information.
     *
     * @param tpf time per frame
     */

    @Override
    public void update( float tpf ){
        Player player = ((Player) this.worldManager.getPlayer(this.myClient.getId()));
        if (player != null){
            this.updateMessage(player);
            SyncCharacterMessage msg = player.getSyncCharacterMessage();
            this.myClient.send(msg);
        }

        Collection<ILevelItem> levelItems = this.level.getItems();
        for (ILevelItem iLevelItem : levelItems){
            if (iLevelItem instanceof InteractableEntity){
                InteractableEntity inEnt = (InteractableEntity) iLevelItem;
                if (inEnt.wasUpdated){
                    TriggerActionMessage msg = inEnt.getTriggerActionMessage();
                    this.myClient.send(msg);
                }
            }
        }


        for (CollisionEvent event : this.collisionEvents){
            if (event.check(tpf)){
                this.collisionEvents.remove(event);
                event.collisionEndTrigger();
            }
        }

    }

    /**
     * Creates on screen text that displays the users velocity, distance moved
     * since last update, current position, and the current frame #
     */

    private void updateMessage( Player player ){

        if (this.counter % 1000 == 0){ // We only update once every 1000 frames so
            // that the player can actually move.
            assert this.newLocation != null;
            if (this.oldLocation != null && this.oldTime != 0 && this.newTime != 0){
                this.oldLocation.distance(this.newLocation);
            }

            this.oldLocation = this.newLocation.clone();
            this.newLocation = player.getLocalTranslation();

            this.oldTime = this.newTime;
            this.newTime = System.currentTimeMillis();

            this.counter = 0;

            float distance = this.oldLocation.distance(this.newLocation);
            long time = this.newTime - this.oldTime;
            float velocity = distance / time;
            this.velocityMessage = ("ID: " + this.getId() + "V: " + velocity
                    + ", D: " + distance + ", P: " + this.newLocation + "F: " + this.counter);
        }

        this.audioVisioApp.setFPSText(this.velocityMessage);
        this.counter++;
    }

//    public int getID(){
//        return ID;
//    }
//
//    public void setId(int ID){
//        this.ID = ID;
//    }


    public long getId(){
        return this.myClient.getId();
    }

    @Override
    public void cleanup(){
        if (this.myClient.isConnected()){
            this.myClient.close();
        }

        AudioVisio.stopServer();
        System.exit(0);
    }

    /**
     * Gives mappings to all hotkeys and actions by the user. Adds appropriate
     * listeners to the player.
     *
     * @param player The player entity that is affected by this clients inputs.
     */

    public void initKeys( Player player ){
        this.inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        this.inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        this.inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        this.inputManager.addMapping("Shoot", new MouseButtonTrigger(
                MouseInput.BUTTON_LEFT));

        this.inputManager.addListener(player, "Up");
        this.inputManager.addListener(player, "Down");
        this.inputManager.addListener(player, "Left");
        this.inputManager.addListener(player, "Right");
        this.inputManager.addListener(player, "Jump");

        this.inputManager.addListener(player, "Shoot");

    }

    /**
     * Creates a sphere to show where on an object the player 'shoots' (where
     * the ray from their camera collides with the closest valid object.) TODO
     * may be moved.
     */

    private void initMark(){
        Sphere sphere = new Sphere(30, 30, 0.2f);
        Geometry mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);

    }

    /**
     * TODO rewrite Handles collisions of entities
     *
     * @param event The collision event containing the two nodes that have
     *              collided.
     */

    @Override
    public void collision( PhysicsCollisionEvent event ){
        //TODO are these better than node.getParent()?
        event.getObjectA();
        event.getObjectB();
        if (event.getNodeA() instanceof Entity && event.getNodeB() instanceof Entity){
            Entity entityA = (Entity) event.getNodeA();
            Entity entityB = (Entity) event.getNodeB();
            entityA.collisionTrigger(entityB);
            entityB.collisionTrigger(entityA);

            if (this.collisionEvents.isEmpty()){
                this.collisionEvents.add(new CollisionEvent(entityA, entityB));
            } else {

                for (CollisionEvent collisionEvent : this.collisionEvents){
                    if (collisionEvent.hasSameEntities(entityA, entityB)){
                        collisionEvent.wasUpdated = true;
                    } else {
                        this.collisionEvents.add(new CollisionEvent(entityA, entityB));
                    }
                }
            }
        }


    }

    public boolean isVideoClient(){
        return !this.isAudioClient();
    }

    public boolean isAudioClient(){
        return this.getId() % 2 == 0;
    }
}