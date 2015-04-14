package audiovisio.networking.messages;

import audiovisio.WorldManager;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to all connected players when a new client connects.
 *
 * Tells the other players the ID of the new player.
 */
@Serializable
public class PlayerJoinMessage extends PhysicsSyncMessage {
    private long playerID;
    private Vector3f location;

    public PlayerJoinMessage(){
        this.setReliable(true);
    }

    /**
     * @param playerID The ID of the player who has just connected
     */
    public PlayerJoinMessage( long playerID, Vector3f location ){
        this.syncId = -1;
        this.playerID = playerID;
        this.location = location;

        this.setReliable(true);
    }

    public long getPlayerID(){
        return this.playerID;
    }

    @Override
    public void applyData( Object object ){
        WorldManager manager = (WorldManager) object;
        manager.addPlayer(this.playerID, this.location);
    }
}
