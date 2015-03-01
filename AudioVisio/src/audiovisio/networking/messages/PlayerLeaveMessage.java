package audiovisio.networking.messages;

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
public class PlayerLeaveMessage extends AbstractMessage {
	private int playerID;
	
	public PlayerLeaveMessage() {
		setReliable(true);
	}
	
	/**
	 * @param playerID The ID of the leaving player
	 */
	public PlayerLeaveMessage(int playerID) {
		this.playerID = playerID;

		setReliable(true);
	}
	
	public int getPlayerID() {
		return playerID;
	}
}
