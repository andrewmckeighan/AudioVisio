package audiovisio.networking;

import audiovisio.WorldManager;
import audiovisio.entities.Button;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.networking.messages.*;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.controls.ActionListener;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;

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

public class Server extends SimpleApplication implements
        PhysicsCollisionListener, ActionListener {

    //Networking
    private com.jme3.network.Server myServer = null;
    private WorldManager worldManager        = null;

    //Players
    private Map<Integer, Player> players     = new HashMap<Integer, Player>();

    public Server() {}

    public void startServer() {
        this.start(JmeContext.Type.Headless);
    }


    /**
     * Initializes all variables used to run the server, is called on Jmonkey on this.start()
     *
     * TODO clean up myServer.addConnectionListener
     */

    @Override
    public void simpleInitApp() {
        NetworkUtils.initializeSerializables();

        try {
            myServer = Network.createServer(NetworkUtils.getPort());
            myServer.start();
        } catch (IOException e) {
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
        SyncManager syncManager = new SyncManager(this, myServer);
        syncManager.setServerSyncFrequency(NetworkUtils.NETWORK_SYNC_FREQUENCY);
        stateManager.attach(syncManager);

        worldManager = new WorldManager(this, rootNode);
        stateManager.attach(worldManager);
        syncManager.addObject(-1, worldManager);
        syncManager.setMessageTypes(SyncCharacterMessage.class,
                SyncRigidBodyMessage.class, PlayerJoinMessage.class, PlayerLeaveMessage.class);

        // TODO: Shouldn't need the rest of this method

        ///////////////////////
        //Load Scene (map) //
        ///////////////////////
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = assetManager.loadModel("main.scene");
        sceneModel.setLocalScale(2f);

        // /////////////
        // Physics //
        // /////////////
        CollisionShape sceneShape = CollisionShapeFactory
                .createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.setLocalScale(2f);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        /////////////////////////
        // Generate entities //
        /////////////////////////
        Button testButton = new Button(0f, 1f, 0f);

        Lever testLever = new Lever(3f, 5f, 3f);

        ///////////////////////////
        //Add entities to Scene //
        ///////////////////////////

        testButton.addToScene(rootNode, physicsSpace);
        testLever.addToScene(rootNode, physicsSpace);

        // ////////////////////////////
        // Add objects to rootNode //
        // ////////////////////////////
        rootNode.attachChild(sceneModel);

        // /////////////////////////////////
        // Add objects to physicsSpace //
        // /////////////////////////////////
        physicsSpace.addCollisionListener(this);
        physicsSpace.add(landscape);

        //TODO move this somewhere more appropriate
        myServer.addConnectionListener(new ConnectionListener() {

            /**
             * Handles adding connections to Server
             */
            @Override
            public void connectionAdded(com.jme3.network.Server server,
                                        HostedConnection conn) {
                // DON'T REMOVE THIS LOG MESSAGE. IT BREAKS STUFF
                LogHelper.info("connectionAdded() for connection: " + conn.getId());
                if (players.size() < 2) {

                    LogHelper.info("Sent PlayerJoinMessage: " + conn.getId());
                    worldManager.addPlayer(conn.getId());
                    players.put(conn.getId(), (Player) worldManager.getPlayer(conn.getId()));

                    Integer[] list = players.keySet().toArray(new Integer[players.keySet().size()]);
                    conn.send(new PlayerListMessage(list));
                    LogHelper.info("Sent PlayerListMessage");
                } else {
                    conn.close("Too many clients connect to server");
                    LogHelper.severe("More than 2 players attempted to join");
                }
            }

            /**
             * Handles Removing connections from server
             */
            @Override
            public void connectionRemoved(com.jme3.network.Server server,
                                          HostedConnection conn) {
                if (players.containsKey(conn.getId())) {
                    players.remove(conn.getId());
                }
                worldManager.removePlayer(conn.getId());

                if (players.isEmpty() || players.size() == 0) {
                    myServer.close();
                }

//                LogHelper.info(players.size() + players.toString());
            }
        });
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void destroy() {
        myServer.close();
        super.destroy();
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
        return players.get(id);
    }

}

