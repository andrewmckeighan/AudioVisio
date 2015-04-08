package audiovisio;

import audiovisio.entities.AudioPlayer;
import audiovisio.entities.InteractableEntity;
import audiovisio.entities.Player;
import audiovisio.entities.VisualPlayer;
import audiovisio.level.Level;
import audiovisio.networking.SyncManager;
import audiovisio.networking.SyncMessageValidator;
import audiovisio.networking.messages.PhysicsSyncMessage;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.states.ClientAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class manages the list of players between servers and clients.
 *
 * Keeps a list of player entities and their ID. All worldManagers have identical lists,
 * so changes will be the same through all instances of the game.
 */

public class WorldManager extends AbstractAppState implements SyncMessageValidator {

    // Networking
    private SyncManager    syncManager;
    private Server         server;
    // SimpleAppState references.
    private Node           rootNode;
    private Application    app;
    private ClientAppState client;
    private AssetManager   assetManager;
    private PhysicsSpace   space;
    // Lists
    private HashMap<Long, Player>             players             = new HashMap<Long, Player>();
    private HashMap<Long, InteractableEntity> interactableHashMap = new HashMap<Long, InteractableEntity>();

    /**
     * Constructor, Gets references to the simpleApplication and the root node.
     *
     * @param app      The owners Application.
     * @param rootNode The owners rootNode.
     */
    public WorldManager( Application app, ClientAppState client, Node rootNode ){
        this.app = app;
        this.client = client;
        this.rootNode = rootNode;
        this.assetManager = app.getAssetManager();
        this.space = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
        this.server = app.getStateManager().getState(SyncManager.class).getServer();
        this.syncManager = app.getStateManager().getState(SyncManager.class);
    }

    /**
     * Creates a new player entity and adds it to the players map.
     *
     * If this manager is owned by the server, it broadcasts the message to clients.
     *
     * @param playerID Unique id of the player, matches the clients ID.
     */

    public Player addPlayer( long playerID ){

        LogHelper.fine("adding player: ");
        Player player;
        Level level;

        if (this.isServer()){
            level = this.app.getStateManager().getState(ServerAppState.class).getLevel();
        } else {
            level = this.client.getLevel();
        }

        if (ClientAppState.isAudio){
            player = new VisualPlayer();
        } else {
            player = new AudioPlayer();
        }
        player.load(level);
        player.setID(playerID);
        if (this.isServer()){
            this.syncManager.broadcast(new PlayerJoinMessage(playerID));
            player.setServer(true);
        } else {
            if (this.client != null){
                if (this.client.getId() == playerID){
                    player.setCam(this.app.getCamera());
                    this.client.initKeys(player);
                }
            }
        }

        player.init(this.assetManager);
        player.start(this.rootNode, this.space);

        this.syncManager.addObject(playerID, player);
        LogHelper.info(playerID + ":" + player);
        this.players.put(playerID, player);

        return player;
    }

    public boolean isServer(){
        return this.server != null;
    }

    /**
     * Removes the player from the syncManager.
     *
     * (or why does addPlayer add to players?)
     *
     * @param id ID of the player to remove.
     */

    public void removePlayer( long id ){
        LogHelper.info("removing player: " + id);
        if (this.isServer()){
            this.syncManager.broadcast(new PlayerLeaveMessage(id));
        }
        this.syncManager.removeObject(id);
        Player player = this.players.remove(id);
        if (player == null){
            LogHelper.warn("tried to remove player who wasn't there: " + id);
            return;
        }
        //TODO?
        if (!this.isServer()){
            //remove player from scene
        }
        player.removeFromParent();
        this.space.removeAll(player);
    }

    public Spatial getPlayer( long syncID ){
        return this.players.get(syncID);
    }

    //TODO
    public void loadLevel(){
    }

    /**
     * @param message
     *
     * @return
     */
    //TODO
    @Override
    public boolean checkMessage( PhysicsSyncMessage message ){
//        if (message.syncId >= 0 && getEntity(message.syncId) == null) {
//            return false;
//        }
        return true;
    }

    /**
     * @return
     */
    public Server getServer(){
        return this.server;
    }

    /**
     * @param server
     */
    public void setServer( Server server ){
        this.server = server;
    }

    /**
     * @return
     */
    public SyncManager getSyncManager(){
        return this.syncManager;
    }

    /**
     * @return
     */
    public PhysicsSpace getPhysicsSpace(){
        return this.space;
    }

    /**
     * @return
     */
    public List<InteractableEntity> getInteractableEntityList(){
        //TODO
        return new LinkedList<InteractableEntity>();
    }
}
