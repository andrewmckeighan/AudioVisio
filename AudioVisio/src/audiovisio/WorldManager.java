package audiovisio;

import audiovisio.networking.SyncManager;
import audiovisio.networking.SyncMessageValidator;
import audiovisio.networking.messages.PhysicsSyncMessage;
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
}
