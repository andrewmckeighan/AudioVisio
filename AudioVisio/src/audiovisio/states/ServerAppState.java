package audiovisio.states;

import audiovisio.AudioVisio;
import audiovisio.Items;
import audiovisio.WorldManager;
import audiovisio.entities.Button;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.level.Level;
import audiovisio.level.LevelReader;
import audiovisio.networking.SyncManager;
import audiovisio.networking.messages.*;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import audiovisio.networking.utilities.GeneralUtilities; //TODO is this needed?

/**
 * This class manages the server, Addin abd removing players,
 * and all other methods needed to run jmonkeys simpleApplication
 *
 * @author Taylor Premo
 * @author James Larson
 * @author Matt Gerst
 */

public class ServerAppState extends AbstractAppState implements
        PhysicsCollisionListener, ActionListener {
    Level currentLevel;

    //Networking
    private com.jme3.network.Server myServer;
    private WorldManager            worldManager;
    private SimpleApplication       AV;
    private AssetManager            AM;

    //Players
    private Map<Integer, Player> players = new HashMap<Integer, Player>();

    public ServerAppState(){}

//    public void startServer() {
//        this.start(JmeContext.Type.Headless);
//    }


    /**
     * Initializes all variables used to run the server, is called on Jmonkey on this.start()
     *
     * TODO clean up myServer.addConnectionListener
     */

    @Override
    public void initialize( AppStateManager stateManager, Application app ){
        LogHelper.info("Starting server...");

        Items.init();
        this.currentLevel = LevelReader.read(AudioVisio.level);
        this.currentLevel.loadLevel();

        this.AV = (SimpleApplication) app;
        this.AM = this.AV.getAssetManager();
        try{
            this.myServer = Network.createServer(NetworkUtils.getPort());
            this.myServer.start();
        } catch (IOException e){
            LogHelper.severe("Error on server start", e);
            System.exit(1);
        }

        ////////////
        //Physics //
        ////////////
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        final PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace(); //TODO why is this final?

        ////////////////
        // Networking //
        ////////////////
        SyncManager syncManager = new SyncManager(this.AV, this.myServer);
        syncManager.setServerSyncFrequency(NetworkUtils.NETWORK_SYNC_FREQUENCY);
        stateManager.attach(syncManager);

        this.worldManager = new WorldManager(this.AV, null, this.AV.getRootNode());
        stateManager.attach(this.worldManager);
        syncManager.addObject(-1, this.worldManager);
        syncManager.setMessageTypes(SyncCharacterMessage.class,
                SyncRigidBodyMessage.class, PlayerJoinMessage.class, PlayerLeaveMessage.class,
                TriggerActionMessage.class);

        // TODO: Shouldn't need the rest of this method

        ///////////////////////
        //Load Scene (map) //
        ///////////////////////
//        AM.registerLocator("town.zip", ZipLocator.class);
//        Spatial sceneModel = AM.loadModel("main.scene");
//        sceneModel.setLocalScale(2f);

        // /////////////
        // Physics //
        // /////////////
//        CollisionShape sceneShape = CollisionShapeFactory
//                .createMeshShape(sceneModel);
//        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
//        sceneModel.setLocalScale(2f);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        /////////////////////////
        // Generate entities //
        /////////////////////////
        Button testButton = new Button();

        Lever testLever = new Lever(3f, 5f, 3f);

        ///////////////////////////
        //Add entities to Scene //
        ///////////////////////////

//        testButton.addToScene(rootNode, physicsSpace);
//        testLever.addToScene(rootNode, physicsSpace);

        // ////////////////////////////
        // Add objects to rootNode //
        // ////////////////////////////
//        AV.getRootNode().attachChild(sceneModel);

        // /////////////////////////////////
        // Add objects to physicsSpace //
        // /////////////////////////////////
        physicsSpace.addCollisionListener(this);
//        physicsSpace.add(landscape);

        //TODO move this somewhere more appropriate
        this.myServer.addConnectionListener(new ConnectionListener() {

            /**
             * Handles adding connections to Server
             */
            @Override
            public void connectionAdded( com.jme3.network.Server server,
                                         HostedConnection conn ){
                // DON'T REMOVE THIS LOG MESSAGE. IT BREAKS STUFF
                //TODO explain/fix this
                LogHelper.info("connectionAdded() for connection: " + conn.getId());
                if (ServerAppState.this.players.size() < 2){

                    LogHelper.info("Sent PlayerJoinMessage: " + conn.getId());
                    ServerAppState.this.worldManager.addPlayer(conn.getId());
                    ServerAppState.this.players.put(conn.getId(), (Player) ServerAppState.this.worldManager.getPlayer(conn.getId()));

//                    Integer[] list = ServerAppState.this.players.keySet().toArray(new Integer[ServerAppState.this.players.keySet().size()]);
//                    conn.send(new PlayerListMessage(list));
//                    LogHelper.info("Sent PlayerListMessage");
                } else {
                    conn.close("Too many clients connect to server");
                    LogHelper.severe("More than 2 players attempted to join");
                }
            }

            /**
             * Handles Removing connections from server
             */
            @Override
            public void connectionRemoved( com.jme3.network.Server server,
                                           HostedConnection conn ){
                if (ServerAppState.this.players.containsKey(conn.getId())){
                    ServerAppState.this.players.remove(conn.getId());
                }
                ServerAppState.this.worldManager.removePlayer(conn.getId());

                if (ServerAppState.this.players.isEmpty() || ServerAppState.this.players.size() == 0){
                    ServerAppState.this.myServer.close();
                }

//                LogHelper.info(players.size() + players.toString());
            }
        });
        LogHelper.info("Server Started!");

        this.currentLevel.init(this.AM, syncManager);
        this.currentLevel.start(this.AV.getRootNode(), physicsSpace);
    }

    @Override
    public void cleanup() {
        this.myServer.close();
    }

    /**
     * TODO
     * Handles custom collision events, typically between two entities.
     *
     * @param event The Event object, contains both nodes that collided along with other relevant information.
     */

    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeA().getParent() instanceof Entity && event.getNodeB().getParent() instanceof Entity) {
            Entity entityA = (Entity) event.getNodeA().getParent();
            Entity entityB = (Entity) event.getNodeB().getParent();
            entityA.collisionTrigger(entityB);
            entityB.collisionTrigger(entityA);
            if ("button".equals(event.getNodeA().getName())) {

                if ("Oto-ogremesh".equals(event.getNodeB().getName())) {
                    Button b = (Button) event.getNodeA().getParent();
                    b.startPress();
                }
            }
            if ("button".equals(event.getNodeB().getName())) {

                if ("Oto-ogremesh".equals(event.getNodeA().getName())) {
                    Button b = (Button) event.getNodeB().getParent();
                    b.startPress();
                }
            }
        }
    }

    @Override
    public void onAction(String arg0, boolean arg1, float arg2) {
        // TODO Method is needed in order to implement actionListener.
    }

    public Player getPlayer(int id) {
        return this.players.get(id);
    }

}

