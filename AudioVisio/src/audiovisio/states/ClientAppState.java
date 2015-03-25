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
    private AudioVisio   AV;
    private InputManager IM;
    private AssetManager AM;
    public NetworkClient myClient = Network.createClient();
    private WorldManager worldManager;

    // On Screen Message
    private CharSequence velocityMessage = "";
    private Vector3f oldLocation;
    private Vector3f newLocation = new Vector3f();
    private long oldTime;
    private long newTime;
    private int  counter;

    public ClientAppState(){
    }

    @Override
    public void initialize( AppStateManager stateManager, Application app ){
        LogHelper.info("Starting client...");
        NetworkUtils.initializeSerializables();
        this.AV = (AudioVisio) app;
        this.AM = this.AV.getAssetManager();
        this.IM = this.AV.getInputManager();

        // ///////////
        // Physics //
        // ///////////
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

        // ////////////////////
        // Load Scene (map) //
        // ////////////////////
        this.AM.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = this.AM.loadModel("main.scene");
        sceneModel.setLocalScale(2f);

        // /////////////////
        // Create Camera //
        // /////////////////
        this.AV.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        this.AV.getFlyByCamera().setMoveSpeed(100);

        // ////////////////
        // Sync Manager //
        // ////////////////
        SyncManager syncManager = new SyncManager(this.AV, this.myClient);
        syncManager.setMaxDelay(NetworkUtils.NETWORK_SYNC_FREQUENCY);
        syncManager.setMessageTypes(SyncCharacterMessage.class,
                SyncRigidBodyMessage.class, PlayerJoinMessage.class,
                PlayerLeaveMessage.class);
        stateManager.attach(syncManager);

        this.worldManager = new WorldManager(this.AV, this, this.AV.getRootNode());
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
        CollisionShape sceneShape = CollisionShapeFactory
                .createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.setLocalScale(2f);

        // /////////////////////
        // Generate entities //
        // /////////////////////
        Button testButton = new Button(0f, 1f, 0f);
        testButton.createMaterial(this.AM);

        Lever testLever = new Lever(3f, 5f, 3f);
        testLever.createMaterial(this.AM);

        // //////////////////////////
        // Initialization Methods //
        // //////////////////////////
        // TODO may be moved to Player/elsewhere
        this.initCrossHairs(); // a "+" in the middle of the screen to help aiming

        // ///////////////////////////
        // Add objects to rootNode //
        // ///////////////////////////
        this.AV.getRootNode().attachChild(sceneModel);
        this.AV.getRootNode().addLight(ambientLight);
        this.AV.getRootNode().addLight(directionalLight);

        // ///////////////////////////////
        // Add objects to physicsSpace //
        // ///////////////////////////////
        physicsSpace.addCollisionListener(this);
        physicsSpace.add(landscape);
        LogHelper.info("Client done initializing...");
    }

    /**
     * Gives mappings to all hotkeys and actions by the user. Adds appropriate
     * listeners to the player.
     *
     * @param player The player entity that is affected by this clients inputs.
     */

    public void initKeys( Player player ){
        this.IM.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        this.IM.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        this.IM.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.IM.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.IM.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        this.IM.addMapping("Shoot", new MouseButtonTrigger(
                MouseInput.BUTTON_LEFT));

        this.IM.addListener(player, "Up");
        this.IM.addListener(player, "Down");
        this.IM.addListener(player, "Left");
        this.IM.addListener(player, "Right");
        this.IM.addListener(player, "Jump");

        this.IM.addListener(player, "Shoot");

    }

    /**
     * Creates a sphere to show where on an object the player 'shoots' (where
     * the ray from their camera collides with the closest valid object.) TODO
     * may be moved.
     */

    private void initMark(){
        Sphere sphere = new Sphere(30, 30, 0.2f);
        Geometry mark = new Geometry("BOOM!", sphere);
        Material mark_mat = new Material(this.AM, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Red);
        mark.setMaterial(mark_mat);

    }

    /**
     * Creates a '+' char on the center of the screen and adds it to the
     * 'guiNode' TODO may be moved
     */

    private void initCrossHairs(){
        this.AV.setDisplayStatView(false);
        BitmapFont guiFont = this.AM.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation(
                // center
                this.AV.getWidth() / 2 - ch.getLineWidth() / 2,
                this.AV.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        this.AV.getGuiNode().attachChild(ch);

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

        this.AV.setFPSText(this.velocityMessage);
        this.counter++;
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
     * TODO rewrite Handles collisions of entities
     *
     * @param event The collision event containing the two nodes that have
     *              collided.
     */

    @Override
    public void collision( PhysicsCollisionEvent event ){
        if (event.getNodeA().getParent() instanceof Entity
                && event.getNodeB().getParent() instanceof Entity){
            Entity entityA = (Entity) event.getNodeA().getParent();
            Entity entityB = (Entity) event.getNodeB().getParent();
            entityA.collisionTrigger(entityB);
            entityB.collisionTrigger(entityA);
            if ("button".equals(event.getNodeA().getName())){

                if ("Oto-ogremesh".equals(event.getNodeB().getName())){
                    Button b = (Button) event.getNodeA().getParent();
                    b.startPress();
                }
            }
            if ("button".equals(event.getNodeB().getName())){

                if ("Oto-ogremesh".equals(event.getNodeA().getName())){
                    Button b = (Button) event.getNodeB().getParent();
                    b.startPress();
                }
            }
        }

    }

    public long getId(){
        return this.myClient.getId();
    }

    public boolean isAudioClient(){
        return this.getId() % 2 == 0;
    }

    public boolean isVideoClient(){
        return !this.isAudioClient();
    }
}