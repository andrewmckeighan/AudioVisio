package audiovisio;

import audiovisio.entities.Player;
import audiovisio.networking.SyncManager;
import audiovisio.networking.SyncMessageValidator;
import audiovisio.networking.messages.PhysicsSyncMessage;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.utils.LogHelper;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;

public class WorldManager extends AbstractAppState implements SyncMessageValidator {

    private Server server;
    private Client client;
    private long myPlayerId = -2;
    private Node rootNode;

    private HashMap<Long, Spatial> entities = new HashMap<Long, Spatial>();
    private HashMap<Long, Player> players = new HashMap<Long, Player>();
    private int newId = 0;
    private Application app;
    private AssetManager assetManager;
    private PhysicsSpace space;
    private SyncManager syncManager;

    public WorldManager(Application app, Node rootNode) {
        this.app = app;
        this.rootNode = rootNode;
        this.assetManager = app.getAssetManager();
        this.space = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
        this.server = app.getStateManager().getState(SyncManager.class).getServer();
        syncManager = app.getStateManager().getState(SyncManager.class);
    }

    public boolean isServer() {
        return server != null;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void loadLevel() {
    }

    public SyncManager getSyncManager() {
        return syncManager;
    }

    public PhysicsSpace getPhysicsSpace() {
        return space;
    }

    @Override
    public boolean checkMessage(PhysicsSyncMessage message) {
//        if (message.syncId >= 0 && getEntity(message.syncId) == null) {
//            return false;
//        }
        return true;
    }

	public void addPlayer(long syncID, int playerID, Vector3f spawnLocation) {
		LogHelper.info("adding player: ");
		if(isServer()){
			syncManager.broadcast(new PlayerJoinMessage(syncID, playerID, spawnLocation));
		}
		Player player = new Player();
        player.createModel(assetManager);
        player.setLocalTranslation(spawnLocation);
		syncManager.addObject(syncID, player);
		player.addToScene(rootNode, space);
        entities.put(syncID, player);
	}

    public Spatial getPlayer(long syncID) {
        return entities.get(syncID);
    }

	public void removePlayer(long id) {
		LogHelper.info("removing player: " + id);
		if(isServer()){
			syncManager.broadcast(new PlayerLeaveMessage(id));
		}
		syncManager.removeObject(id);
		Player player = players.remove(id);
		if(player == null){
			LogHelper.warn("tried to remove player who wasn't there: " + id);
			return;
		}
		//TODO?
		if(!isServer()){
			//remove player from scene
		}
		player.removeFromParent();
		space.removeAll(player);
	}
}
