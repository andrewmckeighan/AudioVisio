package audiovisio.networking.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to all connected players when a new client connects.
 * 
 * Tells the other players the ID of the new player.
 */
@Serializable
public class PlayerJoinMessage extends AbstractMessage {
	private int playerID;
	
	public PlayerJoinMessage() {
		setReliable(true);
	}
	
	/**
	 * @param playerID The ID of the player who has just connected
	 */
	public PlayerJoinMessage(int playerID) {
		this.playerID = playerID;

		setReliable(true);
	}
	
	public int getPlayerID() {
		return this.playerID;
	}
}
