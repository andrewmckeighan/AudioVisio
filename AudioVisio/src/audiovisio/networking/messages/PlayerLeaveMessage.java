package audiovisio.networking.messages;

import audiovisio.WorldManager;

import com.jme3.network.AbstractMessage;
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
	private int playerID;
	
	public PlayerLeaveMessage() {
		setReliable(true);
	}
	
	/**
	 * @param playerID The ID of the leaving player
	 */
	public PlayerLeaveMessage(long id, int playerID) {
		this.syncId = id;
		this.playerID = playerID;

		setReliable(true);
	}
	
	public PlayerLeaveMessage(long id) {
		this.syncId = id;
	}

	public int getPlayerID() {
		return playerID;
	}

	@Override
	public void applyData(Object object) {
		WorldManager manager = (WorldManager) object;
		manager.removePlayer(this.syncId);
	}
}
