package audiovisio;

import audiovisio.entities.AudioPlayer;
import audiovisio.entities.Player;
import audiovisio.entities.VisualPlayer;
import audiovisio.networking.SyncManager;
import audiovisio.networking.SyncMessageValidator;
import audiovisio.networking.messages.PhysicsSyncMessage;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.states.ClientAppState;
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

/**
 * This class manages the list of players between servers and clients.
 *
 * Keeps a list of player entities and their ID. All worldManagers have identical lists,
 * so changes will be the same through all instances of the game.
 */

public class WorldManager extends AbstractAppState implements SyncMessageValidator {

    // Networking
    private SyncManager syncManager;
    private Server      server;

    // SimpleAppState references.
    private Node           rootNode;
    private Application    app;
    private ClientAppState client;
    private AssetManager   assetManager;
    private PhysicsSpace   space;

    // Lists
    private HashMap<Long, Player> players = new HashMap<Long, Player>();

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

    public void addPlayer( long playerID ){
        LogHelper.fine("adding player: ");
        Player player;
        if (playerID % 2 == 0){
            player = new AudioPlayer();
        } else {
            player = new VisualPlayer();
        }
        player.setID(playerID);
        if (this.isServer()) {
            this.syncManager.broadcast(new PlayerJoinMessage(playerID));
            player.setServer(true);
        } else {
//            assert this.app instanceof audiovisio.states.ClientAppState;
            if (this.client != null) {
                if (this.client.getId() == playerID) {
                    player.setCam(this.app.getCamera());
                    this.client.initKeys(player);
                }
            }
        }

        player.setModel(Player.createModel(this.assetManager));
        this.syncManager.addObject(playerID, player);
        player.addToScene(this.rootNode, this.space);
        player.setRootNode(this.rootNode);
        player.setAssetManager(this.assetManager);
        if(!this.isServer()) {
            player.init();
        }
        LogHelper.info(playerID + ":" + player);
        this.players.put(playerID, player);
    }

    /**
     * Removes the player from the syncManager.
     *
     * (or why does addPlayer add to players?)
     *
     * @param id ID of the player to remove.
     */

    public void removePlayer(long id) {
        LogHelper.info("removing player: " + id);
        if (this.isServer()) {
            this.syncManager.broadcast(new PlayerLeaveMessage(id));
        }
        this.syncManager.removeObject(id);
        Player player = this.players.remove(id);
        if (player == null) {
            LogHelper.warn("tried to remove player who wasn't there: " + id);
            return;
        }
        //TODO?
        if (!this.isServer()) {
            //remove player from scene
        }
        player.removeFromParent();
        this.space.removeAll(player);
    }

    public Spatial getPlayer(long syncID) {
        return this.players.get(syncID);
    }

    //TODO
    public void loadLevel() {
    }


    //TODO
    @Override
    public boolean checkMessage(PhysicsSyncMessage message) {
//        if (message.syncId >= 0 && getEntity(message.syncId) == null) {
//            return false;
//        }
        return true;
    }

    public Server getServer() {
        return this.server;
    }

    public boolean isServer() {
        return this.server != null;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public SyncManager getSyncManager() {
        return this.syncManager;
    }

    public PhysicsSpace getPhysicsSpace() {
        return this.space;
    }
}
