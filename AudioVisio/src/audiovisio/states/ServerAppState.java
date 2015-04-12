package audiovisio.states;

import audiovisio.AudioVisio;
import audiovisio.Items;
import audiovisio.WorldManager;
import audiovisio.entities.Entity;
import audiovisio.entities.Player;
import audiovisio.level.Level;
import audiovisio.level.LevelLoader;
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

    @Override
    public void update( float tpf ){
        LogHelper.divider("Server Update");
    }

    /**
     * Initializes all variables used to run the server, is called on Jmonkey on this.start()
     *
     * TODO clean up myServer.addConnectionListener
     */

    @Override
    public void initialize( AppStateManager stateManager, Application app ){
        LogHelper.info("Starting server...");

        Items.init();
        this.currentLevel = LevelLoader.read(AudioVisio.level);
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

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // /////////////////////////////////
        // Add objects to physicsSpace //
        // /////////////////////////////////
        physicsSpace.addCollisionListener(this);

        //TODO move this somewhere more appropriate
        this.myServer.addConnectionListener(new ConnectionListener() {

            /**
             * Handles adding connections to Server
             */
            @Override
            public void connectionAdded( com.jme3.network.Server server,
                                         HostedConnection conn ){
                LogHelper.info("connectionAdded() for connection: " + conn.getId());
                if (ServerAppState.this.players.size() < 2){

                    LogHelper.info("Sent PlayerJoinMessage: " + conn.getId());
                    ServerAppState.this.worldManager.addPlayer(conn.getId());
                    ServerAppState.this.players.put(conn.getId(), (Player) ServerAppState.this.worldManager.getPlayer(conn.getId()));
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

                if (!ServerAppState.this.players.isEmpty()){
                    ServerAppState.this.myServer.close();
                }
            }
        });
        LogHelper.info("Server Started!");

        this.currentLevel.init(this.AM, syncManager);
        this.currentLevel.start(this.AV.getRootNode(), physicsSpace);
    }

    @Override
    public void cleanup(){
        this.myServer.close();
    }

    /**
     * TODO
     * Handles custom collision events, typically between two entities.
     *
     * @param event The Event object, contains both nodes that collided along with other relevant information.
     */

    @Override
    public void collision( PhysicsCollisionEvent event ){
        if (event.getNodeA().getParent() instanceof Entity && event.getNodeB().getParent() instanceof Entity){
            Entity entityA = (Entity) event.getNodeA().getParent();
            Entity entityB = (Entity) event.getNodeB().getParent();
            LogHelper.info(entityA + ": " + entityB);
            entityA.collisionTrigger(entityB);
            entityB.collisionTrigger(entityA);
        }
    }

    /**
     * Unimplemented method stub.
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    public void onAction( String arg0, boolean arg1, float arg2 ){
        // TODO Method is needed in order to implement actionListener.
    }

    /**
     * @param id
     *
     * @return
     */
    public Player getPlayer( int id ){
        return this.players.get(id);
    }

    public Level getLevel(){
        return this.currentLevel;
    }
}

