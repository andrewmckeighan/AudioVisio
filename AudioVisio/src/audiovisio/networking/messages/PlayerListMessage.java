package audiovisio.networking.messages;

import java.util.Set;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to a newly joined client giving it a list of all
 * currently connected players.
 */
@Serializable
public class PlayerListMessage extends AbstractMessage {
	private Integer[] playerList;
	
	public PlayerListMessage() {}
	
	/**
	 * @param objects A list of IDs of all currently connected players
	 */
	public PlayerListMessage(Integer[] objects) {
		this.playerList = objects;
	}
	
	public Integer[] getPlayerList() {
		return playerList;
	}
}
