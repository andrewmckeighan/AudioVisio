package audiovisio.networking.messages;

import audiovisio.WorldManager;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to the other connected clients when a
 * player leaves.
 * 
 * This message tells the other clients the ID of the player
 * who has just left.
 */
@Serializable
public class PlayerLeaveMessage extends PhysicsSyncMessage {
    private long playerID;

    public PlayerLeaveMessage(){
        this.setReliable(true);
    }

    /**
     * @param id The ID of the leaving player
     */
    public PlayerLeaveMessage( long id ){
        this.syncId = -1;
        this.playerID = id;

        this.setReliable(true);
    }

    public long getPlayerID(){
        return this.playerID;
    }

    @Override
    public void applyData( Object object ){
        WorldManager manager = (WorldManager) object;
        manager.removePlayer(this.syncId);
    }
}
